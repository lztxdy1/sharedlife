package com.wang.service;

import com.wang.entity.Timeline;

import java.util.List;

/**
 * 时光轴Service接口
 * @author laptop
 */
public interface TimelineService {

    /**
     * 分页查询时光轴
     * @param timeline
     * @param page
     * @param pageSize
     * @return
     */
    public List<Timeline> list(Timeline timeline, Integer page, Integer pageSize);

    /**
     * 获取总记录数
     * @return
     */
    public Long getCount(Timeline timeline);

    /**
     * 根据id查询时光轴
     * @param timelineId
     * @return
     */
    public Timeline findByTimelineId(Integer timelineId);

    /**
     * 添加或修改时光轴
     * @param timeline
     */
    public void save(Timeline timeline);

    /**
     * 通过id删除时光轴
     * @param timelineId
     */
    public void delete(Integer timelineId);
}
