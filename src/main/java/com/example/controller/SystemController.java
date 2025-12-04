package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("status", "UP");
        result.put("service", "人脸识别签到系统");
        result.put("version", "1.0.0");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    @GetMapping("/info")
    public Map<String, Object> systemInfo() {
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("name", "人脸识别学生签到系统");
        result.put("description", "基于Spring Boot + Spring Security + 百度AI的人脸识别系统");
        result.put("author", "Your Name");
        result.put("javaVersion", System.getProperty("java.version"));
        result.put("os", System.getProperty("os.name"));
        return result;
    }
}