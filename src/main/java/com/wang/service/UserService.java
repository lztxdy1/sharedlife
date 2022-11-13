package com.wang.service;

import com.wang.entity.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 用户Service接口
 * @author laptop
 */
public interface UserService {

    /**
     * 根据openId查询用户
     * @param openId
     * @return
     */
    public User findByOpenId(String openId);
    /**
     * 分页查询用户
     * @param user
     * @param page
     * @param pageSize
     * @return
     */
    public List<User> list(User user, String s_bRegistrationDate, String s_eRegistrationDate, Integer page, Integer pageSize);

    /**
     * 根据userId查询用户
     * @param userId
     * @return
     */
    public User findByUserId(Integer userId);

    /**
     * 添加或修改用户
     * @param user
     */
    public void save(User user);

    /**
     * 根据userId删除用户
     * @param userId
     */
    public void delete(Integer userId);

    /**
     * 根据用户名和用户密码查询
     * @param username
     * @param password
     * @return
     */
    public User findUserByUsernameAndPassword(String username, String password);

    /**
     * 根据真实姓名查询用户id
     * @param nickname
     * @return
     */
    public Integer getUserIdByTrueName(@Param("nickname") String nickname);

    /**
     * 根据真实姓名查询用户
     * @param nickname
     * @return
     */
    public User getUserByTrueName(String nickname);

    /**
     * 获取今日注册用户总数
     * @param user
     * @param s_bRegistrationDate
     * @param s_eRegistrationDate
     * @return
     */
    public Long getTodayRegisterCount(User user, String s_bRegistrationDate, String s_eRegistrationDate);

    /**
     * 获取用户总数
     * @return
     */
    public Long getUserCount(User user, String s_bRegistrationDate, String s_eRegistrationDate);

    /**
     * 查询关注用户
     * @param userList
     * @return
     */
    public List<User> findByListId(List<Integer> userList);

    /**
     * 获取总用户数
     * @return
     */
    public Long getCount();

}
