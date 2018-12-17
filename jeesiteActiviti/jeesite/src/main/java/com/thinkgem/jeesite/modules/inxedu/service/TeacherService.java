package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.TeacherDao;
import com.thinkgem.jeesite.modules.inxedu.entity.teacher.QueryTeacher;
import com.thinkgem.jeesite.modules.inxedu.entity.teacher.Teacher;

@Service
@Transactional
public class TeacherService implements TeacherDao {

	@Autowired
	TeacherDao teacherDao;

	/**
	 * 添加Teacher
	 */
	public int addTeacher(Teacher teacher) {
		return teacherDao.addTeacher(teacher);
	}

	/**
	 * 删除讲师
	 */
	public void deleteTeacherById(int tcId) {
		teacherDao.deleteTeacherById(tcId);
	}

	/**
	 * 修改讲师信息
	 */
	public void updateTeacher(Teacher teacher) {
		teacherDao.updateTeacher(teacher);
	}

	/**
	 * 通过讲师ID，查询讲师数据
	 */
	public Teacher getTeacherById(int id) {
		return teacherDao.get(id);

	}

	/**
	 * 分页查询讲师列表
	 */
	public List<Teacher> queryTeacherListPage(@Param("param1") QueryTeacher query) {
		return teacherDao.queryTeacherListPage(query);
	}

	/**
	 * 查询课程讲师列表
	 */
	public List<Teacher> queryCourseTeacerList(int courseId) {
		return teacherDao.queryCourseTeacerList(courseId);

	}

	/**
	 * 条件查询老师列表
	 */
	public List<Teacher> queryTeacherList(QueryTeacher queryTeacher) {
		return teacherDao.queryTeacherList(queryTeacher);
	}

	@Override
	public Teacher get(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
