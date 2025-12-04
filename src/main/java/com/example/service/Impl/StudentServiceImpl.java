package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Student;
import com.example.mapper.StudentMapper;
import com.example.service.FaceCompareService;
import com.example.service.StudentService;
import com.example.util.ImageBase64Util;
import com.example.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ImageBase64Util imageBase64Util;
    @Autowired
    private FaceCompareService faceCompareService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Map<String, Object> registerStudent(String stuId, String stuName, String clsId, String passwd, MultipartFile faceImage) {
        Map<String, Object> result = new HashMap<>();

        try{
            if (stuId == null || stuId.trim().isEmpty()) {
                return errorResult("学号不能为空！");
            }
            if (stuName == null || stuName.trim().isEmpty()) {
                return errorResult("姓名不能为空！");
            }
            if (passwd == null || passwd.trim().isEmpty()) {
                return errorResult("密码不能为空！");
            }
            if (faceImage == null || faceImage.isEmpty()) {
                return errorResult("请上传人脸照片！");
            }

            if (isStudentIdExists(stuId)) {
                return errorResult("学号已存在！");
            }

            String imageBase64 = imageBase64Util.inputStreamToBase64(faceImage.getInputStream());
            Map<String, Object> detectResult = faceCompareService.detectFace(imageBase64);

            if (detectResult.get("success").equals(false)) {
                return errorResult("未检测到人脸，请重新上传照片！");
            }

            Student student = new Student();
            student.setStuId(stuId.trim());
            student.setStuName(stuName.trim());
            student.setClsId(clsId);
            student.setFaceBase64(imageBase64);
            student.setPasswd(passwd.trim());
            student.setEnabled(true);
            student.setCreateTime(LocalDateTime.now());

            int insertResult = studentMapper.insert(student);
            if (insertResult > 0) {
                result.put("success", true);
                result.put("message", "学生注册成功！");
                result.put("studentId", student.getStuId());
                result.put("studentName", student.getStuName());
            } else {
                result.put("success", false);
                result.put("message", "学生注册失败！");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "注册失败：" + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> login(String stuId, String passwd) {
        Map<String, Object> result = new HashMap<>();

        try {
            Student student = studentMapper.selectById(stuId);
            if (student == null) {
                return errorResult("学生不存在！");
            }
            if (student.getEnabled().equals(false)) {
                return errorResult("用户已禁用！");
            }
            if (!student.getPasswd().equals(passwd)) {
                return errorResult("密码错误！");
            }

            String token = jwtTokenUtil.generateToken(
                    student.getStuId(),
                    student.getStuName(),
                    "STUDENT"
            );

            result.put("success", true);
            result.put("message", "登录成功");
            result.put("token", token);
            result.put("studentId", student.getStuId());
            result.put("studentName", student.getStuName());
            result.put("role", "STUDENT");

            System.out.println(result);

            Map<String, Object> studentInfo = new HashMap<>();
            studentInfo.put("stuId", student.getStuId());
            studentInfo.put("stuName", student.getStuName());
            studentInfo.put("clsId", student.getClsId());
            result.put("studentInfo", studentInfo);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "登录失败：" + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> faceLogin(MultipartFile faceImage) {
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            String imageBase64 = imageBase64Util.inputStreamToBase64(faceImage.getInputStream());
//
//            List< Student> students = studentMapper.selectList(
//                    new QueryWrapper<Student>()
//                            .eq("enabled", true)
//                            .isNotNull("face_base64")
//                            .ne("face_base64","")
//            );
//
//            Student matchedStudent = null;
//            double maxScore = 0;
//
//            for (Student student : students) {
//                String
//            }
//        } catch (Exception e) {
//            result.put("success", false);
//            result.put("message", "登录失败：" + e.getMessage());
//        }
        return Map.of();
    }

    @Override
    public Student getStudentById(String stuId) {
        return studentMapper.selectById(stuId);
    }

    @Override
    public Map<String, Object> updateStudent(String stuId, String stuName, String clsId) {
        Map<String, Object> result = new HashMap<>();

        try {
            Student student = getStudentById(stuId);
            if (student == null) {
                return errorResult("学生不存在！");
            }

            boolean hasUpdate = false;

            if (stuName != null && !stuName.trim().isEmpty() && !stuName.trim().equals(student.getStuName())) {
                student.setStuName(stuName.trim());
                hasUpdate = true;
            }

            if (clsId != null) {
                String newClsId = clsId.trim();
                if (!newClsId.equals(student.getClsId())) {
                    student.setClsId(newClsId);
                    hasUpdate = true;
                }
            }

            if (hasUpdate) {
                int updateResult = studentMapper.updateById(student);
                if (updateResult > 0) {
                    result.put("success", true);
                    result.put("message", "学生信息更新成功！");
                    result.put("studentId", student.getStuId());
                    result.put("studentName", student.getStuName());
                    result.put("clsId", student.getClsId());
                } else {
                    result.put("success", false);
                    result.put("message", "学生信息更新失败！");
                }
            } else {
                result.put("success", false);
                result.put("message", "没有需要更新的信息！");
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> updateStudentFace(String stuId, MultipartFile faceImage) {
        Map<String, Object> result = new HashMap<>();

        try {
            Student student = getStudentById(stuId);
            if (student == null) {
                return errorResult("学生不存在！");
            }

            if (faceImage == null || faceImage.isEmpty()) {
                return errorResult("请上传人脸照片！");
            }

            String imageBase64 = imageBase64Util.inputStreamToBase64(faceImage.getInputStream());
            Map<String, Object> detectResult = faceCompareService.detectFace(imageBase64);
            if ((detectResult.get("success")).equals(false)) {
                return errorResult("未检测到人脸，请重新上传清晰照片！");
            }

            student.setFaceBase64(imageBase64);
            int updateResult = studentMapper.updateById(student);

            if (updateResult > 0) {
                result.put("success", true);
                result.put("message", "人脸照片更新成功！");
                result.put("studentId", student.getStuId());
            } else {
                result.put("success", false);
                result.put("message", "人脸照片更新失败！");
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新人脸照片失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> changeStuPasswd(String stuId, String oldPasswd, String newPasswd) {
        Map<String, Object> result = new HashMap<>();

        try {
            Student student = getStudentById(stuId);
            if (student == null) {
                return errorResult("学生不存在！");
            }

            if (!student.getPasswd().equals(oldPasswd)) {
                return errorResult("原密码错误！");
            }

            if (newPasswd == null || newPasswd.trim().length() < 6) {
                return errorResult("新密码长度至少6位！");
            }

            student.setPasswd(newPasswd.trim());
            int updateResult = studentMapper.updateById(student);

            if (updateResult > 0) {
                result.put("success", true);
                result.put("message", "密码修改成功！");
                result.put("studentId", student.getStuId());
            } else {
                result.put("success", false);
                result.put("message", "密码修改失败！");
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "密码修改失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public List<Student> getStudentByClass(String clsId) {
        if (clsId == null || clsId.trim().isEmpty()) {
            return List.of(); // 返回空列表而不是null
        }

        return studentMapper.selectList(
                new QueryWrapper<Student>()
                        .eq("cls_id", clsId.trim())
                        .orderByAsc("stu_id")
        );
    }

    @Override
    public List<Student> getAllStudents() {
        return studentMapper.selectList(
                new QueryWrapper<Student>()
                        .orderByAsc("stu_id")
        );
    }

    @Override
    public boolean isStudentIdExists(String stuId) {
        return studentMapper.selectById(stuId) != null;
    }

    @Override
    public Map<String, Object> enableStudent(String stuId, boolean enabled) {
        Map<String, Object> result = new HashMap<>();

        try {
            Student student = getStudentById(stuId);
            if (student == null) {
                return errorResult("学生不存在！");
            }

            if (Boolean.TRUE.equals(student.getEnabled()) == enabled) {
                result.put("success", false);
                result.put("message", "学生状态已是" + (enabled ? "启用" : "禁用"));
                return result;
            }

            student.setEnabled(enabled);
            int updateResult = studentMapper.updateById(student);

            if (updateResult > 0) {
                result.put("success", true);
                result.put("message", "学生" + (enabled ? "启用" : "禁用") + "成功");
                result.put("studentId", student.getStuId());
                result.put("enabled", student.getEnabled());
            } else {
                result.put("success", false);
                result.put("message", "学生状态更新失败！");
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "状态更新失败: " + e.getMessage());
        }

        return result;
    }

    private Map<String, Object> errorResult(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        return result;
    }
}