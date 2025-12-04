package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("students")
public class Student {
    @TableId
    private String stuId;
    private String stuName;
    private String clsId;
    private String faceBase64;

    private String passwd;
    private Boolean enabled;
    private LocalDateTime createTime;
}
