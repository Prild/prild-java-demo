package com.thinkgem.jeesite.modules.inxedu.web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.asual.lesscss.LessEngine;
import com.thinkgem.jeesite.common.constants.CacheConstans;
import com.thinkgem.jeesite.common.constants.CommonConstants;
import com.thinkgem.jeesite.common.utils.EhCacheUtils;
import com.thinkgem.jeesite.common.utils.ObjectUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.service.CommentService;
import com.thinkgem.jeesite.modules.inxedu.entity.comment.Comment_;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseDto;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseStudyhistory;
import com.thinkgem.jeesite.modules.inxedu.entity.course.QueryCourse;
import com.thinkgem.jeesite.modules.inxedu.entity.teacher.QueryTeacher;
import com.thinkgem.jeesite.modules.inxedu.entity.teacher.Teacher;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteImages;
import com.thinkgem.jeesite.modules.inxedu.service.Comment_DaoService;
import com.thinkgem.jeesite.modules.inxedu.service.CourseDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.CourseStudyhistoryService;
import com.thinkgem.jeesite.modules.inxedu.service.StudentUserService;
import com.thinkgem.jeesite.modules.inxedu.service.TeacherService;
import com.thinkgem.jeesite.modules.inxedu.service.WebsiteImagesDaoService;

@Controller
@RequestMapping("${frontPath}")
public class WebFrontController extends BaseController {
	@Autowired
	private WebsiteImagesDaoService websiteImagesDaoService;
	@Autowired
	private CourseDaoService courseDaoService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private StudentUserService studentUserService;
	@Autowired
	private CourseStudyhistoryService courseStudyhistoryService;
	@Autowired
	private Comment_DaoService commentService;

	/**
	 * 学生学习记录的动态
	 */
	@RequestMapping("/index/ajax/studentDynamic")
	public ModelAndView studentDynamic(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("modules/web/front/ajax-student-dynamic");
		try {
			Object object = EhCacheUtils.get(CacheConstans.QUESTIONS_HOT_RECOMMEND);
			if (ObjectUtils.isNotEmpty(object)) {
				model.addObject("courseStudyhistoryList", object);
			} else {
				// courseStudyhistoryList
				CourseStudyhistory courseStudyhistory = new CourseStudyhistory();
				courseStudyhistory.setQueryLimit(4);
				List<CourseStudyhistory> courseStudyhistoryList = courseStudyhistoryService.getCourseStudyhistoryList(courseStudyhistory);
				model.addObject("courseStudyhistoryList", courseStudyhistoryList);
				EhCacheUtils.set(CacheConstans.QUESTIONS_HOT_RECOMMEND, courseStudyhistoryList, 3600);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return model;
	}

	/**
	 * 首页 精品课程、最新课程、全部课程
	 */
	@RequestMapping("/index/ajax/bna")
	public ModelAndView queryCourse(HttpServletRequest request, @RequestParam("order") String order) {
		ModelAndView model = new ModelAndView("modules/web/front/ajax-course-bna");
		try {
			if (order != null && !order.equals("")) {
				QueryCourse queryCourse = new QueryCourse();
				queryCourse.setOrder(order);
				// 只查询上架的
				queryCourse.setIsavaliable(1);
				// 获得精品课程、最新课程、全部课程
				List<CourseDto> courseDtoBNAList = courseDaoService.queryCourse(queryCourse);
				request.setAttribute("queryCourse", queryCourse);
				request.setAttribute("courseDtoBNAList", courseDtoBNAList);
			}
		} catch (Exception e) {
			logger.error("WebFrontController.queryCourse", e);
		}
		return model;
	}

	/**
	 * 首页 为你推荐换一换功能(随机获取)
	 */
	@RequestMapping("/index/ajax/huanyihuan")
	public ModelAndView queryRecommenCourseListByRecommendId(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("modules/web/front/ajax-course-recommend");
		try {
			// RecommendId是分类Id
			// 获得为你推荐的课程
			List<CourseDto> courseDtoList = courseDaoService.queryRecommenCourseListByRecommendId(2l, 4l);
			request.setAttribute("courseDtoList", courseDtoList);
		} catch (Exception e) {
			logger.error("WebFrontController.queryRecommenCourseListByRecommendId", e);
			e.printStackTrace();
		}
		return model;
	}

	/**
	 * 修改主题色
	 * 
	 */
	@RequestMapping("/theme/ajax/update")
	@ResponseBody
	public Object updateTheme(HttpServletRequest request, @RequestParam String color) {
		Map<String, Object> json = new HashMap<String, Object>();
		changeColor(request, color);
		json = this.setJson(true, color, "");
		return json;
	}

	@RequestMapping("/index")
	public ModelAndView getIndexpage(HttpServletRequest request, Model model) {
		ModelAndView mView = new ModelAndView();
		mView.setViewName("modules/web/front/index");
		try {
			model.addAttribute("num", System.currentTimeMillis());
			// 获得banner图
			Map<String, List<WebsiteImages>> websiteImages = websiteImagesDaoService.queryImagesByType_();
			model.addAttribute("websiteImages", websiteImages);
			// 不同的主题显示不同的颜色
			String cacheColor = (String) EhCacheUtils.get("inxedu_index_theme_color");
			if (StringUtils.isNotEmpty(cacheColor)) {
				if ("blue".equals(cacheColor)) {
					List<WebsiteImages> websiteImagesList = websiteImages.get("type_16");
					model.addAttribute("websiteImagesList", websiteImagesList);
				}
				if ("green".equals(cacheColor)) {
					List<WebsiteImages> websiteImagesList = websiteImages.get("type_17");
					model.addAttribute("websiteImagesList", websiteImagesList);
				}
				if ("orange".equals(cacheColor)) {
					List<WebsiteImages> websiteImagesList = websiteImages.get("type_1");
					model.addAttribute("websiteImagesList", websiteImagesList);
				}
			} else {
				changeColor(request, "orange");
				List<WebsiteImages> websiteImagesList = websiteImages.get("type_1");
				model.addAttribute("websiteImagesList", websiteImagesList);
			}
			model.addAttribute("theme_color", cacheColor);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// 使用缓存并显示4位最受欢迎的老师展示在首页teacherList
		Object teacherObject = EhCacheUtils.get(CacheConstans.INDEX_TEACHER_RECOMMEND);
		if (ObjectUtils.isNotEmpty(teacherObject)) {
			model.addAttribute("teacherList", teacherObject);
		} else {
			QueryTeacher queryTeacher = new QueryTeacher();
			queryTeacher.setCount(4);
			List<Teacher> teacherList = teacherService.queryTeacherList(queryTeacher);// 获取所有星标老师
			EhCacheUtils.set(CacheConstans.INDEX_TEACHER_RECOMMEND, teacherList, 3600);
			model.addAttribute("teacherList", teacherList);
		}

		// 课程互动commentList
		Comment_ comment = new Comment_();
		comment.setLimitNumber(10);
		List<Comment_> commentList = commentService.queryCommentInteraction(comment);
		model.addAttribute("commentList", commentList);

		return mView;

	}

	/**
	 * 1.首次进来默认主题orange;2.点击改变主题颜色执行
	 */
	public void changeColor(HttpServletRequest request, String colorfalg) {
		String color = "#ea562e";
		if (colorfalg.equals("blue")) {
			color = "#009ed9";
		} else if (colorfalg.equals("green")) {
			color = "#68cb9b";
		}

		// 放入缓存
		EhCacheUtils.set("inxedu_index_theme_color", colorfalg, 21600);// 缓存六小时);
		// 获得项目根目录
		String strDirPath = request.getSession().getServletContext().getRealPath("/");
		// 读取字符串
		StringBuffer sb = new StringBuffer();
		// 当前读取行数
		int rowcount = 1;
		// 要修改的行数
		int updaterowcount = 2;
		FileReader fr;
		try {
			String path = strDirPath + "/static_/inxweb/css/less/theme.less";
			fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				if (rowcount == updaterowcount && StringUtils.isNotEmpty(color)) {
					sb.append("@mColor:" + color + ";");
				} else {
					sb.append(line);
				}
				line = br.readLine();
				rowcount++;
			}
			br.close();
			fr.close();
			LessEngine engine = new LessEngine();
			FileWriter fw = new FileWriter(strDirPath + "/static_/inxweb/css/theme.css");
			fw.write(engine.compile(sb.toString()));
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
