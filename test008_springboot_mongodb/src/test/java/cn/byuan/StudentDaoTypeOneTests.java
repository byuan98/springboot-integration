package cn.byuan;

import cn.byuan.dao.StudentDaoTypeOne;
import cn.byuan.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class StudentDaoTypeOneTests {

    @Autowired
    private StudentDaoTypeOne studentDaoTypeOne;

    @Test
    void addOneStudent(){
//        插入10行
        for (Integer count = 0; count < 10; count++) {
            Student student = new Student()
                    .setStudentId("student_"+count) //如果自己不去设置id则系统会分配给一个id
                    .setStudentName("Godfery"+count)
                    .setStudentAge(count)
                    .setStudentScore(98.5-count)
                    .setStudentBirthday(new Date());
            studentDaoTypeOne.save(student);
        }
    }

    @Test
    void deleteOneStudentByStudentId(){
//        删除id为student_0的学生
        studentDaoTypeOne.deleteById("student_0");
    }

    @Test
    void updateOneStudent(){
//        修改姓名为Godfery1的Student年龄为22
        Student student = studentDaoTypeOne.getAllByStudentName("Godfery1");
        student.setStudentAge(22);
        studentDaoTypeOne.save(student);

    }

    @Test
    void getOneStudentByStudentId(){
        System.out.println(studentDaoTypeOne.findById("student_1"));
    }

    @Test
    void getAllStudent(){
        List<Student> studentList = studentDaoTypeOne.findAll();
        studentList.forEach(System.out::println);
    }

}
