package com.wang.service.impl;

import com.wang.entity.Comment;
import com.wang.repository.CommentRepository;
import com.wang.service.CommentService;
import com.wang.util.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

@Service("commentService")
@Transactional
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentRepository commentRepository;
    /**
     * 分页查询评论
     *
     * @param comment
     * @param s_bCommentDate
     * @param s_eCommentDate
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    @Override
    public List<Comment> list(Comment comment, String s_bCommentDate, String s_eCommentDate, Integer page, Integer pageSize, Integer userId) {
        Page<Comment> commentPage = commentRepository.findAll((Specification<Comment>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (StringUtil.isNotEmpty(s_bCommentDate)) {
                predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("commentDate").as(String.class), s_bCommentDate));
            }
            if (StringUtil.isNotEmpty(s_eCommentDate)) {
                predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("commentDate").as(String.class), s_eCommentDate));
            }
            if (comment != null) {
                if (comment.getArticle() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("articel").get("articleId"), comment.getArticle().getArticleId()));
                }
            }
            if (userId == null) {
                predicate.getExpressions().add(cb.equal(root.<String>get("user"), userId));
            }
            return predicate;
        }, PageRequest.of(page, pageSize, Sort.Direction.DESC, "commentDate"));
        return commentPage.getContent();
    }

    /**
     * 分页查询留言
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Comment> messageList(Integer page, Integer pageSize) {
        Page<Comment> commentPage = commentRepository.findAll((Specification<Comment>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            predicate.getExpressions().add(cb.isNull(root.get("article")));
            return predicate;
        }, PageRequest.of(page, pageSize, Sort.Direction.DESC, "commentDate"));
        return commentPage.getContent();
    }

    /**
     * 获取总记录数
     *
     * @param comment
     * @param s_bCommentDate
     * @param s_ePublishDate
     * @param userId
     * @return
     */
    @Override
    public Long getCount(Comment comment, String s_bCommentDate, String s_ePublishDate, Integer userId) {
        long count = commentRepository.count(new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (StringUtil.isNotEmpty(s_bCommentDate)) {
                    predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("commentDate").as(String.class), s_bCommentDate));
                }
                if (StringUtil.isNotEmpty(s_ePublishDate)) {
                    predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("commentDate").as(String.class), s_ePublishDate));
                }
                if (comment != null) {
                    if (comment.getArticle() != null) {
                        predicate.getExpressions().add(cb.equal(root.get("article").get("articleId"), comment.getArticle().getArticleId()));
                    }
                }
                if (userId == null) {
                    predicate.getExpressions().add(cb.equal(root.<String>get("user"), userId));
                }
                return predicate;
            }
        });
        return count;
    }

    /**
     * 获取总留言数
     *
     * @return
     */
    @Override
    public Long getCount2() {
        long count = commentRepository.count((Specification<Comment>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            predicate.getExpressions().add(cb.isNull(root.get("article")));
            return predicate;
        });
        return null;
    }

    /**
     * 删除评论
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        commentRepository.deleteById(id);
    }

    /**
     * 添加评论
     *
     * @param comment
     */
    @Override
    public void add(Comment comment) {
        commentRepository.save(comment);
    }

    /**
     * 根据评论id获取文章id
     *
     * @param id
     * @return
     */
    @Override
    public Integer getArticleIdByCommentId(Integer id) {
        return commentRepository.getArticleId(id);
    }
}
