//package com.example.dto;
//
//import com.example.entity.Student;
//import com.example.entity.User;
//import lombok.Data;
//
//@Data
//public class StudentWithUserInfoDTO {
//    // 学生信息
//    private String stuId;
//    private String stuName;
//    private String clsId;
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
//    public StudentWithUserInfoDTO() {}
//
//    public StudentWithUserInfoDTO(Student student, User user) {
//        // 学生信息
//        this.stuId = student.getStuId();
//        this.stuName = student.getStuName();
//        this.clsId = student.getClsId();
//        this.faceBase64 = student.getFaceBase64();
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