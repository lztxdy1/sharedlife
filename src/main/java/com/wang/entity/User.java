package com.wang.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户实体
 * @author halo
 *
 */
@Data
@Entity
@Table(name = "t_user")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer userId; // 用户Id

  @Column
  private String username;// 用户名

  @Column
  private String password;// 密码

  @Column(length = 200)
  private String openId; // openID

  @Column(length = 200)
  private String nickname; // 昵称

  @Column(length = 200)
  private String headPortrait; // 头像

  @Column(length = 50)
  private String sex; // 性别

  @Column(nullable = false, columnDefinition = "timestamp", updatable = false)
  private Date registrationDate; // 注册日期

  private Date latelyLoginTime; // 最近登录时间

  private String phone;// 手机号

  private java.sql.Date birthday;// 生日

  private String articleIds;// 收藏文章id

  private String userIds;// 关注的用户id

  private String momo;// 备注

}
