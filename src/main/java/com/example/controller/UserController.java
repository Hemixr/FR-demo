//package com.example.controller;
//
//import com.example.entity.User;
//import com.example.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    /**
//     * 用户注册（管理员操作）
//     * 注意：UserService.registerUser只有4个参数，没有faceImage
//     */
//    @PostMapping("/register")
//    @PreAuthorize("hasRole('ADMIN')")
//    public Map<String, Object> registerUser(@RequestParam String userId,
//                                            @RequestParam String userName,
//                                            @RequestParam String passwd,
//                                            @RequestParam String role) {
//        // 调用4个参数的registerUser方法
//        return userService.registerUser(userId, userName, passwd, role);
//    }
//
//    /**
//     * 获取用户信息（自己或管理员）
//     */
//    @GetMapping("/{userId}")
//    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal")
//    public User getUser(@PathVariable String userId) {
//        return userService.getUserById(userId);
//    }
//
//    /**
//     * 更新用户信息（自己或管理员）
//     * 注意：UserService.updateUser只有2个参数，没有faceImage
//     */
//    @PutMapping("/{userId}")
//    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal")
//    public Map<String, Object> updateUser(@PathVariable String userId,
//                                          @RequestParam(required = false) String userName) {
//        // 调用2个参数的updateUser方法
//        return userService.updateUser(userId, userName);
//    }
//
//    /**
//     * 修改密码（只能自己修改）
//     */
//    @PostMapping("/{userId}/change-password")
//    @PreAuthorize("#userId == authentication.principal")
//    public Map<String, Object> changePassword(@PathVariable String userId,
//                                              @RequestParam String oldPasswd,
//                                              @RequestParam String newPasswd) {
//        return userService.updatePasswd(userId, oldPasswd, newPasswd);
//    }
//
//    /**
//     * 重置密码（管理员操作）
//     */
//    @PostMapping("/{userId}/reset-password")
//    @PreAuthorize("hasRole('ADMIN')")
//    public Map<String, Object> resetPassword(@PathVariable String userId) {
//        return userService.resetPasswd(userId);
//    }
//
//    /**
//     * 启用/禁用用户（管理员操作）
//     */
//    @PutMapping("/{userId}/status")
//    @PreAuthorize("hasRole('ADMIN')")
//    public Map<String, Object> toggleUserStatus(@PathVariable String userId,
//                                                @RequestParam boolean status) {
//        return userService.toggleUserStatus(userId, status);
//    }
//
//    /**
//     * 修改用户角色（管理员操作）- 修正拼写错误
//     */
//    @PutMapping("/{userId}/role")
//    @PreAuthorize("hasRole('ADMIN')")
//    public Map<String, Object> changeUserRole(@PathVariable String userId,
//                                              @RequestParam String newRole) {
//        // 修正：参数名从nweRole改为newRole
//        return userService.changeUserRole(userId, newRole);
//    }
//
//    /**
//     * 根据角色获取用户列表（管理员操作）
//     */
//    @GetMapping("/by-role/{role}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public List<User> getUsersByRole(@PathVariable String role) {
//        return userService.getUserByRole(role);
//    }
//
//    /**
//     * 获取所有用户（管理员操作）
//     */
//    @GetMapping("/all")
//    @PreAuthorize("hasRole('ADMIN')")
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    /**
//     * 获取当前登录用户信息
//     */
//    @GetMapping("/me")
//    public Map<String, Object> getCurrentUser() {
//        Map<String, Object> result = new java.util.HashMap<>();
//        result.put("success", true);
//        result.put("message", "请在前端保存token后使用token访问其他接口");
//        return result;
//    }
//
//    /**
//     * 检查用户ID是否存在
//     */
//    @GetMapping("/check/{userId}")
//    public Map<String, Object> checkUserId(@PathVariable String userId) {
//        boolean exists = userService.isUserIdExists(userId);
//        Map<String, Object> result = new java.util.HashMap<>();
//        result.put("exists", exists);
//        result.put("message", exists ? "用户ID已存在" : "用户ID可用");
//        return result;
//    }
//}