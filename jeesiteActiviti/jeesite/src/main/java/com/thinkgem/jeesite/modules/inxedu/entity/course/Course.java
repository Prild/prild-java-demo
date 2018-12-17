package com.thinkgem.jeesite.modules.inxedu.entity.course;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;

/**
 * @author www.inxedu.com
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Course extends DataEntity<Course> implements Serializable {
	private static final long serialVersionUID = 1L;

	private int courseId;
	private String courseName;// 课程名称
	private int isavaliable;// 1 正常　２　下架 3删除
	private int subjectId;// 课程专业ID
	private String subjectLink;// 课程专业链
	private Date addTime;// 课程添加时间
	private java.math.BigDecimal sourcePrice;// 课程原价格（只显示）
	private java.math.BigDecimal currentPrice;// 课程销售价格（实际支付价格）设置为0则可免费观看
	private String title;// 课程简介
	private String context;// 课程详情
	private int lessionNum;// 课时
	private String logo;// 课程图片
	private Date updateTime;
	private int pageBuycount;// 销售数量
	private int pageViewcount;// 浏览数量
	private Date endTime;// 有效结束时间
	private int loseType;// 有效期类型，0：到期时间，1：按天数
	private String loseTime;// 有效期:商品订单过期时间点

	private String studyPercent;// 课程学习进度百分比

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getIsavaliable() {
		return isavaliable;
	}

	public void setIsavaliable(int isavaliable) {
		this.isavaliable = isavaliable;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectLink() {
		return subjectLink;
	}

	public void setSubjectLink(String subjectLink) {
		this.subjectLink = subjectLink;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public java.math.BigDecimal getSourcePrice() {
		return sourcePrice;
	}

	public void setSourcePrice(java.math.BigDecimal sourcePrice) {
		this.sourcePrice = sourcePrice;
	}

	public java.math.BigDecimal getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(java.math.BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public int getLessionNum() {
		return lessionNum;
	}

	public void setLessionNum(int lessionNum) {
		this.lessionNum = lessionNum;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getPageBuycount() {
		return pageBuycount;
	}

	public void setPageBuycount(int pageBuycount) {
		this.pageBuycount = pageBuycount;
	}

	public int getPageViewcount() {
		return pageViewcount;
	}

	public void setPageViewcount(int pageViewcount) {
		this.pageViewcount = pageViewcount;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getLoseType() {
		return loseType;
	}

	public void setLoseType(int loseType) {
		this.loseType = loseType;
	}

	public String getLoseTime() {
		return loseTime;
	}

	public void setLoseTime(String loseTime) {
		this.loseTime = loseTime;
	}

	public String getStudyPercent() {
		return studyPercent;
	}

	public void setStudyPercent(String studyPercent) {
		this.studyPercent = studyPercent;
	}

}
