# SpringBoot整合servlet

## 一、创建SpringBoot项目，仅选择Web模块即可

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210703170353679-579336652.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210703170356204-627526713.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210703170358641-1680800139.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210703170400691-322105629.png)

## 二、在POM文件中添加依赖

~~~xml
<!-- 添加servlet依赖模块 -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
</dependency>
<!-- 添加jstl标签库依赖模块 -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
</dependency>
<!--添加tomcat依赖模块.-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
</dependency>
<!-- 使用jsp引擎，springboot内置tomcat没有此依赖 -->
<dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-jasper</artifactId>
</dependency>
~~~

## 三、创建servlet，通过注解@WebServlet指定name和url

**项目结构如下**

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210703171026596-2058311272.png)

~~~java
package cn.byuan.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "testOneServlet", urlPatterns = "/test_one")// 通过注解@WebServlet指定name和url
public class ServletTest extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("Hello, servlet");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
~~~

## 四、在启动类上添加注解，指定扫描servlet所在的包

~~~java
package cn.byuan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("cn.byuan.servlet")// 指定扫描servlet所在的包
public class Test002SpringbootServletApplication {

    public static void main(String[] args) {
        SpringApplication.run(Test002SpringbootServletApplication.class, args);
    }

}
~~~

## 五、启动项目进行测试

测试地址：http://localhost:8080/test_one

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210703171510683-5733258.png)

**源码地址：https://github.com/byuan98/springboot-integration/tree/master/test002_springboot_servlet**