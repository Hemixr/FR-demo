package com.example.config;

import com.baidu.aip.face.AipFace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaiduAiConfig {
    @Autowired
    private BaiduApiConfig baiduApiConfig;

    public void setBaiduApiConfig(BaiduApiConfig baiduApiConfig) {
        this.baiduApiConfig = baiduApiConfig;
    }

    @Bean
    public AipFace aipFace(){
        AipFace client = new AipFace(baiduApiConfig.getAppId(),
                baiduApiConfig.getApiKey(),
                baiduApiConfig.getSecretKey()
        );
        client.setConnectionTimeoutInMillis(600000);
        client.setSocketTimeoutInMillis(600000);
        return client;
    }
}
