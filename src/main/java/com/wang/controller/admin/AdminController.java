package com.wang.controller.admin;

import com.wang.constant.Constant;
import com.wang.entity.Admin;
import com.wang.entity.User;
import com.wang.service.AdminService;
import com.wang.service.ArticleService;
import com.wang.service.ClassifyService;
import com.wang.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 管理员控制
 * @author halo
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    // md5加密盐
    @Value("wyt")
    private String salt;

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;
    @Resource
    private ArticleService articleService;
    @Resource
    private ClassifyService classifyService;
    /**
     * 后台管理员登录
     * @param admin
     * @param request
     * @return
     */
    @RequestMapping("/login")
    public ModelAndView login(Admin admin, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession();

        try {
            Admin resultAdmin = adminService.findByUserNameAndPassword(admin.getUserName(), admin.getPassword());
            if (resultAdmin == null) {
                mav.addObject("errorInfo", Constant.LOGIN_ERROR_INFO);
                mav.setViewName("/login");
            } else {
                session.setAttribute("adminUser", resultAdmin);
                // 统计用户信息
                Long userCount = userService.getCount();

                // 统计今日注册
                Long todayRegistrationCount = userService.getTodayRegisterCount(new User(), "1", "1");

                // 统计今日登录数
                Long todayLoginCount = userService.getUserCount(new User(), "1", "1");

                // 博客总数
                Long articleCount = articleService.getCount();

                // 统计博客分类
                Long classifyCount = classifyService.getCount();

                session.setAttribute("userCount", userCount);
                session.setAttribute("todayRegistrationCount", todayRegistrationCount);
                session.setAttribute("todayLoginCount", todayLoginCount);
                session.setAttribute("articleCount", articleCount);
                session.setAttribute("classifyCount", classifyCount);
                mav.addObject("success", true);

                mav.setViewName("/admin/index");
            }
        } catch (Exception e) { // 用户名或密码错误
            e.printStackTrace();
            mav.addObject("admin", admin);
            mav.addObject("errorInfo", Constant.LOGIN_ERROR_INFO);
            mav.setViewName("/login");
        }

        return mav;
    }

    /**
     * 查看个人信息
     * @param request
     * @return
     */
    @RequestMapping("/viewPerson")
    public ModelAndView viewPerson(HttpServletRequest request) {
        Admin admin = (Admin) request.getSession().getAttribute("adminUser");
        ModelAndView mav = new ModelAndView();
        Admin user = adminService.findById(admin.getAdminId());
        mav.addObject("user", user);
        mav.setViewName("/admin/adminViewPerson");
        return mav;
    }

    /**
     * 保存用户信息
     * @param admin
     * @return
     */
    @RequestMapping("/save")
    public ModelAndView save(Admin admin) {
        ModelAndView mav = new ModelAndView();
        adminService.save(admin);
        mav.setViewName("/admin/index");
        return mav;
    }

}
