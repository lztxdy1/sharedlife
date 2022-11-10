package com.wang.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * 友情链接实体
 * @author halo
 *
 */
@Data
@Entity
@Table(name="t_link")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Link {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer linkId;  // 链接Id
	
	@Column(length=200)
	private String linkName;  // 链接名称
	
	@Column(length=200)
	private String linkUrl;  // 链接地址
	
	@Column(length=200)
	private String linkEmail;  // 联系人邮箱
	
	private Integer orderNum;  // 排序
	
}
