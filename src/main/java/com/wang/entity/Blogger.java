package com.wang.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * 博主实体
 * @author halo
 *
 */
@Data
@Entity
@Table(name="t_blogger")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Blogger {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bloggerId; // 博主Id
	
	@Column(length=200)
	private String nickName;  // 昵称
	
	@Column(length=200)
	private String headPortrait;  // 头像
	
	@Column(length=500)
	private String motto;  // 座右铭
	
	@Column(length=500)
	private String signature;  // 个性签名
	
	@Column(length=200)
	private String site;  // 地址
	
}
