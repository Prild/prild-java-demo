package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.PraiseDao;
import com.thinkgem.jeesite.modules.inxedu.entity.article.Article_;
import com.thinkgem.jeesite.modules.inxedu.entity.praise.Praise;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.Questions;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.QuestionsComment;
import com.thinkgem.jeesite.modules.inxedu.utils.SingletonLoginUtils;

@Service
@Transactional
public class PraiseDaoService implements PraiseDao {
	@Autowired
	private PraiseDao praiseDao;
	@Autowired
	private ArticleDaoService_ articleDaoService_;
	@Autowired
	private Comment_DaoService comment_DaoService;
	@Autowired
	private QuestionsCommentDaoService questionsCommentDaoService;
	@Autowired
	private QuestionsDaoService questionsDaoService;
	
	@Override
	public Long addPraise(Praise praise) {
		return praiseDao.addPraise(praise);
	}

	@Override
	public int queryPraiseCount(Praise praise) {
		return praiseDao.queryPraiseCount(praise);
	}

	/**
	 * 根据类型更新点赞 1问答点赞 2问答评论点赞 3 文章点赞数4 评论点赞
	 */
	public void addPraise_(Praise praise) {
		int type = praise.getType();
		if (3 == type) {
			Map<String, String> article = new HashMap<>();
			article.put("num", "+1");
			article.put("type", "praiseCount");
			article.put("articleId", praise.getTargetId().toString());
			articleDaoService_.updateArticleNum(article);
		}
		if (4 == type) {// updateCommentNum
			Map<String, String> comment = new HashMap<>();
			comment.put("num", "+1");
			comment.put("type", "praiseCount");
			comment.put("commentId", praise.getTargetId().toString());
			comment_DaoService.updateCommentNum(comment);
		}
		if (2 == type) {
			QuestionsComment questionsComment =  questionsCommentDaoService.getQuestionsCommentById(Integer.valueOf(praise.getTargetId()+""));
			questionsComment.setPraiseCount(questionsComment.getPraiseCount()+1);
			questionsCommentDaoService.updateQuestionsComment(questionsComment);
		}
		if (1==type) {
			Questions questions = questionsDaoService.getQuestionsById(Integer.valueOf(praise.getTargetId()+""));
			questions.setPraiseCount(questions.getPraiseCount()+1);
			questionsDaoService.updateQuestions(questions);
		}
		addPraise(praise);
	}
}
