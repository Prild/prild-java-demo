package com.thinkgem.jeesite.modules.inxedu.web;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.thinkgem.jeesite.common.utils.GsonUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.dao.CourseTeacherDao;
import com.thinkgem.jeesite.modules.inxedu.entity.StudentUser;
import com.thinkgem.jeesite.modules.inxedu.entity.course.Course;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseDto;
import com.thinkgem.jeesite.modules.inxedu.entity.course.QueryCourse;
import com.thinkgem.jeesite.modules.inxedu.entity.subject.QuerySubject;
import com.thinkgem.jeesite.modules.inxedu.entity.subject.Subject;
import com.thinkgem.jeesite.modules.inxedu.entity.teacher.Teacher;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteCourse;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteCourseDetailDTO;
import com.thinkgem.jeesite.modules.inxedu.service.CourseDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.SubjectService;
import com.thinkgem.jeesite.modules.inxedu.service.TeacherService;
import com.thinkgem.jeesite.modules.inxedu.service.WebsiteCourseDaoService;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;
import com.thinkgem.jeesite.modules.sys.web.jeesite.CustomTimeEditor;

@Controller
@RequestMapping("${adminPath}/course")
public class AdminCourseController extends BaseController {
	@InitBinder("queryCourse")
	public void teacherListBinder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("queryCourse.");
	}

	@InitBinder("course")
	public void addCourseBinder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("course.");
	}

	/**
	 * 页面单一时间格式 ，如果页面多时间格式，参照：https://www.cnblogs.com/flying607/p/4691714.html
	 * 或者http://blog.csdn.net/yangdi19940309/article/details/52372270
	 * @param binder
	 * @throws Exception
	 */
	@InitBinder
	public void initBinderFormat(WebDataBinder binder) throws Exception {
		// 时间格式为yyyy-MM-dd类型需要的绑定
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(df, true));
		// 时间格式为HH:mm:ss类型需要的绑定 这里我们自己定义一个CustomTimeEditor类 用来接收需要传过来的类型
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
		binder.registerCustomEditor(Time.class, null, new CustomTimeEditor(tf, true));
	}

	@Autowired
	private CourseDaoService courseDaoService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private CourseTeacherDao courseTeacherDao;
	@Autowired
	private WebsiteCourseDaoService websiteCourseDaoService;

	/**
	 * 课程首页
	 */
	@RequestMapping(value = "/list")
	public ModelAndView teacherList(@ModelAttribute("queryCourse") QueryCourse queryCourse, Integer pageNum, Integer pageSize,HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		model.setViewName("modules/course/course_list");
		try {
			String num = request.getParameter("page.pageNum");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
			
			List<CourseDto> courseList = courseDaoService.queryCourseListPage(queryCourse);
			PageInfo<CourseDto> pageInfo = new PageInfo<CourseDto>(courseList);
			model.addObject("page", pageInfo);
			
			QuerySubject subject = new QuerySubject();
			List<Subject> subjectList = subjectService.getSubjectList(subject);
			model.addObject("courseList", courseList);
			model.addObject("queryCourse", queryCourse);
			model.addObject("subjectList", GsonUtils.GsonString(subjectList));
		} catch (Exception e) {
			logger.error("courseList()--error", e);
		}
		return model;
	}

	/**
	 * 添加课程与讲师的关联数据
	 *            课程
	 */
	private void addCourseTeacher(HttpServletRequest request, Course course) {
		// 先清除之前的数据，再添加
		courseTeacherDao.deleteCourseTeacher(course.getCourseId());
		String teacherIds = request.getParameter("teacherIdArr");
		if (teacherIds != null && teacherIds.trim().length() > 0) {
			String[] tcIdArr = teacherIds.split(",");
			StringBuffer val = new StringBuffer();
			for (int i = 0; i < tcIdArr.length; i++) {
				if (i < tcIdArr.length - 1) {
					val.append("(" + course.getCourseId() + "," + tcIdArr[i] + "),");
				} else {
					val.append("(" + course.getCourseId() + "," + tcIdArr[i] + ")");
				}
			}
			courseTeacherDao.addCourseTeacher(val.toString());
		}
	}

	// start:*************************************************************************************************
	/**
	 * to修改课程页面
	 *            课程对象
	 */
	@RequestMapping("/initUpdate/{id_}")
	public ModelAndView initUpdate(HttpServletRequest request, @PathVariable("id_") Integer id_) {
		ModelAndView mView = new ModelAndView();
		mView.setViewName("modules/course/update_course");
		Course course = courseDaoService.queryCourseById(id_);

		// 课程所属专业
		QuerySubject querySubject = new QuerySubject();
		List<Subject> subjectList = subjectService.getSubjectList(querySubject);
		// 课程关联老师
		List<Teacher> teacherList = teacherService.queryCourseTeacerList(id_);

		mView.addObject("teacherList", GsonUtils.GsonString(teacherList));
		mView.addObject("subjectList", GsonUtils.GsonString(subjectList));
		mView.addObject("course", course);
		return mView;
	}
	// /////////修改课程
	@RequestMapping("/updateCourse")
	@ResponseBody
	public ModelAndView updateCourse(@ModelAttribute("course") Course course, @RequestParam("course.courseId") Integer id, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		model.setViewName("redirect:" + adminPath + "/course/list");
		try {
			// 更新课程表
			course.setCourseId(id);
			courseDaoService.updateCourse(course);
			// 更新课程教师关系表
			this.addCourseTeacher(request, course);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return model;
	}
	// end:*************************************************************************************************

	// start:*************************************************************************************************
	/**
	 * to添加课程页面
	 */
	@RequestMapping(value = "/toAddCourse")
	public ModelAndView toAddCourse() {
		ModelAndView model = new ModelAndView();
		model.setViewName("modules/course/add_course");
		try {
			QuerySubject subject = new QuerySubject();
			List<Subject> subjectList = subjectService.getSubjectList(subject);
			model.addObject("subjectList", GsonUtils.GsonString(subjectList));

		} catch (Exception e) {
			// TODO: handle exception
		}
		return model;
	}
	// /////////添加课程
	@RequestMapping("/addCourse")
	public String addCourse(HttpServletRequest request, @ModelAttribute("course") Course course, Model model) {
		try {
			course.setAddTime(new Date());
			course.setUpdateTime(new Date());
			// course.setIsavaliable(1);//上架
			// 添加课程与讲师的关联数据
			int courseC = courseDaoService.addCourse(course);
			this.addCourseTeacher(request, course);
			model.addAttribute("message", "成功添加" + courseC + "个课程");
		} catch (Exception e) {
			model.addAttribute("error", e.toString());
		}
		return "redirect:" + adminPath + "/course/list";
	}
	// end:*************************************************************************************************

	/**
	 * 注意点：和ajax交互，一定要response不然前台页面报错
	 * @param type 1正常（上架） 2（下架） 3 删除
	 */
	@RequestMapping("/avaliable/{courseId}/{type}")
	@ResponseBody
	public Map<String,Object>  avaliable(@PathVariable("courseId") Integer courseId,@PathVariable("type") Integer type){
		Map<String,Object> json = new HashMap<String,Object>();
		try {
			boolean update =  courseDaoService.updateAvaliableCourse(courseId,type);
			if (update) {
				json.put("success", true);
			}else {
				json.put("success", false);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json;//返回ajax只能这样写，如果想使用model，前台页面需要el表达式接收
	}
	
	/**
	 * 推荐课程页面
	 */
	@RequestMapping("/showrecommendList")
	 public ModelAndView showRecommendCourseList(HttpServletRequest request, @ModelAttribute("queryCourse") QueryCourse queryCourse,Integer pageNum,Integer pageSize){
    	ModelAndView model = new ModelAndView();
    	model.setViewName("modules/course/course_recommend_list");
    	try {
//			subjectList(json格式)
			QuerySubject subject = new QuerySubject();
			List<Subject> subjectList = subjectService.getSubjectList(subject);
			model.addObject("subjectList", GsonUtils.GsonString(subjectList));
//    		courseList(list含分页)
			String num = request.getParameter("page.currentPage");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
			List<CourseDto> courseList = courseDaoService.queryCourseListPage_(queryCourse);
			model.addObject("courseList", courseList);
			PageInfo<CourseDto> pageInfo = new PageInfo<CourseDto>(courseList);
			model.addObject("page", pageInfo);
//    		webstieList(list)
			List<WebsiteCourse> websiteCourseList = websiteCourseDaoService.queryWebsiteCourseList();
			model.addObject("webstieList", websiteCourseList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return model;
	}
}






