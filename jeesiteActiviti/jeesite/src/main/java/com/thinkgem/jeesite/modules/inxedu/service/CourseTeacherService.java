package com.thinkgem.jeesite.modules.inxedu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.CourseTeacherDao;

@Service
@Transactional
public class CourseTeacherService implements CourseTeacherDao {
	@Autowired
	private CourseTeacherDao courseTeacherDao;
	/**
	 * 添加课程与讲师的关联数
	 */
	@Override
	public boolean addCourseTeacher(String value) {
		return courseTeacherDao.addCourseTeacher(value);

	}

	/**
	 * 删除课程与讲师的关联数
	 */
	@Override
	public boolean deleteCourseTeacher(int courseId) {
		return courseTeacherDao.deleteCourseTeacher(courseId);
	}

}
