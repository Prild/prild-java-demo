package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.QuestionsDao;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.Questions;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;
@Service
@Transactional
public class QuestionsDaoService implements QuestionsDao{
@Autowired
private QuestionsDao questionsDao;
	@Override
	public int addQuestions(Questions questions) {
		// TODO Auto-generated method stub
		return questionsDao.addQuestions(questions);
	}

	@Override
	public int deleteQuestionsById(int id) {
		// TODO Auto-generated method stub
		return questionsDao.deleteQuestionsById(id);
	}

	@Override
	public boolean updateQuestions(Questions questions) {
		// TODO Auto-generated method stub
		return questionsDao.updateQuestions(questions);
	}

	@Override
	public Questions getQuestionsById(int id) {
		// TODO Auto-generated method stub
		return questionsDao.getQuestionsById(id);
	}

	@Override
	public List<Questions> getQuestionsList(@Param("param1")Questions questions) {
		// TODO Auto-generated method stub
		return questionsDao.getQuestionsList(questions);
	}

	@Override
	public List<Questions> queryQuestionsOrder(int size) {
		// TODO Auto-generated method stub
		return questionsDao.queryQuestionsOrder(size);
	}

	@Override
	public int queryAllQuestionsCount() {
		// TODO Auto-generated method stub
		return questionsDao.queryAllQuestionsCount();
	}

}
