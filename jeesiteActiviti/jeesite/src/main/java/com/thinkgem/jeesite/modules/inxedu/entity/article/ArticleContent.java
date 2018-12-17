package com.thinkgem.jeesite.modules.inxedu.entity.article;

import java.io.Serializable;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文章内容
 * @author www.inxedu.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleContent extends DataEntity<ArticleContent> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**文章ID*/
	private int articleId;
	/**文章对应的内容*/
	private String content;
	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
