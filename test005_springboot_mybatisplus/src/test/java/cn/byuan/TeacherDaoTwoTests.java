package cn.byuan;

import cn.byuan.entity.Teacher;
import cn.byuan.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TeacherDaoTwoTests {
    @Autowired
    private TeacherService teacherService;

    @Test
    void addOneTeacherTest(){
        Teacher teacher = new Teacher()
                .setTeacherName("test2")
                .setTeacherSex("女")
                .setTeacherSalary(9876.5);
        teacherService.addOneTeacher(teacher);
    }

    @Test
    void updateOneTeacher(){
        Teacher teacher = new Teacher()
                .setTeacherId(1)
                .setTeacherName("wer23")
                .setTeacherSex("女")
                .setTeacherSalary(1234.5);
        teacherService.updateOneTeacher(teacher);
    }

}
