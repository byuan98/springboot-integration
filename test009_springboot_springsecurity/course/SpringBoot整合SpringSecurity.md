# SpringBoot整合SpringSecurity

## 一、创建项目，选择依赖

**选择Spring Web、Thymeleaf即可**

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210710153230287-685032638.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210710153232714-1261187010.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210710153459338-1108295273.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210710153243048-721916923.png)

## 二、在pom文件中导入相关依赖

~~~xml
<!-- 导入SpringSecurity的启动器 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
~~~

## 三、在resources\templates下准备页面

**目录结构如下**

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210710153951862-1965681962.png)

**index.html**

~~~html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>index</title>
</head>
<body>
    <div  align="center">
        <h1>Welcome to index</h1>
        <div>
            <!-- 这里的url是controller层的url -->
            <a th:href="@{/level_1/gotoHtml}">请求level_1</a>
        </div>

        <div>
            <a th:href="@{/level_2/gotoHtml}">请求level_2</a>
        </div>

        <div>
            <a th:href="@{/level_3/gotoHtml}">请求level_3</a>
        </div>

        <!-- 为稍后SpringSecurity的退出登录功能做准备 -->
        <a th:href="@{/logout}">登出</a>

    </div>

</body>
</html>
~~~

**level_1.html、level_2.html、level_3.html内容相同，在此不多赘述，将数字部分替换即可**

~~~html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>level_1</title>
</head>
<body>
    <div align="center">
        <h1>Welcome to level_1</h1>

        <a th:href="@{/}">回到index</a>

    </div>

</body>
</html>
~~~

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210710154641504-1383631562.png)

## 四、构建controller层

~~~java
package cn.byuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LevelAction {

    @RequestMapping({"/", "/index", "index.html"})
    public String goToIndex(){
        return "index";
    }

//    这里的url就是上面index.html中a标签中出现的url    
    @RequestMapping("/level_1/gotoHtml")
    public String goToLevel1(){
        return "level_1";
    }

    @RequestMapping("/level_2/gotoHtml")
    public String goToLevel2(){
        return "level_2";
    }

    @RequestMapping("/level_3/gotoHtml")
    public String goToLevel3(){
        return "level_3";
    }
}

~~~

## 五、创建配置类，进行SpringSecurity的相关配置

SpringSecrity的两大核心：**认证（Authentication）**、**授权（Authorization）**

**SpringSecurity的主要类**

| 主要类                       | 含义               |
| ---------------------------- | ------------------ |
| @EnableWebSecurity           | 开启WebSecurity    |
| WebSecurityConfigurerAdapter | 自定义security策略 |
| AuthenticationManagerBuilder | 自定义认证策略     |

**创建配置类**

~~~java
package cn.byuan.config;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity// 开启WebSecurity模块
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
}
~~~

**光标移入花括号内，按下 ctrl + o**

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210710160446988-682878812.png)

~~~java
package cn.byuan.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity// 开启WebSecurity模块
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /*
     * 配置授权规则
     * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        添加请求授权规则
        http.authorizeRequests()
                .antMatchers("/").permitAll()// 首页所有人都可以访问
                .antMatchers("/level_1/**").hasRole("vip1")// level_1下的所有请求, vip1用户才可以访问
                .antMatchers("/level_2/**").hasRole("vip2")// level_2下的所有请求, vip2用户才可以访问
                .antMatchers("/level_3/**").hasRole("vip3");// level_3下的所有请求, vip3用户才可以访问

        http.formLogin();// 开启登录页面, 即无权限的话跳转到登录页面, 默认地址: /login, 这是为了有人直接访问权限范围内某一url

        http.logout().logoutSuccessUrl("/");// 注销后跳转到首页

        http.rememberMe();// 开启记住我功能, 默认保存两周, 底层使用cookie机制实现
    }

    /*
     * 配置认证规则
     *
     * 在新版本的SpringSecurity中新增了许多加密方法, 不使用加密的话就会出现异常
     * 这里我们在内存中对用户进行模拟, 真正的开发过程中会使用数据库
     *
     * */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("root").password(new BCryptPasswordEncoder().encode("root")).roles("vip1", "vip2", "vip3")
                .and()
                .withUser("zlf").password(new BCryptPasswordEncoder().encode("zlf")).roles("vip1", "vip2")
                .and()
                .withUser("user").password(new BCryptPasswordEncoder().encode("user")).roles("vip1");
    }
}

~~~

## 六、测试

打开浏览器，输入地址：http://localhost:8080/ 敲击回车

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210710160924554-2072235300.png)

点击：请求level_1，会自动跳转至登录页面，输入账号、密码，点击Sign in

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210710161219812-1505432078.png)

由于root拥有所有页面的访问权限，因此访问成功

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210710161415385-802520573.png)

点击回到index，点击退出登录，切换其他账号进行测试

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210710161537902-1138926258.png)
![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210710161539939-354996649.png)

这次我们使用user账号来访问level_2，user只有level_1的访问权限

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210710161739979-1238777055.png)

可以看到，如果没有权限访问指定的url，那么会报错误：403

![](https://img2020.cnblogs.com/blog/1908772/202107/1908772-20210710161859321-245781491.png)



**源码地址：https://github.com/byuan98/springboot-integration/tree/master/test009_springboot_springsecurity**