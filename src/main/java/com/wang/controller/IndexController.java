package com.wang.controller;

import com.wang.entity.Article;
import com.wang.entity.Classify;
import com.wang.entity.User;
import com.wang.service.ArticleService;
import com.wang.service.ClassifyService;
import com.wang.service.NoticeService;
import com.wang.service.UserService;
import com.wang.util.DateUtil;
import com.wang.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.channels.SeekableByteChannel;
import java.util.*;

/**
 * 根路径及其他路径处理
 *
 * @author laptop
 */
@Controller
public class IndexController {

    @Resource
    private NoticeService noticeService;

    @Resource
    private ArticleService articleService;

    @Resource
    private UserService userService;

    @Resource
    private ClassifyService classifyService;

    @Value("${imageFilePath}")
    private String imageFilePath;

    /**
     * 默认主页
     *
     * @param session
     * @return
     */
    @RequestMapping("/")
    public String index(HttpSession session) {
        // 查询公告
        session.setAttribute("noticeList", noticeService.list(0, 5));
        // 返回到index界面
        return "index";
    }

    /**
     * 登录界面
     *
     * @return
     */
    @RequestMapping("login")
    public String login() {
        return "login";
    }

    /**
     * 前台登录界面
     *
     * @return
     */
    @RequestMapping("webLogin")
    public String webLogin() {
        return "webLogin";
    }

    /**
     * 注册界面
     *
     * @return
     */
    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    /**
     * 保存用户信息
     *
     * @param user
     * @return
     */
    @RequestMapping("saveUser")
    public String saveUser(User user) {
        List<Article> articleList = articleService.getRandomArticle(3);
        String ids = "";
        for (int i = 0; i < articleList.size(); i++) {
            Integer articleId = articleList.get(i).getArticleId();
            ids += articleId + ",";
        }
        ids = ids.substring(0, ids.length() - 1);
        user.setArticleIds(ids);
        user.setRegistrationDate(new Date());
        userService.save(user);

        return "webLogin";
    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @RequestMapping("/quit")
    public String quit(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return "index";
    }

    @RequestMapping("/quitAdmin")
    public String quitAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return "login";
    }

    /**
     * 检测登录用户信息
     *
     * @param user
     * @param request
     * @return
     */
    @RequestMapping("/checkLogin")
    public ModelAndView checkLogin(User user, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession();
        User user1 = userService.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (user1 == null) {
            mav.addObject("user", user);
            mav.addObject("errorInfo", "用户名或密码错误！");
            mav.setViewName("webLogin");
        } else {
            user1.setLatelyLoginTime(new Date());
            userService.save(user1);
            session.setAttribute("user", user1);
            mav.addObject("username", user1.getUsername());
            mav.addObject("success", true);
            mav.setViewName("/index");
        }
        return mav;
    }

    /**
     * 查看个人信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/viewPerson")
    public ModelAndView viewPerson(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");
        User resultUser = userService.findByUserId(user.getUserId());
        mav.addObject("user", resultUser);
        mav.setViewName("/viewPerson");
        return mav;
    }

    /**
     * 查看个人收藏文章
     *
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("/viewCollection")
    public ModelAndView viewCollection(HttpServletRequest request, HttpSession session) {
        User user = (User) request.getSession().getAttribute("user");
        ModelAndView mav = new ModelAndView();
        User resultUser = userService.findByUserId(user.getUserId());
        String articleIds = resultUser.getArticleIds();
        List<String> result = new ArrayList<>();
        if (StringUtils.isNotBlank(articleIds)) {
            result = Arrays.asList(StringUtils.split(articleIds, ","));
        }
        List<Integer> resultIds = new ArrayList<>();
        for (String s : result) {
            resultIds.add(Integer.valueOf(s));
        }
        List<Article> articleList = articleService.findByListId(resultIds);
        session.setAttribute("noticeList", noticeService.list(0, 5));
        mav.addObject("retArt", articleList);
        mav.addObject("user", resultUser);
        mav.setViewName("/viewCollection");
        return mav;
    }

    /**
     * 查看个人关注用户
     *
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("/viewFocusUser")
    public ModelAndView viewFocusUser(HttpServletRequest request, HttpSession session) {
        User user = (User) request.getSession().getAttribute("user");
        ModelAndView mav = new ModelAndView();
        User user1 = userService.findByUserId(user.getUserId());
        String userIds = user1.getUserIds();
        List<String> result = new ArrayList<>();
        if (StringUtils.isNotBlank(userIds)) {
            result = Arrays.asList(StringUtils.split(userIds, ","));
        }
        List<Integer> resultIds = new ArrayList<>();
        for (String s : result) {
            resultIds.add(Integer.valueOf(s));
        }
        List<User> userList = userService.findByListId(resultIds);
        session.setAttribute("noticeList", noticeService.list(0, 5));
        mav.addObject("retArt", userList);
        mav.addObject("user", user1);
        mav.setViewName("/viewFocusUser");
        return mav;
    }


    /**
     * 保存用户信息
     *
     * @param user
     * @return
     */
    @RequestMapping("/save")
    public ModelAndView save(User user) {
        ModelAndView mav = new ModelAndView();
        userService.save(user);
        mav.setViewName("/index");
        return mav;
    }

    /**
     * 写笔记
     *
     * @param request
     * @return
     */
    @RequestMapping("/notePage")
    public ModelAndView notePage(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            mav.setViewName("/webLogin");
            return mav;
        }
        List<Classify> classifyList = classifyService.findAll();
        mav.addObject("list", classifyList);
        mav.setViewName("/one");
        return mav;
    }

    /**
     * 新增笔记
     *
     * @param article
     * @param request
     * @return
     */
    @RequestMapping("/addNote")
    public ModelAndView addNote(Article article, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        // 获取当前用户信息
        User user = (User) request.getSession().getAttribute("user");
        article.setUserId(user.getUserId());
        article.setPublishDate(new Date());
        article.setClick(0);
        article.setCommentNum(0);
        article.setContentNoTag(StringUtil.Html2Text(article.getContent()));
        articleService.save(article);
        mav.setViewName("/index");
        return mav;
    }

    /**
     * 保存笔记
     *
     * @param article
     * @return
     */
    @RequestMapping("/saveNote")
    public ModelAndView saveNote(Article article) {
        ModelAndView mav = new ModelAndView();
        Article articleResult = articleService.findById(article.getArticleId());
        article.setPublishDate(new Date());
        // 获取当前用户信息
        articleService.save(article);
        mav.setViewName("/index");
        return mav;
    }

    /**
     * 查看笔记
     *
     * @param session
     * @return
     */
    @RequestMapping("/viewNote")
    public String viewNote(HttpSession session) {
        session.setAttribute("noticeList", noticeService.list(0, 10));
        return "myList";
    }

    /**
     * 删除笔记
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") String id) throws Exception {
        articleService.delete(Integer.parseInt(id));
        return "myList";
    }

    /**
     * 查看个人笔记加载数据列表
     *
     * @param article
     * @param publishDates
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping("/mylist")
    public Map<String, Object> list(Article article,
                                    @RequestParam(value = "publishDates", required = false) String publishDates,
                                    @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                    HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        String s_bPublishDate = null;  // start time
        String s_ePublishDate = null;  // end time
        if (StringUtils.isNotEmpty(publishDates)) {
            String[] dates = publishDates.split("-");
            s_bPublishDate = dates[0];
            s_ePublishDate = dates[1];
        }
        Long total = articleService.getCount(article, s_bPublishDate, s_ePublishDate);
        int totalPage = (int) (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
        resultMap.put("totalPage", totalPage);
        resultMap.put("errorNo", 0);
        resultMap.put("data", articleService.listArticle(article, s_bPublishDate, s_ePublishDate, page - 1, pageSize));
        resultMap.put("total", total);
        return resultMap;
    }

    /**
     * 博主信息界面
     *
     * @return
     */
    @RequestMapping("/blogger")
    public String blogger() {
        return "/blogger/index";
    }

    /**
     * 图片上传处理
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    public Map<String, Object> ckeditorUpload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> resultMap1 = new HashMap<>();
        String fileName = file.getOriginalFilename(); // 获取文件名
        String suffixName = fileName.substring(fileName.lastIndexOf(".")); // 获取文件后缀名
        String newFileName = "";
        try {
            newFileName = DateUtil.getCurrentDateStr() + suffixName;  // 新文件名
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(imageFilePath + newFileName)); // 上传新文件
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultMap.put("code", 0);
        resultMap1.put("filePath", newFileName);
        resultMap.put("data", resultMap1);
        return resultMap;
    }
}
