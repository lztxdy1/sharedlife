package com.wang.controller.admin;

import com.wang.constant.ErrorConstant;
import com.wang.entity.Admin;
import com.wang.service.AdminService;
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
                mav.addObject("errorInfo", ErrorConstant.loginErrorInfo);
                mav.setViewName("/login");
            } else {
                session.setAttribute("adminUser", resultAdmin);

                mav.addObject("success", true);
                mav.setViewName("/admin/index");
            }
        } catch (Exception e) { // 用户名或密码错误
            e.printStackTrace();
            mav.addObject("admin", admin);
            mav.addObject("errorInfo", ErrorConstant.loginErrorInfo);
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
