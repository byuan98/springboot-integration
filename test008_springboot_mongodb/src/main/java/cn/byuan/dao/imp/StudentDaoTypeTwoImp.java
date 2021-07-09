package cn.byuan.dao.imp;

import cn.byuan.dao.StudentDaoTypeTwo;
import cn.byuan.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentDaoTypeTwoImp implements StudentDaoTypeTwo {

//    使用MongoTemplate模板类实现数据库操作
    @Autowired
    private MongoTemplate mongoTemplate;

//    增加一位学生
    public void addOneStudent(Student student){
        mongoTemplate.save(student);

    }

//    根据id删除一位学生
    public void deleteOneStudentByStudentId(String studentId){
        Student student = mongoTemplate.findById(studentId, Student.class);
        if(student != null){
            mongoTemplate.remove(student);
        }

    }

//    修改一位学生的信息
    public void updateOneStudent(Student student){
        mongoTemplate.save(student);
    }

//    根据主键id获取一名学生
    public Student getOneStudentByStudentId(String studentId){
        return mongoTemplate.findById(studentId, Student.class);
    }

//    获取全部学生
    public List<Student> getAllStudent(){
        return mongoTemplate.findAll(Student.class);
    }
}
