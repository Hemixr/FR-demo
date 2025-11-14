package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("lessons")
public class Lessons {
    private Integer lesId;
    private String lesNo;
    private String lesName;
    private String techId;
    private String term;
    private Integer class1;
    private Integer class2;
    private Integer class3;
    private Integer class4;
}
