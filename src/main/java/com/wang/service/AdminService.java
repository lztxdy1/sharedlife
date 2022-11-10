package com.wang.service;

import com.wang.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 管理员Service接口
 * @author halo
 */
public interface AdminService {

    /**
     * 根据用户名和密码查找管理员信息
     * @param username
     * @param password
     * @return
     */
    public Admin findByUserNameAndPassword(String username, String password);

    /**
     * 根据管理员id查找管理员信息
     * @param adminId
     * @return
     */
    public Admin findById(Integer adminId);

    /**
     * 保存管理员信息
     * @param admin
     */
    public void save(Admin admin);
}
