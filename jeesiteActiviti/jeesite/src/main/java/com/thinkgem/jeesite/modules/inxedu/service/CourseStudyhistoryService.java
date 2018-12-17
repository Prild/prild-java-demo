package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.ObjectUtils;
import com.thinkgem.jeesite.modules.inxedu.dao.CourseStudyhistoryDao;
import com.thinkgem.jeesite.modules.inxedu.entity.course.Course;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseStudyhistory;
import com.thinkgem.jeesite.modules.inxedu.entity.kpoint.CourseKpoint;
import com.thinkgem.jeesite.modules.inxedu.utils.SingletonLoginUtils;

@Service
@Transactional
public class CourseStudyhistoryService implements CourseStudyhistoryDao{
@Autowired
private CourseStudyhistoryDao courseStudyhistoryDao;
@Autowired
private CourseDaoService courseDaoService;
@Autowired
private CourseKpointService courseKpointService;
	@Override
	public int addCourseStudyhistory(CourseStudyhistory courseStudyhistory) {
		return courseStudyhistoryDao.addCourseStudyhistory(courseStudyhistory);
	}

	@Override
	public boolean deleteCourseStudyhistoryById(int id) {
		return courseStudyhistoryDao.deleteCourseStudyhistoryById(id);
	}

	@Override
	public boolean updateCourseStudyhistory(CourseStudyhistory courseStudyhistory) {
		return courseStudyhistoryDao.updateCourseStudyhistory(courseStudyhistory);
	}

	@Override
	public List<CourseStudyhistory> getCourseStudyhistoryList(CourseStudyhistory courseStudyhistory) {
		return courseStudyhistoryDao.getCourseStudyhistoryList(courseStudyhistory);
	}

	@Override
	public List<CourseStudyhistory> getCourseStudyhistoryListByCouId(int courseId) {
		return courseStudyhistoryDao.getCourseStudyhistoryListByCouId(courseId);
	}

	@Override
	public int getCourseStudyhistoryCountByCouId(int courseId) {
		return courseStudyhistoryDao.getCourseStudyhistoryCountByCouId(courseId);
	}

	/**
	 * 播放次数
	 */
	public void playertimes(CourseStudyhistory courseStudyhistory) {
		//edu_course_studyhistory和edu_course(学习记录表是否有该课程)
		Course course = courseDaoService.queryCourseById(courseStudyhistory.getCourseId());
		if (ObjectUtils.isEmpty(course)) {
			return;
		}
		
		//edu_course_studyhistory和edu_course_kpoint(学习记录表是否有该课程下的知识点)
		CourseKpoint courseKpoints = courseKpointService.queryCourseKpointById(courseStudyhistory.getKpointId());
		if (ObjectUtils.isEmpty(courseKpoints)) {
			return;
		}
		//有课程有知识点(学员学习的课程在不在edu_course_studyhistory表中)
		CourseStudyhistory history = new CourseStudyhistory();
		history.setUserId(courseStudyhistory.getUserId());
		history.setCourseId(courseStudyhistory.getCourseId());
		List<CourseStudyhistory>  courseStudyhistoriesList = getCourseStudyhistoryList(courseStudyhistory);
		if (ObjectUtils.isEmpty(courseStudyhistoriesList)) {//不在表中，添加
			courseStudyhistory.setCourseName(course.getCourseName());//课程名
			courseStudyhistory.setKpointName(courseKpoints.getName());//知识点名字
			courseStudyhistory.setUpdateTime(new Date());
			courseStudyhistory.setDataback(DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + ",");//playercount小于20时记录,备注观看的时间，叠加
			courseStudyhistory.setPlayercount(1L);//第一次播放
			
			addCourseStudyhistory(courseStudyhistory);//保存
			
		}else {//在表中，更新学员学习该课程的信息
			CourseStudyhistory courseStudy = courseStudyhistoriesList.get(0);
			courseStudy.setKpointName(courseKpoints.getName());
			courseStudy.setCourseName(course.getCourseName());
			courseStudy.setUpdateTime(new Date());
			// 更新时间记录存字段
			courseStudy.setDataback(DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "," + courseStudy.getDataback());
			// 当字符串大于500时截取，留前面最新的
			if (courseStudy.getDataback().length() > 500) {
				courseStudy.setDataback(courseStudy.getDataback().substring(0, 500));
			}
			courseStudy.setPlayercount(courseStudy.getPlayercount() + 1);
			updateCourseStudyhistory(courseStudy);
		}
		
		
		
	}

}
