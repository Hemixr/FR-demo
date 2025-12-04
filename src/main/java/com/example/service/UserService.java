//package com.example.service;
//
//import com.example.entity.User;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//import java.util.Map;
//
//public interface UserService {
//    //用户注册，通用
//    Map<String, Object> registerUser(String userId, String userName, String passwd, String role);
//
//    //登录认证
//    Map<String, Object> login(String userId, String passwd);
//
//    //用户信息管理
//    User getUserById(String userId);
//    Map<String, Object> updateUser(String userId, String userName);
//    Map<String, Object> updatePasswd(String userId, String oldPasswd, String newPasswd);
//
//    Map<String, Object> resetPasswd(String userId);
//
//    //启用|禁用用户
//    Map<String, Object> toggleUserStatus(String userId, boolean status);
//
//    //查询
//    boolean isUserIdExists(String userId);
//    List<User> getUserByRole(String role);
//    List<User> getAllUsers();
//
//    //角色管理
//    Map<String, Object> changeUserRole(String userId, String nweRole);
//
//}
