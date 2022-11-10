package com.wang.repository;

import com.wang.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 用户Repository接口
 * @author halo
 */
public interface AdminRepository extends JpaRepository<Admin, Integer>, JpaSpecificationExecutor<Admin> {

    public Admin findByUsernameAndPassword(String username, String password);

}
