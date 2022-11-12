package com.wang.service.impl;

import com.wang.entity.Timeline;
import com.wang.repository.TimelineRepository;
import com.wang.service.TimelineService;
import com.wang.util.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 时光轴Service接口实现
 * @author laptop
 */
@Service("timelineService")
@Transactional
public class TimelineServiceImpl implements TimelineService {

    @Resource
    private TimelineRepository timelineRepository;
    /**
     * 分页查询时光轴
     *
     * @param timeline
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Timeline> list(Timeline timeline, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "publishDate");
        Page<Timeline> timelinePage = timelineRepository.findAll((Specification<Timeline>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (timeline != null) {
                if (StringUtil.isNotEmpty(timeline.getYear())) {
                    predicate.getExpressions().add(cb.like(root.get("year"), "%" + timeline.getYear().trim() + "%"));
                }
                if (StringUtil.isNotEmpty(timeline.getMonth())) {
                    predicate.getExpressions().add(cb.like(root.get("month"), "%" + timeline.getMonth().trim() + "%"));
                }
            }
            return predicate;
        }, pageable);
        return timelinePage.getContent();
    }

    /**
     * 获取总记录数
     *
     * @param timeline
     * @return
     */
    @Override
    public Long getCount(Timeline timeline) {
        long count = timelineRepository.count((Specification<Timeline>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (timeline != null) {
                if (StringUtil.isNotEmpty(timeline.getYear())) {
                    predicate.getExpressions().add(cb.like(root.get("year"), "%" + timeline.getYear().trim() + "%"));
                }
                if (StringUtil.isNotEmpty(timeline.getMonth())) {
                    predicate.getExpressions().add(cb.like(root.get("month"), "%" + timeline.getMonth().trim() + "%"));
                }
            }
            return predicate;
        });
        return count;
    }

    /**
     * 根据id查询时光轴
     *
     * @param timelineId
     * @return
     */
    @Override
    public Timeline findByTimelineId(Integer timelineId) {
        return timelineRepository.getOne(timelineId);
    }

    /**
     * 添加或修改时光轴
     *
     * @param timeline
     */
    @Override
    public void save(Timeline timeline) {
        timelineRepository.save(timeline);
    }

    /**
     * 通过id删除时光轴
     *
     * @param timelineId
     */
    @Override
    public void delete(Integer timelineId) {
        timelineRepository.deleteById(timelineId);
    }
}
