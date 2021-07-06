package cn.byuan;

import cn.byuan.entity.Teacher;
import cn.byuan.service.TeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TeacherDaoFourTests {
    @Autowired
    private TeacherService teacherService;

    @Test
    void getAllTeacherPageTest(){
        Page<Teacher> teacherPage = teacherService.getAllTeacherPage(3);

        teacherPage.getRecords().forEach(System.out::println);
    }

    @Test
    void getAllTeacherByTeacherSexPageTest(){
        Page<Teacher> teacherPage = teacherService.getAllTeacherByTeacherSexPage(1, "男");

        teacherPage.getRecords().forEach(System.out::println);



    }

}
