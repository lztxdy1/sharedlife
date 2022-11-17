package com.wang.run;

import com.wang.service.ArticleService;
import com.wang.service.BloggerService;
import com.wang.service.ClassifyService;
import com.wang.service.LinkService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

/**
 * 启动服务加载数据
 */
@Component("startupRunner")
public class StartupRunner implements CommandLineRunner {

    @Resource
    private ServletContext application;

    @Resource
    private BloggerService bloggerService;

    @Resource
    private LinkService linkService;

    @Resource
    private ArticleService articleService;

    @Resource
    private ClassifyService classifyService;

    /**
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        this.loadData();
    }

    /**
     * 加载数据到应用中
     */
    public void loadData() {
        application.setAttribute("classifyList", classifyService.list(0, 200)); // 加载文章类别
        application.setAttribute("blogger", bloggerService.find()); //加载博主信息
        application.setAttribute("clickArticleList", articleService.getClickArticle(10)); // 加载热门文章（点击量排行）
        application.setAttribute("recommendArticleList", articleService.getRecommendArticle(10)); // 加载置顶原创文章
        application.setAttribute("linkList", linkService.list(null, 0, 10)); // 10条友情链接
    }
}
