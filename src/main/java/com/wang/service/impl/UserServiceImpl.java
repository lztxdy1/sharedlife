package com.wang.service.impl;

import com.wang.entity.User;
import com.wang.repository.UserRepository;
import com.wang.service.UserService;
import com.wang.util.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户Service接口实现
 * @author laptop
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;
    /**
     * 根据openId查询用户
     *
     * @param openId
     * @return
     */
    @Override
    public User findByOpenId(String openId) {
        return userRepository.findByOpenId(openId);
    }

    /**
     * 分页查询用户
     *
     * @param user
     * @param s_bRegistrationDate
     * @param s_eRegistrationDate
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<User> list(User user, String s_bRegistrationDate, String s_eRegistrationDate, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "registrationDate");
        Page<User> userPage = userRepository.findAll((Specification<User>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (StringUtil.isNotEmpty(s_bRegistrationDate)) {
                predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("latelyLoginTime").as(String.class), s_bRegistrationDate));
            }
            if (StringUtil.isNotEmpty(s_eRegistrationDate)) {
                predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("latelyLoginTime").as(String.class), s_eRegistrationDate));
            }
            if (user != null) {
                if (StringUtil.isNotEmpty(user.getSex())) {
                    predicate.getExpressions().add(cb.equal(root.<String>get("sex"), user.getSex()));
                }
                if (StringUtil.isNotEmpty(user.getNickname())) {
                    predicate.getExpressions().add(cb.like(root.get("nickname"), "%" + user.getNickname().trim() + " %"));
                }
            }
            return predicate;
        }, pageable);
        return userPage.getContent();
    }

    /**
     * 根据userId查询用户
     *
     * @param userId
     * @return
     */
    @Override
    public User findByUserId(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * 添加或修改用户
     *
     * @param user
     */
    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * 根据userId删除用户
     *
     * @param userId
     */
    @Override
    public void delete(Integer userId) {
        userRepository.deleteById(userId);
    }

    /**
     * 根据用户名和用户密码查询
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    /**
     * 根据真实姓名查询用户id
     *
     * @param nickname
     * @return
     */
    @Override
    public Integer getUserIdByTrueName(String nickname) {
        return userRepository.getUserIdByTrueName(nickname);
    }

    /**
     * 根据真实姓名查询用户
     *
     * @param nickname
     * @return
     */
    @Override
    public User getUserByTrueName(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    /**
     * 获取今日注册用户总数
     *
     * @param user
     * @param s_bRegistrationDate
     * @param s_eRegistrationDate
     * @return
     */
    @Override
    public Long getTodayRegisterCount(User user, String s_bRegistrationDate, String s_eRegistrationDate) {
        Long count = userRepository.count((Specification<User>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (StringUtil.isNotEmpty(s_bRegistrationDate)) {
                predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("registrationDate").as(LocalDateTime.class),
                        LocalDate.now().atTime(0, 0, 0)));
            }
            if (StringUtil.isNotEmpty(s_eRegistrationDate)) {
                predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("registrationDate").as(LocalDateTime.class),
                        LocalDate.now().atTime(23, 59, 59)));
            }

            if (user != null) {
                if (StringUtil.isNotEmpty(user.getSex())) {
                    predicate.getExpressions().add(cb.equal(root.<String>get("sex"), user.getSex()));
                }
                if (StringUtil.isNotEmpty(user.getNickname())) {
                    predicate.getExpressions().add(cb.like(root.get("nickname"), "%" + user.getNickname().trim() + " %"));
                }
            }
            return predicate;
        });
        return count;
    }

    /**
     * 获取用户总数
     *
     * @return
     */
    @Override
    public Long getUserCount(User user, String s_bRegistrationDate, String s_eRegistrationDate) {
        Long count = userRepository.count((Specification<User>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (StringUtil.isNotEmpty(s_bRegistrationDate)) {
                predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("latelyLoginDate").as(LocalDateTime.class),
                        LocalDate.now().atTime(0, 0, 0)));
            }
            if (StringUtil.isNotEmpty(s_eRegistrationDate)) {
                predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("latelyLoginDate").as(LocalDateTime.class),
                        LocalDate.now().atTime(23, 59, 59)));
            }

            if (user != null) {
                if (StringUtil.isNotEmpty(user.getSex())) {
                    predicate.getExpressions().add(cb.equal(root.<String>get("sex"), user.getSex()));
                }
                if (StringUtil.isNotEmpty(user.getNickname())) {
                    predicate.getExpressions().add(cb.like(root.get("nickname"), "%" + user.getNickname().trim() + " %"));
                }
            }
            return predicate;
        });
        return count;
    }

    /**
     * 查询关注用户
     *
     * @param userList
     * @return
     */
    @Override
    public List<User> findByListId(List<Integer> userList) {
        return userRepository.findAllById(userList);
    }
}
