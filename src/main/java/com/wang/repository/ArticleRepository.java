package com.wang.repository;

import com.wang.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 文章Repository接口
 * @author halo 
 */
public interface ArticleRepository extends JpaRepository<Article, Integer>, JpaSpecificationExecutor<Article> {

    /**
     * n条热门文章（点击排行）
     * @param n
     * @return
     */
    @Query(value = "select * from t_article order by click desc limit 0,?1", nativeQuery = true)
    public List<Article> getClickArticle(Integer n);


    /**
     * n条置顶原创文章（博主推荐）
     * @param n
     * @return 
     * 
     */
    @Query(value = "select * from t_article where is_top=1 and is_original=1 order by publish_date desc limit 0,?1", nativeQuery = true)
    public List<Article> getRecommendArticle(Integer n);

    /**
     * 文章浏览量+1
     * @param articleId
     *
     */
    @Modifying
    @Query(value = "update t_article set click = click + 1 where article_id = ?1", nativeQuery = true)
    public void increaseClick(Integer articleId);

    /**
     * 评论数量+1
     * @param articleId
     */
    @Modifying
    @Query(value = "update t_article set comment_num = comment_num + 1 where article_id = ?1", nativeQuery = true)
    public void increaseComment(Integer articleId);

    /**
     * 评论数量-1
     * @param articleId
     */
    @Modifying
    @Query(value = "update t_article set comment_num = comment_num - 1 where article_id = ?1", nativeQuery = true)
    public void reduceComment(Integer articleId);

    /**
     * n条随机文章
     * @param n
     * @return
     */
    @Query(value = "select * from t_article order by rand() limit ?1", nativeQuery = true)
    public List<Article> getRandomArticle(Integer n);

    /**
     * 通过用户id查文章
     * @param userId
     * @return
     */
    @Query(value = "select * from t_article where user_id = ?1", nativeQuery = true)
    public List<Article> findByUserId(Integer userId);
}
