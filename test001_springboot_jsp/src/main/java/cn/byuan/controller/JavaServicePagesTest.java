package cn.byuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/text")
public class JavaServicePagesTest {

    private static final String datePattern="yyyy-MM-dd E HH:mm:ss";

    @RequestMapping("/m1.action")
    public String methodOne(HttpServletRequest request){
        request.setAttribute("time", new SimpleDateFormat(datePattern).format(new Date()));
        return "test";
    }
}
