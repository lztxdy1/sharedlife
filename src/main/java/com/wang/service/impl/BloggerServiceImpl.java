package com.wang.service.impl;

import com.wang.entity.Blogger;
import com.wang.repository.BloggerRepository;
import com.wang.service.BloggerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * 博主Service实现类
 * @author laptop
 */
@Service("bloggerService")
@Transactional
public class BloggerServiceImpl implements BloggerService {

    @Resource
    private BloggerRepository bloggerRepository;
    /**
     * 查询博主信息
     *
     * @return
     */
    @Override
    public Blogger find() {
        return bloggerRepository.findAll().get(0);
    }

    /**
     * 添加或删除博主
     *
     * @param blogger
     */
    @Override
    public void save(Blogger blogger) {
        bloggerRepository.save(blogger);
    }
}
