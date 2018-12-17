package com.thinkgem.jeesite.modules.inxedu.entity.course;

import java.io.Serializable;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 喜欢的课程
 * @author cwx
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CourseFavorites extends DataEntity<CourseFavorites> implements Serializable{
	private static final long serialVersionUID = 5055812991457774890L;
	private int id_;
    private int courseId;
    private int userId;
    private Date addTime;
	public int getId_() {
		return id_;
	}
	public void setId_(int id_) {
		this.id_ = id_;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}
