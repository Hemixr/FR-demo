package com.example.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Students;
import com.example.mapper.StudentsMapper;
import com.example.service.StudentsService;
import com.example.util.ImageBase64Util;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class StudentsServiceImpl extends ServiceImpl<StudentsMapper, Students> implements StudentsService {
    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private ImageBase64Util imageBase64Util;

    @Autowired
    private FaceCompareServiceImpl faceCompareServiceImpl;

    private final OkHttpClient client = new OkHttpClient().newBuilder()
            .readTimeout(300, TimeUnit.SECONDS)
            .build();

    @Override
    public Map<String, Object> registerStudent(String stuId, String stuName, Integer claId, MultipartFile faceImage){
        Map<String, Object> result = new HashMap<>();

        try {
            if (stuId == null || stuId.trim().isEmpty()){
                result.put("success", false);
                result.put("message", "学号不能为空！");
                return result;
            }

            if (stuName == null || stuName.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "姓名不能为空！");
                return result;
            }

            if (claId == null) {
                result.put("success", false);
                result.put("message", "班级不能为空！");
                return result;
            }

            if (faceImage == null || faceImage.isEmpty()) {
                result.put("success", false);
                result.put("message", "请上传人脸照片！");
                return result;
            }

            if (isStudentIdExists(stuId)) {
                result.put("success", false);
                result.put("message", "学号已存在！");
                return result;
            }

            //图片文件格式识别  待实现


            String imageBase64 = imageBase64Util.inputStreamToBase64(faceImage.getInputStream());

            Map<String, Object> detectResult = faceCompareServiceImpl.detectFace(imageBase64);
            if (!Boolean.TRUE.equals(detectResult.get("success"))) {
                result.put("success", false);
                result.put("message", "图片未检测到人脸，请重新上传！");
                return result;
            }

            //创建学生对象，并保存
            Students stu = new Students();
            stu.setStuId(stuId.trim());
            stu.setStuName(stuName.trim());
            stu.setClassId(claId);
            stu.setFaceBase64(imageBase64);

            int insertResult = studentsMapper.insert(stu);

            if (insertResult > 0) {
                System.out.println("学生注册成功！" + stuId);
                result.put("success", true);
                result.put("message", "学生注册成功！");
                result.put("student", stu);
                result.put("timestamp", TimeUnit.SECONDS);
            } else {
                System.out.println("学生注册失败！" + stuId);
                result.put("success", false);
                result.put("message", "学生注册失败！");
            }

        } catch (IOException e) {
            System.out.println("学生注册失败！" + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "学生注册失败！" + e.getMessage());
        }

        return result;
    }

    @Override
    public Students getStudentById(String stuId){
        try {
            return studentsMapper.selectById(stuId);
        } catch (Exception e) {
            System.out.println("查询学生信息失败！" + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isStudentIdExists(String stuId){
        try {
            Students stu = studentsMapper.selectById(stuId);
            return stu != null;
        } catch (Exception e){
            System.out.println("查询学生信息失败！" + e.getMessage());
            return false;
        }
    }
}
