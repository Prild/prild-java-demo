package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.Comment_Dao;
import com.thinkgem.jeesite.modules.inxedu.entity.comment.Comment_;
@Service
@Transactional
public class Comment_DaoService implements Comment_Dao{
@Autowired
private Comment_Dao comment_Dao;
	@Override
	public List<Comment_> getCommentByPage(Comment_ comment) {
		// TODO Auto-generated method stub
		return comment_Dao.getCommentByPage(comment);
	}

	@Override
	public boolean addComment(Comment_ comment) {
		// TODO Auto-generated method stub
		return comment_Dao.addComment(comment);
	}

	/**
	 * 更新回复数/添加回复
	 */
	public boolean addComment_(Comment_ comment) {
		// TODO Auto-generated method stub
		int type  = comment.getType();//文章
		if (type==1) {
			Map<String, String> commMap = new HashMap<>();
			commMap.put("type", "replyCount");
			commMap.put("num", "+1");
			commMap.put("commentId", comment.getpCommentId().toString());
			updateCommentNum(commMap);
		}
		
		return comment_Dao.addComment(comment);
	}
	
	
	@Override
	public boolean updateComment(Comment_ comment) {
		// TODO Auto-generated method stub
		return comment_Dao.updateComment(comment);
	}

	@Override
	public List<Comment_> queryCommentInteraction(Comment_ comment) {
		// TODO Auto-generated method stub
		return comment_Dao.queryCommentInteraction(comment);
	}

	@Override
	public boolean updateCommentNum(Map<String, String> map) {
		// TODO Auto-generated method stub
		return comment_Dao.updateCommentNum(map);
	}

	@Override
	public Comment_ queryComment(Comment_ comment) {
		// TODO Auto-generated method stub
		return comment_Dao.queryComment(comment);
	}

	@Override
	public boolean delComment(int commentId) {
		// TODO Auto-generated method stub
		return comment_Dao.delComment(commentId);
	}

	@Override
	public List<Comment_> queryCommentList(Comment_ comment) {
		// TODO Auto-generated method stub
		return comment_Dao.queryCommentList(comment);
	}

}
