package com.thinkgem.jeesite.modules.inxedu.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.QuestionsTagRelation;


/**
 * 问答和 问答标签的 关联表dao层接口
 * @author www.inxedu.com
 */
@MyBatisDao
public interface QuestionsTagRelationDao {
	/**
	 * 添加
	 * @param questionsTagRelation
	 * @return
	 */
	public int addQuestionsTagRelation(QuestionsTagRelation questionsTagRelation);
	
	/**
	 * 查询
	 */
	public List<QuestionsTagRelation> queryQuestionsTagRelation(QuestionsTagRelation questionsTagRelation);
	
	/**
	 * 删除
	 */
	public int deleteQuestionsTagRelation(int id);
}
