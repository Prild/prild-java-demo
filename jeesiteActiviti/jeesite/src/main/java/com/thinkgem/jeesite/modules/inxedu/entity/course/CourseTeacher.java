package com.thinkgem.jeesite.modules.inxedu.entity.course;

import java.io.Serializable;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课程-教师关联表
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CourseTeacher extends DataEntity<CourseTeacher> implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id_;
    private int courseId;
    private int teacherId;
}
