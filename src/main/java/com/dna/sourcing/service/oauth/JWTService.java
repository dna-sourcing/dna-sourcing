package com.dna.sourcing.service.oauth;

import ch.qos.logback.classic.Logger;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dna.sourcing.model.common.ResponseBean;
import com.dna.sourcing.util.HttpUtil;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JWTService {

    //
    private Gson gson = new Gson();

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(JWTService.class);

    //
    String ontAuthJWTUrl;
    String ontAuthJWTVerificationUrl;

    //
    public JWTService(@Value("${ont.auth.jwt.url}") String ontAuthJWTUrl, @Value("${ont.auth.jwt.verification.url}") String ontAuthJWTVerificationUrl) {
        this.ontAuthJWTUrl = ontAuthJWTUrl;
        this.ontAuthJWTVerificationUrl = ontAuthJWTVerificationUrl;
    }

    //
    public Map<String,String> getAccessToken(String user_ontid) throws Exception {
        //
        JSONObject obj = new JSONObject();
        obj.put("user_ontid", user_ontid);
        //
        HttpUtil.HttpInfo httpInfo = HttpUtil.doPost(ontAuthJWTUrl, HttpUtil.JSON, obj);
        //
        try {
            ResponseBean response = gson.fromJson(httpInfo.responseBody, ResponseBean.class);
            Map<String,String> rst = (Map<String,String>) response.getResult();
            return rst;
        } catch (Exception e) {
            //
            logger.error(e.getMessage());
            //
            throw new Exception(e.getMessage());
        }
    }

    //
    public void verify(String access_token) throws Exception {
        //
        JSONObject obj = new JSONObject();
        obj.put("access_token", access_token);
        //
        HttpUtil.HttpInfo httpInfo = HttpUtil.doPost(ontAuthJWTVerificationUrl, HttpUtil.JSON, obj);
        //
        try {
            ResponseBean response = gson.fromJson(httpInfo.responseBody, ResponseBean.class);
            boolean rst = (boolean) response.getResult();
            if (!rst) {
                throw new Exception("verify failed.");
            }
        } catch (Exception e) {
            //
            logger.error(e.getMessage());
            //
            throw new Exception(e.getMessage());
        }
    }

    //
    public String getContentUser(String access_token) throws JWTDecodeException {
        //
        DecodedJWT jwt = JWT.decode(access_token);
        // 只能输出String类型，如果是其他类型返回null
        String dnaid = (String) jwt.getClaim("content").asMap().get("ontid"); // 这里不能改成dnaid
        return dnaid;
    }
}
