package com.wang.controller.admin;

import com.wang.entity.Article;
import com.wang.service.ArticleService;
import com.wang.util.StringUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 文章控制器
 * @author laptop
 */
@RestController
@RequestMapping("/admin/article")
public class ArticleAdminController {

    @Resource
    private ArticleService articleService;
    /**
     * 生成所有帖子的索引（审核通过的）
     * @return
     */
    @ResponseBody
    @RequestMapping("/genAllIndex")
    public boolean genAllIndex() {
        List<Article> articleList = articleService.listArticle();
        for (Article article : articleList) {
            article.setContentNoTag(StringUtil.stripHtml(article.getContent()));

        }
        return true;
    }
}
