package com.wang.service.impl;

import com.wang.entity.Admin;
import com.wang.service.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminServiceTest {

    @Resource
    public AdminService adminService;

    @Test
    public void save() {
        Admin admin = new Admin();
        admin.setAdminId(123456);
        admin.setUserName("xiaowang");
        admin.setPassword("123456");
        admin.setTrueName("小王");
        admin.setHeadPortrait("");
        admin.setSex("男");
        admin.setSignature("");
        admin.setPhone("12345678901");
        adminService.save(admin);
    }

    @Test
    public void findById() {
        System.out.println(adminService.findById(1));
    }

    @Test
    public void findByUserNameAndPassword() {
        System.out.println(adminService.findByUserNameAndPassword("halo", "123456"));
    }
}
