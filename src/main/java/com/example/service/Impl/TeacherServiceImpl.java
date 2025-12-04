package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Teacher;
import com.example.mapper.TeacherMapper;
import com.example.service.FaceCompareService;
import com.example.service.TeacherService;
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
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
    @Autowired
    private ImageBase64Util imageBase64Util;
    @Autowired
    private FaceCompareService faceCompareService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    @Transactional
    public Map<String, Object> registerTeacher(String teachId, String teachName, String college, String passwd, MultipartFile faceImage) {
        Map<String, Object> result = new HashMap<>();

        try{
            if (teachId == null || teachId.trim().isEmpty()) {
                return errorResult("编号不能为空！");
            }
            if (teachName == null || teachName.trim().isEmpty()) {
                return errorResult("姓名不能为空！");
            }
            if (passwd == null || passwd.trim().isEmpty()) {
                return errorResult("密码不能为空！");
            }
            if (faceImage == null || faceImage.isEmpty()) {
                return errorResult("请上传人脸照片！");
            }

            if (isTeacherIdExists(teachId)) {
                return errorResult("编号已存在！");
            }

            String imageBase64 = imageBase64Util.inputStreamToBase64(faceImage.getInputStream());
            Map<String, Object> detectResult = faceCompareService.detectFace(imageBase64);

            if (detectResult.get("success").equals(false)) {
                return errorResult("未检测到人脸，请重新上传照片！");
            }

            Teacher teacher = new Teacher();
            teacher.setTeachId(teachId.trim());
            teacher.setTeachName(teachName.trim());
            teacher.setCollege(college);
            teacher.setFaceBase64(imageBase64);
            teacher.setPasswd(passwd.trim());
            teacher.setEnabled(true);
            teacher.setCreateTime(LocalDateTime.now());

            int insertResult = teacherMapper.insert(teacher);
            if (insertResult > 0) {
                result.put("success", true);
                result.put("message", "教师注册成功！");
                result.put("teacherId", teacher.getTeachId());
                result.put("teacherName", teacher.getTeachName());
            } else {
                result.put("success", false);
                result.put("message", "教师注册失败！");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "注册失败：" + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> login(String teachId, String passwd) {
        Map<String, Object> result = new HashMap<>();

        try {
            Teacher teacher = teacherMapper.selectById(teachId);
            if (teacher == null) {
                return errorResult("教师不存在！");
            }
            if (teacher.getEnabled().equals(false)) {
                return errorResult("用户已禁用！");
            }
            if (!teacher.getPasswd().equals(passwd)) {
                return errorResult("密码错误！");
            }

            String token = jwtTokenUtil.generateToken(
                    teacher.getTeachId(),
                    teacher.getTeachName(),
                    "TEACHER"
            );

            result.put("success", true);
            result.put("message", "登录成功");
            result.put("token", token);
            result.put("teacherId", teacher.getTeachId());
            result.put("teacherName", teacher.getTeachName());
            result.put("role", "TEACHER");

            System.out.println(result);

            Map<String, Object> teacherInfo = new HashMap<>();
            teacherInfo.put("teachId", teacher.getTeachId());
            teacherInfo.put("teachName", teacher.getTeachName());
            teacherInfo.put("college", teacher.getCollege());
            result.put("teacherInfo", teacherInfo);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message" , "登录失败：" + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> faceLogin(MultipartFile faceImage) {
        return Map.of();
    }

    @Override
    public Teacher getTeacherById(String teachId) {
        return teacherMapper.selectById(teachId);
    }

    @Override
    public Map<String, Object> updateTeacher(String teachId, String teachName, String college) {
        Map<String, Object> result = new HashMap<>();

        try {
            Teacher teacher = getTeacherById(teachId);
            if (teacher == null) {
                return errorResult("教师不存在！");
            }

            boolean hasUpdate = false;

            if (teachName != null && !teachName.trim().isEmpty() && !teachName.trim().equals(teacher.getTeachName())) {
                teacher.setTeachName(teachName.trim());
                hasUpdate = true;
            }

            if (college != null) {
                String newCollege = college.trim();
                if (!newCollege.equals(teacher.getCollege())) {
                    teacher.setCollege(newCollege);
                    hasUpdate = true;
                }
            }

            if (hasUpdate) {
                int updateResult = teacherMapper.updateById(teacher);
                if (updateResult > 0) {
                    result.put("success", true);
                    result.put("message", "教师信息更新成功！");
                    result.put("teacherId", teacher.getTeachId());
                    result.put("teacherName", teacher.getTeachId());
                    result.put("college", teacher.getCollege());
                } else {
                    result.put("success", false);
                    result.put("message", "教师信息更新失败！");
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
    public Map<String, Object> updateTeacherFace(String teachId, MultipartFile faceImage) {
        Map<String, Object> result = new HashMap<>();

        try {
            Teacher teacher = getTeacherById(teachId);
            if (teacher == null) {
                return errorResult("教师不存在！");
            }

            if (faceImage == null || faceImage.isEmpty()) {
                return errorResult("请上传人脸照片！");
            }

            String imageBase64 = imageBase64Util.inputStreamToBase64(faceImage.getInputStream());
            Map<String, Object> detectResult = faceCompareService.detectFace(imageBase64);
            if ((detectResult.get("success")).equals(false)) {
                return errorResult("未检测到人脸，请重新上传清晰照片！");
            }

            teacher.setFaceBase64(imageBase64);
            int updateResult = teacherMapper.updateById(teacher);

            if (updateResult > 0) {
                result.put("success", true);
                result.put("message", "人脸照片更新成功！");
                result.put("teacherId", teacher.getTeachId());
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
    public Map<String, Object> changeTeachPasswd(String teachId, String oldPasswd, String newPasswd) {
        Map<String, Object> result = new HashMap<>();

        try {
            Teacher teacher = getTeacherById(teachId);
            if (teacher == null) {
                return errorResult("教师不存在！");
            }

            if (!teacher.getPasswd().equals(oldPasswd)) {
                return errorResult("原密码错误！");
            }

            if (newPasswd == null || newPasswd.trim().length() < 6) {
                return errorResult("新密码长度至少6位！");
            }

            teacher.setPasswd(newPasswd.trim());
            int updateResult = teacherMapper.updateById(teacher);

            if (updateResult > 0) {
                result.put("success", true);
                result.put("message", "密码修改成功！");
                result.put("teacherId", teacher.getTeachId());
            } else {
                result.put("success", false);
                result.put("message", "密码修改失败");
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "密码修改失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public List<Teacher> getTeacherByCollege(String college) {
        if (college == null || college.trim().isEmpty()) {
            return List.of();
        }
        return teacherMapper.selectList(
                new QueryWrapper<Teacher>()
                        .eq("college",college)
                        .orderByAsc("teach_id")
        );
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherMapper.selectList(
                new QueryWrapper<Teacher>()
                        .orderByAsc("teach_id")
        );
    }

    @Override
    public boolean isTeacherIdExists(String teachId) {
        return teacherMapper.selectById(teachId) != null;
    }

    @Override
    public Map<String, Object> enableTeacher(String teachId, boolean enabled) {
        Map<String, Object> result = new HashMap<>();

        try {
            Teacher teacher = getTeacherById(teachId);
            if (teacher == null) {
                return errorResult("教师不存在！");
            }

            if (Boolean.TRUE.equals(teacher.getEnabled()) == enabled) {
                result.put("success", false);
                result.put("message", "教师状态已是" + (enabled ? "启用" : "禁用"));
                return result;
            }

            teacher.setEnabled(enabled);
            int updateResult = teacherMapper.updateById(teacher);

            if (updateResult > 0) {
                result.put("success", true);
                result.put("message", "教师" + (enabled ? "启用" : "禁用") + "成功");
                result.put("teacherId", teacher.getTeachId());
                result.put("enabled", teacher.getEnabled());
            } else {
                result.put("success", false);
                result.put("message", "教师状态更新失败！");
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