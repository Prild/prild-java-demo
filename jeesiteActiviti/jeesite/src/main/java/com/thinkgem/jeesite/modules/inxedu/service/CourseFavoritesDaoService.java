package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.CourseFavoritesDao;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseFavorites;
import com.thinkgem.jeesite.modules.inxedu.entity.course.FavouriteCourseDTO;

@Service
@Transactional
public class CourseFavoritesDaoService implements CourseFavoritesDao {
	// *************************************************************************************************************
	@Autowired
	private CourseFavoritesDao courseFavoritesDao;// spring 管理每个地方都是注入的，不然报空指针异常
	// *************************************************************************************************************

	@Override
	public boolean createCourseFavorites(CourseFavorites cf) {
		return courseFavoritesDao.createCourseFavorites(cf);

	}

	@Override
	public boolean deleteCourseFavoritesById(String ids) {
		return courseFavoritesDao.deleteCourseFavoritesById(ids);

	}

	@Override
	public List<FavouriteCourseDTO> queryFavoritesPage(int userId) {
		return courseFavoritesDao.queryFavoritesPage(userId);
	}

	@Override
	public int checkFavorites(@Param("userId")Integer userId, @Param("courseId")Integer courseId) {
		return courseFavoritesDao.checkFavorites(userId,courseId);
	}

	/**
	 * 检测用户是否收藏过
	 */
	public boolean checkFavorites_(Integer userId, Integer courseId) {
		int count = checkFavorites(userId,courseId);
		if (count > 0) {
			return true;
		}
		return false;
	}

}
