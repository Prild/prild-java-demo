package com.thinkgem.jeesite.modules.inxedu.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.article.ArticleContent;
import com.thinkgem.jeesite.modules.inxedu.entity.article.Article_;
import com.thinkgem.jeesite.modules.inxedu.entity.article.QueryArticle_;
import com.thinkgem.jeesite.modules.inxedu.entity.teacher.Teacher;
import com.thinkgem.jeesite.modules.inxedu.service.ArticleDaoService_;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

@Controller
@RequestMapping("${adminPath}/article")
public class AdminArticleController extends BaseController {

	@Autowired
	private ArticleDaoService_ articleDaoService_;

	@InitBinder({ "articleContent" })
	public void initArticleContentBinder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("articleContent.");
	}

	@InitBinder({ "article" })
	public void initArticleBinder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("article.");
	}

	@InitBinder({ "queryArticle" })
	public void initQueryArticleBinder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("queryArticle.");
	}

	
	/**
	 * 进入文章添加页面
	 */
	@RequestMapping("/initcreate")
	public ModelAndView initAddArticle(HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		model.setViewName("modules/article/add-article");
		return model;
	}
	//执行 添加
	@RequestMapping("/addarticle")
	public ModelAndView addArticle(HttpServletRequest request, @ModelAttribute("article") Article_ article, @ModelAttribute("articleContent") ArticleContent articleContent) {
		ModelAndView model = new ModelAndView();
		model.setViewName("redirect:" + adminPath + "/article/showlist");
		try {
			// 添加时间
			article.setCreateTime(new Date());
			// 添加文章
			articleDaoService_.createArticle(article);
			// 添加文章内容
			articleContent.setArticleId(article.getArticleId());
			articleDaoService_.addArticleContent(articleContent);
		} catch (Exception e) {
			logger.error("AdminArticleController.addArticle()---error", e);
		}
		return model;
	}
	
	// start:****************************************************************************
	/**
	 * 进入文章修改页面initupdate
	 */
	@RequestMapping("/initupdate/{articleId}")
	public ModelAndView initUpdateArticle(HttpServletRequest request, @PathVariable("articleId") int articleId) {
		ModelAndView model = new ModelAndView();
		model.setViewName("modules/article/update-article");
		try {
			// 查询文章信息
			Article_ article = articleDaoService_.queryArticleById(articleId);
			model.addObject("article", article);
			// 查询文章内容
			String content = articleDaoService_.queryArticleContentByArticleId(articleId);
			model.addObject("content", content);
		} catch (Exception e) {
			logger.error("AdminArticleController.initUpdateArticle()--erro", e);
		}
		return model;
	}
	//执行修改
	@RequestMapping("/updatearticle")
	public ModelAndView updateArticle(HttpServletRequest request, @ModelAttribute("article") Article_ article, @ModelAttribute("articleContent") ArticleContent articleContent) {
		ModelAndView model = new ModelAndView();
		model.setViewName("redirect:" + adminPath + "/article/showlist");
		try {
			articleDaoService_.updateArticle(article);
			articleContent.setArticleId(article.getArticleId());
			articleDaoService_.updateArticleContent(articleContent);
			// 修改成功返回原列表页面
		} catch (Exception e) {
			logger.error("AdminArticleController.updateArticle()--error", e);
		}
		return model;
	}
	
	// end:****************************************************************************

	/**
	 * 删除文章(数组形式删除)
	 */
	@RequestMapping("/delete")
	public ModelAndView delete(HttpServletRequest request, @RequestParam("articleId") String[] aridArr) {
		ModelAndView model = new ModelAndView();
		model.setViewName("redirect:" + adminPath + "/article/showlist");
		try {
			System.out.println("////////////////////////////////" + aridArr);
			// String[] aridArr = request.getParameterValues("articleId");
			if (aridArr != null && aridArr.length > 0) {
				articleDaoService_.deleteArticleByIds(aridArr);
				articleDaoService_.deleteArticleContentByArticleIds(aridArr);
			}
		} catch (Exception e) {
			logger.error("AdminArticleController.delete()--error", e);
		}
		return model;
	}

	/**
	 * 分页查询文章列表
	 */
	@RequestMapping("/showlist")
	public ModelAndView showArticleList(HttpServletRequest request, @ModelAttribute("queryArticle") QueryArticle_ queryArticle,Integer pageNum,Integer pageSize) {
		ModelAndView model = new ModelAndView();
		try {
			model.setViewName("modules/article/article-list");
			String num = request.getParameter("page.currentPage");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
			List<Article_> articleList = articleDaoService_.queryArticlePage(queryArticle);
			PageInfo<Article_> pageInfo = new PageInfo<Article_>(articleList);
			model.addObject("page", pageInfo);
			
			model.addObject("articleList", articleList);
		} catch (Exception e) {
			logger.error("AdminArticleController.showArticleList()--error", e);
		}
		return model;
	}
}
