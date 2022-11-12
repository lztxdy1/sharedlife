package com.wang.service.impl;

import com.wang.entity.Reply;
import com.wang.repository.ReplyRepository;
import com.wang.service.ReplyService;
import com.wang.util.StringUtil;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
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
 * 回复Service接口实现
 */
@Service("replyService")
@Transactional
public class ReplyServiceImpl implements ReplyService {

    @Resource
    private ReplyRepository replyRepository;
    /**
     * 获取回复列表
     *
     * @param reply
     * @return
     */
    @Override
    public List<Reply> list(Reply reply) {

        Sort sort = new Sort(Sort.Direction.ASC, "replyDate");
        List<Reply> replyList = replyRepository.findAll((Specification<Reply>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (reply != null) {
                if (reply.getComment() != null) {
                    predicate.getExpressions().add(cb.equal(root.<String>get("comment"), reply.getComment().getCommentId()));
                }
            }
            return predicate;
        }, sort);
        return replyList;
    }

    /**
     * 分页查询回复
     *
     * @param reply
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Reply> list(Reply reply, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "replyDate");

        Page<Reply> replyPage = replyRepository.findAll(new Specification<Reply>() {
            @Override
            public Predicate toPredicate(Root<Reply> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (reply != null) {
                    if (reply.getComment() != null) {
                        predicate.getExpressions().add(cb.equal(root.<String>get("comment"), reply.getComment().getCommentId()));
                    }
                }
                return predicate;
            }
        }, pageable);

        return replyPage.getContent();
    }

    /**
     * 获取总记录数
     *
     * @return
     */
    @Override
    public Long getCount() {
        return replyRepository.count();
    }

    /**
     * 添加回复
     *
     * @param reply
     */
    @Override
    public void save(Reply reply) {
        replyRepository.save(reply);
    }

    /**
     * 根据id删除回复
     *
     * @param replyId
     */
    @Override
    public void delete(Integer replyId) {
        replyRepository.deleteById(replyId);
    }
}
