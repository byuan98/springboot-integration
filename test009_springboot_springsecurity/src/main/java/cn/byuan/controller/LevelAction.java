package cn.byuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LevelAction {

    @RequestMapping({"/", "/index", "index.html"})
    public String goToIndex(){
        return "index";
    }

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
