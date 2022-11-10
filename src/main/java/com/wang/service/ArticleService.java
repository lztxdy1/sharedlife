package com.wang.service;

import com.wang.entity.Article;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

/**
 * 文章Service接口
 * @author halo
 */
public interface ArticleService {

    /**
     * 分页查询文章
     * @param article
     * @param s_bPublishDate
     * @param s_ePublishDate
     * @param page
     * @param pageSize
     * @return
     */
    public List<Article> listArticle(Article article, String s_bPublishDate, String s_ePublishDate,
                                     Integer page, Integer pageSize);

    /**
     * 查询所有文章
     * @return
     */
    public List<Article> listArticle();

    /**
     * n条置顶原创文章（博主推荐）
     * @param n
     * @return
     */
    public List<Article> getRecommendArticle(Integer n);

    /**
     * 获取总记录数
     * @param article
     * @param s_bPublishDate
     * @param s_ePublishDate
     * @return
     */
    public Long getCount(Article article, String s_bPublishDate, String s_ePublishDate);

    /**
     * 根据id查询文章
     * @param articleId
     * @return
     */
    public Article findById(Integer articleId);

    /**
     * 保存文章
     * @param article
     */
    public void save(Article article);

    /**
     * 删除文章
     * @param articleId
     */
    public void delete(Integer articleId);

    /**
     * 浏览数量+1
     * @param articleId
     */
    @Modifying
    public void increaseClick(Integer articleId);

    /**
     * 评论数量+1
     * @param articleId
     */
    @Modifying
    public void increaseComment(Integer articleId);

    /**
     * 评论数量-1
     * @param articleId
     */
    @Modifying
    public void reduceComment(Integer articleId);

    /**
     * n条随机文章
     * @param n
     * @return
     */
    public List<Article> getRandomArticle(Integer n);

    /**
     * n条热门文章（点击排行）
     * @param n
     * @return
     */
    public List<Article> getClickArticle(Integer n);

    /**
     * 获取笔记总数
     * @return
     */
    public Long getCount();

    /**
     * 查询用户收藏的文章
     * @param retIds
     * @return
     */
    public List<Article> findByListId(List<Integer> retIds);

    /**
     * 根据用户id查询文章
     * @param userId
     * @return
     */
    public List<Article> findByUserId(Integer userId);
}
