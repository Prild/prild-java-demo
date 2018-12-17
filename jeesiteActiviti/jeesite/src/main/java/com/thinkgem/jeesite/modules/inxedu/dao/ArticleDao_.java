package com.thinkgem.jeesite.modules.inxedu.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.article.ArticleContent;
import com.thinkgem.jeesite.modules.inxedu.entity.article.Article_;
import com.thinkgem.jeesite.modules.inxedu.entity.article.QueryArticle_;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

@MyBatisDao
public interface ArticleDao_ {
	/**
	 * 创建文章
	 * @param article 文章实体
	 * @return 返回文章ID
	 */
	public int createArticle(Article_ article);
	
	/**
	 * 添加文章内容
	 * @param content 文章内容实体
	 */
	public boolean addArticleContent(ArticleContent content);
	
	/**
	 * 修改文章信息
	 * @param article 文章实体
	 */
	public boolean updateArticle(Article_ article);
	
	/**
	 * 修改文章内容 
	 * @param content
	 */
	public boolean updateArticleContent(ArticleContent content);
	
	/**
	 * 删除文章
	 * @param articleIds 文章ID串 如：(1,2,3,4)
	 */
	public boolean deleteArticleByIds(String[] articleIds);
	
	/**
	 * 删除文章内容
	 * @param articleIds 文章ID串 如：(1,2,3,4)
	 */
	public boolean deleteArticleContentByArticleIds(String[] articleIds);
	
	/**
	 * 通过文章ID查询文章信息
	 * @param articleId 文章ID
	 * @return Article文章实体信息
	 */
	public Article_ queryArticleById(int articleId);
	
	/**
	 * 通过文章ID查询文章内容
	 * @param articleId 文章内容
	 * @return String类型文章内容
	 */
	public String queryArticleContentByArticleId(int articleId);
	
	/**
	 * 分页查询文章列表
	 * @param query 查询条件
	 * @param page 分页条件
	 * @return List<Article>文章列表
	 */
	public List<Article_> queryArticlePage(@Param("param1")QueryArticle_ query);
	
	/**
	 * 修改累加文章点击量
	 */
	public boolean updateArticleNum(Map<String,String> map);
	
	/**
	 * 公共多条件查询文章资讯列表,用于前台
	 */
	public List<Article_> queryArticleList(@Param("e")QueryArticle_ queryArticle);
	
	/**
	 * 获取所有文章总记录数
	 * @return 总记录数
	 */
	public int queryAllArticleCount();

}
