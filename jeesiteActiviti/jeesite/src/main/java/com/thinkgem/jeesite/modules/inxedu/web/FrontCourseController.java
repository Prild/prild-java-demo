package com.thinkgem.jeesite.modules.inxedu.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.thinkgem.jeesite.common.utils.ObjectUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.course.Course;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseDto;
import com.thinkgem.jeesite.modules.inxedu.entity.course.QueryCourse;
import com.thinkgem.jeesite.modules.inxedu.entity.kpoint.CourseKpoint;
import com.thinkgem.jeesite.modules.inxedu.entity.subject.QuerySubject;
import com.thinkgem.jeesite.modules.inxedu.entity.subject.Subject;
import com.thinkgem.jeesite.modules.inxedu.entity.teacher.QueryTeacher;
import com.thinkgem.jeesite.modules.inxedu.entity.teacher.Teacher;
import com.thinkgem.jeesite.modules.inxedu.service.CourseDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.CourseFavoritesDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.CourseKpointService;
import com.thinkgem.jeesite.modules.inxedu.service.SubjectService;
import com.thinkgem.jeesite.modules.inxedu.service.TeacherService;
import com.thinkgem.jeesite.modules.inxedu.utils.SingletonLoginUtils;

@Controller
@RequestMapping("${frontPath}/front")
public class FrontCourseController extends BaseController {
	@InitBinder("queryCourse")
	public void courseInitBinder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("queryCourse.");
	}

	@Autowired
	private CourseDaoService courseDaoService;
	@Autowired
	private CourseKpointService courseKpointService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private CourseFavoritesDaoService courseFavoritesDaoService;
	@Autowired
	private SubjectService subjectService;

	@RequestMapping("/showcoulist")
	public ModelAndView allCourse(HttpServletRequest request, Integer pageNum, Integer pageSize, @ModelAttribute("queryCourse") QueryCourse queryCourse) {
		ModelAndView model = new ModelAndView("modules/web/course/courses-list");
		try {
			// courseList
			queryCourse.setIsavaliable(1);// 上架的

			String num = request.getParameter("page.currentPage");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 12 : pageSize);
			List<CourseDto> courseList = courseDaoService.queryWebCourseListPage(queryCourse);//页面点击讲师、 专业、或者关注度、最新、热点
			PageInfo<CourseDto> pageInfo = new PageInfo<CourseDto>(courseList);
			model.addObject("page", pageInfo);
			model.addObject("courseList", courseList);

			// teacherList
			QueryTeacher queryTeacher = new QueryTeacher();
			List<Teacher> teacherList = teacherService.queryTeacherList(queryTeacher);
			model.addObject("teacherList", teacherList);

			// subjectList
			QuerySubject query = new QuerySubject();
			query.setParentId(0);
			query.setSubjectId(-1);
			List<Subject> subjectList = subjectService.getSubjectList(query);
			model.addObject("subjectList", subjectList);

			// sonSubjectList(业务：点击父专业弹出子专业)
			Integer subjectId = queryCourse.getSubjectId();
			Subject subject = new Subject();
			if (ObjectUtils.isNotEmpty(subjectId)) {
				subject.setSubjectId(Integer.valueOf(subjectId));
				subject = subjectService.getSubjectBySubject(subject);//
				List<Subject> sonSubjectList = null;
				if (ObjectUtils.isNotEmpty(subject)) {
					if (subject.getSubjectId() != 0) {// 有子
						sonSubjectList = subjectService.getSubjectListByOne(subject.getSubjectId());
						model.addObject("sonSubjectList", sonSubjectList);
						request.setAttribute("sonSubjectList", sonSubjectList);
					} else {// 无子
						sonSubjectList = subjectService.getSubjectListByOne(subject.getSubjectId());
						request.setAttribute("sonSubjectList", sonSubjectList);
					}
					model.addObject("sonSubjectList", sonSubjectList);
					request.setAttribute("sonSubjectList", sonSubjectList);

				}
			}

			// String subjectId =request.getParameter("queryCourse.subjectId");
			// if (StringUtils.isNoneEmpty(subjectId)) {
			// List<Subject> sonSubjectList =
			// subjectService.getSubjectListByOne(Integer.valueOf(subjectId));
			// model.addObject("sonSubjectList", sonSubjectList);
			// }

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return model;
	}

	/**
	 * 课程详情
	 */
	@RequestMapping("/couinfo/{courseId}")
	public ModelAndView startLearnCourse(@PathVariable("courseId") Integer courseId, ShiroHttpServletRequest request) {
		ModelAndView model = new ModelAndView("modules/web/course/course-infor");
		try {
			// course
			Course course = courseDaoService.queryCourseById(courseId);
			if (ObjectUtils.isNotEmpty(course)) {
				model.addObject("course", course);
				courseDaoService.updateCourseCount("pageViewcount", courseId);// 更新课程浏览量

				// parentKpointList 课程知识点目录
				List<CourseKpoint> parentKpointList = new ArrayList<CourseKpoint>();// 一级
																					// 课程知识点
				List<CourseKpoint> kpointList = courseKpointService.queryCourseKpointByCourseId(courseId);

				if (kpointList != null && kpointList.size() > 0) {
					for (CourseKpoint temp : kpointList) {
						if (temp.getParentId() == 0) {
							parentKpointList.add(temp);
						}
					}
					int freeVideoId = 0;
					for (CourseKpoint tempParent : parentKpointList) {
						for (CourseKpoint temp : kpointList) {
							if (temp.getParentId() == tempParent.getKpointId()) {
								tempParent.getKpointList().add(temp);
							}
							// 获取一个可以试听的视频id
							if (freeVideoId == 0 && temp.getFree() == 1 && temp.getKpointType() == 1) {
								freeVideoId = temp.getKpointId();
								model.addObject("freeVideoId", freeVideoId);
							}
						}
					}
					model.addObject("parentKpointList", parentKpointList);
				}

				// teacherList
				List<Teacher> teacherList = teacherService.queryCourseTeacerList(courseId);
				model.addObject("teacherList", teacherList);

				// courseList 相关课程(排除自己，存在于课程专业链中相关专业的课程)
				List<CourseDto> courseList = courseDaoService.queryInterfixCourseList(course.getSubjectId(), courseId, 5);
				model.addObject("courseList", courseList);

				// 是否登录/登录用户是否收藏
				int userId = SingletonLoginUtils.getLoginUserId(request);// 在用户登录时候已经保存并设置了缓存
				if (userId > 0) {
//					int isFavorites = courseFavoritesDaoService.checkFavorites(userId, courseId);
//					model.addObject("isFavorites", isFavorites > 0 ? true : false);
				}
			}
			model.addObject("isok", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return model;

	}
}
