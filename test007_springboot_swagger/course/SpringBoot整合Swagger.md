# SpringBoot整合Swagger

**项目完整目录如下**

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210708212625772-1833175967.png)

## 一、创建项目，选择依赖

仅选择**Spring Web**即可

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210708210257920-936226648.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210708210300190-1841056828.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210708210302618-1537454328.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210708210304235-920037103.png)

## 二、在pom文件中引入相关依赖

~~~xml
<!-- 引入lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>

<!-- 引入swagger相关的jar -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>

<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>
~~~

## 三、创建Swagger的配置类，并进行配置

~~~java
package cn.byuan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.byuan.controller"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .title("SpringBoot整合Swagger")
                        .description("详细信息")
                        .version("1.0")
                        .contact(new Contact(
                                "mtb",
                                "https://www.cnblogs.com/byuan",
                                "byuan98@outlook.com"))
                        .license("The Apache License")
                        .licenseUrl("https://github.com/byuan98/springboot-integration")
                        .build()
                );
    }
}

~~~

## 四、发布项目，打开浏览器访问swagger的ui进行测试

  http://localhost:8080/swagger-ui.html

可正常打开

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210708211226174-1783801295.png)

## 五、创建实体类

~~~java
package cn.byuan.entity;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "学生id")// 对属性进行简要说明
    private Integer studentId;

    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "学生分数")
    private Double studentScore;

}
~~~

## 六、创建vo，对返回结果进行封装

~~~java
package cn.byuan.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

//定义一个返回结果类
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class ResponseVo<E> {
    private String message; //操作的提示信息
    private Integer status; //响应状态码
    private E data; //获取数据
}

~~~

## 七、创建service层

~~~java
package cn.byuan.service;

import cn.byuan.entity.Student;
import cn.byuan.vo.ResponseVo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {
//    这里我们不适用数据库, 使用Map集合来模拟数据库中的表    
        private static Map<Integer, Student> studentMap=new HashMap<>();
        private static Integer studentId=10001;
        static {
            studentMap.put(studentId, new Student(studentId, "Godfery", 98.5));
            studentId++;
            studentMap.put(studentId, new Student(studentId, "Echo", 95.5));
            studentId++;
            studentMap.put(studentId, new Student(studentId, "Abi", 96.5));
            studentId++;
        }

//    插入一名学生返回影响行数
        public ResponseVo<Integer> addOneStudent(Student student){
            student.setStudentId(studentId);
            studentMap.put(studentId, student);
            studentId++;
            return new ResponseVo<>("插入一条数据成功", 200, 1);
        }

//    删除一位学生返回影响行数
        public  ResponseVo<Integer> deleteOneStudentByStudentId(Integer studentId){
            if(studentMap.containsKey(studentId) == false){
                return new ResponseVo<>("您输入的id不存在", 200, 0);
            }
            studentMap.remove(studentId);
            return new ResponseVo<>("删除成功", 200, 1);
        }

//    修改一位学生返回影响行数
        public ResponseVo<Integer> updateOneStudent(Student student){
            if(studentMap.containsKey(student.getStudentId()) == false){
                return new ResponseVo<>("根据学生id,您所修改的学生不存在", 200, 0);
            }
            studentMap.put(student.getStudentId(), student);
            return new ResponseVo<>("学生修改成功", 200, 1);
        }

//    输入studentId查询并返回对应的学生
        public ResponseVo<Student> getOneStudentByStudentId(Integer studentId){
            if(studentMap.containsKey(studentId) == false){
                return new ResponseVo<>("您所查询的学生不存在", 200, null);
            }
            return new ResponseVo<>("查询成功", 200, studentMap.get(studentId));
        }

//    获取所有学生
        public ResponseVo<Collection<Student>> getAllStudent(){
            return new ResponseVo<>("获取全部学生成功", 200, studentMap.values());
        }
}

~~~

## 八、创建controller层

~~~java
package cn.byuan.controller;

import cn.byuan.entity.Student;
import cn.byuan.service.StudentService;
import cn.byuan.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Api(tags = "学生管理相关接口")
@RestController //@Controller + @ResponseBody
@RequestMapping("/student")
public class StudentAction {
    @Autowired
    private StudentService studentService;

    @ApiOperation("添加一名学生")// 为每个handler添加方法功能描述
    @PostMapping("/add_student.action")
    @ApiImplicitParam(name = "student", value = "所添加的学生", dataTypeClass = Student.class)
    public ResponseVo<Integer> addOneStudent(Student student) {
        return studentService.addOneStudent(student);
    }

    @ApiOperation("根据studentId删除一名学生")
    @DeleteMapping("/delete_student/{studentId}.action")
    public ResponseVo<Integer> deleteOneStudentByStudentId(@PathVariable Integer studentId) {
        return studentService.deleteOneStudentByStudentId(studentId);
    }

    @ApiOperation("修改一名学生")
    @PutMapping("/update_student.action")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "studentId", value = "学号", required = true), //required为是否必填项
            @ApiImplicitParam(name = "studentName", value = "学生姓名", required = false),
            @ApiImplicitParam(name = "studentSex", value = "学生性别", required = false),
            @ApiImplicitParam(name = "studentScore", value = "学生分数", required = false)
    })
    public ResponseVo<Integer> updateOneStudent(Student student) {
        return studentService.updateOneStudent(student);
    }

    @ApiOperation("根据id获取一名学生")
    @GetMapping("/get_ont_student/{studentId}.action")
    public ResponseVo<Student> getOntStudentByStudentId(@PathVariable Integer studentId) {
        return studentService.getOneStudentByStudentId(studentId);
    }

    @ApiOperation("获取全部学生")
    @GetMapping("/get_all_student.action")
    public ResponseVo<Collection<Student>> getAllStudent() {
        return studentService.getAllStudent();
    }
}
~~~

## 九、重新启动项目，使用浏览器访问swagger的url进行测试

  http://localhost:8080/swagger-ui.html

**以获取全部学生为例**

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210708213204895-1877606831.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210708213209401-1814682540.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210708213211921-1989981193.png)

**源码地址：https://github.com/byuan98/springboot-integration/tree/master/test007_springboot_swagger**

