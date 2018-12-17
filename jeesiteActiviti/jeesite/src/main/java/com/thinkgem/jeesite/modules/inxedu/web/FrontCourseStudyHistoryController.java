package com.thinkgem.jeesite.modules.inxedu.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseStudyhistory;
import com.thinkgem.jeesite.modules.inxedu.service.CourseStudyhistoryService;
import com.thinkgem.jeesite.modules.inxedu.utils.SingletonLoginUtils;
@Controller
@RequestMapping("${frontPath}")
public class FrontCourseStudyHistoryController extends BaseController{
	@Autowired
	private CourseStudyhistoryService courseStudyhistoryService;
	
	@RequestMapping("/couserStudyHistory/ajax/playertimes")
	@ResponseBody
	public Object addCoursePlayerTime(HttpServletRequest request, @ModelAttribute("courseId") int courseId,
			@ModelAttribute("kpointId") int kpointId) {
		Map<String,Object> json = new HashMap<String,Object>();
		try {
			// 要更新3个表 edu_course_profile,edu_course_studyhistory,edu_course_kpoint
			try {
				CourseStudyhistory courseStudyhistory = new CourseStudyhistory();
				courseStudyhistory.setCourseId(courseId);
				courseStudyhistory.setKpointId(kpointId);
				courseStudyhistory.setUserId(SingletonLoginUtils.getLoginUserId(request));
				courseStudyhistoryService.playertimes(courseStudyhistory);
				json = this.setJson(true, "", null);
			} catch (Exception e) {
				logger.error("CourseStudyhistoryController.addCoursePlayerTime()",e);
				return this.setAjaxException(json);
			}
			return json;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json;
		}
	
}
