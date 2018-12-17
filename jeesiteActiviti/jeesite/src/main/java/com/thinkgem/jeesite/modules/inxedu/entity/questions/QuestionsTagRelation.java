package com.thinkgem.jeesite.modules.inxedu.entity.questions;

import java.io.Serializable;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 问答和 问答标签的 关联表
 *@author www.inxedu.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionsTagRelation extends DataEntity<QuestionsTagRelation> implements Serializable{
	private static final long serialVersionUID = 7687324559966427231L;
	private int id_;//id
	private int questionsId;//问答id
	private int questionsTagId;//问答标签id
	
	private String tagName;//问答标签名

	public int getId_() {
		return id_;
	}

	public void setId_(int id_) {
		this.id_ = id_;
	}

	public int getQuestionsId() {
		return questionsId;
	}

	public void setQuestionsId(int questionsId) {
		this.questionsId = questionsId;
	}

	public int getQuestionsTagId() {
		return questionsTagId;
	}

	public void setQuestionsTagId(int questionsTagId) {
		this.questionsTagId = questionsTagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
