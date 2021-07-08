package cn.byuan.controller;

import cn.byuan.entity.Student;
import cn.byuan.service.StudentService;
import cn.byuan.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Api(tags = "学生管理相关接口")
@RestController //@Controller + @ResponseBody
@RequestMapping("/student")
public class StudentAction {
    @Autowired
    private StudentService studentService;

    @ApiOperation("添加一名学生")// 为每个handler添加方法功能描述
    @PostMapping("/add_student.action")
    @ApiImplicitParam(name = "student", value = "所添加的学生", dataTypeClass = Student.class)
    public ResponseVo<Integer> addOneStudent(Student student) {
        return studentService.addOneStudent(student);
    }

    @ApiOperation("根据studentId删除一名学生")
    @DeleteMapping("/delete_student/{studentId}.action")
    public ResponseVo<Integer> deleteOneStudentByStudentId(@PathVariable Integer studentId) {
        return studentService.deleteOneStudentByStudentId(studentId);
    }

    @ApiOperation("修改一名学生")
    @PutMapping("/update_student.action")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "studentId", value = "学号", required = true), //required为是否必填项
            @ApiImplicitParam(name = "studentName", value = "学生姓名", required = false),
            @ApiImplicitParam(name = "studentSex", value = "学生性别", required = false),
            @ApiImplicitParam(name = "studentScore", value = "学生分数", required = false)
    })
    public ResponseVo<Integer> updateOneStudent(Student student) {
        return studentService.updateOneStudent(student);
    }

    @ApiOperation("根据id获取一名学生")
    @GetMapping("/get_ont_student/{studentId}.action")
    public ResponseVo<Student> getOntStudentByStudentId(@PathVariable Integer studentId) {
        return studentService.getOneStudentByStudentId(studentId);
    }

    @ApiOperation("获取全部学生")
    @GetMapping("/get_all_student.action")
    public ResponseVo<Collection<Student>> getAllStudent() {
        return studentService.getAllStudent();
    }
}