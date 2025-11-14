package com.example.util;

import com.alibaba.fastjson2.JSONObject;
import com.example.config.BaiduApiConfig;
import jakarta.annotation.Resource;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class BaiduTokenUtil {
    @Resource
    private BaiduApiConfig baiduApiConfig;
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10,TimeUnit.SECONDS)
            .build();
    private String accessToken;
    private long expireTime;

    //获取AccessToken（缓存未过期则重复使用）
    public String getAccessToken() throws Exception{
        if (accessToken != null && System.currentTimeMillis() < expireTime){
            return accessToken;
        }

        FormBody formBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", baiduApiConfig.getApiKey())
                .add("client_secret", baiduApiConfig.getSecretKey())
                .build();
        Request request = new Request.Builder()
                .url(baiduApiConfig.getTokenUrl())
                .post(formBody)
                .build();
        try(Response response = client.newCall(request).execute()){
            String responseStr = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(responseStr);
            accessToken = jsonObject.getString("access_token");
            long expiresIn = jsonObject.getLong("expires_in") * 1000;
            expireTime = System.currentTimeMillis() + expiresIn - 86400000;
            return accessToken;
        }
    }
}
