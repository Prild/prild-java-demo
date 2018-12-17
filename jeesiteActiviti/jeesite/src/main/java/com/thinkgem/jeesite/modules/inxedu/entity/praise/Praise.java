package com.thinkgem.jeesite.modules.inxedu.entity.praise;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.thinkgem.jeesite.common.persistence.BaseEntity;

/**
 * 点赞表
 *@author www.inxedu.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Praise extends BaseEntity<Praise> implements Serializable{
	private static final long serialVersionUID = 7687324559966427231L;
	private int id_;//id
	private Long userId;//用户id
	private Long targetId;//点赞的目标id
	private int type;//点赞类型 1问答点赞 2问答评论点赞
	private Date addTime;//点赞和点踩的时间
	@Override
	public void preInsert() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void preUpdate() {
		// TODO Auto-generated method stub
		
	}
	public int getId_() {
		return id_;
	}
	public void setId_(int id_) {
		this.id_ = id_;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

}
