package com.wang.repository;

import com.wang.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 用户Repository接口
 * @author laptop
 */
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /**
     * 根据openId查找用户
     * @param openId
     * @return
     */
    @Query(value = "select * from t_user where open_id = ?1 limit 1", nativeQuery = true)
    public User findByOpenId(String openId);

    /**
     * 根据用户真实姓名模糊查询userId
     * @param nickname
     * @return
     */
    @Query(value = "select user_id from t_user where nicinamme like %:nickname%", nativeQuery = true)
    public Integer getUserIdByTrueName(@Param("nickname") String nickname);

    /**
     * 根据账号密码查询用户
     * @param username
     * @param password
     * @return
     */
    public User findByUsernameAndPassword(String username, String password);

    /**
     * 通过昵称查询用户
     * @param nickname
     * @return
     */
    public User findByNickname(String nickname);
}
