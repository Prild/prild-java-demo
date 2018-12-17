package com.thinkgem.jeesite.modules.inxedu.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteCourseDetail;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteCourseDetailDTO;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;


/** 推荐课程关联Dao层 
 * @author www.inxedu.com
 * */
@MyBatisDao
public interface WebsiteCourseDetailDao {
	/**
	 * 创建推荐课程 
	 * @param detail
	 */
	public boolean createWebsiteCourseDetail(String detail);
	
	/**
	 * 分页查询推荐课程
	 * @param dto 查询条件
	 * @param page 分页条件
	 * @return List<WebsiteCourseDetailDTO>
	 */
	public List<WebsiteCourseDetailDTO> queryCourseDetailPage(@Param("param1")WebsiteCourseDetailDTO dto);
	
	/**
	 * 删除推荐课程
	 * @param id 推荐课程ID
	 */
	public boolean deleteDetailById(int id);
	
	/**
	 * 修改推荐课程排序
	 * @param map 修改条件
	 */
	public boolean updateSort(int id,int sort);
	
	/**
	 * 通过类型ID，查询该类型的推荐课程
	 * @param recommendId 类型ID
	 * @return List<WebsiteCourseDetail>
	 */
	public List<WebsiteCourseDetail> queryDetailListByrecommendId(int recommendId);
	
}