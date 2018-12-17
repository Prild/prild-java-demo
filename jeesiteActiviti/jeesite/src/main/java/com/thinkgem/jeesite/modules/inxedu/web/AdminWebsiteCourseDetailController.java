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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseDto;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteCourse;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteCourseDetail;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteCourseDetailDTO;
import com.thinkgem.jeesite.modules.inxedu.service.WebsiteCourseDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.WebsiteCourseDetailService;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

@Controller
@RequestMapping("${adminPath}/website/detail")
public class AdminWebsiteCourseDetailController extends BaseController {
	@InitBinder({ "dto" })
	public void initBinder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("dto.");
	}

	@Autowired
	private WebsiteCourseDaoService websiteCourseDaoService;
	@Autowired
	private WebsiteCourseDetailService websiteCourseDetailService;
	
	
	/**
	 * 删除"推荐课程"
	 */
	@RequestMapping("/deletedeail/{id}")
	public ModelAndView deleteDetail(HttpServletRequest request,@PathVariable("id") int id){
		ModelAndView model = new ModelAndView();
		model.setViewName("redirect:"+adminPath+"/detail/list");
		try {
			websiteCourseDetailService.deleteDetailById(id);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return model;
		
		
	}
	
	/**
	 * 添加"推荐课程"
	 */
	@RequestMapping("/addrecommendCourse")
	@ResponseBody
	public Map<String, Object> addrecommendCourse(@RequestParam("courseIds") String courseIds, @RequestParam("recommendId") int recommendId) {
		Map<String, Object> json = new HashMap<String, Object>();
		System.out.println("*******************courseIds*************************" + courseIds);
		try {
			//没有选择课程
			if(courseIds==null || courseIds.trim().length()==0){
				json.put("success", false);
				json.put("message", "没有选择推荐课程");
				return json;
			}
			// 推荐分类
			WebsiteCourse websiteCourse = websiteCourseDaoService.queryWebsiteCourseById(recommendId);
			//选择课程数 > 分类规定数
			int count_ = StringUtils.countMatches(courseIds, ",");
			if (courseIds.contains(",") && count_ > websiteCourse.getCourseNum() + 1) {
				json.put("success", false);
				json.put("message", "选择数大于推荐规定数");
				return json;
			}
			// courseIds格式 ,13,14,15,16,17,18,19,20,21,25,35, 转化成数组
			if (courseIds.trim().startsWith(",")) {
				courseIds = courseIds.trim().substring(1, courseIds.trim().length());
			}
			if (courseIds.trim().endsWith(",")) {
				courseIds = courseIds.trim().substring(0, courseIds.trim().length() - 1);
			}
			String[] _arr = courseIds.split(",");
			// 保存推荐分类id和课程id到表
			if (_arr.length > 0) {
				StringBuffer val = new StringBuffer();
				for (int i = 0; i < _arr.length; i++) {
					if (i < _arr.length - 1) {
						val.append("(0," + recommendId + "," + _arr[i] + ",0),");//数据库以 ${value} 形式插入数据，ID,RECOMMEND_ID,COURSE_ID,ORDER_NUM,id自增，排序默认给
					} else {
						val.append("(0," + recommendId + "," + _arr[i] + ",0)");
					}
				}
				websiteCourseDetailService.createWebsiteCourseDetail(val.toString());
				json.put("success", true);
				json.put("entity", "/detail/list");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return json;

	}

	/**
	 * 更新"推荐课程"的排序值
	 * 
	 * @param id
	 *            课程id
	 * @param sort
	 *            排序值
	 */
	@RequestMapping("/updatesort/{id}/{sort}")
	@ResponseBody
	public Map<String, Object> updateSort(@PathVariable("id") int id, @PathVariable("sort") int sort) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			boolean update = websiteCourseDetailService.updateSort(id, sort);
			if (update) {
				json.put("success", true);
			} else {
				json.put("success", false);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json;

	}

	/**
	 * "推荐课程"首页
	 */
	@RequestMapping("/list")
	public ModelAndView queryDetail(@ModelAttribute("dto") WebsiteCourseDetailDTO dto,HttpServletRequest request,Integer pageNum,Integer pageSize) {
		ModelAndView model = new ModelAndView();
		
		model.setViewName("modules/website/course/websiteCourseDetail_list");
		try {
			String num = request.getParameter("page.currentPage");
			if (StringUtils.isNotEmpty(num)) {
				pageNum = Integer.valueOf(num);
			}
			PageHelper.startPage(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
			// 推荐课程列表
			List<WebsiteCourseDetailDTO> dtoList = websiteCourseDetailService.queryCourseDetailPage(dto);
			model.addObject("dtoList", dtoList);
			PageInfo<WebsiteCourseDetailDTO> pageInfo = new PageInfo<WebsiteCourseDetailDTO>(dtoList);
			model.addObject("page", pageInfo);
			
			// 推荐课程分类列表
			List<WebsiteCourse> websiteCourseList = websiteCourseDaoService.queryWebsiteCourseList();
			model.addObject("websiteCourseList", websiteCourseList);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return model;

	}
}
