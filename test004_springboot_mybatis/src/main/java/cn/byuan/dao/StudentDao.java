package cn.byuan.dao;

import cn.byuan.entity.Student;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StudentDao {

    @Insert("insert into tab_student values (null, #{studentName}, #{studentSex}, #{studentScore})")
    Integer addOneStudent(Student student);

    @Delete("delete from tab_student where pk_student_id=#{studentId}")
    Integer deleteOneStudentByStudentId(Integer studentId);

    @Update("update tab_student set student_name=#{studentName}, student_sex=#{studentSex}, student_score=#{studentScore} where pk_student_id=#{studentId}")
    Integer updateOneStudentByStudentId(Student student);

    @Select("select * from tab_student where pk_student_id=#{studentId}")
    // 通过注解配置Mapper映射文件，使用该方式无需创建Mapper映射文件
    // Mapper仅在查询时使用，增、删、改均不使用
    // 在增删改上使用@Results会报错
    @Results(id = "studentMapper", value = {
            @Result(id = true, property ="studentId", column ="pk_student_id", javaType = Integer.class),
            @Result(property ="studentName", column = "student_name", javaType = String.class),
            @Result(property ="studentSex", column = "student_sex", javaType = String.class),
            @Result(property ="studentScore", column = "student_score", javaType = Double.class)
    })
    Student getOneStudentByStudentId(Integer studentId);

    @Select("select * from tab_student")
    // 使用刚才创建的Mapper注解
    @ResultMap(value = {"studentMapper"})
    List<Student> getAllStudent();
}
