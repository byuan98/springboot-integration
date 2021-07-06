package cn.byuan.service;

import cn.byuan.dao.TeacherDao;
import cn.byuan.entity.Teacher;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImp implements TeacherService{
    @Autowired
    private TeacherDao teacherDao;

    //    添加一位老师
    public Integer addOneTeacher(Teacher teacher){
        return teacherDao.insert(teacher);
    }

    //    根据id删除一位老师
    public Integer deleteOneTeacherByTeacherId(Integer teacherId){
        return teacherDao.deleteById(teacherId);
    }

    //    修改一位老师的信息
    public Integer updateOneTeacher(Teacher teacher){
        return teacherDao.updateById(teacher);
    }

    //    根据id查询一位老师
    public Teacher getOneTeacherByTeacherId(Integer teacherId){
        return teacherDao.selectById(teacherId);
    }

    //    获取所有老师
    public List<Teacher> getAllTeacher(){
        return teacherDao.selectList(null);
    }

    //    对表中所有信息进行分页, 传入参数为要查询的页数
    public Page<Teacher> getAllTeacherPage(Integer pageNumber){
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();

        Page<Teacher> teacherPage = new Page<>(pageNumber, 5);// 每页大小为5

        teacherDao.selectPage(teacherPage, teacherQueryWrapper);

        return teacherPage;
    }

    //    按条件(性别)进行分页
    public Page<Teacher> getAllTeacherByTeacherSexPage(Integer pageNumber, String teacherSex){
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();

        teacherQueryWrapper.eq("teacher_sex", teacherSex);// 根据条件进行分页, 这里填写的是表中的列名

        Page<Teacher> teacherPage = new Page<>(pageNumber, 5);

        teacherDao.selectPage(teacherPage, teacherQueryWrapper);
        return teacherPage;
    }
}
