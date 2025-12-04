package com.example.controller;

import com.example.entity.Student;
import com.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // ========== 信息查询 ==========

    @GetMapping("/{stuId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or #stuId == authentication.principal")
    public Map<String, Object> getStudent(@PathVariable String stuId) {
        Student student = studentService.getStudentById(stuId);
        if (student == null) {
            return errorResult("学生不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "获取成功");
        result.put("stuId", student.getStuId());
        result.put("stuName", student.getStuName());
        result.put("clsId", student.getClsId());
        result.put("enabled", student.getEnabled());
        result.put("createTime", student.getCreateTime());
        result.put("faceBase64", student.getFaceBase64());

        return result;
    }

    @GetMapping("/class/{clsId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public Map<String, Object> getStudentsByClass(@PathVariable String clsId) {
        List<Student> students = studentService.getStudentByClass(clsId);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "获取成功");
        result.put("students", students.stream().map(this::convertToMap).collect(Collectors.toList()));
        result.put("count", students.size());

        return result;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public Map<String, Object> getAllStudents() {
        List<Student> students = studentService.getAllStudents();

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "获取成功");
        result.put("students", students.stream().map(this::convertToMap).collect(Collectors.toList()));
        result.put("total", students.size());

        return result;
    }

    // ========== 信息管理 ==========

    @PutMapping("/{stuId}")
    @PreAuthorize("hasRole('ADMIN') or #stuId == authentication.principal")
    public Map<String, Object> updateStudent(@PathVariable String stuId,
                                             @RequestParam(required = false) String stuName,
                                             @RequestParam(required = false) String clsId) {
        return studentService.updateStudent(stuId, stuName, clsId);
    }

    @PostMapping("/{stuId}/face")
    @PreAuthorize("hasRole('ADMIN') or #stuId == authentication.principal")
    public Map<String, Object> updateStudentFace(@PathVariable String stuId,
                                                 @RequestParam MultipartFile faceImage) {
        return studentService.updateStudentFace(stuId, faceImage);
    }

    @PostMapping("/{stuId}/password")
    @PreAuthorize("#stuId == authentication.principal")
    public Map<String, Object> changePassword(@PathVariable String stuId,
                                              @RequestParam String oldPassword,
                                              @RequestParam String newPassword) {
        return studentService.changeStuPasswd(stuId, oldPassword, newPassword);
    }

    // ========== 状态管理 ==========

    @PutMapping("/{stuId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> enableStudent(@PathVariable String stuId,
                                             @RequestParam boolean enabled) {
        return studentService.enableStudent(stuId, enabled);
    }

    // ========== 检查学号 ==========

    @GetMapping("/check/{stuId}")
    public Map<String, Object> checkStudentId(@PathVariable String stuId) {
        boolean exists = studentService.isStudentIdExists(stuId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("exists", exists);
        result.put("message", exists ? "学号已存在" : "学号可用");
        return result;
    }

    // ========== 辅助方法 ==========

    /**
     * 将Student实体转换为Map
     */
    private Map<String, Object> convertToMap(Student student) {
        if (student == null) {
            return new HashMap<>();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("stuId", student.getStuId());
        map.put("stuName", student.getStuName());
        map.put("clsId", student.getClsId());
        map.put("enabled", student.getEnabled());
        map.put("createTime", student.getCreateTime());

        return map;
    }

    /**
     * 错误结果
     */
    private Map<String, Object> errorResult(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        return result;
    }
}