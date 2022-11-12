package com.wang.service;

import com.wang.entity.Link;

import java.util.ArrayList;
import java.util.List;

/**
 * 友情链接Service接口
 * @author laptop
 */
public interface LinkService {
    /**
     * 分页查询友情链接
     *
     * @param link
     * @param page
     * @param pageSize
     * @return
     */
    public List<Link> list(Link link, Integer page, Integer pageSize);

    /**
     * 获取总记录数
     *
     * @return
     */
    public Long getCount(Link link);

    /**
     * 根据linkId获取友情链接
     *
     * @param linkId
     * @return
     */
    public Link findByLinkId(Integer linkId);

    /**
     * 添加或修改友情链接
     *
     * @param link
     */
    public void save(Link link);

    /**
     * 根据id删除友情链接
     * @param linkId
     */
    public void delete(Integer linkId);
}
