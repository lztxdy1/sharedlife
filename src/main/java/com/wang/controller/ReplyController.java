package com.wang.controller;

import com.wang.entity.Reply;
import com.wang.entity.User;
import com.wang.service.ReplyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回复控制器
 */
@RestController
@RequestMapping("/reply")
public class ReplyController {

    @Resource
    private ReplyService replyService;

    /**
     * 获取回复
     *
     * @param reply
     * @return
     */
    @RequestMapping("/list")
    public Map<String, Object> list(Reply reply) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Reply> replyList = replyService.list(reply);
        resultMap.put("data", replyList);
        return resultMap;
    }

    /**
     * 添加回复
     *
     * @param reply
     * @param session
     * @return
     */
    @RequestMapping("/add")
    public Map<String, Object> add(Reply reply, HttpSession session) {
        Map<String, Object> resultMap = new HashMap<>();
        User currentUser = (User) session.getAttribute("user");
        reply.setReplyDate(new Date());
        reply.setUser(currentUser);
        replyService.save(reply);
        resultMap.put("reply", reply);
        resultMap.put("success", true);
        return resultMap;
    }
}
