package com.wang.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Date;

/**
 * 时光轴实体
 * @author halo
 *
 */
@Entity
@Table(name="t_timeline")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Timeline {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer timelineId;  // 时光轴Id
	
	private Date publishDate;  // 发布日期
	
	@Column(length=200)
	private String year;  //年
	
	@Column(length=200)
	private String month;  //月
	
	@Column(length=200)
	private String content;  // 内容
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getTimelineId() {
		return timelineId;
	}

	public void setTimelineId(Integer timelineId) {
		this.timelineId = timelineId;
	}

	@JsonSerialize(using=CustomDateTimeSerializer.class)
	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
