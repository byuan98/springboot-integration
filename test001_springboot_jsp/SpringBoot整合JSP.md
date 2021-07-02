# SpringBoot整合JSP

## 一、创建SpringBoot项目，仅选择Web模块即可
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210702221900000-792891886.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210702221906913-1232062031.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210702221911658-741798323.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210702221920552-1251331918.png)

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

## 三、创建目录：webapp/WEB-INF、webapp/jsps
**注意：一定要在main目录下**
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210702222051355-1639968669.png)

## 四、修改步骤三中的目录结构
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210702222113317-1504301400.png)

## 五、在核心配置文件中添加视图解析器的前缀和后缀

**文件位置：src/main/resources/application.properties**

~~~properties
spring.mvc.view.prefix=/jsps/

spring.mvc.view.suffix=.jsp
~~~

## 六、创建JSP
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210702222711619-717927670.png)

**文件位置：webapp/jsps/test.jsp**

~~~jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
    <body>
        <h1>Hello, test.jsp</h1><br/>
        
        1+1=${1+1}<br/>
        
        ${requestScope.time}<br/>
    </body>
</html>
~~~

## 七、创建一个action进行测试
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210702222142161-718818032.png)

~~~java
package cn.byuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/test")
public class JavaServicePagesTest {
    private static final String datePattern="yyyy-MM-dd E HH:mm:ss";
    @RequestMapping("/m1.action")
    public String methodOne(HttpServletRequest request){
        request.setAttribute("time", new SimpleDateFormat(datePattern).format(new Date()));
        return "test";
    }
}
~~~

## 八、配置web resources directorys
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210702222155077-473393612.png)

## 九、运行项目，打开浏览器输入URL
http://localhost:8080/text/m1.action
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210702222415034-422709856.png)

