package com.thinkgem.jeesite.modules.inxedu.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.subject.QuerySubject;
import com.thinkgem.jeesite.modules.inxedu.entity.subject.Subject;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.User;


/**
 * 专业dao层接口
 * @author www.inxedu.com
 */
@MyBatisDao
public interface SubjectDao {
	/**
	 * 创建专业
	 * @param subject
	 * @return 返回专业ID
	 */
	public int createSubject(Subject subject);
	/**
	 * 查询专业列表
	 * @return List<Subject>
	 */
	public List<Subject> getSubjectList(@Param("e")QuerySubject query);
	/**
	 * 修改专业父ID
	 * @param map
	 */
	public void updateSubjectParentId(@Param("subjectId")int subjectId, @Param("parentId")int parentId) ;
	/**
	 * 修改专业
	 * @param subject
	 */
	public void updateSubject(@Param("e")Subject subject) ;
	/**
	 * 修改排序
	 */
	public void updateSubjectSort(@Param("e")Subject subject);
	/**
	 * 删除专业 
	 * @param subjectId 要删除的专业ID
	 */
	public void deleteSubject(@Param("value")Integer subjectId);
	/**
     * 查询项目
     */
	public Subject getSubjectBySubject(@Param("e")Subject subject);
    
    /**
     * 根据父级ID查找子项目集合
     */
	public List<Subject> getSubjectListByOne(@Param("value")Integer subjectId);
}
