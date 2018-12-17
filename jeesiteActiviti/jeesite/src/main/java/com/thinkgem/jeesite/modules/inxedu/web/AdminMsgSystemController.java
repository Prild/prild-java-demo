package com.thinkgem.jeesite.modules.inxedu.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.letter.MsgSystem;
import com.thinkgem.jeesite.modules.inxedu.service.MsgSystemDaoService;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

@Controller
@RequestMapping("${adminPath}/letter")
public class AdminMsgSystemController extends BaseController {
	@Autowired
	private MsgSystemDaoService msgSystemDaoService;

	@InitBinder("msgSystem")
	public void initBinderMsgSystem(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("msgSystem.");
	}

	/**
	 * 系统消息列表
	 */
	@RequestMapping("/systemmsglist")
	public ModelAndView systemMsgList(HttpServletRequest request, @ModelAttribute("msgSystem") MsgSystem msgSystem,Integer pageNum,Integer pageSize) {
		ModelAndView model = new ModelAndView();
		model.setViewName("modules/letter/to_msg_system_list");
		try {
			String num = request.getParameter("page.currentPage");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
			List<MsgSystem> nsgSystemList = msgSystemDaoService.queryMsgSystemList(msgSystem);
			PageInfo<MsgSystem> pageInfo = new PageInfo<MsgSystem>(nsgSystemList);
			model.addObject("page", pageInfo);
			request.setAttribute("nsgSystemList", nsgSystemList);
		} catch (Exception e) {
			logger.error("AdminUserController.systemMsgList--error", e);
		}
		return model;
	}

	/**
	 * 批量删除
	 */
	@RequestMapping("/delsystem")
	@ResponseBody
	public Map<String, Object> delSystem(HttpServletRequest request, @RequestParam("ids") String[] ids) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			if (ids != null && ids.length > 0) {
				// msgSystemDaoService.delMsgSystemById(ids);
				int del = msgSystemDaoService.delMsgSystemById(ids);
				if (del > 0) {
					json.put("success", true);
					json.put("message", "删除成功");
				}else {
					json.put("success", false);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return json;
	}
}
