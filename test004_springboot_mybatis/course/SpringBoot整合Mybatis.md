# SpringBoot整合Mybatis

## 一、创建SpringBoot项目

**选择Spring Web、JDBC API、MyBatis Framework、MySQL Driver**

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210705160228692-1818247168.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210705160231255-515401895.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210705160233649-361797880.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210705160235574-2060454433.png)

## 二、修改MySQL驱动版本、引入其他相关JAR包

~~~xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <!-- 修改JDBC版本 -->
    <version>5.1.47</version>
    <scope>runtime</scope>
</dependency>

<!-- 引入lombok -->
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

## 四、在核心配置文件application.properties中设置连接数据库的四大参数以及相关配置

~~~properties
# 配置数据库连接的四大参数
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

spring.datasource.url=jdbc:mysql://192.168.133.139:3306/db_springboot_test?useUnicode=true&characterEncoding=utf8&autoReconnect=true

spring.datasource.username=root

spring.datasource.password=root


# 指定sql映射文件的位置
#mybatis.mapper-locations=classpath:mappers/*.xml


# 为实体类起别名
mybatis.type-aliases-package=cn.godfery.entity


# 显示SQL语句
logging.level.cn.godfery.dao=debug
~~~

## 五、项目最终结构

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210705162609544-1938748876.png)

## 六、创建实体类

~~~java
package cn.byuan.entity;

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

## 七、创建DAO层接口

~~~java
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
~~~

## 八、在测试类中进行测试

~~~java
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
~~~

**测试结果：**

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210705162306806-929685989.png)

**源码地址：https://github.com/byuan98/springboot-integration/tree/master/test004_springboot_mybatis**