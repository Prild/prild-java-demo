package com.thinkgem.jeesite.modules.inxedu.entity.course;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author www.inxedu.com
 * 课程查询条件
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QueryCourse extends DataEntity<QueryCourse> implements Serializable{
    private static final long serialVersionUID = 4550896941810655734L;
    private Integer subjectId;
    private String courseName;
    private int isavaliable;
    private Integer teacherId;
    private int count;
    private String order;
    private String isFree;//查询免费课程
    
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date beginCreateTime;//查询 开始添加时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endCreateTime;//查询 结束添加时间
	public Integer getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
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
	public Integer getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getIsFree() {
		return isFree;
	}
	public void setIsFree(String isFree) {
		this.isFree = isFree;
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
