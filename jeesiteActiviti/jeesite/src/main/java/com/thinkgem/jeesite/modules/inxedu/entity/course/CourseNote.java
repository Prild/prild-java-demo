package com.thinkgem.jeesite.modules.inxedu.entity.course;

import java.io.Serializable;

import com.thinkgem.jeesite.common.persistence.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课程笔记
 * @author cwx
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CourseNote extends BaseEntity<CourseNote> implements Serializable{
	private static final long serialVersionUID = 1L;
    private String id;//主键
    private String kpointId;//节点ID
    private String courseId;//课程id
    private String userId;//用户ID
    private String content;//笔记内容
    private java.util.Date updateTime;//添加修改时间
    private int status;//0公开1隐藏
	@Override
	public void preInsert() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void preUpdate() {
		// TODO Auto-generated method stub
		
	}
}
