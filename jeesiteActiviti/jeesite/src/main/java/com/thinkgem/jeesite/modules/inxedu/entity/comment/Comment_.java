package com.thinkgem.jeesite.modules.inxedu.entity.comment;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.modules.inxedu.entity.article.Article_;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author www.inxedu.com
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Comment_ extends BaseEntity<Comment_>{
	private static final long serialVersionUID = -2378128532752351624L;
	private Integer commentId;//评论Id
	private Integer userId;//用户id
	private Integer pCommentId;//父级评论id // 为0 则是一级评论 不为0则是评论的回复
	private String content;//评论内容
	private Date addTime;//发送时间
	private Integer otherId;//相关Id
	private int praiseCount;//点赞数
	private int replyCount;//回复数
	private Integer type;//类型 1 文章 2 课程

	private String email;//用户Email
	private String userName;//用户昵称
	private String courseName;//课程名
	private int courseId;//课程id
	private String picImg;//用户头像
	private int commentNumber;//回复数
	private int limitNumber;//查询数
	private String order ="commentId";// commentId 降序  praiseCount 降序
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date beginCreateTime;//查询 开始时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endCreateTime;//查询 结束时间

	@Override
	public void preInsert() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void preUpdate() {
		// TODO Auto-generated method stub
		
	}
	public Integer getCommentId() {
		return commentId;
	}
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getpCommentId() {
		return pCommentId;
	}
	public void setpCommentId(Integer pCommentId) {
		this.pCommentId = pCommentId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Integer getOtherId() {
		return otherId;
	}
	public void setOtherId(Integer otherId) {
		this.otherId = otherId;
	}
	public int getPraiseCount() {
		return praiseCount;
	}
	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}
	public int getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public String getPicImg() {
		return picImg;
	}
	public void setPicImg(String picImg) {
		this.picImg = picImg;
	}
	public int getCommentNumber() {
		return commentNumber;
	}
	public void setCommentNumber(int commentNumber) {
		this.commentNumber = commentNumber;
	}
	public int getLimitNumber() {
		return limitNumber;
	}
	public void setLimitNumber(int limitNumber) {
		this.limitNumber = limitNumber;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Date getBeginCreateTime() {
		return beginCreateTime;
	}
	public void setBeginCreateTime(Date beginCreateTime) {
		this.beginCreateTime = beginCreateTime;
	}
	public Date getEndCreateTime() {
		return endCreateTime;
	}
	public void setEndCreateTime(Date endCreateTime) {
		this.endCreateTime = endCreateTime;
	}

}
