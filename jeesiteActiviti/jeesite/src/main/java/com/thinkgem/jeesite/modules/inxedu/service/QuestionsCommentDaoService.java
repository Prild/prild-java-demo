package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.QuestionsCommentDao;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.QuestionsComment;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

@Service
@Transactional
public class QuestionsCommentDaoService implements QuestionsCommentDao {
	@Autowired
	private QuestionsCommentDao questionsCommentDao;

	@Override
	public int addQuestionsComment(QuestionsComment questionsComment) {
		// TODO Auto-generated method stub
		return questionsCommentDao.addQuestionsComment(questionsComment);
	}

	@Override
	public int deleteQuestionsCommentById(int id) {
		// TODO Auto-generated method stub
		return questionsCommentDao.deleteQuestionsCommentById(id);
	}

	@Override
	public QuestionsComment getQuestionsCommentById(int id) {
		// TODO Auto-generated method stub
		return questionsCommentDao.getQuestionsCommentById(id);
	}

	@Override
	public boolean updateQuestionsComment(QuestionsComment questionsComment) {
		// TODO Auto-generated method stub
		return questionsCommentDao.updateQuestionsComment(questionsComment);
	}

	@Override
	public List<QuestionsComment> getQuestionsCommentList(QuestionsComment questionsComment) {
		// TODO Auto-generated method stub
		return questionsCommentDao.getQuestionsCommentList(questionsComment);
	}

	@Override
	public List<QuestionsComment> queryQuestionsCommentListByQuestionsId(QuestionsComment questionsComment) {
		// TODO Auto-generated method stub
		return questionsCommentDao.queryQuestionsCommentListByQuestionsId(questionsComment);
	}

	@Override
	public int delQuestionsCommentByQuestionId(int id) {
		// TODO Auto-generated method stub
		return questionsCommentDao.deleteQuestionsCommentById(id);
	}

	@Override
	public List<QuestionsComment> queryQuestionsCommentList(@Param("param1")QuestionsComment questionsComment) {
		// TODO Auto-generated method stub
		return questionsCommentDao.queryQuestionsCommentList(questionsComment);
	}

	@Override
	public int delQuestionsCommentByCommentId(int commentId) {
		// TODO Auto-generated method stub
		return questionsCommentDao.delQuestionsCommentByCommentId(commentId);
	}

}
