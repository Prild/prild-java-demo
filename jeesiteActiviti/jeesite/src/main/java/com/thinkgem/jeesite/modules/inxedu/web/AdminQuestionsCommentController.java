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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.Questions;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.QuestionsComment;
import com.thinkgem.jeesite.modules.inxedu.service.QuestionsCommentDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.QuestionsDaoService;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;

@Controller
@RequestMapping("${adminPath}/questionscomment")
public class AdminQuestionsCommentController extends BaseController {
	@Autowired
	private QuestionsCommentDaoService questionsCommentDaoService;
	@Autowired
	private QuestionsDaoService questionsDaoService;

	@InitBinder("questionsComment")
	public void QuestionsComment(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("questionsComment.");
	}
	
	/**
	 * 删除答复
	 */
	@RequestMapping("/del/{commentId}")
	@ResponseBody
	public Map<String, Object> delQuestionscomment(HttpServletRequest request, @PathVariable("commentId") Integer commentId) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			int del = questionsCommentDaoService.deleteQuestionsCommentById(commentId);
			if (del > 0) {
				jsonMap.put("success", true);
			} else {
				jsonMap.put("success", false);
				jsonMap.put("message", "删除失败");

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return jsonMap;
	}

	/**
	 * 采纳为最佳答案
	 */
	@RequestMapping("/ajax/acceptComment/{id_}")
	@ResponseBody
	public Map<String, Object> acceptQuestionscomment(HttpServletRequest request, @PathVariable("id_")Integer id_,@ModelAttribute("QuestionsComment") QuestionsComment questionsComment) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			/**
			 * 问题和回复的关系：1.如果问题有最佳答案，修改不了;2.最佳答案从本人回复中去除
			 */
			//根据id查出所有记录
			QuestionsComment q = questionsCommentDaoService.getQuestionsCommentById(id_);
			Questions questions = questionsDaoService.getQuestionsById(q.getQuestionId());
			if (questions.getStatus()==1) {
				json.put("success", false);
				json.put("message", "该问题已经有最佳答案");
				return json;
			}
			if (q.getCusId().intValue()==questions.getCusId()) {
				json.put("success", false);
				json.put("message", "自己回复的不能成为最佳答案");
				return json;
			}
			//更新QuestionsComment只更新"是否是最佳答案"
			q.setId_(id_);
			q.setIsBest(1);
			boolean commentUpdate = questionsCommentDaoService.updateQuestionsComment(q);
			
			//更新Questions 只更新"状态为已经有最佳答案"
			questions.setId_(q.getQuestionId());
			questions.setStatus(1);
			boolean questionsUdate = questionsDaoService.updateQuestions(questions);
			
			if (commentUpdate&&questionsUdate) {
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
	//start:*****************************************************************************************
	/**
	 *获得答复
	 */
	@RequestMapping("/getCommentById/{questionsCommentId}")
	@ResponseBody
	public Map<String, Object> getCommentById(HttpServletRequest request, @PathVariable("questionsCommentId")Integer questionsCommentId,@ModelAttribute("QuestionsComment") QuestionsComment questionsComment) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			QuestionsComment q = questionsCommentDaoService.getQuestionsCommentById(questionsCommentId);
			json.put("success", true);
			json.put("entity", q);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json;
	}
	
	/**
	 * 修改答复
	 */
	@RequestMapping("/updQuestionComment")
	@ResponseBody
	public Map<String, Object> updQuestionComment(@ModelAttribute("QuestionsComment")QuestionsComment questionsComment,@RequestParam("questionsComment.id_")Integer id_,@RequestParam("questionsComment.content") String content,@RequestParam("questionsComment.praiseCount") Integer praiseCount) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			//根据id查询出每一项值
			QuestionsComment q = questionsCommentDaoService.getQuestionsCommentById(id_);
			//只修改这两项值
			q.setContent(content);
			q.setPraiseCount(praiseCount);
			boolean update = questionsCommentDaoService.updateQuestionsComment(q);
			if (update) {
				jsonMap.put("success", true);
			} else {
				jsonMap.put("success", false);
				jsonMap.put("message", "更新失败");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return jsonMap;
	}
	
	//end:*****************************************************************************************
	/**
	 * 查看某一个答复下的所有评论(功能暂时不实现，到页面出结果了跑下流程再完善功能)
	 */
	//start:*************************************************************
	//进入评论页面
	@RequestMapping("/querybycommentid/{id}")
	public ModelAndView queryByCommentId(@ModelAttribute("questionsComment") QuestionsComment questionsComment,@ModelAttribute("page") PageEntity page,
			@PathVariable("id") Integer id) {
		ModelAndView model = new ModelAndView();
		model.setViewName("modules/questions/questionscomment_list_one");
		try {
//			QuestionsComment q = questionsCommentDaoService.getQuestionsCommentById(id);
//			List<QuestionsComment>  questionsCommentList = questionsCommentDaoService.queryQuestionsCommentListByQuestionsId(q, page);
//			model.addObject("questionsCommentList", questionsCommentList);
		} catch (Exception e) {
			
			// TODO: handle exception
		}
		return model;
	}
	
	//end:**************************************************************
	
	/**
	 * 查看所有答复
	 */
	// 进入查看回复页面
	@RequestMapping("/list")
	// info/${questions.id_}和list?questionsComment.questionId=${questions.id_}的区别
	public ModelAndView toQuestionscomment(HttpServletRequest request, @ModelAttribute("questionsComment") QuestionsComment questionsComment,Integer pageNum,Integer pageSize) {
		ModelAndView model = new ModelAndView();
		model.setViewName("modules/questions/questionscomment_list");
		// 查看所有问答
		// questionsCommentList
		
		String num = request.getParameter("page.currentPage");
		if (StringUtils.isNotEmpty(num)) {
			pageNum = Integer.valueOf(num);
		}
		PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
		List<QuestionsComment> questionsCommentList = questionsCommentDaoService.queryQuestionsCommentList(questionsComment);
		PageInfo<QuestionsComment> pageInfo = new PageInfo<QuestionsComment>(questionsCommentList);
		model.addObject("page", pageInfo);
		
		model.addObject("questionsCommentList", questionsCommentList);

		return model;
	}

}
