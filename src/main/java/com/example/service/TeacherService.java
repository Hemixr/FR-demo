package com.example.service;

import com.example.entity.Teacher;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TeacherService {
    Map<String ,Object> registerTeacher(String teachId, String teachName, String college,
                                        String passwd, MultipartFile faceImage);
    Map<String, Object> login(String teachId, String passwd);
    Map<String, Object> faceLogin(MultipartFile faceImage);

    Teacher getTeacherById(String teachId);
    Map<String, Object> updateTeacher(String teachId, String teachName, String college);
    Map<String ,Object> updateTeacherFace(String teachId, MultipartFile faceImage);
    Map<String, Object> changeTeachPasswd(String teachId, String oldPasswd, String newPasswd);

    List<Teacher> getTeacherByCollege(String college);
    List<Teacher> getAllTeachers();

    boolean isTeacherIdExists(String teachId);
    Map<String, Object> enableTeacher(String teachId,boolean enabled);
}
