package com.thinkgem.jeesite.modules.inxedu.web;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.openxml4j.util.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.thinkgem.jeesite.common.utils.ObjectUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.course.Course;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseDto;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseFavorites;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseStudyhistory;
import com.thinkgem.jeesite.modules.inxedu.entity.course.FavouriteCourseDTO;
import com.thinkgem.jeesite.modules.inxedu.entity.kpoint.CourseKpoint;
import com.thinkgem.jeesite.modules.inxedu.entity.teacher.Teacher;
import com.thinkgem.jeesite.modules.inxedu.service.CourseDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.CourseFavoritesDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.CourseKpointService;
import com.thinkgem.jeesite.modules.inxedu.service.CourseStudyhistoryService;
import com.thinkgem.jeesite.modules.inxedu.service.MsgReceiveDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.StudentUserService;
import com.thinkgem.jeesite.modules.inxedu.service.TeacherService;
import com.thinkgem.jeesite.modules.inxedu.service.WebsiteProfileDaoService;
import com.thinkgem.jeesite.modules.inxedu.utils.SingletonLoginUtils;

@Controller
@RequestMapping("${frontPath}")
public class FrontUserMyCourseController extends BaseController {
	@Autowired
	private StudentUserService studentUserService;
	@Autowired
	private WebsiteProfileDaoService websiteProfileDaoService;
	@Autowired
	private MsgReceiveDaoService msgReceiveDaoService;
	@Autowired
	private CourseDaoService courseDaoService;
	@Autowired
	private CourseKpointService courseKpointService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private CourseFavoritesDaoService courseFavoritesDaoService;
	@Autowired
	private CourseStudyhistoryService courseStudyhistoryService;

	@InitBinder("studentUser")
	public void initBinderUser(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("user.");
	}

	/**
	 * 收藏课程
	 */
	@RequestMapping("/front/createfavorites/{courseId}")
	@ResponseBody
	public Map<String, Object> createfavorites(HttpServletRequest request, @PathVariable("courseId") Integer courseId) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			// 未登录
			int userId = SingletonLoginUtils.getLoginUserId(request);
			if (userId <= 0) {
				return this.setJson(false, "未登录", null);
			}
			// 已收藏
			boolean isFavorites = courseFavoritesDaoService.checkFavorites_(userId, courseId);
			if (isFavorites) {
				return this.setJson(false, "已收藏", null);
			}
			// 收藏

			FavouriteCourseDTO courseFavorites = new FavouriteCourseDTO();
			courseFavorites.setCourseId(courseId);
			courseFavorites.setUserId(userId);
			courseFavorites.setAddTime(new Date());
			courseFavoritesDaoService.createCourseFavorites(courseFavorites);

			json = this.setJson(true, "添加成功", null);
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
			json = this.setAjaxException(json);
		}
		return json;
	}
	
//start:***************************视频/文本播放->后台记录播放信息****************************************************
	//视频播放/文本学习
	@RequestMapping("/uc/play/{courseId}")
	public ModelAndView playVideo(HttpServletRequest request, @PathVariable("courseId") int courseId) {
		ModelAndView model = new ModelAndView();

		try {
			model.setViewName("modules/web/ucenter/player-video");
			int userId = SingletonLoginUtils.getLoginUserId(request);

			// 是否收藏------------------
			boolean isFavorites = courseFavoritesDaoService.checkFavorites_(userId, courseId);
			model.addObject("isFavorites", isFavorites);

			Course course = courseDaoService.queryCourseById(courseId);
			if (ObjectUtils.isNotEmpty(course)) {
				model.addObject("course", course);
				// 课程目录--------------parentKpointList
				List<CourseKpoint> parentKpointList = new ArrayList<CourseKpoint>();
				List<CourseKpoint> kpointList = courseKpointService.queryCourseKpointByCourseId(courseId);
				if (kpointList != null && kpointList.size() > 0) {
					for (CourseKpoint temp : kpointList) {
						if (temp.getParentId() == 0) {
							parentKpointList.add(temp);
						}
					}
					for (CourseKpoint tempParent : parentKpointList) {
						for (CourseKpoint temp : kpointList) {
							if (temp.getParentId() == tempParent.getKpointId()) {
								tempParent.getKpointList().add(temp);
							}
						}
					}
					model.addObject("parentKpointList", parentKpointList);// 含一级
																			// 二级目录
				}
				// 猜你想学----------courseList 相关课程(排除自己，存在于课程专业链中相关专业的课程)
				List<CourseDto> courseList = courseDaoService.queryInterfixCourseList(course.getSubjectId(), courseId, 5);
				model.addObject("courseList", courseList);

				// teacherList
				List<Teacher> teacherList = teacherService.queryCourseTeacerList(courseId);
				model.addObject("teacherList", teacherList);

				// 我的学习记录-------------学习进度
				CourseStudyhistory courseStudyhistory = new CourseStudyhistory();
				courseStudyhistory.setUserId(userId);
				courseStudyhistory.setCourseId(courseId);
				List<CourseStudyhistory> couStudyhistorysLearned = courseStudyhistoryService.getCourseStudyhistoryList(courseStudyhistory);
				int learnedSize = 0;
				if (ObjectUtils.isNotEmpty(couStudyhistorysLearned) && couStudyhistorysLearned.size() > 0) {
					learnedSize = couStudyhistorysLearned.size();
				}
				int count = courseKpointService.getSecondLevelKpointCount(Long.valueOf(courseId));
				NumberFormat numberFormat = NumberFormat.getInstance();
				numberFormat.setMaximumFractionDigits(0);
				String studyPercent = numberFormat.format((float) learnedSize / (float) count * 100);
				studyPercent = (count == 0 ? "0" : studyPercent);
				course.setStudyPercent(studyPercent);
				model.addObject("isok", true);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return model;
	}

//end:***************************视频/文本播放->后台记录播放信息****************************************************
}
