package com.example.service;

import com.example.entity.Student;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface StudentService {

    Map<String, Object> registerStudent(String stuId, String stuName, String clsId,
                                        String passwd, MultipartFile faceImage);
    Map<String, Object> login(String stuId, String passwd);
    Map<String, Object> faceLogin(MultipartFile faceImage);

    Student getStudentById(String stuId);
    Map<String, Object> updateStudent(String stuId, String stuName, String clsId);
    Map<String, Object> updateStudentFace(String stuId, MultipartFile faceImage);
    Map<String, Object> changeStuPasswd(String stuId, String oldPasswd, String newPasswd);

    List<Student> getStudentByClass(String clsId);
    List<Student> getAllStudents();

    boolean isStudentIdExists(String stuId);
    Map<String, Object> enableStudent(String stuId, boolean enabled);
}