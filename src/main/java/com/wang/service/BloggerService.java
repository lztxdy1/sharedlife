package com.wang.service;

import com.wang.entity.Blogger;

/**
 * 博主service接口
 * @author laptop
 */
public interface BloggerService {

    /**
     * 查询博主信息
     * @return
     */
    public Blogger find();

    /**
     * 添加或删除博主
     * @param blogger
     */
    public void save(Blogger blogger);
}
