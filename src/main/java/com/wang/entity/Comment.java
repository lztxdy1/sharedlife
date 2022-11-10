package com.wang.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 评论实体
 * @author halo
 *
 */
@Data
@Entity
@Table(name="t_comment")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer commentId; // 评论Id
	
	@ManyToOne
	@JoinColumn(name="articleId")
	private Article article; // 文章Id
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user; // 用户Id
	
	@Column(length=500)
	private String content; // 评论内容
	
	private Date commentDate; // 评论时间

}
