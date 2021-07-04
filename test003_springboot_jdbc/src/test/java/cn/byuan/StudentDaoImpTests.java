package cn.byuan;

import cn.byuan.dao.StudentDao;
import cn.byuan.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class StudentDaoImpTests {
    @Autowired
    private StudentDao studentDao;
    @Test
    void addOneStudentTest(){
        Student student=new Student()
                .setStudentId(null)
                .setStudentName("Godfery")
                .setStudentSex("男")
                .setStudentScore(98.9);
        System.out.println(studentDao.addOneStudent(student));
    }

    @Test
    void deleteOneStudentByStudentIdTest(){
        System.out.println(studentDao.deleteOneStudentByStudentId(2021001));
    }

    @Test
    void updateOneStudentByStudentIdTest(){
        Student student=new Student()
                .setStudentId(2021001)
                .setStudentName("Godfery")
                .setStudentSex("男")
                .setStudentScore(98.9);
        System.out.println(studentDao.updateOneStudentByStudentId(student));
    }

    @Test
    void getOneStudentByStudentId(){
        System.out.println(studentDao.getOneStudentByStudentId(2021001));
    }

    @Test
    void getAllStudentTest(){
        List<Student> studentList=studentDao.getAllStudent();
        for(Student studentPart : studentList){
            System.out.println(studentPart);
        }
    }

}
