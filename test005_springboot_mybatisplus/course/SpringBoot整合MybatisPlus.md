# SpringBoot整合MybatisPlus

**目录（可点击直接跳转，但还是建议按照顺序观看，四部分具有一定的关联性）：**

**[实现基础的增删改查](#p1)**

**[实现自动填充功能](#p2)**

**[实现逻辑删除](#p3)**

**[实现分页](#p4)**

<h2 id="p1">一、实现基础的增删改查功能</h2>

### 1.创建项目、选择依赖

**选择Spring Web、JDBC API、MyBatis Framework、MySQL Driver**

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210706173628705-813445010.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210706173631264-1026193586.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210706173633906-1427855884.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210706173636063-329687501.png)

### 2.在pom文件中引入相关依赖

~~~xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <!-- 修改jdbc版本 -->
    <version>5.1.47</version>
    <scope>runtime</scope>
</dependency>

<!-- 引入MybatisPlus的启动器 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.3.2</version>
</dependency>

<!-- 引入lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>

<!-- 引入druid连接池 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.2.6</version>
</dependency>

<!-- 引入log4j -->
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
~~~

### 3.建库、建表、插入数据

~~~sql
CREATE DATABASE db_mybatisplus;

USE db_mybatisplus;

CREATE TABLE tab_teacher(
    pk_teacher_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '老师主键id,起始为1',
    teacher_name VARCHAR(10) COMMENT '老师姓名',
    teacher_sex CHAR(1) COMMENT '老师性别',
    teacher_salary DOUBLE(6,1) COMMENT '老师工资'
);

-- 反复执行多次，随机插入多条数据
INSERT INTO tab_teacher VALUES(
    NULL,
    SUBSTR(MD5(RAND()), 1, 5),
    IF(RAND()>0.5, '男', '女'),
    RAND()*10000+1000
);

SELECT * FROM tab_teacher;
~~~

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210706175137030-831039298.png)

### 4.配置核心配置文件

~~~properties
# 配置连接数据库的四大参数
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

spring.datasource.url=jdbc:mysql://192.168.133.139/db_mybatisplus?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true

spring.datasource.username=root

spring.datasource.password=root

# 指定连接池的类型
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource

# 显示SQL语句
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
~~~

### 5.创建实体类

~~~java
package cn.byuan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor// 创建无参的构造方法
@AllArgsConstructor// 创建满参的构造方法
@Accessors(chain = true)// 使用链式方法
@Data// 重写toString方法等方法
@TableName("tab_teacher")// 对应表名
public class Teacher implements Serializable {
    @TableId(value = "pk_teacher_id", type = IdType.AUTO)// 主键必须有TableId注解
    private Integer teacherId;

    @TableField("teacher_name")
    private String teacherName;

    @TableField("teacher_sex")
    private String teacherSex;

    @TableField("teacher_salary")
    private Double teacherSalary;

}
~~~

### 6.创建Teacher类的dao接口，继承BaseMapper接口，使用BaseMapper接口的方法

**这里我省去了mapper层，直接让dao层接口继承BaseMapper**

**这里要牢记一个原则：在启动类对继承BaseMapper的类进行扫描，谁继承BaseMapper类就对它进行扫描**

~~~java
package cn.byuan.dao;

import cn.byuan.entity.Teacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherDao extends BaseMapper<Teacher> {

}
~~~

### 7.创建service接口及实现类

接口：

~~~java
import cn.byuan.entity.Teacher;

import java.util.List;

public interface TeacherService {
//    添加一位老师
    Integer addOneTeacher(Teacher teacher);

//    根据id删除一位老师
    Integer deleteOneTeacherByTeacherId(Integer teacherId);

//    修改一位老师的信息
    Integer updateOneTeacher(Teacher teacher);

//    根据id查询一位老师
    Teacher getOneTeacherByTeacherId(Integer teacherId);

//    获取所有老师
    List<Teacher> getAllTeacher();
}
~~~

实现类：

~~~java
package cn.byuan.service;

import cn.byuan.dao.TeacherDao;
import cn.byuan.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImp implements TeacherService{
    @Autowired
    private TeacherDao teacherDao;

    //    添加一位老师
    public Integer addOneTeacher(Teacher teacher){
        return teacherDao.insert(teacher);
    }

    //    根据id删除一位老师
    public Integer deleteOneTeacherByTeacherId(Integer teacherId){
        return teacherDao.deleteById(teacherId);
    }

    //    修改一位老师的信息
    public Integer updateOneTeacher(Teacher teacher){
        return teacherDao.updateById(teacher);
    }

    //    根据id查询一位老师
    public Teacher getOneTeacherByTeacherId(Integer teacherId){
        return teacherDao.selectById(teacherId);
    }

    //    获取所有老师
    public List<Teacher> getAllTeacher(){
        return teacherDao.selectList(null);
    }
}
~~~

### 8.在启动类对继承BaseMapper的类配置扫描

**谁继承了BaseMapper就对谁进行扫描，因为之前我省去了mapper层，直接让dao层接口继承BaseMapper，因此这里扫描的是dao包**

~~~java
package cn.byuan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.byuan.dao")// 那一层继承了BaseMapper就对那一层进行扫描
public class Test005SpringbootMybatisplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(Test005SpringbootMybatisplusApplication.class, args);
    }

}
~~~

### 9.在测试类进行测试

~~~java
package cn.byuan;

import cn.byuan.entity.Teacher;
import cn.byuan.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TeacherDaoOneTests {
    @Autowired
    private TeacherService teacherService;

    @Test
    void addOneTeacherTest(){
        Teacher teacher = new Teacher()
                .setTeacherName("test2")
                .setTeacherSex("女")
                .setTeacherSalary(9876.5);
        teacherService.addOneTeacher(teacher);
    }

    @Test
    void deleteOneTeacherByTeacherIdTest(){
        teacherService.deleteOneTeacherByTeacherId(2);
    }

    @Test
    void updateOneTeacher(){
        Teacher teacher = new Teacher()
                .setTeacherId(1)
                .setTeacherName("qwe12")
                .setTeacherSex("女")
                .setTeacherSalary(1234.5);
        teacherService.updateOneTeacher(teacher);
    }

    @Test
    void getOneTeacherByTeacherId(){
        teacherService.getOneTeacherByTeacherId(1);
    }

    @Test
    void getAllTeacher(){
        teacherService.getAllTeacher();
    }

}


~~~

测试结果：

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210706192017204-2022944254.png)

<h2 id="p2">二、实现自动填充功能</h2>

自动填充功能一般可以用作记录操作发生时间，如某列的最后修改时间等，本部分代码基于第一部分：[实现基础的增删改查、](#p1)

### 1.修改数据库中的表结构

~~~sql
-- 修改数据库中表结构
ALTER TABLE tab_teacher ADD create_time TIMESTAMP COMMENT '记录插入时间';

ALTER TABLE tab_teacher ADD update_time TIMESTAMP COMMENT '记录修改时间';

-- 更新所有表中数据
UPDATE tab_teacher SET create_time=NOW();

UPDATE tab_teacher SET update_time=NOW();

SELECT * FROM tab_teacher;
~~~

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210706192949957-137418345.png)

### 2.修改实体类

这一步简单来说就是将增加的两列添加进Teacher类的属性中，其余与第一部分保持一致；

为实现自动填充还应在新加入的两个属性的TableField中增加**"fill"**属性

~~~java
package cn.byuan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor// 创建无参的构造方法
@AllArgsConstructor// 创建满参的构造方法
@Accessors(chain = true)// 使用链式方法
@Data// 重写toString方法等方法
@TableName("tab_teacher")// 对应表名
public class Teacher implements Serializable {
    @TableId(value = "pk_teacher_id", type = IdType.AUTO)// 主键必须有TableId注解
    private Integer teacherId;

    @TableField("teacher_name")
    private String teacherName;

    @TableField("teacher_sex")
    private String teacherSex;

    @TableField("teacher_salary")
    private Double teacherSalary;

//    增加的两列属性
    @TableField(value = "create_time", fill = FieldFill.INSERT)// 插入时自动填充
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)// 插入和修改时自动填充
    private Date updateTime;

}

~~~

### 3.创建handler层，实现MetaObjectHandler接口，重写insertFill与updateFill方法，指定填充的字段及属性值

~~~java
package cn.byuan.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TeacherHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());

    }
}

~~~

### 4.进行测试

这里我们只测试增加和修改两个方法

~~~java
package cn.byuan;

import cn.byuan.entity.Teacher;
import cn.byuan.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TeacherDaoTwoTests {
    @Autowired
    private TeacherService teacherService;

    @Test
    void addOneTeacherTest(){
        Teacher teacher = new Teacher()
                .setTeacherName("test2")
                .setTeacherSex("女")
                .setTeacherSalary(9876.5);
        teacherService.addOneTeacher(teacher);
    }

    @Test
    void updateOneTeacher(){
        Teacher teacher = new Teacher()
                .setTeacherId(1)
                .setTeacherName("wer23")
                .setTeacherSex("女")
                .setTeacherSalary(1234.5);
        teacherService.updateOneTeacher(teacher);
    }
}

~~~

从SQL语句可以看出，在执行update方法时，已自动为我们填充字段

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210706195005042-1625454638.png)

数据库中新插入数据也没有问题

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210706195343930-1131696085.png)

<h2 id="p3">三、实现逻辑删除功能</h2>

数据是无价的，因此一般而言我们不会直接删除数据。对于"删除"我们一般的做法是**定义一个字段来记录本行数据的可见性**

### 1.修改数据库中的表结构

添加一个字段作为标记

~~~sql
-- 修改数据库中表结构, 添加一个字段作为标记
ALTER TABLE tab_teacher ADD visibility TINYINT COMMENT "0表示未删除，1表示删除";

-- 更新表中所有数据, 全部设置为未删除
UPDATE tab_teacher SET visibility=0;
~~~

### 2.在实体类中添加字段，并添加TableField和TableLogic两个注解

~~~java
package cn.byuan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor// 创建无参的构造方法
@AllArgsConstructor// 创建满参的构造方法
@Accessors(chain = true)// 使用链式方法
@Data// 重写toString方法等方法
@TableName("tab_teacher")// 对应表名
public class Teacher implements Serializable {
    @TableId(value = "pk_teacher_id", type = IdType.AUTO)// 主键必须有TableId注解
    private Integer teacherId;

    @TableField("teacher_name")
    private String teacherName;

    @TableField("teacher_sex")
    private String teacherSex;

    @TableField("teacher_salary")
    private Double teacherSalary;

    @TableField(value = "create_time", fill = FieldFill.INSERT)// 插入时自动填充
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)// 插入和修改时自动填充
    private Date updateTime;
    
//    新添加进来的字段
    @TableField(value = "visibility", fill = FieldFill.INSERT)
    @TableLogic(value = "0", delval = "1")// 指定次字段为逻辑删除字段, 默认0是未删除, 1是已删除
    private Integer visibility;

}

~~~

### 3.在TeacherHandler中指定visibility字段的初始值

~~~java
package cn.byuan.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TeacherHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        
//        新添加的字段
        this.strictInsertFill(metaObject, "visibility", Integer.class, 0);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());

    }
}

~~~

### 4.进行测试

这里只测试两个方法，一个是删除指定id值的老师，一个是根据被删除的老师id查询该老师是否可以被查询

~~~java
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
~~~

可以看到，当我们执行delete方法时，实际上执行的是update方法

而查询语句并没有查询到该老师

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210706201519792-1459055827.png)

数据库中id为1的老师信息依然存在

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210706201713061-2031333407.png)

<h2 id="p4">四、实现分页功能</h2>

### 1.创建一个配置类，通过方法返回一个PaginationInterceptor

~~~java
package cn.byuan.conf;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("cn.byuan.dao")
public class PageConfig {
    @Bean
    public PaginationInterceptor PaginationInterceptor(){
        return new PaginationInterceptor();
    }
}

~~~

### 2.在service中根据selectPage方法进行分页

这里展示两种分页方式，**对表中所有数据进行分页**以及**根据条件进行分页**

接口：

~~~java
package cn.byuan.service;

import cn.byuan.entity.Teacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface TeacherService {
//    添加一位老师
    Integer addOneTeacher(Teacher teacher);

//    根据id删除一位老师
    Integer deleteOneTeacherByTeacherId(Integer teacherId);

//    修改一位老师的信息
    Integer updateOneTeacher(Teacher teacher);

//    根据id查询一位老师
    Teacher getOneTeacherByTeacherId(Integer teacherId);

//    获取所有老师
    List<Teacher> getAllTeacher();

//    该部分增加的方法
//    对表中所有信息进行分页, 传入参数为要查询的页数
    Page<Teacher> getAllTeacherPage(Integer pageNumber);

//    按条件(性别)进行分页
    Page<Teacher> getAllTeacherByTeacherSexPage(Integer pageNumber, String teacherSex);
}

~~~

实现类：

~~~java
package cn.byuan.service;

import cn.byuan.dao.TeacherDao;
import cn.byuan.entity.Teacher;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImp implements TeacherService{
    @Autowired
    private TeacherDao teacherDao;

    //    添加一位老师
    public Integer addOneTeacher(Teacher teacher){
        return teacherDao.insert(teacher);
    }

    //    根据id删除一位老师
    public Integer deleteOneTeacherByTeacherId(Integer teacherId){
        return teacherDao.deleteById(teacherId);
    }

    //    修改一位老师的信息
    public Integer updateOneTeacher(Teacher teacher){
        return teacherDao.updateById(teacher);
    }

    //    根据id查询一位老师
    public Teacher getOneTeacherByTeacherId(Integer teacherId){
        return teacherDao.selectById(teacherId);
    }

    //    获取所有老师
    public List<Teacher> getAllTeacher(){
        return teacherDao.selectList(null);
    }
    
    //    该部分增加的方法
    //    对表中所有信息进行分页, 传入参数为要查询的页数
    public Page<Teacher> getAllTeacherPage(Integer pageNumber){
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();

        Page<Teacher> teacherPage = new Page<>(pageNumber, 5);// 每页大小为5

        teacherDao.selectPage(teacherPage, teacherQueryWrapper);

        return teacherPage;
    }

    //    按条件(性别)进行分页
    public Page<Teacher> getAllTeacherByTeacherSexPage(Integer pageNumber, String teacherSex){
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();

        teacherQueryWrapper.eq("teacher_sex", teacherSex);// 根据条件进行分页, 这里填写的是表中的列名

        Page<Teacher> teacherPage = new Page<>(pageNumber, 5);

        teacherDao.selectPage(teacherPage, teacherQueryWrapper);
        return teacherPage;
    }
}

~~~

### 3.进行测试

~~~java
package cn.byuan;

import cn.byuan.entity.Teacher;
import cn.byuan.service.TeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TeacherDaoFourTests {
    @Autowired
    private TeacherService teacherService;

    @Test
    void getAllTeacherPageTest(){
        //    查看第三页
        Page<Teacher> teacherPage = teacherService.getAllTeacherPage(3);

        teacherPage.getRecords().forEach(System.out::println);
    }

    @Test
    void getAllTeacherByTeacherSexPageTest(){
        //    查询男生第1页的内容
        Page<Teacher> teacherPage = teacherService.getAllTeacherByTeacherSexPage(1, "男");

        teacherPage.getRecords().forEach(System.out::println);
    }
}

~~~

测试结果

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210706210335279-1244783865.png)

### 附：Page对象的一些常用方法

~~~java
Page<Object> page = new Page<>(1, 6);// 指定当前页, 每页记录数
page.getCurrent();// 获取当前页
page.getTotal();// 获取总记录数
page.getSize();// 获取每页的记录数
page.getRecords();// 获取当前页数据的集合
page.getPages();// 获取总页数
page.hasNext();// 是否存在下一页
page.hasPrevious();// 是否存在上一页
~~~



**源码地址：https://github.com/byuan98/springboot-integration/tree/master/test004_springboot_mybatisplus**