package com.example.service;

import com.example.entity.Students;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface StudentsService {

    Map<String, Object> registerStudent(String stuId, String stuName, Integer claId, MultipartFile faceImage);

    Students getStudentById(String stuId);

    boolean isStudentIdExists(String stuId);
}