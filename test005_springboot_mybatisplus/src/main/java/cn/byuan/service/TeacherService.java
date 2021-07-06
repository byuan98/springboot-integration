package cn.byuan.service;

import cn.byuan.entity.Teacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface TeacherService {
//    添加一位老师
    Integer addOneTeacher(Teacher teacher);

//    根据id删除一位老师
    Integer deleteOneTeacherByTeacherId(Integer teacherId);

//    修改一位老师的信息
    Integer updateOneTeacher(Teacher teacher);

//    根据id查询一位老师
    Teacher getOneTeacherByTeacherId(Integer teacherId);

//    获取所有老师
    List<Teacher> getAllTeacher();

//    对表中所有信息进行分页, 传入参数为要查询的页数
    Page<Teacher> getAllTeacherPage(Integer pageNumber);

//    按条件(性别)进行分页
    Page<Teacher> getAllTeacherByTeacherSexPage(Integer pageNumber, String teacherSex);
}
