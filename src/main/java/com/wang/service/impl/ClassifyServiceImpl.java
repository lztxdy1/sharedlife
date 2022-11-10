package com.wang.service.impl;

import com.wang.entity.Classify;
import com.wang.repository.ClassifyRepository;
import com.wang.service.ClassifyService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

/**
 * 分类Service实现
 * @author laptop
 */
@Service("classifyService")
@Transactional
public class ClassifyServiceImpl implements ClassifyService {

    @Resource
    private ClassifyRepository classifyRepository;
    /**
     * 分页查询文章类别
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Classify> list(Integer page, Integer pageSize) {
        return classifyRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.ASC, "classifyId")).getContent();
    }

    /**
     * 获取总记录数
     *
     * @return
     */
    @Override
    public Long getCount() {
        return classifyRepository.count();
    }

    /**
     * 根据文章id查询类别
     *
     * @param articleId
     * @return
     */
    @Override
    public Classify findByArticleId(Integer articleId) {
        return classifyRepository.getOne(articleId);
    }

    /**
     * 添加或修改分类
     *
     * @param classify
     */
    @Override
    public void save(Classify classify) {
        classifyRepository.save(classify);
    }

    /**
     * 删除分类
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        classifyRepository.deleteById(id);
    }

    /**
     * 查询所有分类
     *
     * @return
     */
    @Override
    public List<Classify> findAll() {
        return classifyRepository.findAll();
    }
}
