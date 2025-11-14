//package com.example.service;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.example.config.BaiduApiConfig;
//import com.example.util.BaiduTokenUtil;
//import com.example.util.ImageBase64Util;
//import okhttp3.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class FaceFeatureService {
//
//    @Autowired
//    private BaiduApiConfig baiduApiConfig;
//
//    @Autowired
//    private BaiduTokenUtil baiduTokenUtil;
//
//    @Autowired
//    private ImageBase64Util imageBase64Util;
//
//    private final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder()
//            .readTimeout(300, TimeUnit.SECONDS)
//            .build();
//
//
//     //人脸1:1比对
//    public Map<String, Object> compareImages(String imageBase641, String imageBase642) {
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            System.out.println("=== 开始人脸比对 ===");
//
//            // 清理Base64前缀（确保是纯Base64）
//            String cleanBase641 = imageBase64Util.cleanBase64Prefix(imageBase641);
//            String cleanBase642 = imageBase64Util.cleanBase64Prefix(imageBase642);
//
//            System.out.println("清理后Base64长度 - 图片1: " + cleanBase641.length());
//            System.out.println("清理后Base64长度 - 图片2: " + cleanBase642.length());
//            System.out.println("图片1前20字符: " + (cleanBase641.length() > 20 ? cleanBase641.substring(0, 20) : cleanBase641));
//            System.out.println("图片2前20字符: " + (cleanBase642.length() > 20 ? cleanBase642.substring(0, 20) : cleanBase642));
//
//            // 验证Base64格式
//            if (!imageBase64Util.isPureBase64(cleanBase641)) {
//                System.out.println("图片1 Base64格式无效");
//                result.put("success", false);
//                result.put("message", "第一张图片格式错误");
//                return result;
//            }
//
//            if (!imageBase64Util.isPureBase64(cleanBase642)) {
//                System.out.println("图片2 Base64格式无效");
//                result.put("success", false);
//                result.put("message", "第二张图片格式错误");
//                return result;
//            }
//
//            String accessToken = baiduTokenUtil.getAccessToken();
//            System.out.println("AccessToken: " + accessToken);
//
//            if (accessToken == null || accessToken.isEmpty()) {
//                result.put("success", false);
//                result.put("message", "AccessToken获取失败");
//                return result;
//            }
//
//            // 构建请求
//            JSONArray imageArray = new JSONArray();
//
//            JSONObject image1 = new JSONObject();
//            image1.put("image", cleanBase641); // 使用清理后的纯Base64
//            image1.put("image_type", "BASE64");
//            image1.put("face_type", "LIVE");
//
//            JSONObject image2 = new JSONObject();
//            image2.put("image", cleanBase642); // 使用清理后的纯Base64
//            image2.put("image_type", "BASE64");
//            image2.put("face_type", "LIVE");
//
//            imageArray.add(image1);
//            imageArray.add(image2);
//
//            // 只打印预览用于调试
//            String debugBody = imageArray.toJSONString();
//            System.out.println("请求体预览: " + (debugBody.length() > 200 ? debugBody.substring(0, 200) + "..." : debugBody));
//            System.out.println("完整请求体长度: " + debugBody.length());
//
//            MediaType mediaType = MediaType.parse("application/json");
//            RequestBody body = RequestBody.create(mediaType, imageArray.toJSONString());
//
//            String url = "https://aip.baidubce.com/rest/2.0/face/v3/match?access_token=" + accessToken;
//            System.out.println("请求URL: " + url);
//
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(body)
//                    .addHeader("Content-Type", "application/json")
//                    .build();
//
//            // 执行请求
//            System.out.println("发送请求到百度API...");
//            try (Response response = HTTP_CLIENT.newCall(request).execute()) {
//                System.out.println("响应状态: " + response.code() + " " + response.message());
//
//                if (!response.isSuccessful()) {
//                    String errorBody = response.body().string();
//                    System.out.println("HTTP请求失败，响应体: " + errorBody);
//                    result.put("success", false);
//                    result.put("message", "HTTP请求失败: " + response.code() + " - " + response.message());
//                    return result;
//                }
//
//                String responseBody = response.body().string();
//                System.out.println("响应体原始内容: " + responseBody);
//                System.out.println("响应体长度: " + responseBody.length());
//
//                if (responseBody == null || responseBody.trim().isEmpty()) {
//                    System.out.println("响应体为空");
//                    result.put("success", false);
//                    result.put("message", "API返回空响应");
//                    return result;
//                }
//
//                // 尝试解析JSON
//                try {
//                    JSONObject jsonResponse = JSON.parseObject(responseBody);
//                    System.out.println("JSON解析成功");
//
//                    Integer errorCode = jsonResponse.getInteger("error_code");
//                    String errorMsg = jsonResponse.getString("error_msg");
//
//                    System.out.println("错误码: " + errorCode + ", 错误信息: " + errorMsg);
//
//                    if (errorCode != null && errorCode == 0) {
//                        JSONObject matchResult = jsonResponse.getJSONObject("result");
//                        if (matchResult != null) {
//                            Double score = matchResult.getDouble("score");
//                            if (score != null) {
//                                System.out.println("比对成功，相似度: " + score);
//                                result.put("success", true);
//                                result.put("score", score);
//                                result.put("similarity", Math.round(score));
//                                result.put("isSamePerson", score >= 90);
//                                result.put("message", "比对成功");
//                            } else {
//                                result.put("success", false);
//                                result.put("message", "API返回数据异常: 缺少score字段");
//                            }
//                        } else {
//                            result.put("success", false);
//                            result.put("message", "API返回数据异常: 缺少result字段");
//                        }
//                    } else {
//                        result.put("success", false);
//                        result.put("message", "API调用失败: " + errorMsg);
//                    }
//
//                } catch (Exception e) {
//                    System.out.println("JSON解析异常: " + e.getMessage());
//                    System.out.println("响应内容前100字符: " + (responseBody.length() > 100 ? responseBody.substring(0, 100) : responseBody));
//                    result.put("success", false);
//                    result.put("message", "响应数据解析失败: " + e.getMessage());
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("人脸比对异常: " + e.getMessage());
//            e.printStackTrace();
//            result.put("success", false);
//            result.put("message", "比对过程异常: " + e.getMessage());
//        }
//
//        return result;
//    }
//
//    //人脸检测
//    public Map<String, Object> detectFace(String imageBase64) {
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            // 清理Base64前缀
//            String cleanBase64 = imageBase64Util.cleanBase64Prefix(imageBase64);
//
//            String accessToken = baiduTokenUtil.getAccessToken();
//
//            JSONObject requestBody = new JSONObject();
//            requestBody.put("image", cleanBase64); // 使用清理后的Base64
//            requestBody.put("image_type", "BASE64");
//            requestBody.put("max_face_num", 1);
//
//            MediaType mediaType = MediaType.parse("application/json");
//            RequestBody body = RequestBody.create(mediaType, requestBody.toJSONString());
//
//            Request request = new Request.Builder()
//                    .url("https://aip.baidubce.com/rest/2.0/face/v3/detect?access_token=" + accessToken)
//                    .post(body)
//                    .addHeader("Content-Type", "application/json")
//                    .build();
//
//            try (Response response = HTTP_CLIENT.newCall(request).execute()) {
//                if (response.isSuccessful()) {
//                    String responseBody = response.body().string();
//                    JSONObject jsonResponse = JSON.parseObject(responseBody);
//
//                    if (jsonResponse.getInteger("error_code") == 0) {
//                        JSONObject detectResult = jsonResponse.getJSONObject("result");
//                        int faceNum = detectResult.getInteger("face_num");
//
//                        result.put("success", true);
//                        result.put("face_detected", faceNum > 0);
//                        result.put("face_count", faceNum);
//                        result.put("message", faceNum > 0 ? "检测到人脸" : "未检测到人脸");
//                    } else {
//                        result.put("success", false);
//                        result.put("message", "人脸检测失败: " + jsonResponse.getString("error_msg"));
//                    }
//                } else {
//                    result.put("success", false);
//                    result.put("message", "HTTP请求失败");
//                }
//            }
//        } catch (Exception e) {
//            result.put("success", false);
//            result.put("message", "检测异常: " + e.getMessage());
//        }
//
//        return result;
//    }
//}