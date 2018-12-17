package com.thinkgem.jeesite.modules.inxedu.dao;

import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.QuestionsTag;

/**
 * 问答标签dao层接口
 * @author www.inxedu.com
 */
@MyBatisDao
public interface QuestionsTagDao {
	/**
	 * 创建问答标签
	 * @param questionsTag
	 * @return 返回问答标签ID
	 */
	public int createQuestionsTag(QuestionsTag questionsTag);
	
	/**
	 * 查询问答标签列表
	 * @return List<QuestionsTag>
	 */
	public List<QuestionsTag> getQuestionsTagList(QuestionsTag query);
	
	/**
	 * 修改问答标签父ID
	 * @param map
	 */
	public boolean updateQuestionsTagParentId(Map<String,Object> map);
	
	/**
	 * 修改问答标签
	 * @param questionsTag
	 */
	public boolean updateQuestionsTag(QuestionsTag questionsTag);
	
	/**
	 * 删除问答标签 
	 * @param questionsTagId 要删除的问答标签ID
	 */
	public boolean deleteQuestionsTag(int questionsTagId);
}