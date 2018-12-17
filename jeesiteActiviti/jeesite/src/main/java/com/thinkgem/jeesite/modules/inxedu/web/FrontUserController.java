package com.thinkgem.jeesite.modules.inxedu.web;

import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.thinkgem.jeesite.common.constants.CacheConstans;
import com.thinkgem.jeesite.common.constants.CommonConstants;
import com.thinkgem.jeesite.common.constants.WebSiteProfileType;
import com.thinkgem.jeesite.common.utils.EhCacheUtils;
import com.thinkgem.jeesite.common.utils.GsonUtils;
import com.thinkgem.jeesite.common.utils.MD5Utiles_;
import com.thinkgem.jeesite.common.utils.ObjectUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.StudentUser;
import com.thinkgem.jeesite.modules.inxedu.entity.course.Course;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseDto;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseStudyhistory;
import com.thinkgem.jeesite.modules.inxedu.entity.course.FavouriteCourseDTO;
import com.thinkgem.jeesite.modules.inxedu.entity.course.QueryCourse;
import com.thinkgem.jeesite.modules.inxedu.service.CourseDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.CourseFavoritesDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.CourseKpointService;
import com.thinkgem.jeesite.modules.inxedu.service.CourseStudyhistoryService;
import com.thinkgem.jeesite.modules.inxedu.service.MsgReceiveDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.StudentUserService;
import com.thinkgem.jeesite.modules.inxedu.service.WebsiteProfileDaoService;
import com.thinkgem.jeesite.modules.inxedu.utils.SingletonLoginUtils;
import com.thinkgem.jeesite.modules.inxedu.utils.WebUtils_;

@Controller
@RequestMapping("${frontPath}")
public class FrontUserController extends BaseController {

	@Autowired
	private StudentUserService studentUserService;
	@Autowired
	private WebsiteProfileDaoService websiteProfileDaoService;
	@Autowired
	private MsgReceiveDaoService msgReceiveDaoService;
	@Autowired
	private CourseDaoService courseDaoService;
	@Autowired
	private CourseStudyhistoryService courseStudyhistoryService;
	@Autowired
	private CourseKpointService courseKpointService;
	@Autowired
	private CourseFavoritesDaoService courseFavoritesDaoService;

	@InitBinder("studentUser")
	public void initBinderUser(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("user.");
	}

	// start:*************************我的收藏myFavorites***********************************************
	// 收藏主页
	@RequestMapping("/uc/myFavorites")
	public ModelAndView myFavorites(HttpServletRequest request, Integer pageNum, Integer pageSize) {
		ModelAndView model = new ModelAndView("modules/web/ucenter/favourite_course_list");
		try {
			// favoriteList
			String num = request.getParameter("page.currentPage");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 8 : pageSize);
			List<FavouriteCourseDTO> favoriteList = courseFavoritesDaoService.queryFavoritesPage(SingletonLoginUtils.getLoginUserId(request));
			PageInfo<FavouriteCourseDTO> pageInfo = new PageInfo<FavouriteCourseDTO>(favoriteList);
			model.addObject("favoriteList", favoriteList);
			model.addObject("page", pageInfo);

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return model;
	}

	// 删除收藏
	@RequestMapping("/uc/deleteFaveorite/{favouriteIdStr}")
	public ModelAndView deleteFavorite(HttpServletRequest request, @PathVariable("favouriteIdStr") String favouriteIdStr) {
		ModelAndView model = new ModelAndView();
		try {
			courseFavoritesDaoService.deleteCourseFavoritesById(favouriteIdStr);
			model.setViewName("redirect:" + frontPath + "/uc/myFavorites");
		} catch (Exception e) {
			logger.error("deleteFavorite()---error", e);
		}
		return model;
	}
	// end:*************************我的收藏myFavorites***********************************************

	// start:********************************个人资料设置****************************************************************
	// 初始化修改用户信息页面
	@RequestMapping("/uc/initUpdateUser/{index}")
	public ModelAndView initUpdateUser(HttpServletRequest request, @PathVariable("index") int index) {
		ModelAndView model = new ModelAndView();
		try {
			int userId = SingletonLoginUtils.getLoginUserId(request);
			StudentUser user = studentUserService.queryUserById(userId);
			model.addObject("user", user);
			model.addObject("index", index);
			model.setViewName("modules/web/ucenter/user-info");
		} catch (Exception e) {
			logger.error("initUpdateUser()---error", e);
		}
		return model;
	}

	// 修改基本资料
	@RequestMapping("/uc/updateUser")
	@ResponseBody
	public Map<String, Object> updateUserInfo(HttpServletRequest request, @ModelAttribute("studentUser") StudentUser studentUser) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			studentUserService.updateStudentUser(studentUser);
			json = this.setJson(true, "修改成功", studentUser);
			// 缓存用户
			studentUserService.setLoginInfo(request, studentUser.getUserId(), "false");
		} catch (Exception e) {
			logger.error("updateUserInfo()---error", e);
			return this.setAjaxException(json);
		}
		return json;
	}

	// 修改头像

	// 修改密码
	@RequestMapping("/uc/updatePwd")
	@ResponseBody
	public Map<String, Object> updatePwd(HttpServletRequest request, @ModelAttribute("studentUser") StudentUser studentUser) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			StudentUser user = SingletonLoginUtils.getLoginUser(request);
			if (ObjectUtils.isNotEmpty(user)) {
				String nowPassword = request.getParameter("nowPassword");
				String newPassword = request.getParameter("newPassword");
				String confirmPwd = request.getParameter("confirmPwd");
				if (StringUtils.isEmpty(confirmPwd) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(nowPassword)) {
					return this.setJson(false, "原密码或新密码不能为空", null);
				}
				if (!user.getPassword().equals(MD5Utiles_.MD5(nowPassword))) {
					return this.setJson(false, "原密码输入错误", null);
				}
				if (!(WebUtils_.isPasswordAvailable(newPassword) && WebUtils_.isPasswordAvailable(confirmPwd) && newPassword.equals(confirmPwd))) {
					return this.setJson(false, "两次密码必须一致且密码有字母和数字组合且≥6位≤16位", null);
				}
				// 更新密码
				user.setPassword(MD5Utiles_.MD5(newPassword));
				studentUserService.updateUserPwd(user);
				json = this.setJson(true, "密码更新成功", null);
			}
		} catch (Exception e) {
			this.setAjaxException(json);
			logger.error("updateUserInfo()---error", e);
		}
		return json;
	}

	// end:********************************个人资料设置****************************************************************

	// start:***********************登录->获取个人信息->进入个人主页->退出登录******************************************************
	// 登录
	@RequestMapping("/uc/login")
	@ResponseBody
	public Map<String, Object> doLogin(HttpServletResponse response, @ModelAttribute("studentUser") StudentUser studentUser, @RequestParam("account") String account,
			@RequestParam("password") String password, @RequestParam("ipForget") String ipForget) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
				return this.setJson(false, "账户或密码不能为空", null);
			}
			StudentUser user = studentUserService.getLoginUser(account, MD5Utiles_.MD5(password));
			if (ObjectUtils.isEmpty(user)) {
				return this.setJson(false, "找不到用户", null);
			}
			// 账户 密码正确
			EhCacheUtils.remove(CacheConstans.USER_CURRENT_LOGINTIME + user.getUserId());// 首先删除以前的缓存
			if (user.getIsavalible() == 2) {
				return this.setJson(false, "用户已冻结", null);
			}
			// 是否记住
			Long currentTimestamp = System.currentTimeMillis();
			String uuid = StringUtils.createUUID().replace("-", "");
			user.setLoginTimeStamp(currentTimestamp);// 当前时间戳
			if ("true".equalsIgnoreCase(ipForget)) {
				EhCacheUtils.set(uuid, user, CacheConstans.USER_TIME);// 缓存用户对象(6h)
				EhCacheUtils.set(CacheConstans.USER_CURRENT_LOGINTIME + user.getUserId(), currentTimestamp.toString(), CacheConstans.USER_TIME);// 缓存用户登录时间
																																				// 6h
				WebUtils_.setCookie(response, CacheConstans.WEB_USER_LOGIN_PREFIX, uuid, CacheConstans.USER_TIME);// cookie设置多少秒有效期
																													// 6h
			} else {
				EhCacheUtils.set(uuid, user, CacheConstans.USER_TIME);// 缓存用户对象(6h)
				EhCacheUtils.set(CacheConstans.USER_CURRENT_LOGINTIME + user.getUserId(), currentTimestamp.toString(), CacheConstans.USER_TIME);// 缓存用户登录时间6h
				WebUtils_.setCookie(response, CacheConstans.WEB_USER_LOGIN_PREFIX, uuid, 1000);// 设置cookie
																								// 1000秒有效期，1000秒之后作废
			}

			// 登录日志记录。。。。。。CacheConstans.USER_CURRENT_LOGINTIME+user.getUserId()).toString())
			json = this.setJson(true, "", null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return this.setAjaxException(json);
		}
		return json;

	}

	// 获取个人信息
	@RequestMapping("/uc/getloginUser")
	@ResponseBody
	public Map<String, Object> getloginUser(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("studentUser") StudentUser studentUser) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			StudentUser user = SingletonLoginUtils.getLoginUser(request);
			if (ObjectUtils.isNotEmpty(user)) {
				return this.setJson(true, "", user);
			} else {
				return this.setJson(false, "", null);
			}
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
			return this.setAjaxException(json);
		}
	}

	// 进入个人主页
	@RequestMapping("/uc/index")
	public ModelAndView ucIndex(HttpServletRequest request, @ModelAttribute("queryCourse") QueryCourse queryCourse, Integer pageNum, Integer pageSize) {
		ModelAndView model = new ModelAndView("modules/web/ucenter/uc_freecourse");
		try {
			int userId = SingletonLoginUtils.getLoginUserId(request);

			// courseList(免费课程/上架)
			queryCourse.setIsavaliable(1);
			queryCourse.setIsFree("true");
			String num = request.getParameter("page.currentPage");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 12 : pageSize);
			List<CourseDto> courseList = courseDaoService.queryWebCourseListPage(queryCourse);
			PageInfo<CourseDto> pageInfo = new PageInfo<CourseDto>(courseList);
			model.addObject("page", pageInfo);
			model.addObject("courseList", courseList);
			if (ObjectUtils.isEmpty(courseList)) {
				return model;
			}
			// ///////////////////studyPercent/////////////////////////////
			for (Course course : courseList) {
				// 我的学习记录-------------学习进度
				CourseStudyhistory courseStudyhistory = new CourseStudyhistory();
				courseStudyhistory.setUserId(userId);
				courseStudyhistory.setCourseId(course.getCourseId());
				List<CourseStudyhistory> couStudyhistorysLearned = courseStudyhistoryService.getCourseStudyhistoryList(courseStudyhistory);
				int learnedSize = 0;
				if (ObjectUtils.isNotEmpty(couStudyhistorysLearned) && couStudyhistorysLearned.size() > 0) {
					learnedSize = couStudyhistorysLearned.size();
				}
				int count = courseKpointService.getSecondLevelKpointCount(Long.valueOf(course.getCourseId()));
				NumberFormat numberFormat = NumberFormat.getInstance();
				numberFormat.setMaximumFractionDigits(0);
				String studyPercent = numberFormat.format((float) learnedSize / (float) count * 100);
				studyPercent = (count == 0 ? "0" : studyPercent);
				course.setStudyPercent(studyPercent);
				model.addObject("isok", true);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return model;

	}

	// 退出登录
	@RequestMapping("/uc/exit")
	@ResponseBody
	public Map<String, Object> exitLogin(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			// 清理cookie
			String cookie = WebUtils_.getCookie_(request, CacheConstans.WEB_USER_LOGIN_PREFIX);
			EhCacheUtils.remove(cookie);
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
			return this.setAjaxException(json);
		}
		return json;
	}

	// end:***********************登录->获取个人信息->进入个人主页->退出登录******************************************************

	/**
	 * 注册
	 */
	@RequestMapping("/uc/createuser")
	@ResponseBody
	public Map<String, Object> createUser(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("studentUser") StudentUser studentUser) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			String registerCode = request.getParameter("registerCode") == null ? "" : request.getParameter("registerCode");
			Object randCode = request.getSession().getAttribute(CommonConstants.RAND_CODE);
			if (randCode == null || !registerCode.equals(randCode.toString())) {
				return this.setJson(false, "请输入正确的验证码", null);
			}

			String confirmPwd = request.getParameter("confirmPwd");

			if (studentUser.getEmail() == null || studentUser.getEmail().trim().length() == 0 || !WebUtils_.checkEmail(studentUser.getEmail(), 50)) {
				return this.setJson(false, "请输入正确的邮箱号", null);
			}
			if (studentUserService.checkEmail(studentUser.getEmail().trim())) {
				return this.setJson(false, "该邮箱号已被使用", null);
			}
			if (studentUser.getMobile() == null || studentUser.getMobile().trim().length() == 0 || !WebUtils_.checkMobile(studentUser.getMobile())) {
				return this.setJson(false, "请输入正确的手机号", null);
			}
			if (studentUserService.checkMobile(studentUser.getMobile())) {
				return this.setJson(false, "该手机号已被使用", null);
			}
			if (studentUser.getPassword() == null || studentUser.getPassword().trim().length() == 0 || !WebUtils_.isPasswordAvailable(studentUser.getPassword())) {
				return this.setJson(false, "密码有字母和数字组合且≥6位≤16位", null);
			}
			if (!studentUser.getPassword().equals(confirmPwd)) {
				return this.setJson(false, "两次密码不一致", null);
			}

			studentUser.setCreateTime(new Date());
			studentUser.setIsavalible(1);
			studentUser.setPassword(MD5Utiles_.MD5(studentUser.getPassword()));
			studentUser.setMsgNum(0);
			studentUser.setSysMsgNum(0);
			studentUser.setLastSystemTime(new Date());
			studentUserService.createStudentUser(studentUser);
			request.getSession().removeAttribute(CommonConstants.RAND_CODE);
			json = this.setJson(true, "注册成功", null);

			// 注册时发送系统消息
			Map<String, Object> websitemap = websiteProfileDaoService.getWebsiteProfileByType_(WebSiteProfileType.web.toString());
			Map<String, Object> web = GsonUtils.GsonToMaps(websitemap.get("web").toString());
			String company = web.get("company").toString();
			String conent = "欢迎来到" + company + ",希望您能够快乐的学习";
			msgReceiveDaoService.addSystemMessageByCusId(conent, studentUser.getUserId());

			// 登录或注册都要给浏览器写入cookie
			String uuid = StringUtils.createUUID().replace("-", "");
			WebUtils_.setCookie(response, CacheConstans.WEB_USER_LOGIN_PREFIX, uuid, 10000);// 注册之后直接进入登录状态

			Long currentTimestamp = System.currentTimeMillis();
			EhCacheUtils.set(CacheConstans.USER_CURRENT_LOGINTIME + studentUser.getUserId(), currentTimestamp.toString(), CacheConstans.USER_TIME);// 给每一个用户设置登录缓存6小时,每次登录之前的缓存都会被时间戳替换
			studentUserService.setLoginInfo(request, studentUser.getUserId(), "true");// 设置注册之后的每一个用户是否自动登录;

		} catch (Exception e) {
			logger.error("createUser()--eror", e);
		}
		return json;
	}

}
