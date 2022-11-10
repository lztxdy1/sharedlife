package com.wang.repository;

import com.wang.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 友情链接Repository接口
 * @author laptop
 */
public interface LinkRepository extends JpaRepository<Link, Integer>, JpaSpecificationExecutor<Link> {

}
