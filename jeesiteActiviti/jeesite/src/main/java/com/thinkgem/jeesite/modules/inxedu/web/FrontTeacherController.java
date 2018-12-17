package com.thinkgem.jeesite.modules.inxedu.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseDto;
import com.thinkgem.jeesite.modules.inxedu.entity.course.QueryCourse;
import com.thinkgem.jeesite.modules.inxedu.entity.subject.QuerySubject;
import com.thinkgem.jeesite.modules.inxedu.entity.subject.Subject;
import com.thinkgem.jeesite.modules.inxedu.entity.teacher.QueryTeacher;
import com.thinkgem.jeesite.modules.inxedu.entity.teacher.Teacher;
import com.thinkgem.jeesite.modules.inxedu.service.CourseDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.CourseTeacherService;
import com.thinkgem.jeesite.modules.inxedu.service.SubjectService;
import com.thinkgem.jeesite.modules.inxedu.service.TeacherService;

@Controller
@RequestMapping("${frontPath}/front")
public class FrontTeacherController extends BaseController{
	// 绑定属性 封装参数
	@InitBinder("queryTeacher")
	public void initQueryTeacher(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("queryTeacher.");
	}
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private CourseDaoService courseDaoService;
	@Autowired
	private SubjectService subjectService;
	
	/**
	 * 首页：点击所有教师teacherlist
	 */
	@RequestMapping("/teacherlist")
	public ModelAndView getTeacherList(Integer pageNum,Integer pageSize,HttpServletRequest request){
		ModelAndView view = new ModelAndView("modules/web/teacher/teacher_list");
		
		try {
			//teacherList
			String subjectId =request.getParameter("queryTeacher.subjectId");//initbinder不起作用的情况下使用
			QueryTeacher queryTeacher = new QueryTeacher();
			if (StringUtils.isNotEmpty(subjectId)) {
				queryTeacher.setSubjectId(Integer.valueOf(subjectId));
			}
			String num = request.getParameter("page.currentPage");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 8 : pageSize);
			List<Teacher>  teacherList =teacherService.queryTeacherListPage(queryTeacher);
			PageInfo<Teacher> pageInfo = new PageInfo<Teacher>(teacherList);
			view.addObject("page", pageInfo);
			view.addObject("teacherList", teacherList);
			view.addObject("subjectId", queryTeacher.getSubjectId());//教师专业id
			
			//subjectList
			QuerySubject query = new QuerySubject();
			query.setStatus(0);//0 默认 1删除
			query.setParentId(0);//父ID=0,查询所有父专业
			List<Subject> subjectList = subjectService.getSubjectList(query);
			view.addObject("subjectList", subjectList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return view;
	}
	
	/**
	 * 首页：点击预览教师 
	 */
	@RequestMapping("/teacher/{teacherId}")
	public ModelAndView getTeacherById(@PathVariable("teacherId") int teacherId,@ModelAttribute("Teacher") Teacher teacher,Integer pageNum,Integer pageSize,HttpServletRequest request){
		ModelAndView view = new ModelAndView("modules/web/teacher/teacher_info");
		try {
			//teacher
			teacher = teacherService.getTeacherById(teacherId);
			view.addObject("teacher", teacher);
			
			//courseList
			QueryCourse queryCourse = new QueryCourse();
			queryCourse.setTeacherId(teacherId);
			queryCourse.setIsavaliable(1);//1正常 2删除
			String num = request.getParameter("page.currentPage");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
			List<CourseDto> courseList = courseDaoService.queryWebCourseListPage(queryCourse);
			PageInfo<CourseDto> pageInfo = new PageInfo<CourseDto>(courseList);
			view.addObject("page", pageInfo);
			
			
			view.addObject("courseList", courseList);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return view;
		
	}
	
}
