package com.wang.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 时光轴实体
 * @author halo
 *
 */
@Data
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
	
}
