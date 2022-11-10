package com.wang.repository;

import com.wang.entity.Blogger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 博主Repository接口
 * @author laptop
 */
public interface BloggerRepository extends JpaRepository<Blogger, Integer>, JpaSpecificationExecutor<Blogger> {



}
