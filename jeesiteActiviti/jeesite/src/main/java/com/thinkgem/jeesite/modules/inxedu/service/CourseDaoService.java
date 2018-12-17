package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.CourseDao;
import com.thinkgem.jeesite.modules.inxedu.entity.course.Course;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseDto;
import com.thinkgem.jeesite.modules.inxedu.entity.course.QueryCourse;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

@Service
@Transactional
public class CourseDaoService implements CourseDao {
	@Autowired
	private CourseDao courseDao;

	@Override
	public int addCourse(Course course) {
		// TODO Auto-generated method stub
		return courseDao.addCourse(course);
	}

	public List<CourseDto> queryCourseListPage(@Param("param1")QueryCourse query) {
		return courseDao.queryCourseListPage(query);
	}
	public List<CourseDto> queryCourseListPage_(QueryCourse query) {
		return courseDao.queryCourseListPage_(query);
	}

	@Override
	public Course queryCourseById(int courseId) {
		// TODO Auto-generated method stub
		return courseDao.queryCourseById(courseId);
	}

	@Override
	public boolean updateCourse(Course course) {
		// TODO Auto-generated method stub
		return courseDao.updateCourse(course);
	}

	@Override
	public boolean updateAvaliableCourse(int courseId, int type) {
		return courseDao.updateAvaliableCourse(courseId, type);

	}

	@Override
	public List<CourseDto> queryRecommenCourseList() {
		// TODO Auto-generated method stub
		return courseDao.queryRecommenCourseList();
	}

	@Override
	public List<CourseDto> queryCourseList(QueryCourse query) {
		// TODO Auto-generated method stub
		return courseDao.queryCourseList(query);
	}

	@Override
	public List<CourseDto> queryWebCourseListPage(QueryCourse queryCourse) {
		// TODO Auto-generated method stub
		return courseDao.queryWebCourseListPage(queryCourse);
	}



	@Override
	public List<CourseDto> queryMyCourseList(int userId) {
		// TODO Auto-generated method stub
		return courseDao.queryMyCourseList(userId);
	}

	@Override
	public int queryAllCourseCount() {
		// TODO Auto-generated method stub
		return courseDao.queryAllCourseCount();
	}

	@Override
	public List<Map<String, Object>> queryMyCourseListByMap(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return courseDao.queryMyCourseListByMap(map);
	}

	@Override
	public List<CourseDto> queryRecommenCourseListByRecommendId(Long recommendId, Long count) {
		// TODO Auto-generated method stub
		return courseDao.queryRecommenCourseListByRecommendId(recommendId, count);
	}

	@Override
	public List<CourseDto> queryCourse(QueryCourse queryCourse) {
		// TODO Auto-generated method stub
		return courseDao.queryCourse(queryCourse);
	}

	@Override
	public boolean updateCourseCount(String type, int courseId) {
		return courseDao.updateCourseCount(type, courseId);
		// TODO Auto-generated method stub

	}

	@Override
	public List<CourseDto> queryInterfixCourseList(@Param("subjectId") int subjectId, @Param("courseId") int courseId, @Param("count") int count) {
		// TODO Auto-generated method stub
		return courseDao.queryInterfixCourseList(subjectId, courseId, count);
	}

}
