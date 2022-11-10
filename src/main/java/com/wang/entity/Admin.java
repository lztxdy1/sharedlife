package com.wang.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * 管理员实体
 * @author halo
 *
 */
@Data
@Entity
@Table(name="t_admin")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer adminId; // 管理员Id
	
	@Column(length=200)
	private String userName;  // 用户名
	
	@Column(length=200)
	private String password;  // 密码
	
	@Column(length=200)
	private String trueName;  // 真实姓名
	
	@Column(length=200)
	private String headPortrait;  // 头像
	
	@Column(length=50)
	private String sex;  // 性别
	
	@Column(length=500)
	private String signature;  // 备注
	
	@Column(length=200)
	private String phone;  // 电话
	
}
