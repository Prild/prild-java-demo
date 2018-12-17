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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteCourse;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteCourseDetailDTO;
import com.thinkgem.jeesite.modules.inxedu.service.WebsiteCourseDaoService;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

@Controller
@RequestMapping("${adminPath}/website")
public class AdminWebsiteCourseController extends BaseController {
	@InitBinder("websiteCourse")
	public void initBinderWebsiteCourse(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("websiteCourse.");
	}

	@Autowired
	private WebsiteCourseDaoService websiteCourseDaoService;
	
	 @RequestMapping("/delWebsiteCourseById/{id}")
	 @ResponseBody
	 public ModelAndView delWebsiteCourseById(@PathVariable("id") int id){
		 ModelAndView model = new ModelAndView();
		 model.setViewName("redirect:" + adminPath + "/website/websiteCoursePage");
		 try {
			 websiteCourseDaoService.deleteWebsiteCourseDetailById(id);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return model;
	 }
	
	

	// start:********************************************进入课程分类修改页面

	@RequestMapping("/doUpdateWebsiteCourse/{id}")
	public ModelAndView doUpdateWebsiteCourse(@PathVariable("id") int id, @ModelAttribute("WebsiteCourse") WebsiteCourse websiteCourse) {
		ModelAndView model = new ModelAndView();
		websiteCourse = websiteCourseDaoService.queryWebsiteCourseById(id);
		model.addObject("websiteCourse", websiteCourse);
		model.setViewName("modules/website/course/websiteCourse_update");// 推荐课程分类修改页面
		return model;
	}

	// 执行修改分类
	@RequestMapping("/updateWebsiteCourse")
	@ResponseBody
	public ModelAndView updateWebsiteCourse(HttpServletRequest request, WebsiteCourse websiteCourse, @RequestParam("websiteCourse.id_") int id_) {// 这里的WebsiteCourse不能用@ModelAttribute
		ModelAndView model = new ModelAndView();
		model.setViewName("redirect:" + adminPath + "/website/websiteCoursePage");
		try {
			websiteCourse.setId_(id_);
			websiteCourseDaoService.updateWebsiteCourseById(websiteCourse);
		} catch (Exception e) {
			logger.error("addWebsiteCourse()--error", e);
		}
		return model;
	}

	// end:*****************************************

	@RequestMapping("/websiteCoursePage")
	public ModelAndView getWebsiteCourseList(HttpServletRequest request) {

		// 推荐课程分类列表
		ModelAndView model = new ModelAndView();
		model.setViewName("modules/website/course/websiteCourse_list");
		System.out.println("/////////////////////////////websiteCourse_list///////////////////////////////////////////");
		try {
			// 查询模块课程分类
			List<WebsiteCourse> websiteCourseList = websiteCourseDaoService.queryWebsiteCourseList();
			model.addObject("websiteCourseList", websiteCourseList);
		} catch (Exception e) {
			logger.error("getWebsiteCourseList()--error", e);
		}
		return model;
	}

	// start:********************************************进入课程分类添加页面
	@RequestMapping("/doAddWebsiteCourse")
	public ModelAndView getWebsiteCourse() {
		ModelAndView model = new ModelAndView();
		model.setViewName("modules/website/course/websiteCourse_add");// 推荐课程分类添加页面
		return model;
	}

	// 执行添加分类
	@RequestMapping("/addCourse")
	@ResponseBody
	public ModelAndView addWebsiteCourse(HttpServletRequest request, WebsiteCourse websiteCourse) {
		ModelAndView model = new ModelAndView();
		model.setViewName("redirect:" + adminPath + "/website/websiteCoursePage");
		try {
			websiteCourseDaoService.addWebsiteCourse(websiteCourse);
		} catch (Exception e) {
			logger.error("addWebsiteCourse()--error", e);
		}
		return model;
	}
	// end:*****************************************
}
