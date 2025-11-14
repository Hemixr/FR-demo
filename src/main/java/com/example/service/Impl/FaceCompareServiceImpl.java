package com.example.service.Impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.example.config.BaiduApiConfig;
import com.example.service.FaceCompareService;
import com.example.util.BaiduTokenUtil;
import com.example.util.ImageBase64Util;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class FaceCompareServiceImpl implements FaceCompareService {
    @Autowired
    private BaiduTokenUtil baiduTokenUtil;

    @Autowired
    private BaiduApiConfig baiduApiConfig;

    @Autowired
    private ImageBase64Util imageBase64Util;


    private final OkHttpClient client = new OkHttpClient().newBuilder()
            .readTimeout(300, TimeUnit.SECONDS)
            .build();

    @Override
    public Map<String, Object> compareImages(String imageBase641, String imageBase642){
        Map<String, Object> result = new HashMap<>();

        try {
            String cleanBase641 = imageBase64Util.cleanBase64Prefix(imageBase641);
            String cleanBase642 = imageBase64Util.cleanBase64Prefix(imageBase642);

            //验证Base64格式
            if (!imageBase64Util.isPureBase64(cleanBase641)){
                result.put("success", false);
                result.put("message", "第一张图格式错误！");
                return result;

            }

            if (!imageBase64Util.isPureBase64(cleanBase642)){
                result.put("success", false);
                result.put("message", "第二张图格式错误！");
                return result;

            }

            String accessToken = baiduTokenUtil.getAccessToken();
            if (accessToken == null || accessToken.isEmpty()){
                result.put("success", false);
                result.put("message", "AccessToken获取失败！");
                return result;
            }

            //构建请求
            JSONArray imageArray = new JSONArray();

            JSONObject image1 = new JSONObject();
            image1.put("image", cleanBase641);
            image1.put("image_type", "BASE64");
            image1.put("face_type", "LIVE");

            JSONObject image2 = new JSONObject();
            image2.put("image", cleanBase642);
            image2.put("image_type", "BASE64");
            image2.put("face_type", "LIVE");

            imageArray.add(image1);
            imageArray.add(image2);


            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,imageArray.toJSONString());

            String url = baiduApiConfig.getCompareUrl()+ "?access_token=" + accessToken;

            Request req = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            //执行请求
            try (Response res = client.newCall(req).execute()){
                if (!res.isSuccessful()){
                    result.put("success", false);
                    result.put("message", "HTTP请求失败:" + res.code());
                    return result;
                }

                String resBody = res.body().string();
                if (resBody == null || resBody.trim().isEmpty()){
                    result.put("success", false);
                    result.put("message", "API返回空响应");
                    return result;
                }

                //解析JSON
                JSONObject jsonResponse = JSON.parseObject(resBody);
                Integer errCode = jsonResponse.getInteger("error_code");
                String errMsg = jsonResponse.getString("error_msg");

                if (errCode != null && errCode ==0){
                    JSONObject matchResult = jsonResponse.getJSONObject("result");

                    if (matchResult != null){
                        Double score = matchResult.getDouble("score");
                        if (score != null){
                            result.put("success", true);
                            result.put("score", score);
                            result.put("similarity", Math.round(score));
                            result.put("isSamePerson", score >= 90);
                            result.put("message", "比对成功");

                        }else{
                            result.put("success", false);
                            result.put("message", "API返回数据异常：缺少score字段！");

                        }
                    } else {
                        result.put("success", false);
                        result.put("message", "API返回数据异常：缺少result字段！");
                    }
                } else {
                    result.put("success", false);
                    result.put("message", "API调用失败：" + errMsg);
                }
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "API调用异常：" + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String,Object> detectFace(String imageBase64){
        Map<String, Object> result = new HashMap<>();

        try{
            String cleanBase64 = imageBase64Util.cleanBase64Prefix(imageBase64);
            String accessToken = baiduTokenUtil.getAccessToken();

            JSONObject reqBody = new JSONObject();
            reqBody.put("image", cleanBase64);
            reqBody.put("image_type", "BASE64");
            reqBody.put("max_face_num", 1);

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, reqBody.toJSONString());

            Request req = new Request.Builder()
                    .url(baiduApiConfig.getDetectUrl() + "?access_token=" + accessToken)
                    .post( body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response res = client.newCall(req).execute()){
                if (res.isSuccessful()){
                    String resBody = res.body().string();
                    JSONObject jsonResponse = JSON.parseObject(resBody);

                    if (jsonResponse.getInteger("error_code") == 0){
                        JSONObject detectResult = jsonResponse.getJSONObject("result");
                        int faceNum = detectResult.getInteger("face_num");

                        result.put("success", true);
                        result.put("face_detect", faceNum > 0);
                        result.put("face_count", faceNum);
                        result.put("message", faceNum > 0 ? "检测到人脸" : "未检测到人脸");

                    } else {
                        result.put("success", false);
                        result.put("message", "人脸检测失败: " + jsonResponse.getString("error_msg"));
                    }
                } else {
                    result.put("success", false);
                    result.put("message", "HTTP请求失败");
                }
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "检测异常" + e.getMessage());
        }

        return result;
    }

}
