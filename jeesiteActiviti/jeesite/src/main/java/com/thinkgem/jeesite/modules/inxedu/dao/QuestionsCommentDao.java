package com.thinkgem.jeesite.modules.inxedu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.questions.QuestionsComment;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

/**
 * QuestionsComment管理接口
 * @author www.inxedu.com
 */
@MyBatisDao
public interface QuestionsCommentDao {

    /**
     * 添加QuestionsComment
     * @param questionsComment 要添加的QuestionsComment
     * @return id
     */
    public int addQuestionsComment(QuestionsComment questionsComment);

    /**
     * 根据id删除一个QuestionsComment
     *
     * @param id 要删除的id
     */
    public int deleteQuestionsCommentById(int id);
    
    /**
     * 根据id查询QuestionsComment
     *
     * @param id 要查询的id
     */
    public QuestionsComment getQuestionsCommentById(int id);

    /**
     * 修改QuestionsComment
     *
     * @param questionsComment 要修改的QuestionsComment
     */
    public boolean updateQuestionsComment(QuestionsComment questionsComment);

    /**
     * 根据条件获取QuestionsComment列表
     *
     * @param questionsComment 查询条件
     * @return List<QuestionsComment>
     */
    public List<QuestionsComment> getQuestionsCommentList(@Param("e")QuestionsComment questionsComment);

    /**
     * 通过问答id 查询该问答下的回复
     * @return List<QuestionsComment>
     */
    public List<QuestionsComment> queryQuestionsCommentListByQuestionsId(@Param("param1")QuestionsComment questionsComment);
    
    /**
     * 根据问答id删除QuestionsComment
     *
     * @param id 要删除的id
     */
    public int delQuestionsCommentByQuestionId(int id);
    
    /**
     * 查询所有的问答
     */
    public List<QuestionsComment> queryQuestionsCommentList(@Param("param1")QuestionsComment questionsComment);
    
    /**
     * 根据问答回复id删除QuestionsComment
     */
    public int delQuestionsCommentByCommentId(int commentId);
}