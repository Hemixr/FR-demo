package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("course")
public class Course {
    @TableId
    private String cusId;
    private String cusName;
    private String teachId;
    private String clsId1;
    private String clsId2;
    private String clsId3;
    private String clsId4;
    private Integer term;

}
