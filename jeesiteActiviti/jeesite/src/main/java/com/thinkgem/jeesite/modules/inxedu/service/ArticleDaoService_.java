package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.ArticleDao_;
import com.thinkgem.jeesite.modules.inxedu.entity.article.ArticleContent;
import com.thinkgem.jeesite.modules.inxedu.entity.article.Article_;
import com.thinkgem.jeesite.modules.inxedu.entity.article.QueryArticle_;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;
@Service
@Transactional
public class ArticleDaoService_ implements ArticleDao_{
@Autowired
private ArticleDao_ articleDao;
	@Override
	public int createArticle(Article_ article) {
		// TODO Auto-generated method stub
		return articleDao.createArticle(article);
	}

	@Override
	public boolean addArticleContent(ArticleContent content) {
		return articleDao.addArticleContent(content);
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateArticle(Article_ article) {
		return articleDao.updateArticle(article);
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateArticleContent(ArticleContent content) {
		return articleDao.updateArticleContent(content);
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean deleteArticleByIds(String[] articleIds) {
		return articleDao.deleteArticleByIds(articleIds);
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean deleteArticleContentByArticleIds(String[] articleIds) {
		return articleDao.deleteArticleContentByArticleIds(articleIds);
		// TODO Auto-generated method stub
		
	}

	@Override
	public Article_ queryArticleById(int articleId) {
		// TODO Auto-generated method stub
		return articleDao.queryArticleById(articleId);
	}

	@Override
	public String queryArticleContentByArticleId(int articleId) {
		// TODO Auto-generated method stub
		return articleDao.queryArticleContentByArticleId(articleId);
	}

	@Override
	public List<Article_> queryArticlePage(@Param("param1")QueryArticle_ query) {
		// TODO Auto-generated method stub
		return articleDao.queryArticlePage(query);
	}

	@Override
	public boolean updateArticleNum(Map<String, String> map) {
		return articleDao.updateArticleNum(map);
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Article_> queryArticleList(QueryArticle_ queryArticle) {
		// TODO Auto-generated method stub
		return articleDao.queryArticleList(queryArticle);
	}

	@Override
	public int queryAllArticleCount() {
		// TODO Auto-generated method stub
		return articleDao.queryAllArticleCount();
	}

}
