//package com.example.dto;
//
//import com.example.entity.Teacher;
//import com.example.entity.User;
//import lombok.Data;
//
//@Data
//public class TeacherWithUserInfoDTO {
//    // 教师信息
//    private String teachId;
//    private String teachName;
//    private String college;
//    private String faceBase64;
//
//    // 用户信息
//    private String username;
//    private String role;
//    private Boolean enabled;
//    private String createTime;
//    private String updateTime;
//
//    // 构造函数
//    public TeacherWithUserInfoDTO() {}
//
//    public TeacherWithUserInfoDTO(Teacher teacher, User user) {
//        // 教师信息
//        this.teachId = teacher.getTeachId();
//        this.teachName = teacher.getTeachName();
//        this.college = teacher.getCollege();
//        this.faceBase64 = teacher.getFaceBase64();
//
//        // 用户信息
//        if (user != null) {
//            this.username = user.getUserName();
//            this.role = user.getRole();
//            this.enabled = user.getEnable();
//            this.createTime = user.getCreateTime() != null ?
//                    user.getCreateTime().toString() : null;
//            this.updateTime = user.getUpdateTime() != null ?
//                    user.getUpdateTime().toString() : null;
//        }
//    }
//}