package com.wang.service;

import com.wang.entity.Reply;

import java.util.List;

/**
 * 回复Service接口
 * @author laptop
 */
public interface ReplyService {

    /**
     * 获取回复列表
     * @param reply
     * @return
     */

    public List<Reply> list(Reply reply);
    /**
     * 分页查询回复
     * @param reply
     * @param page
     * @param pageSize
     * @return
     */
    public List<Reply> list(Reply reply, Integer page, Integer pageSize);

    /**
     * 获取总记录数
     * @return
     */
    public Long getCount();

    /**
     * 添加回复
     * @param reply
     */
    public void save(Reply reply);

    /**
     * 根据id删除回复
     * @param replyId
     */
    public void delete(Integer replyId);
}
