package com.thinkgem.jeesite.modules.inxedu.web;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.thinkgem.jeesite.common.utils.GsonUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseDto;
import com.thinkgem.jeesite.modules.inxedu.entity.subject.QuerySubject;
import com.thinkgem.jeesite.modules.inxedu.entity.subject.Subject;
import com.thinkgem.jeesite.modules.inxedu.entity.teacher.QueryTeacher;
import com.thinkgem.jeesite.modules.inxedu.entity.teacher.Teacher;
import com.thinkgem.jeesite.modules.inxedu.service.SubjectService;
import com.thinkgem.jeesite.modules.inxedu.service.TeacherService;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

@Controller
@RequestMapping("${adminPath}/teacher")
public class AdminTeacherController extends BaseController{
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AdminSubjectController.class);
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SubjectService subjectService;
	@InitBinder("teacher")
	public void InitBinderTeacher(WebDataBinder binder){
		binder.setFieldDefaultPrefix("teacher.");//把页面传过来subject.的所有参数封装进对象subject
	}
	@InitBinder("queryTeacher")
	public void InitBinderqueryTeacher(WebDataBinder binder){
		binder.setFieldDefaultPrefix("queryTeacher.");
	}
	
	@RequestMapping(value="/toUpdate/{id_}",method=RequestMethod.GET)//这个地方不要加$!!!!!!!!!参数/toUpdate/${id}
	public ModelAndView toUpdateTeacher(@PathVariable("id_") Integer  id_){
		ModelAndView model = new ModelAndView();
		Teacher teacher = new Teacher();
		teacher = teacherService.getTeacherById(id_);
		model.addObject("teacher", teacher);
		
		QuerySubject query = new QuerySubject();
		List<Subject> subjectList =  subjectService.getSubjectList(query);
		if (teacher != null && teacher.getSubjectId()+"" != null) {
			for (Subject subject : subjectList) {
				if (teacher.getSubjectId()==subject.getSubjectId()) {
					model.addObject("subject",subject);//教师专业
				}
			}
		}
		model.addObject("subjectList", GsonUtils.GsonString(subjectList));//list一定要转成json格式 不然前台报错
		model.setViewName("modules/teacher/teacher_update");
		return model;
		
	}
	
	@RequestMapping(value="/update")
	public String  updateTeacher(@ModelAttribute("teacher") Teacher teacher){
		try {
			teacher.setUpdateTime(new Date());
			teacherService.updateTeacher(teacher);
		} catch (Exception e) {
			logger.error("updateTeacher()--error", e);
		}
		return "redirect:"+adminPath+"/teacher/list";//redirect:/a/teacher/list
	}
	
	@RequestMapping(value="/toadd")
	public ModelAndView  toaddTeacher(){
		ModelAndView model = new ModelAndView();
		model.setViewName("modules/teacher/add_teacher");
		try {
			QuerySubject query = new QuerySubject();
			List<Subject> subjectList =  subjectService.getSubjectList(query);
			model.addObject("subjectList", GsonUtils.GsonString(subjectList));//list一定要转成json格式 不然前台报错
		} catch (Exception e) {
			logger.error("toaddTeacher()--error", e);
			}
		return model;
	}
	
	@RequestMapping(value="/add")
	public String  addTeacher(@ModelAttribute("teacher") Teacher teacher,
			HttpServletResponse resp){
		try {
			teacher.setStatus(0);
//			teacher.setId(IdGen.uuid());
			teacher.setCreateTime(new Date());
			teacherService.addTeacher(teacher);
//			model.setViewName("modules/teacher/add_teacher");
		} catch (Exception e) {
			logger.error("addTeacher()--error", e);
		}
		return "redirect:"+adminPath+"/teacher/list";//redirect:/a/teacher/list
	}
	@RequestMapping(value="/delete/{id_}",method=RequestMethod.GET)
	public String  deleteTeacher(@PathVariable("id_") int id_){
		try {
			teacherService.deleteTeacherById(id_);
		} catch (Exception e) {
			logger.error("addTeacher()--error", e);
		}
		return "redirect:"+adminPath+"/teacher/list";//redirect:/a/teacher/list
	}
	
	
	
	@RequestMapping(value="/list")
	public ModelAndView  teacherList(@ModelAttribute("queryTeacher") QueryTeacher queryTeacher,Integer pageNum,Integer pageSize,HttpServletRequest request){
		ModelAndView model = new ModelAndView();
		model.setViewName("modules/teacher/teacher_list");
		try {
			String num = request.getParameter("page.currentPage");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
			List<Teacher> teacherList =  teacherService.queryTeacherListPage(queryTeacher);
			PageInfo<Teacher> pageInfo = new PageInfo<Teacher>(teacherList);
			model.addObject("page", pageInfo);
			model.addObject("teacherList", teacherList);//页面循环输出
			model.addObject("queryTeacher",queryTeacher);//查询条件
		} catch (Exception e) {
			logger.error("teacherList()--error", e);
		}
		return model;
	}
	
    /**
     * 挑选讲师列表
     */
    @RequestMapping("/selectlist/{type}")
    public ModelAndView queryselectTeacherList(HttpServletRequest request, @ModelAttribute("queryTeacher") QueryTeacher queryTeacher,@PathVariable("type") String type) {      
    	ModelAndView model = new ModelAndView();
    	try {
    		model.setViewName("modules/teacher/select_teacher_list");// 选择讲师列表页面
            // 查詢讲师
            List<Teacher> teacherList = teacherService.queryTeacherListPage(queryTeacher);
            // 把返回的数据放到model中
            model.addObject("teacherList", teacherList);
            model.addObject("queryTeacher", queryTeacher);
            model.addObject("type", type);
        } catch (Exception e) {
            logger.error("AdminTeacherController.queryTeacherList", e);
        }
        return model;
    }	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
