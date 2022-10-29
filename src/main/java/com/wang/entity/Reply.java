package com.wang.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Date;

/**
 * 回复实体
 * @author halo
 *
 */
@Entity
@Table(name="t_reply")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Reply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer replyId; // 回复Id
	
	@ManyToOne
	@JoinColumn(name="commentId")
	private Comment comment; // 评论Id
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user; // 用户Id
	
	@Column(length=500)
	private String content; // 回复内容
	
	private Date replyDate; // 回复时间

	public Integer getReplyId() {
		return replyId;
	}

	public void setReplyId(Integer replyId) {
		this.replyId = replyId;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@JsonSerialize(using=CustomDateTimeSerializer.class)
	public Date getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(Date replyDate) {
		this.replyDate = replyDate;
	}
}
