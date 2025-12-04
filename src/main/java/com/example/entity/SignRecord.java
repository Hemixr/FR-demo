package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("signRecords")
public class SignRecord {
    @TableId
    private Integer taskId;
    private Integer signStatus;
    private String taskName;
    private String cusId;
    private String cusName;
    private String teachId;
    private String stuId;
    private String clsId;
    private LocalDateTime signTime;
}
