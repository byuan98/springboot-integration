package cn.byuan.controller;

import cn.byuan.dao.StudentDao;
import cn.byuan.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController// 等价于所有的方法前面加 @Controller + @ResponseBody
@RequestMapping("/student")
public class StudentAction {
    @Autowired
    private StudentDao studentDao;

    @RequestMapping("/add_one_student.action")
    public String addOneStudent(){
        Student student = new Student()
                .setStudentName(UUID.randomUUID().toString().substring(0, 4))// 利用uuid随机姓名
                .setStudentSex(Math.random()>0.5?"男":"女")// 随机性别
                .setStudentScore(((int)(Math.random()*1000))/10.0);// 随机分数

        Integer row = studentDao.addOneStudent(student);
        return "添加"+row+"行成功";
    }

//    使用url模板映射
    @RequestMapping("/delete_one_student/{studentId}.action")
    public String deleteOneStudentByStudentId(@PathVariable("studentId") Integer studentId){
        Integer row = studentDao.deleteOneStudentByStudentId(studentId);
        return "已删除"+row+"行";
    }

//    由于不准备使用前端页面, 因此修改学生信息使用传入id值随机修改属性的形式
    @RequestMapping("/update_one_student/{studentId}.action")
    public String updateOneStudentByStudentId(@PathVariable("studentId") Integer studentId){
        Student student = new Student()
                .setStudentId(studentId)
                .setStudentName("update"+(int)(Math.random()*10))
                .setStudentSex(Math.random()>0.5?"男":"女")
                .setStudentScore(((int)(Math.random()*1000))/10.0);
        Integer row = studentDao.updateOneStudentByStudentId(student);

        return "修改"+row+"行成功";
    }

    @RequestMapping("/get_one_student/{studentId}.action")
    public Student getOneStudentByStudentId(@PathVariable("studentId") Integer studentId){
        return studentDao.getOneStudentByStudentId(studentId);
    }

    @RequestMapping("/get_all_student.action")
    public List<Student> getAllStudent(){
        return studentDao.getAllStudent();
    }

}
