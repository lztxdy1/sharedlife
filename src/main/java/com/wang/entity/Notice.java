package com.wang.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 公告实体
 * @author halo
 *
 */
@Data
@Entity
@Table(name="t_notice")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Notice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer noticeId;  // 公告Id
	
	@Column(length=500)
	private String content;  // 公告内容
	
	private Date publishDate;  // 发布日期
	
	@Column(length=200)
	private Integer grade;  // 等级  0 重要  1 一般

}
