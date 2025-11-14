package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "baidu.face")
public class BaiduApiConfig {

    private String appId;
    private String apiKey;
    private String secretKey;
    private String tokenUrl;
    private String detectUrl;
    private String compareUrl;

}
