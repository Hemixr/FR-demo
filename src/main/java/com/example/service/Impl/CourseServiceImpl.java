package com.example.service.Impl;

import com.example.entity.Course;
import com.example.mapper.CourseMapper;
import com.example.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> getCourseByTeachId(String teachId){
        return courseMapper.selectByTeachId(teachId);
    }

    @Override
    public List<Course> getCourseByClsId(String clsId){
        return courseMapper.selectByClsId(clsId);
    }

    @Override
    public Course getCourseByCusId(String cusId) {
        return courseMapper.selectById(cusId);
    }
}
