package com.example.controller;

import com.example.service.FaceCompareService;
import com.example.util.ImageBase64Util;
import com.example.util.BaiduTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/face")
public class FaceFeatureController {

    @Autowired
    private FaceCompareService faceCompareService;

    @Autowired
    private ImageBase64Util imageBase64Util;

    @Autowired
    private BaiduTokenUtil baiduTokenUtil;

    // 人脸比对接口
    @PostMapping("/compare")
    public Map<String, Object> compareImages(@RequestParam("file1") MultipartFile file1,
                                             @RequestParam("file2") MultipartFile file2) {
        Map<String, Object> res = new HashMap<>();

        try {
            if (file1.isEmpty() || file2.isEmpty()) {
                res.put("success", false);
                res.put("message", "请选择两张图片");
                return res;
            }

            System.out.println("=== 接收比对请求 ===");
            System.out.println("文件1: " + file1.getOriginalFilename() + ", 大小: " + file1.getSize());
            System.out.println("文件2: " + file2.getOriginalFilename() + ", 大小: " + file2.getSize());

            // 检查文件类型
            String contentType1 = file1.getContentType();
            String contentType2 = file2.getContentType();

            System.out.println("文件1类型: " + contentType1);
            System.out.println("文件2类型: " + contentType2);

            if (!isSupportedImageType(contentType1)) {
                res.put("success", false);
                res.put("message", "不支持的文件格式: " + contentType1 + "，请使用JPEG或PNG格式");
                return res;
            }

            if (!isSupportedImageType(contentType2)) {
                res.put("success", false);
                res.put("message", "不支持的文件格式: " + contentType2 + "，请使用JPEG或PNG格式");
                return res;
            }

            // 转换图片为Base64
            String imageBase641 = imageBase64Util.inputStreamToBase64(file1.getInputStream());
            String imageBase642 = imageBase64Util.inputStreamToBase64(file2.getInputStream());

            System.out.println("Base64长度 - 图片1: " + imageBase641.length() + ", 图片2: " + imageBase642.length());

            // 进行人脸比对
            Map<String, Object> compareResult = faceCompareService.compareImages(imageBase641, imageBase642);

            System.out.println("比对服务返回结果: " + compareResult);

            res.putAll(compareResult);

            if (Boolean.TRUE.equals(compareResult.get("success"))) {
                res.put("threshold", 90); // 显示使用的阈值
            }

        } catch (Exception e) {
            System.out.println("控制器异常: " + e.getMessage());
            e.printStackTrace();
            res.put("success", false);
            res.put("message", "处理失败: " + e.getMessage());
        }

        return res;
    }

    // 检查是否支持的图片类型
    private boolean isSupportedImageType(String contentType) {
        return contentType != null &&
                (contentType.equals("image/jpeg") ||
                        contentType.equals("image/jpg") ||
                        contentType.equals("image/png"));
    }

    @PostMapping("/detect")
    public Map<String, Object> detectFace(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "请选择图片");
                return response;
            }

            String imageBase64 = imageBase64Util.inputStreamToBase64(file.getInputStream());
            // 进行人脸检测
            Map<String, Object> detectResult = faceCompareService.detectFace(imageBase64);

            response.putAll(detectResult);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "检测失败: " + e.getMessage());
        }

        return response;
    }

    // 服务状态检查
    @GetMapping("/status")
    public Map<String, Object> checkStatus() {
        Map<String, Object> response = new HashMap<>();

        try {
            String token = baiduTokenUtil.getAccessToken();
            response.put("success", true);
            response.put("message", "服务正常");
            response.put("token_status", token != null ? "有效" : "无效");
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "服务异常: " + e.getMessage());
        }

        return response;
    }

    // 测试接口
    @GetMapping("/test")
    public String test() {
        return "服务正常运行 - " + System.currentTimeMillis();
    }

    // 测试JSON接口
    @GetMapping("/test-json")
    public Map<String, Object> testJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "JSON测试成功");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    // 检查接口
    @GetMapping("/health")
    public Map<String, String> healthCheck() {
        Map<String, String> result = new HashMap<>();
        result.put("status", "服务运行正常");
        result.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return result;
    }
}