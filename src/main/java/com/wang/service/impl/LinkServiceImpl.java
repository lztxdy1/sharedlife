package com.wang.service.impl;

import com.wang.entity.Link;
import com.wang.repository.LinkRepository;
import com.wang.service.LinkService;
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
import java.util.List;

/**
 * 友情链接Service接口实现
 *
 * @author laptop
 */
@Service("linkService")
@Transactional
public class LinkServiceImpl implements LinkService {

    @Resource
    private LinkRepository linkRepository;

    /**
     * 分页查询友情链接
     *
     * @param link
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Link> list(Link link, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "orderNum");
        Page<Link> linkPage = linkRepository.findAll((Specification<Link>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (link != null) {
                if (StringUtil.isNotEmpty(link.getLinkName())) {
                    predicate.getExpressions().add(cb.like(root.get("linkName"), "%" + link.getLinkName().trim() + "%"));
                }
                if (StringUtil.isNotEmpty(link.getLinkUrl())) {
                    predicate.getExpressions().add(cb.like(root.get("linkUrl"), "%" + link.getLinkUrl().trim() + "%"));
                }
            }
            return predicate;
        }, pageable);
        return linkPage.getContent();
    }

    /**
     * 获取总记录数
     *
     * @param link
     * @return
     */
    @Override
    public Long getCount(Link link) {
        long count = linkRepository.count(new Specification<Link>() {
            @Override
            public Predicate toPredicate(Root<Link> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (link != null) {
                    if (StringUtil.isNotEmpty(link.getLinkName())) {
                        predicate.getExpressions().add(cb.like(root.get("linkName"), "%" + link.getLinkName().trim() + "%"));
                    }
                    if (StringUtil.isNotEmpty(link.getLinkUrl())) {
                        predicate.getExpressions().add(cb.like(root.get("linkUrl"), "%" + link.getLinkUrl().trim() + "%"));
                    }
                }
                return predicate;
            }
        });
        return count;
    }

    /**
     * 根据linkId获取友情链接
     *
     * @param linkId
     * @return
     */
    @Override
    public Link findByLinkId(Integer linkId) {
        return linkRepository.getOne(linkId);
    }

    /**
     * 添加或修改友情链接
     *
     * @param link
     */
    @Override
    public void save(Link link) {
        linkRepository.save(link);
    }

    /**
     * 根据id删除友情链接
     *
     * @param linkId
     */
    @Override
    public void delete(Integer linkId) {
        linkRepository.deleteById(linkId);
    }
}

