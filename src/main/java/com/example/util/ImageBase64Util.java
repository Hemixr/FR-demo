package com.example.util;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Base64;

@Service
public class ImageBase64Util {

    public String inputStreamToBase64(InputStream inputStream) {
        try {
            byte[] imageBytes = inputStream.readAllBytes();

            String base64 = Base64.getEncoder().encodeToString(imageBytes);

            // 打印Base64信息,查看是否成功
            System.out.println("生成的Base64长度: " + base64.length());
            System.out.println("Base64前20字符: " + (base64.length() > 20 ? base64.substring(0, 20) : base64));

            return base64;
        } catch (Exception e) {
            throw new RuntimeException("Base64转换失败", e);
        }
    }

    public String cleanBase64Prefix(String base64) {
        if (base64 == null) return null;

        // 移除数据URL前缀
        if (base64.startsWith("data:image")) {
            int commaIndex = base64.indexOf(",");
            if (commaIndex > 0) {
                String cleaned = base64.substring(commaIndex + 1);
                System.out.println("已清理Base64前缀，原长度: " + base64.length() + ", 新长度: " + cleaned.length());
                return cleaned;
            }
        }

        return base64;
    }

    public boolean isPureBase64(String base64) {
        if (base64 == null || base64.isEmpty()) {
            return false;
        }

        // 检查是否包含常见的前缀
        if (base64.startsWith("data:") ||
                base64.startsWith("data:image") ||
                base64.contains("base64,")) {
            System.out.println("检测到Base64包含前缀，需要清理");
            return false;
        }

        return base64.matches("^[a-zA-Z0-9+/]*={0,2}$");
    }
}