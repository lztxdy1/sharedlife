package com.wang.controller;

import com.wang.entity.Article;
import com.wang.entity.User;
import com.wang.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * 前端页面请求
 */
@Controller
@RequestMapping("/foreground")
public class ForegroundController {

    @Resource
    private ArticleService articleService;

    /**
     * 文章专栏
     * @return
     */
    @RequestMapping("/article")
    public String article() {
        return "/foreground/article";
    }

    /**
     * 杂七杂八
     * @return
     */
    @RequestMapping("/mixed_pic")
    public String mixed_pic() {
        return "/foreground/mixed_pic";
    }

    /**
     * 时光轴
     * @return
     */
    @RequestMapping("/timeline")
    public String timeline() {
        return "/foreground/timeline";
    }

    /**
     * 关于本站
     * @return
     */
    @RequestMapping("/about")
    public String about() {
        return "/foreground/about";
    }

    public ModelAndView detail(@PathVariable(value = "id", required = false) Integer id, HttpSession httpSession) {
        ModelAndView mav = new ModelAndView();
        articleService.increaseClick(id);
        Article article = articleService.findById(id);
        Article article1 = new Article();
        article1.setClassify(article.getClassify());
        httpSession.setAttribute("similarityList", articleService.listArticle(article1, null, null, 0, 10));
        httpSession.setAttribute("RandomArticleList", articleService.getRandomArticle(10));

        // 判断是否被收藏
        boolean isCollected = false;
        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            String ids = user.getArticleIds();
            if (ids != null) {
                List<String> idsList = Arrays.asList(ids.split(","));
                if (idsList.contains(id.toString())) {
                    mav.addObject("flag", true);
                } else {
                    mav.addObject("flag", false);
                }
            } else {
                mav.addObject("flag", isCollected);
            }
        }
        mav.addObject("article", article);
        mav.setViewName("/foreground/detail");
        return mav;
    }
}
