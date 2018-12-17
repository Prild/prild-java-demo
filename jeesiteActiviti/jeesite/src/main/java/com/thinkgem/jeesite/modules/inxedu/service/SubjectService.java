package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.inxedu.dao.SubjectDao;
import com.thinkgem.jeesite.modules.inxedu.entity.subject.QuerySubject;
import com.thinkgem.jeesite.modules.inxedu.entity.subject.Subject;
import com.thinkgem.jeesite.modules.oa.entity.OaNotify;

/**
 * 专业service实现
 * @author www.inxedu.com
 */
@Service
@Transactional
public class SubjectService implements SubjectDao {

	@Autowired
	private SubjectDao subjectDao;

/**
 * 查询专业列表 
 * @param query
 * @return
 */
	@Override
	public List<Subject> getSubjectList(@Param("e")QuerySubject query) {
		return subjectDao.getSubjectList(query);
	}

	@Override
	public void updateSubjectParentId(@Param("subjectId")int subjectId, @Param("parentId")int parentId) {
		subjectDao.updateSubjectParentId(subjectId,parentId);
	}

	@Override
	public void updateSubject(@Param("e")Subject subject) {
		subjectDao.updateSubject(subject);
	}
	/**
	 * 修改排序
	 */
	@Override
	public void updateSubjectSort(@Param("e")Subject subject){
		subjectDao.updateSubjectSort(subject);
	}

	@Override
	public void deleteSubject(@Param("value")Integer subjectId) {
		subjectDao.deleteSubject(subjectId);
	}

	@Override
	public Subject getSubjectBySubject(@Param("e")Subject subject) {
		return subjectDao.getSubjectBySubject(subject);
	}

	@Override
	public List<Subject> getSubjectListByOne(@Param("value")Integer subjectId) {
		return subjectDao.getSubjectListByOne(subjectId);
	}

	@Override
	public int createSubject(Subject subject) {
		// TODO Auto-generated method stub
		return subjectDao.createSubject(subject);
	}

}

