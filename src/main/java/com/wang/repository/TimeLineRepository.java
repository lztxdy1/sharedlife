package com.wang.repository;

import com.wang.entity.Timeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 时间轴Repository接口
 * @author laptop
 */
public interface TimeLineRepository extends JpaRepository<Timeline, Integer>, JpaSpecificationExecutor<Timeline> {

}
