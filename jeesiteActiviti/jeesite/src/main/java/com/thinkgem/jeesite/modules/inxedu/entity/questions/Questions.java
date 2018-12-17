package com.thinkgem.jeesite.modules.inxedu.entity.questions;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 问答
 * @author www.inxedu.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Questions extends DataEntity<Questions> implements Serializable {
	private static final long serialVersionUID = 7687324559966427231L;
    private int id_;// 因为BaseEntity 的id是字符串，所以自定义整数id_ 
    private int cusId;// 创建者
    private String title;// 标题
    private String content;//内容
    private int type;// 分类 1课程问答 2 学习分享
    private int status;// 状态 0可回复1不可回复（采纳最佳答案后改为1 ）
    private int replyCount;// 回复数量
    private int browseCount;// 浏览次数
    private int praiseCount;//点赞数
    private Date addTime;// 添加时间
    
    private String orderFalg="addTime";//排序方式  最新addTime  热门replycount
    private String modelTime;//格式化显示时间
    private String showName;//用户名
    private String email;//用户邮箱
    private String picImg;//用户头像
    private List<QuestionsComment> questionsCommentList;//评论表list(1:n)
    private List<QuestionsTagRelation> questionsTagRelationList;//标签问题关联表list(n:n需要中间表)
    private int questionsTagId;//标签表id
    private Date beginCreateTime;//查询 开始添加时间
    private Date endCreateTime;//查询 结束添加时间
    private int commentUserId;//用于查询我的回答
    
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
        //格式化显示时间
        this.modelTime = getModelDate(addTime);
    }
    
    public String getModelDate(Date oldDate) {
		if (oldDate!=null) {
			Date newDate = new Date();
			long second = (newDate.getTime() - oldDate.getTime()) / 1000L;
			if (second <= 60L)
				return second + "秒前";
			if ((60L < second) && (second <= 3600L)) {
				second /= 60L;
				return second + "分钟前";
			}
			if ((3600L < second) && (second <= 86400L)) {
				second = second / 60L / 60L;
				return second + "小时前";
			}
			if ((86400L < second) && (second <= 864000L)) {
				String formatDate = DateUtils.formatDate(oldDate, "HH:mm");
				second = second / 60L / 60L / 24L;

				return second + "天前";
			}
			return DateUtils.formatDate(oldDate, "yyyy-MM-dd HH:mm");
		}

		return "";
	}

	public int getId_() {
		return id_;
	}

	public void setId_(int id_) {
		this.id_ = id_;
	}

	public int getCusId() {
		return cusId;
	}

	public void setCusId(int cusId) {
		this.cusId = cusId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}

	public int getBrowseCount() {
		return browseCount;
	}

	public void setBrowseCount(int browseCount) {
		this.browseCount = browseCount;
	}

	public int getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	public String getOrderFalg() {
		return orderFalg;
	}

	public void setOrderFalg(String orderFalg) {
		this.orderFalg = orderFalg;
	}

	public String getModelTime() {
		return modelTime;
	}

	public void setModelTime(String modelTime) {
		this.modelTime = modelTime;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPicImg() {
		return picImg;
	}

	public void setPicImg(String picImg) {
		this.picImg = picImg;
	}

	public List<QuestionsComment> getQuestionsCommentList() {
		return questionsCommentList;
	}

	public void setQuestionsCommentList(List<QuestionsComment> questionsCommentList) {
		this.questionsCommentList = questionsCommentList;
	}

	public List<QuestionsTagRelation> getQuestionsTagRelationList() {
		return questionsTagRelationList;
	}

	public void setQuestionsTagRelationList(List<QuestionsTagRelation> questionsTagRelationList) {
		this.questionsTagRelationList = questionsTagRelationList;
	}

	public int getQuestionsTagId() {
		return questionsTagId;
	}

	public void setQuestionsTagId(int questionsTagId) {
		this.questionsTagId = questionsTagId;
	}

	public Date getBeginCreateTime() {
		return beginCreateTime;
	}

	public void setBeginCreateTime(Date beginCreateTime) {
		this.beginCreateTime = beginCreateTime;
	}

	public Date getEndCreateTime() {
		return endCreateTime;
	}

	public void setEndCreateTime(Date endCreateTime) {
		this.endCreateTime = endCreateTime;
	}

	public int getCommentUserId() {
		return commentUserId;
	}

	public void setCommentUserId(int commentUserId) {
		this.commentUserId = commentUserId;
	}

	public Date getAddTime() {
		return addTime;
	}

}
