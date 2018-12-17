package com.thinkgem.jeesite.modules.inxedu.web;

import java.util.ArrayList;
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
import com.mysql.fabric.xmlrpc.base.Array;
import com.thinkgem.jeesite.common.constants.CommonConstants;
import com.thinkgem.jeesite.common.utils.ObjectUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.StudentUser;
import com.thinkgem.jeesite.modules.inxedu.entity.comment.Comment_;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.Questions;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.QuestionsComment;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.QuestionsTag;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.QuestionsTagRelation;
import com.thinkgem.jeesite.modules.inxedu.service.QuestionsCommentDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.QuestionsDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.QuestionsTagDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.QuestionsTagRelationDaoService;
import com.thinkgem.jeesite.modules.inxedu.utils.SingletonLoginUtils;

@Controller
@RequestMapping("${frontPath}")
public class FrontQuestionsController extends BaseController {
	@InitBinder("questions")
	public void getQuestionsBinder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("questions.");
	}

	@InitBinder("questionsComment")
	public void getQuestionsCommentBinder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("questionsComment.");
	}

	@InitBinder("questionsTag")
	public void questionsTagBinder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("questionsTag.");
	}

	@Autowired
	private QuestionsDaoService questionsDaoService;
	@Autowired
	private QuestionsTagDaoService questionsTagDaoService;
	@Autowired
	private QuestionsTagRelationDaoService questionsTagRelationDaoService;
	@Autowired
	private QuestionsCommentDaoService questionsCommentDaoService;

	/**
	 * 热门回答
	 */
	@RequestMapping("/questions/ajax/hotRecommend")
	@ResponseBody
	public Map<String, Object> hotRecommend() {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			List<Questions> questionsList = questionsDaoService.queryQuestionsOrder(4);
			json = this.setJson(true, "", questionsList);
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}

		return json;
	}

	// start:**********************************进入问答详情页*******************************************************************
	// ************************************************************************************************************************
	/**
	 * 点击问题进入问题详情页面 questions
	 */
	@RequestMapping("/questions/info/{questionId}")
	public ModelAndView getQuestionsInfo(@PathVariable("questionId") Integer questionId) {
		ModelAndView model = new ModelAndView("modules/web/questions/questions-info");
		try {
			// questions
			Questions questions = questionsDaoService.getQuestionsById(questionId);
			model.addObject("questions", questions);

			// 更新点击量BROWSE_COUNT
			questions.setBrowseCount(questions.getBrowseCount() + 1);
			questionsDaoService.updateQuestions(questions);

			// questionsTagRelationsList
			QuestionsTagRelation questionsTagRelation = new QuestionsTagRelation();
			questionsTagRelation.setQuestionsId(questionId);
			List<QuestionsTagRelation> questionsTagRelationsList = questionsTagRelationDaoService.queryQuestionsTagRelation(questionsTagRelation);
			model.addObject("questionsTagRelationList", questionsTagRelationsList);

			// 问答 ajax处理

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return model;
	}

	// /问答列表分页展示

	@RequestMapping("/questionscomment/ajax/list")
	@ResponseBody
	public ModelAndView queryComment(@ModelAttribute("questions") Questions questions, @ModelAttribute("questionsComment") QuestionsComment questionsComment,
			@RequestParam("questionsComment.questionId") Integer questionId, Integer pageNum, Integer pageSize, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		try {
			questions = questionsDaoService.getQuestionsById(questionId);

			questionsComment.setQuestionId(questionId);

			String num = request.getParameter("page.currentPage");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 8 : pageSize);

			// 该问答下的回答预览
			if (questions.getReplyCount() >= 1) {
				if (questions.getStatus() == 0) {// 可回复
					questionsComment.setIsBest(0);// 不是最佳答案
				}
				if (questions.getStatus() == 1) {// 不可回复
					questionsComment.setIsBest(1);// 最佳答案
				}
			}

			List<QuestionsComment> questionsCommentList = questionsCommentDaoService.queryQuestionsCommentListByQuestionsId(questionsComment);
			PageInfo<QuestionsComment> pageInfo = new PageInfo<QuestionsComment>(questionsCommentList);
			model.addObject("page", pageInfo);
			model.addObject("questions", questions);
			model.addObject("questionsCommentList", questionsCommentList);

			StudentUser user = SingletonLoginUtils.getLoginUser(request);
			request.setAttribute("user", user);
			model.setViewName("modules/web/questionscomment/questionscomment-ajax-list");
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return model;
	}

	// 添加父评论(修改上一级被评论数)
	@RequestMapping("/questionscomment/ajax/add")
	@ResponseBody
	public Object addQuestionsComment(HttpServletRequest request, @ModelAttribute("questionsComment") QuestionsComment questionsComment) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			int userId = SingletonLoginUtils.getLoginUserId(request);
			if (userId == 0) {
				json = this.setJson(false, "请先登录", "");
				return json;
			}
			questionsComment.setCusId(userId);
			questionsComment.setAddTime(new Date());
			questionsComment.setIsBest(0);
			questionsComment.setReplyCount(0);
			questionsComment.setPraiseCount(0);
			questionsComment.setCommentId(0);
			questionsCommentDaoService.addQuestionsComment(questionsComment);

			// 修改问答的评论数
			Questions questions = questionsDaoService.getQuestionsById(questionsComment.getQuestionId());
			questions.setReplyCount(questions.getReplyCount() + 1);
			questionsDaoService.updateQuestions(questions);
			json = this.setJson(true, "", "");
		} catch (Exception e) {
			json = this.setJson(false, "系统错误,请稍后重试", "");
			logger.error("addQuestionsComment()---error", e);
		}
		return json;
	}

	// 添加子评论(根据传来的参数commentId content)(修改上一级被评论数)
	@RequestMapping("/questionscomment/ajax/addReply")
	@ResponseBody
	public Map<String, Object> addReply(HttpServletRequest request, @ModelAttribute("questionsComment") QuestionsComment questionsComment) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			int userId = SingletonLoginUtils.getLoginUserId(request);
			if (userId == 0) {
				json = this.setJson(false, "请先登录", "");
				return json;
			}
			questionsComment.setCusId(userId);
			questionsComment.setAddTime(new Date());
			questionsComment.setIsBest(0);
			questionsComment.setReplyCount(0);
			questionsComment.setPraiseCount(0);
			questionsComment.setQuestionId(0);
			questionsCommentDaoService.addQuestionsComment(questionsComment);

			// 修改问答回复的评论数
			questionsComment = questionsCommentDaoService.getQuestionsCommentById(questionsComment.getCommentId());
			questionsComment.setReplyCount(questionsComment.getReplyCount() + 1);
			questionsCommentDaoService.updateQuestionsComment(questionsComment);
			json = this.setJson(true, "", "");
		} catch (Exception e) {
			json = this.setJson(false, "系统错误,请稍后重试", "");
			logger.error("addQuestionsCommentReply()---error", e);
		}
		return json;
	}

	// 查询子评论(根据问答回复id)
	@RequestMapping("/questionscomment/ajax/getCommentById/{commentId}")
	public ModelAndView getCommentById(HttpServletRequest request, @PathVariable("commentId") Integer commentId, Questions questions) {
		ModelAndView model = new ModelAndView("modules/web/questionscomment/questionscomment-ajax-listreply");
		try {
			QuestionsComment questionsComment = new QuestionsComment();
			questionsComment.setCommentId(commentId);
			questionsComment.setLimitSize(9);
			questionsComment.setOrderFlag("new");
			List<QuestionsComment> questionsCommentRepList = questionsCommentDaoService.getQuestionsCommentList(questionsComment);
			model.addObject("questionsCommentRepList", questionsCommentRepList);
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return model;

	}

	// 点击'查看更多评论回复'
	@RequestMapping("/questionscommentall/ajax/getCommentById/{pCommentId}")
	@ResponseBody
	public ModelAndView getCommentById(@PathVariable("pCommentId") Integer pCommentId, HttpServletRequest request,
			@ModelAttribute("questionsComment") QuestionsComment questionsComment) {
		ModelAndView model = new ModelAndView("modules/web/questionscomment/questionscomment-ajax-listreply_all");
		try {
			questionsComment.setCommentId(pCommentId);
			questionsComment.setOrderFlag("new");

			List<QuestionsComment> questionsCommentRepList = questionsCommentDaoService.getQuestionsCommentList(questionsComment);
			model.addObject("questionsCommentRepList", questionsCommentRepList);

			questionsComment = questionsCommentDaoService.getQuestionsCommentById(pCommentId);
			model.addObject("questionsComment", questionsComment);

		} catch (Exception e) {
			logger.error("addQuestionsCommentReply()---error", e);
		}
		return model;
	}

	// ************************************************************************************************************************
	// end:**********************************进入问答详情页*******************************************************************

	// start:************************************************************************************************************
	// /我要提问：
	@RequestMapping("/questions/toadd")
	public ModelAndView toAddQuestions(@ModelAttribute("questionsTag") QuestionsTag questionsTag) {
		ModelAndView model = new ModelAndView("modules/web/questions/questions-add");
		try {
			// questionsTagList
			List<QuestionsTag> questionsTagList = questionsTagDaoService.getQuestionsTagList(questionsTag);
			model.addObject("questionsTagList", questionsTagList);
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return model;
	}

	// 提问
	@RequestMapping("/questions/ajax/add")
	@ResponseBody
	public Map<String, Object> addQuestions(HttpServletRequest request, @ModelAttribute("questions") Questions questions, @RequestParam("questionsTag") String tags) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			String registerCode = request.getParameter("randomCode") == null ? "" : request.getParameter("randomCode");
			Object randCode = request.getSession().getAttribute(CommonConstants.RAND_CODE);
			if (randCode == null || !registerCode.equals(randCode.toString())) {
				return this.setJson(false, "请输入正确的验证码", null);
			}
			// 保存问答
			questions.setCusId(SingletonLoginUtils.getLoginUserId(request));
			questions.setAddTime(new Date());
			questionsDaoService.addQuestions(questions);

			// 保存问答标签关系(问答id 标签id)
			if (StringUtils.isNotEmpty(tags)) {
				String[] tagArray = tags.substring(1).split(",");
				for (int i = 0; i < tagArray.length; i++) {
					QuestionsTagRelation questionsTagRelation = new QuestionsTagRelation();
					questionsTagRelation.setQuestionsId(questions.getId_());
					questionsTagRelation.setQuestionsTagId(Integer.valueOf(tagArray[i]));
					questionsTagRelationDaoService.addQuestionsTagRelation(questionsTagRelation);
				}
			}
			json = this.setJson(true, "", questions.getId_());
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return json;
	}

	// end:************************************************************************************************************
	/**
	 * 首页 等待回答：submitForm('status0','order') 最新：submitForm('addTime','order')
	 * 热门：submitForm('replycount','order')"全部回答："submitForm(0,'type')
	 * "课程回答"：submitForm(1,'type') "学习分享"：submitForm(2,'type')
	 * 按照标签筛选：submitForm('5','questionsTagId')
	 */
	@RequestMapping("/questions/list")
	public ModelAndView questionsList(@ModelAttribute("questions") Questions questions, Integer pageNum, Integer pageSize, HttpServletRequest request) {
		ModelAndView model = new ModelAndView("modules/web/questions/questions-list");

		try {
			// questionsList
			String num = request.getParameter("page.currentPage");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 8 : pageSize);
			List<Questions> questionsList = questionsDaoService.getQuestionsList(questions);
			// questionsTagRelationList
			QuestionsTagRelation questionsTagRelation = new QuestionsTagRelation();
			QuestionsComment questionsComment = new QuestionsComment();
			for (Questions questions_ : questionsList) {
				// 该问答下的标签
				questionsTagRelation.setQuestionsId(questions_.getId_());// 根据questionId查询出该问答下的标签
				List<QuestionsTagRelation> questionsTagRelationsList = questionsTagRelationDaoService.queryQuestionsTagRelation(questionsTagRelation);
				questions_.setQuestionsTagRelationList(questionsTagRelationsList);// 把查询出的tag列表放入questions中

				// 该问答下的回答预览
				if (questions_.getReplyCount() >= 1) {
					questionsComment.setLimitSize(1);// 限制一条显示
					if (questions_.getStatus() == 0) {// 可回复
						questionsComment.setIsBest(0);// 不是最佳答案
						questionsComment.setOrderFlag("new");
					}
					if (questions_.getStatus() == 1) {// 不可回复
						questionsComment.setIsBest(1);// 最佳答案
					}
				}
				questionsComment.setQuestionId(questions_.getId_());
				List<QuestionsComment> questionsCommentList = questionsCommentDaoService.queryQuestionsCommentList(questionsComment);
				questions_.setQuestionsCommentList(questionsCommentList);

			}

			PageInfo<Questions> pageInfo = new PageInfo<Questions>(questionsList);
			model.addObject("page", pageInfo);
			model.addObject("questionsList", questionsList);

			// questionsTagList
			QuestionsTag query = new QuestionsTag();
			List<QuestionsTag> questionsTagList = questionsTagDaoService.getQuestionsTagList(query);

			model.addObject("questionsTagList", questionsTagList);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return model;

	}

}
