package com.example.controller;

import com.example.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/teacher/auth")
public class TeacherAuthController {

    @Autowired
    private TeacherService teacherService;

    /**
     * 教师账号密码登录
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String teachId,
                                     @RequestParam String passwd) {
        return teacherService.login(teachId, passwd);
    }

    /**
     * 教师人脸登录（暂未实现）
     */
    @PostMapping("/face-login")
    public Map<String, Object> faceLogin(@RequestParam MultipartFile faceImage) {
        return teacherService.faceLogin(faceImage);
    }

    /**
     * 教师注册（管理员操作）
     */
    @PostMapping("/register")
//    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> registerTeacher(@RequestParam String teachId,
                                               @RequestParam String teachName,
                                               @RequestParam String college,
                                               @RequestParam String passwd,
                                               @RequestParam MultipartFile faceImage) {
        return teacherService.registerTeacher(teachId, teachName, college, passwd, faceImage);
    }

    /**
     * 检查工号是否存在
     */
    @GetMapping("/check/{teachId}")
    public Map<String, Object> checkTeacherId(@PathVariable String teachId) {
        boolean exists = teacherService.isTeacherIdExists(teachId);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("exists", exists);
        result.put("message", exists ? "编号已存在" : "编号可用");
        return result;
    }
}