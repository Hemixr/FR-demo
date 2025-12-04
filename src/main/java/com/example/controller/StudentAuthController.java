package com.example.controller;

import com.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/student/auth")
public class StudentAuthController {

    @Autowired
    private StudentService studentService;

    /**
     * 学生账号密码登录
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String stuId,
                                     @RequestParam String passwd) {
        System.out.println("收到学生登录请求: " + stuId);  // 添加日志
        return studentService.login(stuId, passwd);
    }

    /**
     * 学生人脸登录（暂未实现）
     */
    @PostMapping("/face-login")
    public Map<String, Object> faceLogin(@RequestParam MultipartFile faceImage) {
        return studentService.faceLogin(faceImage);
    }

    /**
     * 学生注册（教师或管理员操作）
     */
    @PostMapping("/register")
    // @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")  // 暂时注释
    public Map<String, Object> registerStudent(@RequestParam String stuId,
                                               @RequestParam String stuName,
                                               @RequestParam(required = false) String clsId,
                                               @RequestParam String passwd,
                                               @RequestParam MultipartFile faceImage) {
        return studentService.registerStudent(stuId, stuName, clsId, passwd, faceImage);
    }

    /**
     * 检查学号是否存在
     */
    @GetMapping("/check/{stuId}")
    public Map<String, Object> checkStudentId(@PathVariable String stuId) {
        boolean exists = studentService.isStudentIdExists(stuId);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("exists", exists);
        result.put("message", exists ? "学号已存在" : "学号可用");
        return result;
    }
}