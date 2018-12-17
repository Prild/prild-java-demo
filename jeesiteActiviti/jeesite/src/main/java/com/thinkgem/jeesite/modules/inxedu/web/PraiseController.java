package com.thinkgem.jeesite.modules.inxedu.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.praise.*;
import com.thinkgem.jeesite.modules.inxedu.service.PraiseDaoService;
import com.thinkgem.jeesite.modules.inxedu.utils.SingletonLoginUtils;

@Controller
@RequestMapping("${frontPath}")
public class PraiseController extends BaseController {
	@InitBinder("praise")
	public void praise(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("praise.");
	}

	@Autowired
	private PraiseDaoService praiseDaoService;

	@RequestMapping("/praise/ajax/add")
	@ResponseBody
	public Map<String, Object> Praise(@ModelAttribute("praise") Praise praise, HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();

		try {
			// 已点过赞
			praise.setUserId(Long.valueOf(SingletonLoginUtils.getLoginUserId(request)));
			int counts = praiseDaoService.queryPraiseCount(praise);
			if (counts > 0) {
				return this.setJson(false, "已点过赞", null);
			}
			praise.setAddTime(new Date());
			praiseDaoService.addPraise_(praise);
			json = this.setJson(true, "点赞成功", null);
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return json;
	}
}
