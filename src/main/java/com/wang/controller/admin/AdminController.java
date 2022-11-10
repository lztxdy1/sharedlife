package com.wang.controller.admin;

import com.wang.service.AdminService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员控制
 * @author halo
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    // md5加密盐
    @Value("wyt")
    private String salt;

    private AdminService adminService;


}
