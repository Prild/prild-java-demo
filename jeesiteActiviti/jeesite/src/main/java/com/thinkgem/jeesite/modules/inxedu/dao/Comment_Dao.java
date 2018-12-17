package com.thinkgem.jeesite.modules.inxedu.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.comment.Comment_;
/**
 * 评论dao层接口
 * @author www.inxedu.com
 */
@MyBatisDao
public interface Comment_Dao {
	/**
	 * 分页查询评论
	 */
	public List<Comment_> getCommentByPage(Comment_ comment);
	/**
	 * 添加评论
	 * @param comment 评论实体
	 */
	public boolean addComment(Comment_ comment);
	/**
	 * 更新评论
	 */
	public boolean updateComment(Comment_ comment);
	/**
	 * 查询评论互动
	 */
	public List<Comment_> queryCommentInteraction(@Param("e")Comment_ comment);
	/**
	 * 更新评论点赞数,回复数等
	 */
	public boolean updateCommentNum(Map<String,String> map);
	/**
	 * 查询评论
	 */
	public Comment_ queryComment(Comment_ comment);
	/**
	 * 删除评论
	 */
	public boolean delComment(int commentId);
	/**
	 * 查询评论 list
	 */
	public List<Comment_> queryCommentList(Comment_ comment);
}
