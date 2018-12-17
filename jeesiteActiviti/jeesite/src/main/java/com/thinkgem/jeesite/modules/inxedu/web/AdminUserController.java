package com.thinkgem.jeesite.modules.inxedu.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.thinkgem.jeesite.common.utils.MD5Utiles_;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.dao.MsgReceiveDao;
import com.thinkgem.jeesite.modules.inxedu.entity.QueryStudentUser;
import com.thinkgem.jeesite.modules.inxedu.entity.StudentUser;
import com.thinkgem.jeesite.modules.inxedu.entity.letter.MsgSystem;
import com.thinkgem.jeesite.modules.inxedu.service.MsgReceiveDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.MsgSystemDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.StudentUserService;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

@Controller
@RequestMapping("${adminPath}/studentuser")
public class AdminUserController extends BaseController {
	@Autowired
	private StudentUserService studentUserService;

	@Autowired
	private MsgReceiveDaoService msgReceiveDaoService;

	@Autowired
	private MsgSystemDaoService msSystemDaoService;

	@InitBinder("queryUser")
	public void getuserList(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("queryUser.");
	}

	@InitBinder("user")
	public void updateUserPwd(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("user.");
	}

	// start:***************************************
	// 发送系统消息页面
	@RequestMapping(value = "/letter/toSendSystemMessages")
	public ModelAndView senSystemMessages(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();// 发送系统消息页面
		modelAndView.setViewName("modules/user/to_send_systemMessage");
		return modelAndView;
	}

	// 执行发送
	@RequestMapping(value = "/letter/sendJoinGroup")
	@ResponseBody
	public Map<String, Object> sendSystemInform(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String content = request.getParameter("content");// 发送系统消息的内容
			MsgSystem msgReceive = new MsgSystem();
			msgReceive.setContent(content);// 添加站内信的内容
			msgReceive.setUpdateTime(new Date());// 更新时间s
			msgReceive.setAddTime(new Date());// 添加时间
			msSystemDaoService.addMsgSystem(msgReceive);
			map.put("message", "success");
		} catch (Exception e) {
			logger.error("AdminUserController.sendSystemInform", e);
		}
		return map;
	}

	// end:********************************************************

	// start:********************************************************
	// 批量发送页面
	@RequestMapping(value = "/letter/toSendSystemMessagesBatch")
	public ModelAndView senSystemMessagesBatch(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();// 发送系统消息页面
		modelAndView.setViewName("modules/user/to_send_systemMessage_batch");
		return modelAndView;
	}

	// 执行批量发送
	@RequestMapping(value = "/letter/sendJoinGroupBatch")
	@ResponseBody
	public Map<String, Object> sendJoinGroupBatch(HttpServletRequest request, @RequestParam("userEmails") String userEmails, @RequestParam("content") String content) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isEmpty(userEmails)) {
				map.put("message", "用户邮箱不能为空");
				return map;
			}
			StudentUser user = studentUserService.queryUserByEmailOrMobile(userEmails);
			if (user != null) {
				msgReceiveDaoService.addSystemMessageByCusId(content, Integer.valueOf(user.getUserId()));
				map.put("message", "success");
			} else {
				map.put("message", "发送失败");

			}
		} catch (Exception e) {
			logger.error("AdminUserController.sendJoinGroupBatch", e);
			map.put("message", "系统错误,请稍后重试");
		}
		return map;
	}

	// end:*****************************************************************

	/**
	 * 首页(用户列表),分页第II种写法：https://www.cnblogs.com/CookiesBear/p/6197826.html
	 * https://www.cnblogs.com/Joanna-Yan/p/7256105.html
	 */
	@RequestMapping("/getuserList")
	public ModelAndView getuserList(@ModelAttribute("queryUser") QueryStudentUser queryUser, Integer pageNum, Integer pageSize,HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("modules/user/user-list");
		try {
			String num = request.getParameter("page.pageNum");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
			List<StudentUser> userList = studentUserService.queryUserListPage(queryUser);
			PageInfo<StudentUser> pageInfo = new PageInfo<StudentUser>(userList);
			mv.addObject("userList", userList);
			mv.addObject("page", pageInfo);
		} catch (Exception e) {
			logger.error("getuserList()--error", e);
			e.printStackTrace();
		}
		return mv;
	}

	// lookuserlog 查看日志
	@RequestMapping(value = "lookuserlog")
	public void lookUserLog() {
		// 不用实现
	}

	/**
	 * 更新用户状态 (1解冻 2冻结)
	 */
	@RequestMapping(value = "/updateuserstate", method = RequestMethod.POST)
	public Map<String, Object> updateUserState(@ModelAttribute("studentUser") StudentUser user, @RequestParam("user.userId") String userId,
			@RequestParam("user.isavalible") Integer isavalible) {
		Map<String, Object> json = new HashMap<String, Object>();
		user.setUserId(Integer.valueOf(userId));
		user.setIsavalible(isavalible);
		boolean update = studentUserService.updateUserStates(user);
		if (update) {
			System.out.println("//////////////////////////////updateuserstate////////////////////////////////////////////");
			json.put("success", true);
		} else {
			json.put("success", false);
		}
		return json;

	}

	/**
	 * 更新用户密码
	 */
	@RequestMapping(value = "/updateUserPwd", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateUserPwd(HttpServletResponse response, @RequestParam("user.userId") String userId, @RequestParam("passwords") String passwords,
			@RequestParam("user.password") String password, @ModelAttribute("user") StudentUser user) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			// 密码和重复密码一致，前后台都验证
			if (password.equals(passwords) && StringUtils.isNotEmpty(password)) {
				user.setUserId(Integer.valueOf(userId));
				user.setPassword(MD5Utiles_.MD5(password));
				boolean update = studentUserService.updateUserPwd(user);
				if (update) {
					json.put("success", true);
				}
			} else {
				json.put("success", false);
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("updateUserPwd()--error", e);
		}
		return json;

	}

	// start:*****************************************************************
	/**
	 * 批量开通页面
	 */
	@RequestMapping("/toBatchOpen")
	public ModelAndView toBatchOpen() {
		ModelAndView model = new ModelAndView();
		model.setViewName("modules/user/batchOpen");
		return model;
	}

	// 执行批量导入操作
	@RequestMapping(value = "/importExcel")
	// 导入excel
	public ModelAndView importExcel(ShiroHttpServletRequest request, Model model, @RequestParam("myFile") MultipartFile myFile, @RequestParam("mark") Integer mark) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("modules/user/batchOpen");
		try {
			String msg = studentUserService.updateImportExcel(request, myFile, mark);

			if (StringUtils.isEmpty(msg)) {
				model.addAttribute("message", "保存成功");
			} else {
				model.addAttribute("error", msg);
			}
		} catch (Exception e) {
			logger.error("updateImportExcel()--error", e);
		}
		return mv;
	}
	// end:*****************************************************************
}
