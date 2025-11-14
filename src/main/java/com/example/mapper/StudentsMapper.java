package com.example.mapper;

import com.example.entity.Students;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentsMapper extends BaseMapper<Students> {
}