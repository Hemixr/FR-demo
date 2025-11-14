package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("classes")
public class Classes {
    @TableId
    private Integer classId;
    private String className;
    private String major;
    private String grade;
}
