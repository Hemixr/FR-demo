package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("teachers")
public class Teachers {
    private String techId;
    private String techName;
    private String college;
}
