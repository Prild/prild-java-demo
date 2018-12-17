package com.thinkgem.jeesite.modules.inxedu.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.StudentUser;
import com.thinkgem.jeesite.modules.inxedu.entity.comment.Comment_;
import com.thinkgem.jeesite.modules.inxedu.service.Comment_DaoService;
import com.thinkgem.jeesite.modules.inxedu.utils.SingletonLoginUtils;

@Controller
@RequestMapping("${frontPath}/web/comment")
public class FrontCommentController extends BaseController {
	@InitBinder("comment")
	public void getBinder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("comment.");
	}

	@Autowired
	private Comment_DaoService comment_DaoService;

	// 查询文章评论回复
	@RequestMapping("/ajax/commentreply")
	@ResponseBody
	public ModelAndView queryCommentReply(HttpServletRequest request, @ModelAttribute("comment") Comment_ comment) {
		ModelAndView model = new ModelAndView("modules/web/comment/comment_reply");
		try {
			comment.setType(1);//文章
			comment.setUserId(SingletonLoginUtils.getLoginUserId(request));
			List<Comment_> commentList = comment_DaoService.queryCommentList(comment);//根据评论id和回复id查询回复列表
			model.addObject("commentList", commentList);
			
			StudentUser user = SingletonLoginUtils.getLoginUser(request);
			model.addObject("user", user);
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return model;
	}

	// 添加评论
	@RequestMapping("/ajax/addcomment")
	@ResponseBody
	public Map<String, Object> addComment(HttpServletRequest request, @ModelAttribute("comment") Comment_ comment) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			if (SingletonLoginUtils.getLoginUserId(request) <= 0) {
				return this.setJson(false, "先登录", null);
			}
			comment.setAddTime(new Date());
			comment.setUserId(SingletonLoginUtils.getLoginUserId(request));
			comment_DaoService.addComment_(comment);
			json = this.setJson(true, "", comment);
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return json;
	}

	// 评论列表
	@RequestMapping("/ajax/query")
	@ResponseBody
	public ModelAndView queryComment(@RequestParam("otherId") Integer otherId, @RequestParam("type") Integer type, @RequestParam("order") String order, Integer pageNum,
			Integer pageSize, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		try {
			Comment_ comment = new Comment_();
			comment.setOtherId(otherId);
			comment.setType(type);
			comment.setOrder(order);
			comment.setpCommentId(0);// 0是一级评论，不为0表示回复

			String num = request.getParameter("page.currentPage");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 8 : pageSize);
			List<Comment_> commentList = comment_DaoService.queryCommentList(comment);
			PageInfo<Comment_> pageInfo = new PageInfo<Comment_>(commentList);
			request.setAttribute("page", pageInfo);
			request.setAttribute("comment", comment);
			request.setAttribute("commentList", commentList);
			StudentUser user = SingletonLoginUtils.getLoginUser(request);
			request.setAttribute("user", user);
			model.setViewName("modules/web/comment/comment");
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return model;
	}
}
