package com.wang.service;

import com.wang.entity.Comment;

import java.util.List;

/**
 * 评论Service接口
 * @author laptop
 */
public interface CommentService {

    /**
     * 分页查询评论
     * @param comment
     * @param s_bCommentDate
     * @param s_eCommentDate
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    public List<Comment> list(Comment comment, String s_bCommentDate, String s_eCommentDate, Integer page, Integer pageSize, Integer userId);

    /**
     * 分页查询留言
     * @param page
     * @param pageSize
     * @return
     */
    public List<Comment> messageList(Integer page, Integer pageSize);

    /**
     * 获取总记录数
     * @param comment
     * @param s_bCommentDate
     * @param s_ePublishDate
     * @param userId
     * @return
     */
    public Long getCount(Comment comment, String s_bCommentDate, String s_ePublishDate, Integer userId);

    /**
     * 获取总留言数
     * @return
     */
    public Long getCount2();

    /**
     * 删除评论
     * @param id
     */
    public void delete(Integer id);

    /**
     * 添加评论
     * @param comment
     */
    public void add(Comment comment);

    /**
     * 根据评论id获取文章id
     * @param id
     * @return
     */
    public Integer getArticleIdByCommentId(Integer id);
}
