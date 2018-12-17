package com.thinkgem.jeesite.modules.inxedu.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.thinkgem.jeesite.common.utils.GsonUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.Questions;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.QuestionsTag;
import com.thinkgem.jeesite.modules.inxedu.service.QuestionsDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.QuestionsTagDaoService;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

@Controller
@RequestMapping("${adminPath}/questions")
public class AdminQuestionsTagsController extends BaseController {

	@InitBinder("questionsTag")
	public void updateInitBinder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("questionsTag.");
	}
	@InitBinder("questions")
	public void initBinderQuestions(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("questions.");
	}
	
	@Autowired
	private QuestionsTagDaoService questionsTagDaoService;
	@Autowired
	private QuestionsDaoService questionsDaoService;
	

	
	/**
	 * 更新标签name
	 */
	@RequestMapping("/updatequestionsTagName")
	@ResponseBody
	public Map<String, Object> updateQuestionsTagName(@ModelAttribute("questionsTag") QuestionsTag questionsTag) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			boolean update = questionsTagDaoService.updateQuestionsTag(questionsTag);
			if (update) {
				json.put("success", true);
			} else {
				json.put("success", false);
				json.put("message", "更新失败");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json;
	}

	/**
	 * 增加标签
	 */
	@RequestMapping("/createQuestionsTag")
	@ResponseBody
	public Map<String, Object> createQuestionsTag(@ModelAttribute("questionsTag") QuestionsTag questionsTag) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			int create = questionsTagDaoService.createQuestionsTag(questionsTag);
			if (create > 0) {
				json.put("success", true);
				json.put("entity", questionsTag);
			} else {
				json.put("success", false);
				json.put("message", "新建标签失败");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json;

	}

	/**
	 * 删除标签
	 */
	@RequestMapping("/deleteQuestionsTag/{questionsTagId}")
	@ResponseBody
	public Map<String, Object> deleteQuestionsTag(@PathVariable("questionsTagId") int questionsTagId) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			boolean delete = questionsTagDaoService.deleteQuestionsTag(questionsTagId);
			if (delete) {
				json.put("success", true);
			} else {
				json.put("success", false);
				json.put("message", "删除失败");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json;

	}

	/**
	 * 首页：问答标签列表展示
	 */
	@RequestMapping("/toQuestionsTagList")
	public ModelAndView toQuestionsTagList() {
		ModelAndView model = new ModelAndView();
		model.setViewName("modules/questions/questionsTag_list");
		try {
			QuestionsTag queryQuestionsTag = new QuestionsTag();
			List<QuestionsTag> questionsTagList = questionsTagDaoService.getQuestionsTagList(queryQuestionsTag);
			model.addObject("questionsTagList", GsonUtils.GsonString(questionsTagList));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return model;

	}

}
