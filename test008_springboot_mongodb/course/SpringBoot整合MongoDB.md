# SpringBoot整合MongoDB

## 一、创建项目，选择依赖

**仅选择Spring Web、Spring Data MongoDB即可**

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210709193030808-1016329912.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210709193033221-1445781612.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210709193035272-464363853.png)

## 二、引入相关依赖（非必要）

这里只是为了实体类的创建方便而引入lombok

~~~xml
<!-- 引入lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
~~~

## 三、如果是第一次使用MongoDB，首先先创建用户

~~~shell
> use admin
switched to db admin
> db.createUser({user:"zlfeng", pwd:"123456", roles:[{role:"readWriteAnyDatabase", db:"admin"}]});
Successfully added user: {
	"user" : "zlfeng",
	"roles" : [
		{
			"role" : "readWriteAnyDatabase",
			"db" : "admin"
		}
	]
}
~~~

### MongoDB权限介绍

|         权限         |                             说明                             |
| :------------------: | :----------------------------------------------------------: |
|         read         |                    允许用户读取指定数据库                    |
|      readWrite       |                    允许用户读写指定数据库                    |
|       dbAdmin        | 允许用户在指定数据库中执行管理函数，如索引创建、删除、查看统计或访问system.profile |
|      userAdmin       | 允许用户向system.users集合写入，可以在指定数据库中创建、删除和管理用户 |
|     clusterAdmin     | 必须在admin数据库中定义，赋予用户所有分片和复制集相关函数的管理权限 |
|   readAnyDatabase    |     必须在admin数据库中定义，赋予用户所有数据库的读权限      |
| readWriteAnyDatabase |    必须在admin数据库中定义，赋予用户所有数据库的读写权限     |
| userAdminAnyDatabase |  必须在admin数据库中定义，赋予用户所有数据库的userAdmin权限  |
|  dbAdminAnyDatabase  |   必须在admin数据库中定义，赋予用户所有数据库的dbAdmin权限   |
|         root         |         必须在admin数据库中定义，超级账号，超级权限          |

## 四、定义核心配置文件

~~~properties
# 登录用户所在的数据库
spring.data.mongodb.authentication-database=admin

# 数据库的ip地址
spring.data.mongodb.host=192.168.133.142

# MongoDB端口号
spring.data.mongodb.port=27017

# 用户账号
spring.data.mongodb.username=zlfeng

# 用户密码
spring.data.mongodb.password=123456

# 指定使用的数据库
# 不必预先创建，不存在该数据库会自动创建
spring.data.mongodb.database=db_student
~~~

## 五、创建实体类

~~~java
package cn.byuan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class Student implements Serializable {
    @Id// 必须指定id列
    private String studentId;

    private String studentName;

    private Integer studentAge;

    private Double studentScore;
    
    private Date studentBirthday;
}

~~~

## 六、创建dao层，这里的dao层有两种写法

### （一）dao层写法一

#### 1. 编码部分

~~~java
package cn.byuan.dao;

import cn.byuan.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

/*
* dao层写法一
* 这里的用法其实和SpringDataJPA相似, 可根据需要来自定义方法
*
* 这种写法不需要写实现类
*
* MongoRepository<行对应的对象类型, 主键列类型>
* */
public interface StudentDaoTypeOne extends MongoRepository<Student, String> {
    
//    可根据需求自己定义方法, 无需对方法进行实现
    Student getAllByStudentName(String studentName);
        
}

~~~

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210709200539792-76469417.png)

#### 2.测试部分

在进行测试之前，我们先查询一下数据库，此时不存在db_student的库

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210709210413275-1983609887.png)

**开始测试**

~~~java
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

~~~

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210709210541150-908487633.png)

我们先来查看一下数据库

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210709210727367-1128798357.png)

进入数据库查看一下数据

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210709211135842-683082025.png)

### （二）dao层写法二

#### 1.编码部分

**接口部分**

~~~java
package cn.byuan.dao;

import cn.byuan.entity.Student;

import java.util.List;

/*
* dao层写法二
*
* 写法二需要进行实现
* */
public interface StudentDaoTypeTwo {
//    增加一位学生
    void addOneStudent(Student student);

//    根据id删除一位学生
    void deleteOneStudentByStudentId(String studentId);

//    修改一位学生的信息
    void updateOneStudent(Student student);

//    根据主键id获取一名学生
    Student getOneStudentByStudentId(String studentId);

//    获取全部学生
    List<Student> getAllStudent();
}

~~~

**实现类**

~~~java
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

~~~

#### 2.测试部分

~~~java
package cn.byuan;

import cn.byuan.dao.StudentDaoTypeOne;
import cn.byuan.dao.StudentDaoTypeTwo;
import cn.byuan.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class StudentDaoTypeTwoTests {

    @Autowired
    private StudentDaoTypeTwo studentDaoTypeTwo;

    @Test
    void addOneStudent(){
//        插入10行
        for (Integer count = 0; count < 10; count++) {
            Student student = new Student()
                    .setStudentId("study_"+count) //如果自己不去设置id则系统会分配给一个id
                    .setStudentName("Echo"+count)
                    .setStudentAge(count)
                    .setStudentScore(98.5-count)
                    .setStudentBirthday(new Date());
            studentDaoTypeTwo.addOneStudent(student);
        }
    }

    @Test
    void deleteOneStudentByStudentId(){
//        删除id为study_0的学生
        studentDaoTypeTwo.deleteOneStudentByStudentId("study_0");
    }

    @Test
    void updateOneStudent(){
//        修改id为study_1的Student年龄为21
        Student student = studentDaoTypeTwo.getOneStudentByStudentId("study_1");
        student.setStudentAge(21);
        studentDaoTypeTwo.updateOneStudent(student);

    }

    @Test
    void getOneStudentByStudentId(){
        System.out.println(studentDaoTypeTwo.getOneStudentByStudentId("study_1"));
    }

    @Test
    void getAllStudent(){
        List<Student> studentList = studentDaoTypeTwo.getAllStudent();
        studentList.forEach(System.out::println);
    }

}

~~~

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210709212515481-1481619949.png)

进入数据库查看一下数据

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210709212645102-1286114846.png)



**源码地址：https://github.com/byuan98/springboot-integration/tree/master/test008_springboot_mongodb**

