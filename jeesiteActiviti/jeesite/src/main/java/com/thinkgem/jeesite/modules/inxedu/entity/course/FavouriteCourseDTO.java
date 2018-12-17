package com.thinkgem.jeesite.modules.inxedu.entity.course;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *收藏的课程
 * @author cwx
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FavouriteCourseDTO extends CourseFavorites implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int favouriteId; // 收藏课程id
	private String courseName; // 课程名字
	private String logo;// 课程图片
	private List<Map<String,Object>> teacherList;//该课程 下的老师list
	public int getFavouriteId() {
		return favouriteId;
	}
	public void setFavouriteId(int favouriteId) {
		this.favouriteId = favouriteId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public List<Map<String, Object>> getTeacherList() {
		return teacherList;
	}
	public void setTeacherList(List<Map<String, Object>> teacherList) {
		this.teacherList = teacherList;
	}
	
}

/**
 * private int id_;
private String courseId;
private String userId;
private Date addTime;
 */
