package com.example.service;

import com.example.entity.Course;

import java.util.List;

public interface CourseService {
    List<Course> getCourseByTeachId(String teachId);

    List<Course> getCourseByClsId(String clsId);

    Course getCourseByCusId(String cusId);
}
