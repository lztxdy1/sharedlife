package com.wang.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


/**
 * 文章类别实体
 * @author halo
 *
 */
@Data
@Entity
@Table(name="t_classify")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Classify {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer classifyId;  //类别Id
	
	@NotEmpty(message="文章类别名称不能为空")
	@Column(length=200)
	private String classifyName;  //类别名称
	
}
