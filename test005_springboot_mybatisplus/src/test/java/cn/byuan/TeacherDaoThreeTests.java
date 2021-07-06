package cn.byuan;

import cn.byuan.entity.Teacher;
import cn.byuan.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TeacherDaoThreeTests {
    @Autowired
    private TeacherService teacherService;

    @Test
    void deleteOneTeacherByTeacherIdTest(){
//        删除id为1的老师
        teacherService.deleteOneTeacherByTeacherId(1);
    }

    @Test
    void getOneTeacherByTeacherId(){
//        查询id为1的老师
        teacherService.getOneTeacherByTeacherId(1);
    }

}
