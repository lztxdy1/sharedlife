package com.wang.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 回复实体
 * @author halo
 *
 */
@Data
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
}
