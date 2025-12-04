package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("classes")
public class Class {
    @TableId
    private String clsId;
    private String clsName;
    private String college;
    private Integer grade;
}
