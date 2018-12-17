package com.thinkgem.jeesite.modules.inxedu.entity.course;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.modules.sys.utils.CommonDateParseUtil;

/**
 * 
 * @ClassName com.inxedu.os.inxedu.entity.course.CourseStudyhistory
 * @description 记录播放记录
 * @author www.inxedu.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CourseStudyhistory extends BaseEntity<CourseStudyhistory> implements Serializable {
    private static final long serialVersionUID = 5434482371608343070L;
    private int id_;
    private int userId;
    private int courseId;
    private int kpointId;
    private Long playercount;//播放次数
    private String courseName;//课程名称
    private String kpointName;//节点名称
    private String databack;//playercount小于20时记录,备注观看的时间，叠加
    private java.util.Date updateTime;//更新时间
    private String logo;	//图片
    private String teacherName;	//教师名称
    
    private String userShowName;//用户名
    private String userEmail;//用户邮箱
    private String userImg;//用户头像

    //辅助字段
    private int queryLimit;//查询 的个数
    private String picImg;//用户头像
    private String showName;//用户昵称
    private String updateTimeFormat;//时间 格式化显示

    public void setUpdateTime(Date date){
        this.updateTime=date;
//        this.updateTimeFormat= StringUtils.getModelDate(this.getUpdateTime());
        this.updateTimeFormat= CommonDateParseUtil.date2string(this.getUpdateTime(), CommonDateParseUtil.YYYY_MM_DD_HH_MM);
    }

	@Override
	public void preInsert() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preUpdate() {
		// TODO Auto-generated method stub
		
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public int getId_() {
		return id_;
	}

	public void setId_(int id_) {
		this.id_ = id_;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getKpointId() {
		return kpointId;
	}

	public void setKpointId(int kpointId) {
		this.kpointId = kpointId;
	}

	public Long getPlayercount() {
		return playercount;
	}

	public void setPlayercount(Long playercount) {
		this.playercount = playercount;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getKpointName() {
		return kpointName;
	}

	public void setKpointName(String kpointName) {
		this.kpointName = kpointName;
	}

	public String getDataback() {
		return databack;
	}

	public void setDataback(String databack) {
		this.databack = databack;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getUserShowName() {
		return userShowName;
	}

	public void setUserShowName(String userShowName) {
		this.userShowName = userShowName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public int getQueryLimit() {
		return queryLimit;
	}

	public void setQueryLimit(int queryLimit) {
		this.queryLimit = queryLimit;
	}

	public String getPicImg() {
		return picImg;
	}

	public void setPicImg(String picImg) {
		this.picImg = picImg;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getUpdateTimeFormat() {
		return updateTimeFormat;
	}

	public void setUpdateTimeFormat(String updateTimeFormat) {
		this.updateTimeFormat = updateTimeFormat;
	}

}
