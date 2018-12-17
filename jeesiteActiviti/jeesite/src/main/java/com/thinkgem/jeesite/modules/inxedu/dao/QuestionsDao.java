package com.thinkgem.jeesite.modules.inxedu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.Questions;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

/**
 * questions管理接口
 * @author www.inxedu.com
 */
@MyBatisDao
public interface QuestionsDao {

    /**
     * 添加Questions
     *
     * @param questions 要添加的Questions
     * @return id
     */
    public int addQuestions(Questions questions);

    /**
     * 根据id删除一个Questions
     *
     * @param id 要删除的id
     */
    public int deleteQuestionsById(int id);

    /**
     * 修改Questions
     *
     * @param questions 要修改的Questions
     */
    public boolean updateQuestions(Questions questions);

    /**
     * 根据id获取单个Questions对象
     *
     * @param id 要查询的id
     * @return Questions
     */
    public Questions getQuestionsById(int id);

    /**
     * 根据条件获取Questions列表
     *
     * @param questions 查询条件
     * @param page       分页参数
     * @return List<Questions>
     */
    public List<Questions> getQuestionsList(@Param("param1")Questions questions);

    /**
     * 最新排行
     *
     * @param size 传入显示的条数
     * @return List<Questions>
     */
    public List<Questions> queryQuestionsOrder(int size);
    
    /**
     * 所有问答数
     * @return
     */
    public int queryAllQuestionsCount();
}