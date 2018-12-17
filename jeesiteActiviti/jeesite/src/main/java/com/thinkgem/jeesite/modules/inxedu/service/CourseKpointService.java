package com.thinkgem.jeesite.modules.inxedu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.inxedu.dao.CourseKpointDao;
import com.thinkgem.jeesite.modules.inxedu.entity.kpoint.CourseKpoint;
import com.thinkgem.jeesite.modules.inxedu.entity.kpoint.CourseKpointDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CourseKpoint 课程章节 管理接口
 * 
 * @author www.inxedu.com
 * @param <T>
 */
@Service
@Transactional
public class CourseKpointService implements CourseKpointDao {

	@Autowired
	private CourseKpointDao courseKpointDao;

	public List<CourseKpoint> queryCourseKpointByCourseId(int courseId) {
		return courseKpointDao.queryCourseKpointByCourseId(courseId);
	}

	public CourseKpointDto queryCourseKpointById(int kpointId) {
		return courseKpointDao.queryCourseKpointById(kpointId);
	}

	public boolean deleteKpointByIds(String ids) {
		boolean delete = false;
		if (ids != null && ids.trim().length() > 0) {
			if (ids.trim().endsWith(",")) {
				ids = ids.trim().substring(0, ids.trim().length() - 1);
			}
			delete = courseKpointDao.deleteKpointByIds(ids);
		}
		return delete;
	}

	public boolean updateKpointParentId(int kpointId, int parentId) {
		return courseKpointDao.updateKpointParentId(kpointId, parentId);

	}

	@Override
	public int getSecondLevelKpointCount(Long courseId) {
		return courseKpointDao.getSecondLevelKpointCount(courseId);
	}

	@Override
	public int addCourseKpoint(CourseKpoint courseKpoint) {
		// TODO Auto-generated method stub
		return courseKpointDao.addCourseKpoint(courseKpoint);
	}

	@Override
	public boolean updateKpoint(CourseKpoint kpoint) {
		return courseKpointDao.updateKpoint(kpoint);
		// TODO Auto-generated method stub

	}
}