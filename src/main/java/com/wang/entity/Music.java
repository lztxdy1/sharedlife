package com.wang.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * yy实体
 * @author halo
 *
 */
@Data
@Entity
@Table(name="t_music")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Music {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer musicId;  // yyId
	
	@Column(length=200)
	private String name;  // yy名称
	
	@Column(length=200)
	private String artist;  // gs
	
	@Column(length=500)
	private String url;  // yy地址
	
	@Column(length=500)
	private String cover;  // 封面地址

}
