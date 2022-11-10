package com.wang.service;

import com.wang.entity.Classify;

import java.util.List;

/**
 * 博客分类Service接口
 * @author laptop
 */
public interface ClassifyService {

    /**
     * 分页查询文章类别
     * @param page
     * @param pageSize
     * @return
     */
    public List<Classify>  list(Integer page, Integer pageSize);

    /**
     * 获取总记录数
     * @return
     */
    public Long getCount();

    /**
     * 根据文章id查询类别
     * @param articleId
     * @return
     */
    public Classify findByArticleId(Integer articleId);

    /**
     * 添加或修改分类
     * @param classify
     */
    public void save(Classify classify);

    /**
     * 删除分类
     * @param id
     */
    public void delete(Integer id);

    /**
     * 查询所有分类
     * @return
     */
    public List<Classify> findAll();
}
