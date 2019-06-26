package com.dna.sourcing.service.dnaid_server;

import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dna.sourcing.util.HttpUtil;
import com.dna.sourcing.util._codec.Base64ConvertUtil;
import com.dna.sourcing.util._crypt.*;
import com.dna.sourcing.util._hash.HMACSha256;
import com.dna.sourcing.util._hash.MD5Utils;
import okhttp3.*;
import okio.Buffer;
import org.json.JSONException;
import org.junit.Assert;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class DnaidServerService {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(DnaidServerService.class);

    //
    private final AESUtil utilAES;

    private final RSAUtil utilRSA;

    //
    private final String host;
    private final String pathRegister;
    //
    private final String appId;
    private final String appSecret;
    //
    private final String phonePassword;

    @Autowired
    public DnaidServerService(@Value("${com.dna.sourcing.dnaid.app.id}") String appId,
                              @Value("${com.dna.sourcing.dnaid.app.secret}") String appSecret,
                              @Value("${com.dna.sourcing.dnaid.app.host}") String host,
                              @Value("${com.dna.sourcing.dnaid.app.path.register}") String pathRegister,
                              @Value("${com.dna.sourcing.dnaid.app.phone.password}") String phonePassword,
                              AESUtil utilAES,
                              RSAUtil utilRSA) {
        //
        this.appId = appId;
        this.appSecret = appSecret;
        //
        this.host = host;
        this.pathRegister = pathRegister;
        //
        this.phonePassword = phonePassword;
        //
        this.utilAES = utilAES;
        this.utilRSA = utilRSA;
    }

    // public String registerPhoneWithoutCode(String phone, String password) throws Exception {
    //
    //     //
    //     final String URI = host + pathRegister;
    //
    //     //
    //     JSONObject jsonObject = new JSONObject();
    //     jsonObject.put("phone", phone);
    //     jsonObject.put("password", password);
    //     String json = JSON.toJSONString(jsonObject);
    //
    //     //
    //     String dnaid = postHmac(URI, jsonObject);
    //     return dnaid;
    // }

    public String registerPhoneWithoutCode(String phone) throws Exception {

        //
        final String URI = host + pathRegister;

        //
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phone);
        jsonObject.put("password", phonePassword);
        // String json = JSON.toJSONString(jsonObject);

        //
        String dnaid = postHmac(URI, jsonObject);
        return dnaid;
    }

    private String postHmac(String URI, JSONObject json) throws Exception {

        //加密
        String key = utilAES.generateKey();
        String enKey = null;
        try {
            // json = ow.writeValueAsString(jsonObject);
            // enKey = utilRSA.encryptByPublicKey(key);
            enKey = utilRSA.encrypt2Base64ByPublicKey(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            Assert.fail();
        }
        // String enJson = utilAES.encryptData(key, JSON.toJSONString(json));
        String tmp = JSON.toJSONString(json);
        String enJson = utilAES.gcmEncryptData(key.getBytes(), tmp);
        // ug/3zPp6fCYzfNL71bmQlgqP+nsOUOQd8K0x2Pq+zXqaQnpoH7YgrF3jRMty4dsUkeOrsK0K5/vbxnXkGLbXOA==

        RequestBean requestBean = new RequestBean(enJson);

        //hmac
        byte[] md5 = MD5Utils.MD5Encode(JSON.toJSONString(requestBean));
        String requestContentBase64String = Base64ConvertUtil.encode(md5);

        Long tsLong = System.currentTimeMillis() / 1000;
        String requestTimeStamp = tsLong.toString();

        UUID uuid = UUID.randomUUID();
        String nonce = Base64ConvertUtil.encode(uuid.toString().getBytes(StandardCharsets.UTF_8));

        //
        String rawData = appId + "POST" + pathRegister + requestTimeStamp + nonce + requestContentBase64String;  // TODO
        String signature = Base64ConvertUtil.encode(HMACSha256.sha256_HMAC(rawData, appSecret));
        String authHMAC = String.format("ont,%s,%s,%s,%s", appId, signature, nonce, requestTimeStamp);

        //
        HttpUtil.HttpInfo httpInfo = new HttpUtil.HttpInfo();
        //
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", enJson);
        //
        String c_type = String.valueOf(new MediaType("application", "dnaid.platform.api.v1+json"));
        //
        RequestBody body = RequestBody.create(okhttp3.MediaType.get(c_type), jsonObject.toString());
        //
        Request.Builder builder = new Request.Builder();
        // builder.addHeader("Authorization", authHMAC);
        builder.addHeader("Hmac", authHMAC);
        builder.addHeader("Secure-Key", enKey);
        builder.addHeader("Accept", c_type);
        builder.addHeader("Content-Type", c_type);
        Request request = builder.url(URI).post(body).build();

        //
        // RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        // final HttpEntity<RequestBean> entity = new HttpEntity<RequestBean>(requestBean, headers);

        /*
        RequestBean(data=ug/3zPp6fCYzfNL71bmQlgqP+nsOUOQd8K0x2Pq+zXqaQnpoH7YgrF3jRMty4dsUkeOrsK0K5/vbxnXkGLbXOA==)
         */

        //
        // ResponseEntity<Result> response = restTemplate.postForEntity(URI, entity, Result.class);

        //
        httpInfo.requestHeaders = request.headers();
        // https://stackoverflow.com/questions/28696964/okhttp-how-to-log-request-body
        if (request.body() != null) {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            if (copy.body() != null) {
                copy.body().writeTo(buffer);
            }
            httpInfo.requestBody = buffer.readUtf8();
        }

        //
        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            httpInfo.responseHeaders = response.headers();
            if (response.body() != null) {
                httpInfo.responseBody = response.body().string();
            }
        }

        //
        String result = httpInfo.responseBody.toString();

/*
{
  "code" : 0,
  "msg" : "SUCCESS",
  "result" : {
    "user_dnaid" : "did:dna:ASnjegahz6pL216ovioyB9UKyYuJCqQyrp"
  }
}
*/

/*
{
  "code" : 61002,
  "msg" : "FAIL, user already exist.",
  "result" : false
}
 */

        //
        org.json.JSONObject object = new org.json.JSONObject(result);
        //
        try {
            int err = object.getInt("code");
            if (err == 0) {
                String rstStr = object.get("result").toString();
                org.json.JSONObject o2 = new org.json.JSONObject(rstStr);
                return o2.getString("user_dnaid");
            } else {
                throw new Exception(object.getString("msg"));
            }
        } catch (JSONException e) {
            logger.error(e.getMessage());
            throw new Exception("dnaid_server " + e.getMessage());
        }

    }
}
