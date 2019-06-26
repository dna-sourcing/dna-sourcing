package com.dna.sourcing.util;

import okhttp3.*;
import okio.Buffer;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class HttpUtil {

    private static OkHttpClient client = new OkHttpClient();

    public static HttpInfo doPost(String url, MediaType type, JSONObject jsonObject) throws IOException {
        //
        HttpInfo httpInfo = new HttpInfo();
        //
        RequestBody body = null;
        //
        if (type.equals(JSON)) {
            body = RequestBody.create(JSON, jsonObject.toString());
        }
        if (type.equals(FORM)) {
            //
            FormBody.Builder builder = new FormBody.Builder();
            //
            Iterator<?> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = (String) jsonObject.get(key);
                // if (jsonObject.getRecord(key) instanceof JSONObject) {
                // }
                builder.add(key, value);
            }
            //
            body = builder.build();
        }
        //
        Request request = new Request.Builder().url(url).post(body).build();
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
        try (Response response = client.newCall(request).execute()) {
            httpInfo.responseHeaders = response.headers();
            if (response.body() != null) {
                httpInfo.responseBody = response.body().string();
            }
        }
        //
        return httpInfo;
    }

    public static class HttpInfo {
        public Headers requestHeaders;
        public String requestBody;
        public Headers responseHeaders;
        public String responseBody;
    }

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final MediaType FORM = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");

}
