package com.thinkgem.jeesite.modules.inxedu.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.article.Article_;
import com.thinkgem.jeesite.modules.inxedu.entity.article.QueryArticle_;
import com.thinkgem.jeesite.modules.inxedu.service.ArticleDaoService_;
import com.thinkgem.jeesite.modules.inxedu.service.Comment_DaoService;

@Controller
@RequestMapping("${frontPath}")
public class FrontArticlelistController extends BaseController {
	@Autowired
	private ArticleDaoService_ articleDaoService_;
	@Autowired
	private Comment_DaoService comment_DaoService;

	// start:***********************点击"文章"*******************************************
	// 文章
	@RequestMapping("/front/articlelist")
	public ModelAndView articlelist(HttpServletRequest request, Integer pageNum, Integer pageSize) {
		ModelAndView model = new ModelAndView();

		try {

			// articleList
			QueryArticle_ queryArticle = new QueryArticle_();
			queryArticle.setType(2);// 已发布
			String num = request.getParameter("page.currentPage");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
			List<Article_> articleList = articleDaoService_.queryArticleList(queryArticle);
			PageInfo<Article_> pageInfo = new PageInfo<Article_>(articleList);
			model.addObject("articleList", articleList);
			model.addObject("page", pageInfo);

			model.setViewName("modules/web/article/article-list");
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return model;

	}

	// 文章详情front/articleinfo/${article.articleId}
	@RequestMapping("/front/articleinfo/{articleId}")
	public ModelAndView goInArticle(HttpServletRequest request, @PathVariable("articleId") Integer articleId) {
		ModelAndView model = new ModelAndView();
		try {
			// article
			Article_ article_ = articleDaoService_.queryArticleById(articleId);
			model.addObject("article", article_);
			// content
			String content = articleDaoService_.queryArticleContentByArticleId(articleId);
			model.addObject("content", content);
			
			model.setViewName("modules/web/article/article-info");
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return model;
	}

	//更新文章点击量updateArticleClickNum
	@RequestMapping("/front/updateArticleClickNum/{articleId}")
	@ResponseBody
	public Map<String, Object> updateArticleClickNum(@PathVariable("articleId") Integer articleId){
	Map<String, Object> json = new HashMap<String, Object>();
	try {
		Map<String, String> article = new HashMap<>();
		article.put("num", "+1");
		article.put("type", "clickNum");
		article.put("articleId", articleId.toString());
		
		articleDaoService_.updateArticleNum(article);
		
		Article_ article_ = articleDaoService_.queryArticleById(articleId);//显示
		json = this.setJson(true, "",article_ );
	} catch (Exception e) {
		e.printStackTrace();// TODO: handle exception
		json=  this.setAjaxException(json);
	}
	
	return json;
	}
	
	
	// 文章排行榜/front/ajax/recommend
	@RequestMapping("/front/ajax/recommend")
	public ModelAndView articlelistRecommend(HttpServletRequest request) {
		ModelAndView model = new ModelAndView();

		try {

			// articleList
			QueryArticle_ queryArticle = new QueryArticle_();
			queryArticle.setType(2);// 已发布
			queryArticle.setCount(6);
			queryArticle.setOrderby(1);// 按点击数排序
			List<Article_> articleList = articleDaoService_.queryArticleList(queryArticle);
			model.addObject("articleList", articleList);

			model.setViewName("modules/web/article/article-recommend");
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return model;

	}

	// end:***********************点击"文章"*******************************************
}
