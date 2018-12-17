package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.QuestionsTagRelationDao;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.QuestionsTagRelation;

@Service
@Transactional
public class QuestionsTagRelationDaoService implements QuestionsTagRelationDao {
	@Autowired
	private QuestionsTagRelationDao questionsTagRelationDao;

	@Override
	public int addQuestionsTagRelation(QuestionsTagRelation questionsTagRelation) {
		// TODO Auto-generated method stub
		return questionsTagRelationDao.addQuestionsTagRelation(questionsTagRelation);
	}

	@Override
	public List<QuestionsTagRelation> queryQuestionsTagRelation(QuestionsTagRelation questionsTagRelation) {
		// TODO Auto-generated method stub
		return questionsTagRelationDao.queryQuestionsTagRelation(questionsTagRelation);
	}

	@Override
	public int deleteQuestionsTagRelation(int id) {
		// TODO Auto-generated method stub
		return questionsTagRelationDao.deleteQuestionsTagRelation(id);
	}

}
