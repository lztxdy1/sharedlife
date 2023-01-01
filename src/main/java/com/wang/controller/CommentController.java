package com.wang.controller;

import com.wang.entity.Comment;
import com.wang.entity.User;
import com.wang.service.ArticleService;
import com.wang.service.CommentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private ArticleService articleService;

    /**
     * 分页查询评论
     *
     * @param comment
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public Map<String, Object> commentList(Comment comment, @RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Comment> commentList = commentService.list(comment, null, null, page - 1, pageSize, null);
        Long total = commentService.getCount(comment, null, null, null);
        int totalPage = (int) (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
        resultMap.put("data", commentList);
        resultMap.put("totalPage", totalPage);
        return resultMap;
    }

    /**
     * 分页查询评论
     *
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/messageList")
    public Map<String, Object> messageList(@RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Comment> messageList = commentService.messageList(page - 1, pageSize);
        Long total = commentService.getCount2();
        int totalPage = (int) (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
        resultMap.put("totalPage", totalPage);
        resultMap.put("data", messageList);
        return resultMap;
    }

    /**
     * 添加评论
     *
     * @param comment
     * @param session
     * @return
     */
    @RequestMapping("/add")
    public Map<String, Object> add(Comment comment, HttpSession session) {
        Map<String, Object> resultMap = new HashMap<>();
        User currentUser = (User) session.getAttribute("user");
        comment.setCommentDate(new Date());
        comment.setUser(currentUser);
        commentService.add(comment);
        if (comment.getArticle() != null) {
            articleService.increaseComment(comment.getArticle().getArticleId());
        }

        resultMap.put("comment", comment);
        resultMap.put("success", true);
        return resultMap;
    }
}
