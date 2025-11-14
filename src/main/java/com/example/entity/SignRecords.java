package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("signRecords")
public class SignRecords {
    private Integer recId;
    private String stuId;
    private Integer lesId;
    private String term;
    private String classId;
    private LocalDateTime signTime;
    private Integer signStatus;
    private Float faceScore;
}
