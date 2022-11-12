package com.wang.service;

import com.wang.entity.Notice;

import java.util.List;

/**
 * 公告Service接口
 * @author laptop
 */
public interface NoticeService {

    /**
     * 分页查询公告
     * @param notice
     * @param page
     * @param pageSize
     * @return
     */
    public List<Notice> list(Notice notice, Integer page, Integer pageSize);

    /**
     * 获取总记录数
     * @param notice
     * @return
     */
    public Long getCount(Notice notice);


    /**
     * 通过noticeId查询Notice
     * @param noticeId
     * @return
     */
    public Notice findByNoticeId(Integer noticeId);

    /**
     * 添加或修改Notice
     * @param notice
     */
    public void save(Notice notice);

    /**
     * 根据id删除Notice
     * @param noticeId
     */
    public void delete(Integer noticeId);
}
