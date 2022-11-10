package com.wang.repository;

import com.wang.entity.Classify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 文章类别Repository接口
 * @author laptop
 */
public interface ClassifyRepository extends JpaRepository<Classify, Integer>, JpaSpecificationExecutor<Classify> {

}
