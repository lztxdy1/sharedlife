package com.wang.controller;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import com.wang.entity.User;
import com.wang.service.UserService;
import com.wang.util.StringUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * QQ登录控制器
 */
@RestController
@RequestMapping("/QQ")
public class QQController {

    @Resource
    private UserService userService;

    /**
     * QQ登录跳转
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/qqLogin")
    public void qqLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        try {
            response.sendRedirect(new Oauth().getAuthorizeURL(request));
        } catch (QQConnectException e) {
            new QQConnectException("跳转到QQ登录界面异常");
        }
    }

    /**
     * qq登录回调
     *
     * @param request
     * @param response
     * @param session
     * @return
     * @throws QQConnectException
     */
    @RequestMapping(value = "/QQCallBack")
    public String qqCallback(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws QQConnectException {
        response.setContentType("text/html;charset=utf-8");
        AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
        String accessToken = null;
        String openId = null;
        try {
            String code = request.getParameter("code");
            String state = request.getParameter("state");
            String session_state = (String) session.getAttribute("qq_connect_state");
            if (StringUtil.isEmpty(session_state) && session_state.equals(state)) {
                accessToken = accessTokenObj.getAccessToken();
                session.setAttribute("accessToken", accessToken);

                if (StringUtil.isEmpty(accessToken)) {
                    System.out.println("没有获取到相应参数");
                } else {
                    OpenID openIDObj = new OpenID(accessToken);
                    openId = openIDObj.getUserOpenID();
                    UserInfo qzoneUserInfo = new UserInfo(accessToken, openId);
                    UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();

                    if (userInfoBean != null && userInfoBean.getRet() == 0 && StringUtil.isEmpty(userInfoBean.getMsg())) {
                        // 获取用户成功
                        User user = userService.findByOpenId(openId);
                        if (null != user) {
                            // 已经注册过，更新用户信息，直接将信息存储在session中，然后跳转
                            user.setNickname(userInfoBean.getNickname());
                            user.setHeadPortrait(userInfoBean.getAvatar().getAvatarURL100());
                            user.setSex(userInfoBean.getGender());
                            user.setLatelyLoginTime(new Date());
                            userService.save(user);
                        } else {
                            // 该用户首次登录，需要先注册
                            user = new User();
                            user.setOpenId(openId);
                            user.setNickname(userInfoBean.getNickname());
                            user.setHeadPortrait(userInfoBean.getAvatar().getAvatarURL100());
                            user.setSex(userInfoBean.getGender());
                            user.setLatelyLoginTime(new Date());
                            user.setRegistrationDate(new Date());
                            userService.save(user);
                        }
                        // 存储用户信息
                        session.setAttribute("currentUser", user);
                    }
                }
            } else {
                System.out.println("非法请求！！！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

    /**
     * 注销登录
     *
     * @param session
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/loginOut")
    public String loginOut(HttpSession session, HttpServletResponse response) throws IOException {
        session.removeAttribute("currentUser");
        return "redirect:/";
    }
}
