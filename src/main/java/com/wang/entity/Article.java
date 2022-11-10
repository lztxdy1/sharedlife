package com.wang.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 文章实体
 * 
 * @author halo
 *
 */
@Data
@Entity
@Table(name = "t_article")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer articleId; // 文章Id

  @Column(length = 200)
  private String title; // 文章标题

  @Lob
  @Column(columnDefinition = "TEXT")
  private String content; // 文章内容

  @Transient
  private String contentNoTag; // 博客内容 无网页标签 Lucene分词用到

  private Date publishDate; // 发布日期

  @Column(length = 200)
  private String author; // 作者

  @ManyToOne
  @JoinColumn(name = "classifyId")
  private Classify classify; // 文章类别

  private Integer click; // 点击数

  private Integer commentNum; // 评论数

  private Integer isTop; // 置顶 0否 1是

  private Integer isOriginal; // 原创 0否 1是

  @Column(length = 500)
  private String imageName; // 图片名称

  @Column
  private Integer userId;// 用户id

}
