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
