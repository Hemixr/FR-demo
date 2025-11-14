package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("students")
public class Students {
    @TableId
    private String stuId;
    private String stuName;
    private String faceBase64;
    private Integer classId;

}
