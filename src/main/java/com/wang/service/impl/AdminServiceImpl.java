package com.wang.service.impl;

import com.wang.entity.Admin;
import com.wang.repository.AdminRepository;
import com.wang.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 管理员Service实现类
 * @author halo
 */

@Service("adminService")
@Transactional
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminRepository adminRepository;


    /**
     * 根据用户名和密码查找管理员信息
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public Admin findByUserNameAndPassword(String username, String password) {
        return adminRepository.findByUsernameAndPassword(username, password);
    }

    /**
     * 根据管理员id查找管理员信息
     *
     * @param adminId
     * @return
     */
    @Override
    public Admin findById(Integer adminId) {
        return adminRepository.findById(adminId).orElse(null);
    }

    /**
     * 保存管理员信息
     *
     * @param admin
     */
    @Override
    public void save(Admin admin) {
        adminRepository.save(admin);
    }
}
