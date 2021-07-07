# SpringBoot整合Druid

## 一、创建项目，选择依赖

选择**Spring Web、JDBC API、MySQL Driver**即可

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210707191143714-1836162871.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210707191145704-898358443.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210707191147585-145818298.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210707191149396-2047157662.png)

## 二、在pom中引入相关依赖

~~~xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <!-- 修改MySQL驱动版本 -->
    <version>5.1.26</version>
    <scope>runtime</scope>
</dependency>

<!-- 引入druid -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.0.20</version>
</dependency>

<!-- 引入log4j -->
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>

<!-- 引入lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
~~~

## 三、建库、建表、插入数据

~~~sql
CREATE DATABASE db_springboot_test CHARSET='utf8';

USE db_springboot_test;

CREATE TABLE tab_student(
    pk_student_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '学生学号，起始值2021001',
    idx_student_name VARCHAR(10) COMMENT '学生姓名',
    idx_student_sex CHAR(1) COMMENT '学生性别',
    idx_student_score DOUBLE(4,1) COMMENT '学生分数'
) AUTO_INCREMENT 2021001 COMMENT '学生表' 

-- 随机向表中插入数据, 可连续执行多次
INSERT INTO tab_student VALUES(NULL,SUBSTR(MD5(RAND()),1,4),IF(RAND()>0.5,'男','女'),RAND()*100);

SELECT * FROM tab_student;
~~~

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210707192231870-1389832500.png)

## 四、设置核心配置文件的属性

~~~properties
# 配置数据库连接的四大参数
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

spring.datasource.url=jdbc:mysql://192.168.133.139:3306/db_springboot_test?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true

spring.datasource.username=root

spring.datasource.password=root


# 指定连接池的类型
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource

# 实现druid的SQL统计
spring.datasource.filters=stat,wall,log4j
spring.datasource.connectionProperties=druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500;
~~~

## 五、创建实体类

~~~java
package cn.byuan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor// 生成无参的构造方法
@AllArgsConstructor// 生成满参的构造方法
@Accessors(chain = true)// 使用链式调用
@Data// 自动生成get/set方法、重写toString方法等方法
public class Student implements Serializable {
    private Integer studentId;
    private String studentName;
    private String studentSex;
    private Double studentScore;
}

~~~

## 六、创建dao层的StudentDao接口并实现StudentDao接口，完成与数据库的交互

接口：

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

实现类：

~~~java
package cn.byuan.dao.impl;

import cn.byuan.dao.StudentDao;
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
public class StudentDaoImpl implements StudentDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;// 类似于PreparedStatement

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
        String sql="update tab_student set idx_student_name=?, idx_student_sex=?, idx_student_score=? where pk_student_id=?";
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
                        .setStudentName(resultSet.getString("idx_student_name"))
                        .setStudentSex(resultSet.getString("idx_student_sex"))
                        .setStudentScore(resultSet.getDouble("idx_student_score"));
                return student;
            }
        };
    }
}

~~~

## 七、创建一个配置类，配置druid的后台参数

~~~java
package cn.byuan.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DruidConfig {
    //    创建数据源对象
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDataSource(){
        return new DruidDataSource();
    }

    //    创建ServletRegistrationBean
    @Bean
    public ServletRegistrationBean getServletRegistrationBean(){
//        创建bean时指定后台服务的url
        ServletRegistrationBean<StatViewServlet> registrationBean=new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");

//        创建一个map，指定账号密码
        HashMap<String, String> userMap=new HashMap<>();
        userMap.put("loginUsername", "Godfery");
        userMap.put("loginPassword", "123456");

//        指定允许的用户
        userMap.put("allow", "");

//        将map与bean进行绑定
        registrationBean.setInitParameters(userMap);

        return registrationBean;
    }
}

~~~

## 八、创建controller层

~~~java
package cn.byuan.controller;

import cn.byuan.dao.StudentDao;
import cn.byuan.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController// 等价于所有的方法前面加 @Controller + @ResponseBody
@RequestMapping("/student")
public class StudentAction {
    @Autowired
    private StudentDao studentDao;

    @RequestMapping("/add_one_student.action")
    public String addOneStudent(){
        Student student = new Student()
                .setStudentName(UUID.randomUUID().toString().substring(0, 4))// 利用uuid随机姓名
                .setStudentSex(Math.random()>0.5?"男":"女")// 随机性别
                .setStudentScore(((int)(Math.random()*1000))/10.0);// 随机分数

        Integer row = studentDao.addOneStudent(student);
        return "添加"+row+"行成功";
    }

//    使用url模板映射
    @RequestMapping("/delete_one_student/{studentId}.action")
    public String deleteOneStudentByStudentId(@PathVariable("studentId") Integer studentId){
        Integer row = studentDao.deleteOneStudentByStudentId(studentId);
        return "已删除"+row+"行";
    }

//    由于不准备使用前端页面, 因此修改学生信息使用传入id值随机修改属性的形式
    @RequestMapping("/update_one_student/{studentId}.action")
    public String updateOneStudentByStudentId(@PathVariable("studentId") Integer studentId){
        Student student = new Student()
                .setStudentId(studentId)
                .setStudentName("update"+(int)(Math.random()*10))
                .setStudentSex(Math.random()>0.5?"男":"女")
                .setStudentScore(((int)(Math.random()*1000))/10.0);
        Integer row = studentDao.updateOneStudentByStudentId(student);

        return "修改"+row+"行成功";
    }

    @RequestMapping("/get_one_student/{studentId}.action")
    public Student getOneStudentByStudentId(@PathVariable("studentId") Integer studentId){
        return studentDao.getOneStudentByStudentId(studentId);
    }

    @RequestMapping("/get_all_student.action")
    public List<Student> getAllStudent(){
        return studentDao.getAllStudent();
    }

}

~~~

## 九、运行项目，进行测试

**首先通过设置的账号和密码登录durid的后台;http://localhost:8080/druid**

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210707200519101-1645728588.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210707200521964-567566560.png)

**逐个输入action的url进行测试**

增加：http://localhost:8080/student/add_one_student.action

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210707200916860-1153224281.png)

删除：http://localhost:8080/student/delete_one_student/2021005.action

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210707201859965-1747083883.png)

修改：http://localhost:8080/student/update_one_student/2021001.action

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210707202242011-1331280805.png)

查询一个：http://localhost:8080/student/get_one_student/2021001.action

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210707202537167-987538696.png)

查询全部：http://localhost:8080/student/get_all_student.action

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210707202545112-399867134.png)

**源码地址：https://github.com/byuan98/springboot-integration/tree/master/test006_springboot_druid**
