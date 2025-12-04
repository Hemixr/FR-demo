package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
    //根据教师id查询课程
    @Select("select * from courses where teach_id = #{teachId}")
    List<Course> selectByTeachId(String teachId);

    //根据班级id查询课程
    @Select("select * from courses where cls_id1 = #{clsId} or cls_id2 = #{clsId} or cls_id3 = #{clsId} or cls_id4 = #{clsId}")
    List<Course> selectByClsId(String clsId);
}
