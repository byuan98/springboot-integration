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
