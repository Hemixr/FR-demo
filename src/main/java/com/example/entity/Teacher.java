package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("teachers")
public class Teacher {
    @TableId
    private String teachId;
    private String teachName;
    private String college;
    private String faceBase64;

    private String passwd;
    private Boolean enabled;
    private LocalDateTime createTime;
}
