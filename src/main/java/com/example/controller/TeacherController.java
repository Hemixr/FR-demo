package com.example.controller;

import com.example.entity.Teacher;
import com.example.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    // ========== 信息查询 ==========

    /**
     * 获取教师信息（自己、管理员或其他教师）
     */
    @GetMapping("/{teachId}")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public Map<String, Object> getTeacher(@PathVariable String teachId) {

        System.out.println("===调试信息===");
        System.out.println("接收到请求，teachId参数：" + teachId );
        System.out.println("参数类型: " + teachId.getClass());

        Teacher teacher = teacherService.getTeacherById(teachId);

        System.out.println("查询结果: " + teacher);
        System.out.println("查询的teachId: " + (teacher != null ? teacher.getTeachId() : "null"));
        if (teacher == null) {
            return errorResult("教师不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "获取成功");
        result.put("teachId", teacher.getTeachId());
        result.put("teachName", teacher.getTeachName());
        result.put("college", teacher.getCollege());
        result.put("enabled", teacher.getEnabled());
        result.put("createTime", teacher.getCreateTime());
        result.put("faceBase64", teacher.getFaceBase64());

        return result;
    }

    /**
     * 根据学院获取教师列表
     */
    @GetMapping("/college/{college}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public Map<String, Object> getTeachersByCollege(@PathVariable String college) {
        List<Teacher> teachers = teacherService.getTeacherByCollege(college);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "获取成功");
        result.put("teachers", teachers.stream().map(this::convertToMap).collect(Collectors.toList()));
        result.put("count", teachers.size());

        return result;
    }

    /**
     * 获取所有教师列表（管理员和教师可见）
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public Map<String, Object> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "获取成功");
        result.put("teachers", teachers.stream().map(this::convertToMap).collect(Collectors.toList()));
        result.put("total", teachers.size());

        return result;
    }

    // ========== 信息管理 ==========

    /**
     * 更新教师信息（自己或管理员）
     */
    @PutMapping("/{teachId}")
    @PreAuthorize("hasRole('ADMIN') or #teachId == authentication.principal")
    public Map<String, Object> updateTeacher(@PathVariable String teachId,
                                             @RequestParam(required = false) String teachName,
                                             @RequestParam(required = false) String college) {
        return teacherService.updateTeacher(teachId, teachName, college);
    }

    /**
     * 更新教师人脸照片（自己或管理员）
     */
    @PostMapping("/{teachId}/face")
    @PreAuthorize("hasRole('ADMIN') or #teachId == authentication.principal")
    public Map<String, Object> updateTeacherFace(@PathVariable String teachId,
                                                 @RequestParam MultipartFile faceImage) {
        return teacherService.updateTeacherFace(teachId, faceImage);
    }

    /**
     * 修改密码（只能自己修改）
     */
    @PostMapping("/{teachId}/password")
    @PreAuthorize("#teachId == authentication.principal")
    public Map<String, Object> changePassword(@PathVariable String teachId,
                                              @RequestParam String oldPasswd,
                                              @RequestParam String newPasswd) {
        return teacherService.changeTeachPasswd(teachId, oldPasswd, newPasswd);
    }

    // ========== 状态管理 ==========

    /**
     * 启用/禁用教师（管理员操作）
     */
    @PutMapping("/{teachId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> enableTeacher(@PathVariable String teachId,
                                             @RequestParam boolean enabled) {
        return teacherService.enableTeacher(teachId, enabled);
    }

    /**
     * 获取当前教师信息（快捷方式）
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('TEACHER')")
    public Map<String, Object> getCurrentTeacher() {
        // 从Spring Security中获取当前登录的用户ID
        // 注意：这里需要你的Security配置正确
        return Map.of(
                "success", false,
                "message", "请先实现从SecurityContext获取用户ID"
        );
    }

    /**
     * 教师统计信息（管理员操作）
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getTeacherStatistics() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        long enabledCount = teachers.stream().filter(Teacher::getEnabled).count();

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "教师统计信息");
        result.put("total", teachers.size());
        result.put("enabledCount", enabledCount);
        result.put("disabledCount", teachers.size() - enabledCount);

        return result;
    }

    // ========== 辅助方法 ==========

    /**
     * 将Teacher实体转换为Map
     */
    private Map<String, Object> convertToMap(Teacher teacher) {
        if (teacher == null) {
            return new HashMap<>();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("teachId", teacher.getTeachId());
        map.put("teachName", teacher.getTeachName());
        map.put("college", teacher.getCollege());
        map.put("enabled", teacher.getEnabled());
        map.put("createTime", teacher.getCreateTime());
        // 不返回人脸base64数据，因为太大

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