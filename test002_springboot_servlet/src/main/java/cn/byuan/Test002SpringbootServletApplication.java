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
