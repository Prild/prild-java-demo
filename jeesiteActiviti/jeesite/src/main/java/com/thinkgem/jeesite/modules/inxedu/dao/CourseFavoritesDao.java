package com.thinkgem.jeesite.modules.inxedu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseFavorites;
import com.thinkgem.jeesite.modules.inxedu.entity.course.FavouriteCourseDTO;


/**
 * 课程收藏管理接口
 * @author www.inxedu.com
 */
@MyBatisDao
public interface CourseFavoritesDao {
	/**
	 * 添加课程收藏
	 * @param cf
	 */
	public boolean createCourseFavorites(CourseFavorites cf);
	
	/**
	 * 删除课程收藏
	 * @param ids
	 */
	public boolean deleteCourseFavoritesById(String ids);
	
	/**
	 * 检测用户是否收藏过
	 * @param map
	 * @return int
	 */
	public int checkFavorites(@Param("userId")Integer userId, @Param("courseId")Integer courseId);
	
	/**
	 * 分页查询用户收藏列表
	 * @param userId 用户ID
	 * @param page 分页条件
	 * @return List<FavouriteCourseDTO>
	 */
	public List<FavouriteCourseDTO> queryFavoritesPage(@Param("userId")int userId);
    
    
}