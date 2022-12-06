package com.wang.controller;

import com.wang.entity.Article;
import com.wang.entity.Classify;
import com.wang.entity.User;
import com.wang.lucene.ArticleIndex;
import com.wang.service.ArticleService;
import com.wang.service.ClassifyService;
import com.wang.service.MusicService;
import com.wang.service.UserService;
import com.wang.util.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前端页面请求
 */
@Controller
@RequestMapping("/foreground")
public class ForegroundController {

    @Resource
    private ArticleService articleService;

    @Resource
    private ClassifyService classifyService;

    @Resource
    private UserService userService;

    @Resource
    private ArticleIndex articleIndex;

    @Resource
    private MusicService musicService;

    @Value("downloadImagePath")
    private String downloadImagePath;

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

    /**
     * 文章详情
     * @param articleId
     * @param session
     * @return
     */
    @RequestMapping("detail/{id}")
    public ModelAndView detail(@PathVariable(value = "id", required = false) Integer articleId, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        articleService.increaseClick(articleId);
        Article article = articleService.findById(articleId);
        Article article1 = new Article();
        article1.setClassify(article.getClassify());
        session.setAttribute("similarityList", articleService.listArticle(article1, null, null, 0, 10));
        session.setAttribute("RandomArticleList", articleService.getRandomArticle(10));

        // 判断是否被收藏
        boolean isCollected = false;
        User user = (User) session.getAttribute("user");
        if (user != null) {
            String ids = user.getArticleIds();
            if (ids != null) {
                List<String> idsList = Arrays.asList(ids.split(","));
                if (idsList.contains(articleId.toString())) {
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

    /**
     * 文章详情
     * @param articleId
     * @param session
     * @return
     */
    @RequestMapping("/myDetail/{id}")
    public ModelAndView myDetail(@PathVariable(value = "id", required = false) Integer articleId, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        articleService.increaseClick(articleId);
        Article article = articleService.findById(articleId);
        Article article1 = new Article();
        article1.setClassify(article.getClassify());
        session.setAttribute("similarityList", articleService.listArticle(article1, null, null, 0, 10));
        session.setAttribute("RandomArticleList", articleService.getRandomArticle(10));  // 获取10条随机文章

        // 判读文章是否被收藏
        boolean isCollected = false;
        User user = (User) session.getAttribute("user");
        if (user != null && user.getArticleIds() != null) {
            String ids = user.getArticleIds();
            List<String> idsList = Arrays.asList(ids.split(","));
            if (idsList.contains(articleId.toString())) {
                mav.addObject("flag", true);
            } else {
                mav.addObject("flag", isCollected);
            }
        }

        List<Classify> classifyList = classifyService.findAll();
        mav.addObject("list", classifyList);
        mav.addObject("article", article);
        mav.setViewName("/myDetail");
        return mav;
    }

    @RequestMapping("/otherPerson/{id}")
    public ModelAndView otherPerson(@PathVariable(value = "id", required = false) Integer userId, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        User user = userService.findByUserId(userId);
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            mav.setViewName("redirect:/webLogin");
            return mav;
        }

        String userIds = currentUser.getUserIds();
        boolean flag = false;
        if (userIds != null) {
            List<String> userIdsList = Arrays.asList(userIds.split(","));
            flag = userIdsList.contains(userId.toString());
        }

        // 查询该用户所有文章
        List<Article> articleList = articleService.findByUserId(userId);
        mav.addObject("artList", articleList);
        mav.addObject("user", user);
        mav.addObject("personFlag", flag);
        mav.addObject("artCount", articleList.size());
        mav.setViewName("otherPerson");
        return mav;
    }

    /**
     * 根据关键字查询相关文章信息
     * @param q
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("/q")
    public Map<String, Object> search(@RequestParam(value = "q", required = false) String q,
                                      @RequestParam(value = "page", required = false) String page) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        int pageSize = 5;
        List<Article> articleList = articleIndex.searchArticle(q);
        for (Article a : articleList) {
            Article article = articleService.findById(a.getArticleId());
            a.setClick(article.getClick());
            a.setCommentNum(article.getCommentNum());
            a.setAuthor(article.getAuthor());
            a.setImageName(article.getImageName());
            a.setClassify(article.getClassify());
            a.setIsOriginal(article.getIsOriginal());
            a.setIsTop(article.getIsTop());
        }

        int total = articleList.size();
        int totalPage = (int) ((total / pageSize == 0) ? total / pageSize : total / pageSize + 1); // 总页数
        int toIndex = Math.min(articleList.size(), Integer.parseInt(page) * pageSize);
        resultMap.put("data", articleList.subList((Integer.parseInt(page) - 1) * pageSize, toIndex));
        resultMap.put("q", q);
        resultMap.put("totalPage", totalPage);
        resultMap.put("total", total);
        return resultMap;
    }

    /**
     * 查询音乐
     * @return
     */
    @RequestMapping("/musicList")
    @ResponseBody
    public Map<String, Object> list() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", musicService.list(null, 0, 20));
        resultMap.put("success", 1);
        return resultMap;
    }

    public void testDownload(HttpServletResponse response, String fileName) throws Exception {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        String suffixName = fileName.substring(fileName.lastIndexOf(".")); // 获取文件名后缀
        response.setHeader("Content-Disposition", "attachment;filename=" + DateUtil.getCurrentDateStr() + suffixName);
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(downloadImagePath + suffixName)));
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, buffer.length);
                os.flush();
                i = bis.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("success");
    }
}
