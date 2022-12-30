package com.wang.controller.admin;

import com.wang.entity.User;
import com.wang.service.UserService;
import com.wang.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/admin/user")
public class UserAdminController {

    @Resource
    private UserService userService;

    @Value("${MD5Salt}")
    private String salt;

    /**
     * 根据userId查询用户
     *
     * @param userId
     * @return
     */
    @RequestMapping("/findById")
    public Map<String, Object> findById(Integer userId) {
        Map<String, Object> resultMap = new HashMap<>();
        User user = userService.findByUserId(userId);
        resultMap.put("errorNo", 0);
        resultMap.put("data", user);
        return resultMap;
    }

    /**
     * 分页查询用户
     *
     * @param user
     * @param latelyLoginTimes
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public Map<String, Object> list(User user,
                                    @RequestParam(value = "latelyLoginTimes", required = false) String latelyLoginTimes,
                                    @RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        String s_bRegistrationDate = null;   // 开始时间
        String s_eRegistrationDate = null;   // 结束时间
        if (StringUtil.isNotEmpty(latelyLoginTimes)) {
            String[] dateArray = latelyLoginTimes.split(" - ");
            s_bRegistrationDate = dateArray[0];
            s_eRegistrationDate = dateArray[1];
        }
        List<User> userList = userService.list(user, s_bRegistrationDate, s_eRegistrationDate, page - 1, pageSize);
        Long total = userService.getUserCount(user, s_bRegistrationDate, s_eRegistrationDate);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("errorNo", 0);
        resultMap.put("data", userList);
        resultMap.put("total", total);
        return resultMap;
    }

    /**
     * 取消关注
     *
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping("/removeFocusUser")
    public ModelAndView removeFocusUser(HttpServletRequest request, String userId) {
        ModelAndView mav = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");   // 当前登录用户

        String userIds = user.getUserIds();
        List<String> tempList = Arrays.asList(userIds.split(","));
        List<String> lineIdList = new ArrayList<>(tempList);
        lineIdList.remove(userId);
        String result = StringUtils.join(lineIdList, ",");
        user.setUserIds(result);
        userService.save(user);
        mav.setViewName("redirect:/viewFocusUser");
        return mav;
    }

    /**
     * 关注用户
     *
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping("/addFocusUser")
    public ModelAndView addFocusUser(HttpServletRequest request, String userId) {
        ModelAndView mav = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");   // 当前登录用户

        String userIds = user.getUserIds();
        List<String> tempList = Arrays.asList(userIds.split(","));
        List<String> lineIdList = new ArrayList<>(tempList);
        lineIdList.add(userId);
        String result = StringUtils.join(lineIdList, ",");
        user.setUserIds(result);
        userService.save(user);
        mav.setViewName("redirect:/viewFocusUser");
        return mav;
    }

    /**
     * 关注用户
     *
     * @param userId
     * @param session
     * @return
     */
    @RequestMapping("/addFocusUser/{userId}")
    public ModelAndView addFocusUser(@PathVariable(value = "userId", required = false) Integer userId,
                                     HttpSession session) {
        ModelAndView mav = new ModelAndView();
        User user = (User) session.getAttribute("user");  // 当前登录用户
        String userIds = user.getUserIds();

        List<String> tempList = new ArrayList<>();
        if (userIds != null) {
            tempList = Arrays.asList(userIds.split(","));
        }

        List<String> lineIdList = new ArrayList<>(tempList);
        lineIdList.add(userId.toString());
        String result = StringUtils.join(lineIdList, ",");
        user.setUserIds(result);
        userService.save(user);
        mav.setViewName("redirect:/viewFocusUser");
        return mav;
    }

    /**
     * 取消收藏
     *
     * @param request
     * @param articleId
     * @return
     */
    @RequestMapping("/removeCollection")
    public ModelAndView removeCollection(HttpServletRequest request, String articleId) {
        ModelAndView mav = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");   // 当前登录用户

        String articleIds = user.getArticleIds();
        List<String> tempList = Arrays.asList(articleIds.split(","));
        List<String> lineIdList = new ArrayList<>(tempList);
        lineIdList.remove(articleId);
        String result = StringUtils.join(lineIdList, ",");
        user.setArticleIds(result);
        userService.save(user);
        mav.setViewName("redirect:/viewCollection");
        return mav;
    }


    /**
     * 添加收藏收藏
     *
     * @param request
     * @param articleId
     * @return
     */
    @RequestMapping("/addCollection")
    public ModelAndView addCollection(HttpServletRequest request, String articleId) {
        ModelAndView mav = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");   // 当前登录用户

        String articleIds = user.getArticleIds();
        List<String> tempList = Arrays.asList(articleIds.split(","));
        List<String> lineIdList = new ArrayList<>(tempList);
        lineIdList.add(articleId);
        String result = StringUtils.join(lineIdList, ",");
        user.setArticleIds(result);
        userService.save(user);
        mav.setViewName("redirect:/viewCollection");
        return mav;
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @RequestMapping("/delete")
    public Map<String, Object> delete(Integer userId) {
        Map<String, Object> resultMap = new HashMap<>();
        userService.delete(userId);
        resultMap.put("errorNo", 0);
        return resultMap;
    }

}
