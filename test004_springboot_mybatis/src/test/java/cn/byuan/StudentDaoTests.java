package cn.byuan;

import cn.byuan.dao.StudentDao;
import cn.byuan.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class StudentDaoTests {

    @Autowired
    private StudentDao studentDao;

    @Test
    void addOneStudent(){
        Student student=new Student()
                .setStudentName("Echo")
                .setStudentSex("男")
                .setStudentScore(96.5);
        System.out.println(studentDao.addOneStudent(student));
    }

    @Test
    void deleteOneStudentByStudentId(){
        System.out.println(studentDao.deleteOneStudentByStudentId(2021005));
    }

    @Test
    void updateOneStudentByStudentId(){
        Student student=new Student()
                .setStudentId(2021001)
                .setStudentName("Echo")
                .setStudentSex("男")
                .setStudentScore(96.5);
        System.out.println(studentDao.updateOneStudentByStudentId(student));
    }

    @Test
    void getOneStudentByStudentIdTest() {
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
