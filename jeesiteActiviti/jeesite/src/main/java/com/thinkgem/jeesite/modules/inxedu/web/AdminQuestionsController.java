package com.thinkgem.jeesite.modules.inxedu.web;

import java.util.Date;
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
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.Questions;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.QuestionsComment;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.QuestionsTag;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.QuestionsTagRelation;
import com.thinkgem.jeesite.modules.inxedu.entity.teacher.Teacher;
import com.thinkgem.jeesite.modules.inxedu.service.QuestionsCommentDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.QuestionsDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.QuestionsTagDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.QuestionsTagRelationDaoService;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;
@Controller
@RequestMapping("${adminPath}/questions")
public class AdminQuestionsController extends BaseController{
	@Autowired
	private QuestionsTagDaoService questionsTagDaoService;
	@Autowired
	private QuestionsDaoService questionsDaoService;
	@Autowired
	private QuestionsCommentDaoService questionsCommentDaoService;
	@Autowired
	private QuestionsTagRelationDaoService questionsTagRelationDaoService;
	

	@InitBinder("questionsComment")
	public void QuestionsComment(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("questionsComment.");
	}
	@InitBinder("questionsTag")
	public void QuestionsTag(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("questionsTag.");
	}
	@InitBinder("questions")
	public void initBinderQuestions(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("questions.");
	}
	/**
	 * 修改
	 */
	//start:***************************************************************
	//进入修改页面
	@RequestMapping("/info/{id_}")
	public ModelAndView toUpdateQuestions(HttpServletRequest request,@ModelAttribute("questions")Questions questions,@PathVariable("id_") int id_){
		ModelAndView model = new ModelAndView("modules/questions/questions_info");// 这个一定是页面的
		try {
			//查询问题
			questions.setId_(id_);
			questions = questionsDaoService.getQuestionsById(id_);
			
			//查询 评论
			if(questions.getStatus()==1){//问题是否有回复
				QuestionsComment questionsComment=new QuestionsComment();
				//查找最佳回答回复
				questionsComment.setIsBest(1);//已采纳
				questionsComment.setQuestionId(questions.getId_());
				questionsComment.setLimitSize(1);
				questions.setQuestionsCommentList(questionsCommentDaoService.getQuestionsCommentList(questionsComment));
			}
			
			//查询 标签questions.questionsTagRelationList
			QuestionsTagRelation questionsTagRelation = new QuestionsTagRelation();
			questionsTagRelation.setQuestionsId(questions.getId_());//根据特定问答id查询出多个标签,并放入questions里面的链表中，页面循环取出
			List<QuestionsTagRelation>  questionsTagRelationList = questionsTagRelationDaoService.queryQuestionsTagRelation(questionsTagRelation);
			questions.setQuestionsTagRelationList(questionsTagRelationList);
			
			model.addObject("questions", questions);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return model;
	}
	//执行修改
	@RequestMapping("/updQuestions")
	@ResponseBody
	public Map<String, Object> updateQuestions(@ModelAttribute("questions")Questions questions,@RequestParam("questions.id_")int id){
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			//这些还是原来的
			Questions q = questionsDaoService.getQuestionsById(id);
			questions.setCusId(q.getCusId());
			questions.setType(q.getType());
			questions.setStatus(q.getStatus());
			questions.setReplyCount(q.getReplyCount());
			questions.setAddTime(q.getAddTime());
			
			//更新传过来的
			boolean update = questionsDaoService.updateQuestions(questions);
			if (update) {
				json.put("success", true);
				json.put("entity", "/questions/list");
			}else {
				json.put("success", "false");
				json.put("message", "修改失败");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json;
	}
	//end:***************************************************************
	/**
	 * 删除del
	 */
	@RequestMapping("/del/{questionsId}")
	@ResponseBody
	public Map<String, Object> delQuestions(@PathVariable("questionsId") int questionsId){
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			//删除问题
			int delQuestion = questionsDaoService.deleteQuestionsById(questionsId);
			
			//删除该问题下的评论
			int delQuestionsComment = questionsCommentDaoService.delQuestionsCommentByQuestionId(questionsId);
			
			//删除该问题下的标签
			int delTagRelation = questionsTagRelationDaoService.deleteQuestionsTagRelation(questionsId);
			
			
			if (delQuestion>0&&delQuestionsComment>=0&&delTagRelation>=0) {
				json.put("success", true);
			}else {
				json.put("success", "false");
				json.put("message", "删除失败");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json;
	}
	
	
	/**
	 * 首页
	 */
	@RequestMapping("/list")
	public ModelAndView getQuestionsList(HttpServletRequest request,@ModelAttribute("questions")Questions questions,Integer pageNum,Integer pageSize){
		ModelAndView model = new ModelAndView("modules/questions/questions_list");// 这个一定是页面的
		try{
			String num = request.getParameter("page.currentPage");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
			List<Questions> questionsList = questionsDaoService.getQuestionsList(questions);
			PageInfo<Questions> pageInfo = new PageInfo<Questions>(questionsList);
			model.addObject("page", pageInfo);
			
			model.addObject("questionsList", questionsList);
			//查询所有问答标签
			QuestionsTag questionsTag=new QuestionsTag();
			List<QuestionsTag> questionsTagList=questionsTagDaoService.getQuestionsTagList(questionsTag);
			model.addObject("questionsTagList", questionsTagList);
		}catch (Exception e) {
			logger.error("getQuestionsList()---error",e);
		}
		return model;
	}
}
