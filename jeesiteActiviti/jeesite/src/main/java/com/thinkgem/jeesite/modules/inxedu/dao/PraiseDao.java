package com.thinkgem.jeesite.modules.inxedu.dao;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.praise.Praise;


/**
 * 点赞管理接口
 *@author www.inxedu.com
 */
@MyBatisDao
public interface PraiseDao {
	/**
	 * 添加点赞记录
	 */
	public Long addPraise(Praise praise);
	
	/**
	 * 根据条件查询点赞数
	 */
	public int queryPraiseCount(@Param("e")Praise praise);
}
