package com.wang.controller.admin;

import com.wang.constant.Constant;
import com.wang.entity.Article;
import com.wang.entity.User;
import com.wang.lucene.ArticleIndex;
import com.wang.run.StartupRunner;
import com.wang.service.ArticleService;
import com.wang.service.UserService;
import com.wang.util.DateUtil;
import com.wang.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * 文章控制器
 *
 * @author laptop
 */
@RestController
@RequestMapping("/admin/article")
public class ArticleAdminController {

    @Resource
    private ArticleService articleService;

    @Resource
    private ArticleIndex articleIndex;

    @Resource
    private UserService userService;

    @Resource
    private StartupRunner startupRunner;

    @Value("${imageFilePath}")
    private String imageFilePath;

    /**
     * 生成所有帖子的索引（审核通过的）
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/genAllIndex")
    public boolean genAllIndex() {
        List<Article> articleList = articleService.listArticle();
        for (Article article : articleList) {
            try {
                article.setContentNoTag(StringUtil.stripHtml(article.getContent())); //去除html标签
                articleIndex.addIndex(article);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @RequestMapping("/list")
    public Map<String, Object> list(Article article,
                                    @RequestParam(value = "publishDate", required = false) String publishDates,
                                    @RequestParam(value = "p", required = false) Integer p,
                                    @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                    HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("user");
        List<Article> articleList = new ArrayList<Article>();

        String s_bPublishDate = null;  // 开始时间
        String s_ePublishDate = null;  // 结束时间
        if (StringUtil.isNotEmpty(publishDates)) {
            String[] strings = publishDates.split("-"); //拆分时间段
            s_bPublishDate = strings[0];
            s_ePublishDate = strings[1];
        }

        if (p != null && p == 1) {
            User user1 = userService.findByUserId(user.getUserId());
            article.setUserId(user1.getUserId());
        } else if (p != null && p == 2) {
            User user1 = userService.findByUserId(user.getUserId());
            String articleIds = user1.getArticleIds();
            List<String> resultList = new ArrayList<>();
            if (StringUtils.isNotBlank(articleIds)) {
                resultList = Arrays.asList(StringUtils.split(articleIds, ","));
            }
            List<Integer> resultIds = new ArrayList<>();
            for (String s : resultList) {
                resultIds.add(Integer.valueOf(s).intValue());
            }
            articleList = articleService.findByListId(resultIds);
        }
        Long total = articleService.getCount(article, s_bPublishDate, s_ePublishDate);
        if (p != null && p == 2) {
            total = (long) articleList.size();
        }

        int totalPage = (int) (total % pageSize == 0 ? total / pageSize : total / pageSize + 1); // 总页数
        resultMap.put("totalPage", totalPage);
        resultMap.put("errorNo", 0);

        if (p != null && p == 2) {
            resultMap.put("data", articleList);
        } else {
            resultMap.put("data", articleService.listArticle(article, s_bPublishDate, s_ePublishDate, page - 1,
                    pageSize));
        }
        resultMap.put("total", total);
        return resultMap;
    }

    /**
     * 根据文章id查询文章
     *
     * @param articleId
     * @return
     */
    @RequestMapping("/findById")
    public Map<String, Object> findById(Integer articleId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> tempMap = new HashMap<String, Object>();

        Article article = articleService.findById(articleId);
        tempMap.put("articleId", article.getArticleId());
        tempMap.put("title", article.getTitle());
        tempMap.put("author", article.getAuthor());
        tempMap.put("content", article.getContent());
        tempMap.put("click", article.getClick());
        tempMap.put("publishDate", article.getPublishDate());
        tempMap.put("isTop", article.getIsTop());
        tempMap.put("isOriginal", article.getIsOriginal());
        tempMap.put("commentNum", article.getCommentNum());
        tempMap.put("classify", article.getClassify());
        tempMap.put("imageName", article.getImageName());

        resultMap.put("errorNo", 0);
        resultMap.put("data", tempMap);
        return resultMap;
    }

    /**
     * 添加或修改文章
     *
     * @param article
     * @param mode
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    public Map<String, Object> save(Article article, @RequestParam(value = "_mode", required = false) String mode)
            throws Exception {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (article.getIsTop() == null) {
            article.setIsTop(0);
        }
        if (article.getIsOriginal() == null) {
            article.setIsOriginal(0);
        }
        if (article.getClick() == null) {
            article.setClick(0);
        }
        if (article.getCommentNum() == null) {
            article.setCommentNum(0);
        }
        if (StringUtil.isNotEmpty(article.getImageName())) {
            article.setImageName("image.jpg");
        }
        article.setPublishDate(new Date());
        article.setContentNoTag(StringUtil.Html2Text(article.getContent()));
        articleService.save(article);

        if (Constant.ADD_ARTICLE.equals(mode)) {
            articleIndex.addIndex(article);
        } else if (Constant.EDIT_ARTICLE.equals(mode)) {
            articleIndex.updateIndex(article);
        }
        resultMap.put("errorNo", 0);
        resultMap.put("data", 1);
        startupRunner.loadData();
        return resultMap;
    }

    /**
     * 批量删除文章
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    public Map<String, Object> delete(@RequestParam(value = "articleId") String ids) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            articleService.delete(Integer.parseInt(idsStr[i]));
            articleIndex.deleteIndex(idsStr[i]);
        }
        resultMap.put("errorNo", 0);
        resultMap.put("data", 1);
        startupRunner.loadData();
        return resultMap;
    }

    public String ckeditorUpload(@RequestParam(value = "upload") MultipartFile file, String CKEditorFuncNum) {
        String fileName = file.getOriginalFilename();  // 获取文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 获取文件后缀
        String newFileName = "";
        try {
            newFileName = DateUtil.getCurrentDateStr() + suffixName;  // 生成新的文件名
            // 上传
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(imageFilePath + newFileName));

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 回调到页面
        StringBuffer buffer = new StringBuffer();
        buffer.append("<script type=\"text/javascript\">");
        buffer.append("window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum + ",'" + "/static/image/"
                + newFileName + "','')");
        buffer.append("</script>");
        return buffer.toString();
    }
}
