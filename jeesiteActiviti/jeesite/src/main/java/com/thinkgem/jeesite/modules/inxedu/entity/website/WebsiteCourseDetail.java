package com.thinkgem.jeesite.modules.inxedu.entity.website;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * @description 推荐课程
 * @author www.inxedu.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WebsiteCourseDetail extends DataEntity<WebsiteCourseDetail> implements Serializable {
	private static final long serialVersionUID = 7475674095165175841L;

	private int id_;// 主键
	private int recommendId;// 分类id
	private int courseId;// 课程id
	private int orderNum;// 排序值
}
