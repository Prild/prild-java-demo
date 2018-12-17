package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.QuestionsTagDao;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.QuestionsTag;
@Service
@Transactional
public class QuestionsTagDaoService implements QuestionsTagDao{
@Autowired
private QuestionsTagDao questionsTagDao;
	@Override
	public int createQuestionsTag(QuestionsTag questionsTag) {
		// TODO Auto-generated method stub
		return questionsTagDao.createQuestionsTag(questionsTag);
	}

	@Override
	public List<QuestionsTag> getQuestionsTagList(QuestionsTag query) {
		// TODO Auto-generated method stub
		return questionsTagDao.getQuestionsTagList(query);
	}

	@Override
	public boolean updateQuestionsTagParentId(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return questionsTagDao.updateQuestionsTagParentId(map);
	}

	@Override
	public boolean updateQuestionsTag(QuestionsTag questionsTag) {
		// TODO Auto-generated method stub
		return questionsTagDao.updateQuestionsTag(questionsTag);
	}

	@Override
	public boolean deleteQuestionsTag(int questionsTagId) {
		// TODO Auto-generated method stub
		return questionsTagDao.deleteQuestionsTag(questionsTagId);
	}

}
