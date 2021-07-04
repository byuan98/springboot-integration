package cn.byuan.dao;

import cn.byuan.entity.Student;

import java.util.List;

public interface StudentDao {
    //    添加一个学生
    Integer addOneStudent(Student student);

    //    根据主键studentId删除一个学生
    Integer deleteOneStudentByStudentId(Integer studentId);

    //    根据主键studentId修改一个学生
    Integer updateOneStudentByStudentId(Student student);

    //    根据主键studentId查询一个学生
    Student getOneStudentByStudentId(Integer studentId);

    //    获取全部学生
    List<Student> getAllStudent();
}
