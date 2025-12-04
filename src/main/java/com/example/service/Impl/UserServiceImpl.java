//package com.example.service.Impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.example.entity.User;
//import com.example.mapper.UserMapper;
//import com.example.service.UserService;
//import com.example.util.ImageBase64Util;
//import com.example.util.JwtTokenUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    public Map<String, Object> registerUser(String userId, String userName, String passwd,String role){
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            if (userId == null || userId.trim().isEmpty()) {
//                result.put("success", false);
//                result.put("message", "用户ID不能为空！");
//                return result;
//            }
//
//            if (isUserIdExists(userId)) {
//                result.put("success", false);
//                result.put("message", "用户ID已存在！");
//                return result;
//            }
//
//            if (!"STUDENT".equals(role) && !"TEACHER".equals(role) && !"ADMIN".equals(role)) {
//                result.put("success", false);
//                result.put("message", "角色参数错误");
//                return result;
//            }
//
//            //创建用户
//            User user = new User();
//            user.setUserId(userId.trim());
//            user.setUserName(userName.trim());
//            user.setPasswd(passwordEncoder.encode(passwd.trim()));
//            user.setRole(role);
//            user.setEnable(true);
//            user.setCreateTime(LocalDateTime.now());
//            user.setUpdateTime(LocalDateTime.now());
//
//            int insertResult = userMapper.insert(user);
//
//            if (insertResult > 0) {
//                result.put("success", true);
//                result.put("message", "用户注册成功");
//
//                Map<String, Object> safeUser = new HashMap<>();
//                safeUser.put("userId", user.getUserId());
//                safeUser.put("username", user.getUserName());
//                safeUser.put("role", user.getRole());
//                result.put("user", safeUser);
//            } else {
//                result.put("success", false);
//                result.put("message", "用户注册失败");
//            }
//
//        } catch (Exception e) {
//            result.put("success", false);
//            result.put("message", "注册失败！" + e.getMessage());
//        }
//
//        return result;
//    }
//
//    @Override
//    public Map<String, Object> login(String userId, String passwd){
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            User user = getUserById(userId);
//            if (user == null) {
//                result.put("success", false);
//                result.put("message", "用户不存在！");
//                return result;
//            }
//
//            if (!user.getEnable()) {
//                result.put("success", false);
//                result.put("message", "用户已被禁用！");
//                return result;
//            }
//
//            if (!passwordEncoder.matches(passwd, user.getPasswd())) {
//                result.put("success", false);
//                result.put("message", "密码错误！");
//                return result;
//            }
//
//            String token = jwtTokenUtil.generateToken(user);
//
//            result.put("success", true);
//            result.put("message", "登录成功");
//            result.put("token", token);
//            result.put("userId", user.getUserId());
//            result.put("username", user.getUserName());
//            result.put("role", user.getRole());
//        } catch (Exception e) {
//            result.put("success", false);
//            result.put("message", "登录失败！" + e.getMessage());
//        }
//
//        return result;
//    }
//
//    @Override
//    public User getUserById(String userId){
//        return userMapper.selectById(userId);
//    }
//
//    @Override
//    public Map<String, Object> updateUser(String userId, String userName){
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            User user = getUserById(userId);
//            if (user == null) {
//                result.put("success", false);
//                result.put("message", "用户不存在！");
//                return result;
//            }
//
//            if (userName != null && !userName.trim().isEmpty() && !userName.trim().equals(user.getUserName())) {
//                user.setUserName(userName.trim());
//                user.setUpdateTime(LocalDateTime.now());
//
//                int updateResult = userMapper.updateById(user);
//
//                if (updateResult > 0) {
//                    result.put("success", true);
//                    result.put("message", "用户信息更新成功");
//
//                    Map<String, Object> safeUser = new HashMap<>();
//                    safeUser.put("userId", user.getUserId());
//                    safeUser.put("username", user.getUserName());
//                    safeUser.put("role", user.getRole());
//                    result.put("user", safeUser);
//                } else {
//                    result.put("success", false);
//                    result.put("message", "用户信息更新失败");
//                }
//            } else {
//                result.put("success", false);
//                result.put("message", "未发现需要更新的信息");
//            }
//
//        } catch (Exception e) {
//            result.put("success", false);
//            result.put("message", "更新失败: " + e.getMessage());
//        }
//
//        return result;
//    }
//
//    @Override
//    public Map<String, Object> updatePasswd(String userId, String oldPasswd, String newPasswd) {
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            User user = getUserById(userId);
//            if (user == null) {
//                result.put("success", false);
//                result.put("message", "用户不存在！");
//                return result;
//            }
//
//            if (!passwordEncoder.matches(oldPasswd, user.getPasswd())) {
//                result.put("success", false);
//                result.put("message", "原密码错误！");
//                return  result;
//            }
//
//            user.setPasswd(passwordEncoder.encode(newPasswd).trim());
//            user.setUpdateTime(LocalDateTime.now());
//            int updateResult = userMapper.updateById(user);
//
//            if (updateResult > 0) {
//                result.put("success", true);
//                result.put("message", "密码修改成功！");
//            } else {
//                result.put("success", false);
//                result.put("message", "密码修改失败！");
//            }
//        } catch (Exception e) {
//            result.put("success", false);
//            result.put("message", "密码修改失败！" + e.getMessage());
//        }
//
//        return result;
//    }
//
//    @Override
//    public Map<String, Object> resetPasswd(String userId) {
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            User user = getUserById(userId);
//            if (user == null) {
//                result.put("success", false);
//                result.put("message", "用户不存在！");
//                return result;
//            }
//
//            user.setPasswd(passwordEncoder.encode(user.getUserId()));
//            user.setUpdateTime(LocalDateTime.now());
//            int updateResult = userMapper.updateById(user);
//
//            if (updateResult > 0) {
//                result.put("success", true);
//                result.put("message", "密码重置成功！");
//            } else {
//                result.put("success", false);
//                result.put("message", "密码重置失败！");
//            }
//        } catch (Exception e) {
//            result.put("success", false);
//            result.put("message", "密码重置失败！" + e.getMessage());
//        }
//
//        return result;
//    }
//
//    @Override
//    public Map<String, Object> toggleUserStatus(String userId, boolean status) {
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            User user = getUserById(userId);
//            if (user == null) {
//                result.put("success", false);
//                result.put("message", "用户不存在！");
//                return result;
//            }
//
//            user.setEnable(status);
//            user.setUpdateTime(LocalDateTime.now());
//            int updateResult = userMapper.updateById(user);
//
//            if (updateResult > 0) {
//                result.put("success", true);
//                result.put("message", "用户状态修改成功！");
//            } else {
//                result.put("success", false);
//                result.put("message", "用户状态修改失败！");
//            }
//        } catch (Exception e) {
//            result.put("success", false);
//            result.put("message", "用户状态修改失败！" + e.getMessage());
//        }
//
//        return result;
//    }
//
//    @Override
//    public boolean isUserIdExists(String userId) {
//        return userMapper.selectById(userId) != null;
//    }
//
//    @Override
//    public List<User> getUserByRole(String role) {
//        return userMapper.selectList(
//                new QueryWrapper<User>().eq("role", role).orderByAsc("user_id")
//        );
//    }
//
//    @Override
//    public List<User> getAllUsers() {
//        return userMapper.selectList(
//                new QueryWrapper<User>().orderByAsc("user_id")
//        );
//    }
//
//    @Override
//    public Map<String, Object> changeUserRole(String userId, String newRole) {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            User user = getUserById(userId);
//            if (user == null) {
//                result.put("success", false);
//                result.put("message", "用户不存在！");
//                return result;
//            }
//
//            user.setRole(newRole);
//            user.setUpdateTime(LocalDateTime.now());
//            int updateResult = userMapper.updateById(user);
//
//            if (updateResult > 0) {
//                result.put("success", true);
//                result.put("message", "用户角色修改成功！");
//            } else {
//                result.put("success", false);
//                result.put("message", "用户角色修改失败！");
//            }
//        } catch (Exception e) {
//            result.put("success", false);
//            result.put("message", "用户角色修改失败！" + e.getMessage());
//        }
//
//        return result;
//    }
//}
