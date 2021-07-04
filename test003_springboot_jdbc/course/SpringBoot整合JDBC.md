# SpringBoot整合JDBC

## 一、创建SpringBoot项目

**选择Spring Web、JDBC API、MySQL Driver**

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210704163955653-1738426790.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210704164233650-97949493.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210704164000696-296281011.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210704164002797-611394317.png)

## 二、在pom配置文件中修改JDBC版本，导入lombok

~~~xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <!-- 在pom文件中修改jdbc版本 -->
    <version>5.1.47</version>
    <scope>runtime</scope>
</dependency>

<!-- 导入lombok -->
<!-- lombok能够通过注解减少冗余代码，比如实体类的get/set方法等 -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
~~~

## 三、在MySQL中建库、建表、插入数据

~~~sql
CREATE DATABASE db_springboot_test CHARSET='utf8';

USE db_springboot_test;

CREATE TABLE tab_student(
    pk_student_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '学生学号，起始值2021001',
    student_name VARCHAR(10) COMMENT '学生姓名',
    student_sex CHAR(1) COMMENT '学生性别',
    student_score DOUBLE(4,1) COMMENT '学生分数'
) COMMENT '学生表';

-- 执行一次，设定初始值
INSERT INTO tab_student VALUES(2021001,SUBSTR(MD5(RAND()),1,4),IF(RAND()>0.5,'男','女'),RAND()*100);

-- 可根据需要执行多次，随机插入数据
INSERT INTO tab_student VALUES(NULL,SUBSTR(MD5(RAND()),1,4),IF(RAND()>0.5,'男','女'),RAND()*100);

SELECT * FROM tab_student;
~~~

## 四、在核心配置文件application.properties中设置连接数据库的四大参数

~~~properties
# 这里要是数据库所在主机的ip地址，如果数据库在本机可使用localhost
spring.datasource.url=jdbc:mysql://192.168.133.139:3306/db_springboot_test

spring.datasource.driver-class-name=com.mysql.jdbc.Driver

spring.datasource.username=root

spring.datasource.password=root
~~~

## 五、创建实体类

~~~java
package cn.godfery.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor// 生成一个无参的构造方法
@AllArgsConstructor// 生成一个包含所有参数的构造方法
@Accessors(chain = true)// 生成方法的链式调用
@Data// 生成get/set方法、重写toString方法等
public class Student implements Serializable {
    private Integer studentId;// 学生id
    private String studentName;// 学生姓名
    private String studentSex;// 学生性别
    private Double studentScore;// 学生分数
}
~~~

## 六、创建并实现dao接口，提供基本的增删改查功能

**定义一个Student的dao接口**

~~~java
package cn.byuan.dao;

import cn.byuan.entity.Student;

import java.util.List;

public interface StudentDao {
//    添加一个学生
    Integer addOneStudent(Student student);

//    根据主键studentId删除一个学生
    Integer deleteOneStudentByStudentId(Integer studentId);

//    根据主键studentId修改一个学生
    Integer updateOneStudentByStudentId(Student student);

//    根据主键studentId查询一个学生
    Student getOneStudentByStudentId(Integer studentId);

//    获取全部学生
    List<Student> getAllStudent();
}
~~~

**实现dao接口**

~~~java
package cn.byuan.dao;

import cn.byuan.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StudentDaoImp implements StudentDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;// 此对象由springboot创建，类似于PreparedStatement
//    jdbcTemplate.update方法：执行dml
//    jdbcTemplate.query方法：执行dql
//    jdbcTemplate.call方法：执行存储过程和函数


    //    添加一个学生
    @Override
    public Integer addOneStudent(Student student) {
        String sql="insert into tab_student values(null, ?, ?, ?)";
        return jdbcTemplate.update(sql, new PreparedStatementSetter() {// 匿名内部类
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, student.getStudentName());
                preparedStatement.setString(2, student.getStudentSex());
                preparedStatement.setDouble(3, student.getStudentScore());
            }
        });
    }

    //    根据主键studentId删除一个学生
    @Override
    public Integer deleteOneStudentByStudentId(Integer studentId) {
        String sql="delete from tab_student where pk_student_id=?";
        return jdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, studentId);
            }
        });
    }

    //    根据主键studentId修改一个学生
    @Override
    public Integer updateOneStudentByStudentId(Student student) {
        String sql="update tab_student set student_name=?, student_sex=?, student_score=? where pk_student_id=?";
        return jdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, student.getStudentName());
                preparedStatement.setString(2, student.getStudentSex());
                preparedStatement.setDouble(3, student.getStudentScore());
                preparedStatement.setInt(4, student.getStudentId());
            }
        });
    }

    //    根据主键studentId查询一个学生
    @Override
    public Student getOneStudentByStudentId(Integer studentId) {
        String sql="select * from tab_student where pk_student_id=?";
        return jdbcTemplate.query(sql, getRowMapper(), studentId).get(0);
    }

    //    获取全部学生
    @Override
    public List<Student> getAllStudent() {
        String sql="select * from tab_student";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    private RowMapper<Student> getRowMapper(){// 查询所使用的RowMapper对象，这里单独拿出来提高代码复用性
        return new RowMapper<Student>() {
            @Override
            public Student mapRow(ResultSet resultSet, int i) throws SQLException {
                Student student=new Student();
                student.setStudentId(resultSet.getInt("pk_student_id"))
                        .setStudentName(resultSet.getString("student_name"))
                        .setStudentSex(resultSet.getString("student_sex"))
                        .setStudentScore(resultSet.getDouble("student_score"));
                return student;
            }
        };
    }
}

~~~

## 七、在测试类中进行测试

~~~java
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
~~~

**测试结果**
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210704171616041-1466185865.png)

**源码地址：https://github.com/byuan98/springboot-integration/tree/master/test002_springboot_jdbc**