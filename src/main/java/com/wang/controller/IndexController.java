package com.wang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 根路径及其他路径处理
 * @author laptop
 */
@Controller
public class IndexController {

    /**
     * 后台默认首页
     * @return
     */
    @RequestMapping("/index")
    public String root() {
        return "/common/index";
    }
}
