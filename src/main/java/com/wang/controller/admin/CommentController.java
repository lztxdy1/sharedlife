package com.wang.controller.admin;

import com.wang.entity.Comment;
import com.wang.entity.User;
import com.wang.service.ArticleService;
import com.wang.service.CommentService;
import com.wang.service.ReplyService;
import com.wang.service.UserService;
import com.wang.util.StringUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/admin/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private UserService userService;

    @Resource
    private ReplyService replyService;

    @Resource
    private ArticleService articleService;

    /**
     * 分页查询评论
     * @param comment
     * @param commentDates
     * @param page
     * @param pageSize
     * @param nickname
     * @return
     */
    @RequestMapping("/list")
    public Map<String, Object> list(Comment comment,
                                    @RequestParam(value = "commentDates", required = false) String commentDates,
                                    @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                    @RequestParam(value = "nickname", required = false) String nickname) {
        String s_bCommentDate = null;
        String s_eCommentDate = null;
        if (StringUtil.isNotEmpty(commentDates)) {
            String[] strArray = commentDates.split(" - ");
            s_bCommentDate = strArray[0];
            s_eCommentDate = strArray[1];
        }
        Integer userId = null;
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtil.isNotEmpty(nickname)) {
            User user = userService.getUserByTrueName(nickname);
            if (user != null) {
                userId = user.getUserId();
            }
            if (userId == null) {
                resultMap.put("errorInfo", "用户昵称不存在，没有评论！！！");
            } else {
                resultMap.put("errorNo", 0);
            }
        } else {
            resultMap.put("errorNo", 0);
        }
        List<Comment> commentList = commentService.list(comment, s_bCommentDate, s_eCommentDate, page - 1,
                pageSize, userId);
        Long total = commentService.getCount(comment, s_bCommentDate, s_eCommentDate, userId);
        resultMap.put("total", total);
        resultMap.put("data", commentList);
        return resultMap;
    }

    @RequestMapping("/delete")
    public Map<String, Object> delete(@RequestParam(value = "commentId") String ids) {
        String[] idsArray = ids.split(",");
        Map<String, Object> resultMap = new HashMap<>();
        for (int i = 0; i < idsArray.length; i++) {
            Integer articleId = commentService.getArticleIdByCommentId(Integer.parseInt(idsArray[i]));
            commentService.delete(Integer.parseInt(idsArray[i]));
            if (articleId != null) {
                articleService.reduceComment(articleId);
            }
        }
        resultMap.put("errorNo", 0);
        resultMap.put("data", 1);
        return resultMap;
    }
}
