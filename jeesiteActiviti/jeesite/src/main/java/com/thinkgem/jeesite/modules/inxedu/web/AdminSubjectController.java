package com.thinkgem.jeesite.modules.inxedu.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.utils.GsonUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.dao.SubjectDao;
import com.thinkgem.jeesite.modules.inxedu.entity.subject.QuerySubject;
import com.thinkgem.jeesite.modules.inxedu.entity.subject.Subject;
import com.thinkgem.jeesite.modules.inxedu.service.SubjectService;

/**
 * 专业 Controller
 * @author www.inxedu.com
 */
@Controller
@RequestMapping("${adminPath}/subj")
public class AdminSubjectController extends BaseController{
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AdminSubjectController.class);
	@Autowired
	private SubjectService subjectService;
	
	@InitBinder("subject")
	public void InitBinder(WebDataBinder binder){
		binder.setFieldDefaultPrefix("subject.");//把页面传过来subject.的所有参数封装进对象subject
	}
	
	/**
	 * 到专业列表页面
	 */
	@RequestMapping("toSubjectList")
	public ModelAndView toSubjectList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		try {
			model.setViewName("modules/subject/subject_list");
			QuerySubject querySubject = new QuerySubject();
			List<Subject> subjectList = subjectService.getSubjectList(querySubject);
			model.addObject("subjectList", GsonUtils.GsonString(subjectList));
//			model.addObject("subjectList", Gson.toJson(subjectList).toString());
		} catch (Exception e) {
			logger.error("toSubjectList()---error", e);
		}
		return model;
	}

	@RequestMapping(value="deleteSubject/{subjectId}")
	@ResponseBody
	public Map<String,Object>  deleteSubject(@PathVariable("subjectId") Integer subjectId,HttpServletResponse response){
		Map<String,Object> json = new HashMap<String,Object>();
		try {
			subjectService.deleteSubject(subjectId);
			response.setStatus(200);
			json.put("success", true);
		} catch (Exception e) {
			json.put("false", e.getMessage());
			logger.error("deleteSubject()---error", e);
			}
		return json;
	}
	
	
	/**
	 * 增加专业
	 * @param subject
	 * @param subjectName
	 * @param parentId
	 */
	@RequestMapping(value="createSubject")
	@ResponseBody
	public Map<String,Object>  createSubject(@ModelAttribute("subject") Subject subject,@RequestParam("subject.subjectName") String subjectName,@RequestParam("subject.parentId") Integer parentId,
			HttpServletResponse resp){
		Map<String,Object> json = new HashMap<String,Object>();
		try {
			subject.setParentId(parentId);
			subject.setSubjectName(subjectName);
			subjectService.createSubject(subject);
			json.put("success", true);
			json.put("entity", subject);
		} catch (Exception e) {
			json.put("false", e.getMessage());
			logger.error("createSubject()--error", e);
			}
		return json;
	}
	
	
	/**
	 * 修改专业名称
	 * @param subject
	 * @param subjectName
	 * @param subjectId
	 */
	@RequestMapping(value="updatesubjectName")
	@ResponseBody
	public void updatesubjectName(@ModelAttribute("subject") Subject subject,@RequestParam("subject.subjectName") String subjectName,@RequestParam("subject.subjectId") Integer subjectId
		,HttpServletResponse response){
		try {
			subject.setSubjectName(subjectName);
			subjectService.updateSubject(subject);
			response.setStatus(200);
		} catch (Exception e) {
			response.setStatus(500, e.getMessage());
			logger.error("updateSort()--error", e);
		}
	}
	
	
	
	/**
	 * 更新父id
	 * @param parentId
	 * @param subjectId
	 */
	@RequestMapping(value="updateparentid")
	public void updateParentid(@RequestParam("parentId") Integer parentId,@RequestParam("subjectId") Integer subjectId){
		try {
			subjectService.updateSubjectParentId(subjectId, parentId);
		} catch (Exception e) {
			logger.error("updateSort()--error", e);
		}
	}
	
	
	
	///////////////////////////////第一种方法，一个参数一个参数接收传送//////////////////////////////////////////////////////////
	@RequestMapping(value = "updatesort", method = RequestMethod.POST)
	public void updatesort(@ModelAttribute("subject")Subject subject,HttpServletRequest request, HttpServletResponse response,@RequestParam("subject.subjectId") Integer subjectId,
			@RequestParam("subject.sort") Integer sort){
		try {
			subject.setSubjectId(subjectId);
			subject.setSort(sort);
			subjectService.updateSubjectSort(subject);
			 
		} catch (Exception e) {
			logger.error("updateSort()--error", e);
		}
	}
	
	////////////////////////////////第二种方法  使用initBind封装///////////////////////////////////////////////////////////////
	/**
	 * 修改排序
	 */
	@RequestMapping("updatesort")
	@ResponseBody
	public Map<String, Object> updateSort(@ModelAttribute("subject") Subject subject) {//对应initBinder("subject")
		Map<String,Object> json = new HashMap<String,Object>();
		try {
			subjectService.updateSubjectSort(subject);
//			json = this.setJson(true, null, null);
			
		} catch (Exception e) {
//			this.setAjaxException(json);
			logger.error("updateSort()--error", e);
		}
		return json;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
