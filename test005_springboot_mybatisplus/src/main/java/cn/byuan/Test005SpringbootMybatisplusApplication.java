package cn.byuan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.byuan.dao")// 谁继承了BaseMapper就对谁进行扫描
public class Test005SpringbootMybatisplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(Test005SpringbootMybatisplusApplication.class, args);
    }

}
