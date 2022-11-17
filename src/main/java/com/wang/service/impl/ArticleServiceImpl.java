package com.wang.service.impl;

import com.wang.entity.Article;
import com.wang.repository.ArticleRepository;
import com.wang.service.ArticleService;
import com.wang.util.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 文章Service接口实现类
 * @author halo
 */
@Service("articleService")
@Transactional
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleRepository articleRepository;
    /**
     * 分页查询文章
     *
     * @param article
     * @param s_bPublishDate
     * @param s_ePublishDate
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Article> listArticle(Article article, String s_bPublishDate, String s_ePublishDate, Integer page, Integer pageSize) {
        List<Sort.Order> orders = new ArrayList<Order>();
        orders.add(new Order(Direction.DESC, "isTop"));
        orders.add(new Order(Direction.DESC, "publishDate"));
        Page<Article> articlePage = articleRepository.findAll(new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (StringUtil.isNotEmpty(s_bPublishDate)) {
                    predicate.getExpressions()
                            .add(cb.greaterThanOrEqualTo(root.get("publishDate").as(String.class), s_bPublishDate));
                }

                if (StringUtil.isNotEmpty(s_ePublishDate)) {
                    predicate.getExpressions()
                            .add(cb.greaterThanOrEqualTo(root.get("publishDate").as(String.class), s_ePublishDate));
                }

                if (article != null) {
                    if (StringUtil.isNotEmpty(article.getTitle())) {
                        predicate.getExpressions()
                                .add(cb.like(root.get("title"), "%" + article.getTitle().trim() + "%"));
                    }

                    if (article.getClassify() != null) {
                        predicate.getExpressions()
                                .add(cb.equal(root.get("classify").get("classifyId"), article.getClassify().getClassifyId()));
                    }

                    if (article.getUserId() != null) {
                        predicate.getExpressions()
                                .add(cb.equal(root.get("userId"), article.getUserId()));
                    }
                }
                return predicate;
            }
        }, PageRequest.of(page, pageSize, Sort.by(orders)));
        return articlePage.getContent();
    }

    /**
     * 查询所有文章
     *
     * @return
     */
    @Override
    public List<Article> listArticle() {
        return articleRepository.findAll();
    }

    /**
     * n条置顶原创文章（博主推荐）
     *
     * @param n
     * @return
     */
    @Override
    public List<Article> getRecommendArticle(Integer n) {
        return articleRepository.getRecommendArticle(n);
    }

    /**
     * 获取总记录数
     *
     * @param article
     * @param s_bPublishDate
     * @param s_ePublishDate
     * @return
     */
    @Override
    public Long getCount(Article article, String s_bPublishDate, String s_ePublishDate) {
        Long count = articleRepository.count(new Specification<Article>() {

            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (StringUtil.isNotEmpty(s_bPublishDate)) {
                    predicate.getExpressions()
                            .add(cb.greaterThanOrEqualTo(root.get("publishDate").as(String.class), s_bPublishDate));
                }

                if (StringUtil.isNotEmpty(s_ePublishDate)) {
                    predicate.getExpressions()
                            .add(cb.greaterThanOrEqualTo(root.get("publishDate").as(String.class), s_ePublishDate));
                }

                if (article != null) {
                    if (StringUtil.isNotEmpty(article.getTitle())) {
                        predicate.getExpressions()
                                .add(cb.like(root.get("title"), "%" + article.getTitle().trim() + "%"));
                    }

                    if (article.getClassify() != null) {
                        predicate.getExpressions()
                                .add(cb.equal(root.get("classify").get("classifyId"), article.getClassify().getClassifyId()));
                    }

                    if (article.getUserId() != null) {
                        predicate.getExpressions()
                                .add(cb.equal(root.get("userId"), article.getUserId()));
                    }
                }
                return predicate;
            }
        });
        return count;
    }

    /**
     * 根据id查询文章
     *
     * @param articleId
     * @return
     */
    @Override
    public Article findById(Integer articleId) {
        return articleRepository.getOne(articleId);
    }

    /**
     * 保存文章
     *
     * @param article
     */
    @Override
    public void save(Article article) {
        articleRepository.save(article);

    }

    /**
     * 删除文章
     *
     * @param articleId
     */
    @Override
    public void delete(Integer articleId) {
        articleRepository.deleteById(articleId);
    }

    /**
     * 浏览数量+1
     *
     * @param articleId
     */
    @Override
    public void increaseClick(Integer articleId) {
        articleRepository.increaseClick(articleId);
    }

    /**
     * 评论数量+1
     *
     * @param articleId
     */
    @Override
    public void increaseComment(Integer articleId) {
        articleRepository.increaseComment(articleId);
    }

    /**
     * 评论数量-1
     *
     * @param articleId
     */
    @Override
    public void reduceComment(Integer articleId) {
        articleRepository.reduceComment(articleId);
    }

    /**
     * n条随机文章
     *
     * @param n
     * @return
     */
    @Override
    public List<Article> getRandomArticle(Integer n) {
        return articleRepository.getRandomArticle(n);
    }

    /**
     * n条热门文章（点击排行）
     *
     * @param n
     * @return
     */
    @Override
    public List<Article> getClickArticle(Integer n) {
        return articleRepository.getClickArticle(n);
    }

    /**
     * 获取笔记总数
     *
     * @return
     */
    @Override
    public Long getCount() {
        return articleRepository.count();
    }

    /**
     * 查询用户收藏的文章
     *
     * @param retIds
     * @return
     */
    @Override
    public List<Article> findByListId(List<Integer> retIds) {
        return articleRepository.findAllById(retIds);
    }

    /**
     * 根据用户id查询文章
     *
     * @param userId
     * @return
     */
    @Override
    public List<Article> findByUserId(Integer userId) {
        return articleRepository.findByUserId(userId);
    }


}
