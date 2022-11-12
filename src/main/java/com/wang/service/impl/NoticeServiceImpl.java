package com.wang.service.impl;

import com.wang.entity.Notice;
import com.wang.repository.NoticeRepository;
import com.wang.service.NoticeService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公共Service接口实现
 * @author laptop
 */
@Service("noticeService")
@Transactional
public class NoticeServiceImpl implements NoticeService {

    @Resource
    private NoticeRepository noticeRepository;
    /**
     * 分页查询公告
     *
     * @param notice
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Notice> list(Notice notice, Integer page, Integer pageSize) {
        return noticeRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "publishDate")).getContent();
    }

    /**
     * 获取总记录数
     *
     * @param notice
     * @return
     */
    @Override
    public Long getCount(Notice notice) {
        return noticeRepository.count();
    }

    /**
     * 通过noticeId查询Notice
     *
     * @param noticeId
     * @return
     */
    @Override
    public Notice findByNoticeId(Integer noticeId) {
        return noticeRepository.getOne(noticeId);
    }

    /**
     * 添加或修改Notice
     *
     * @param notice
     */
    @Override
    public void save(Notice notice) {
        noticeRepository.save(notice);
    }

    /**
     * 根据id删除Notice
     *
     * @param noticeId
     */
    @Override
    public void delete(Integer noticeId) {
        noticeRepository.deleteById(noticeId);
    }
}
