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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.thinkgem.jeesite.common.utils.GsonUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.kpoint.CourseKpoint;
import com.thinkgem.jeesite.modules.inxedu.entity.kpoint.CourseKpointDto;
import com.thinkgem.jeesite.modules.inxedu.service.CourseKpointService;

@Controller
@RequestMapping("${adminPath}/kpoint")
public class AdminKpointController extends BaseController {
	@InitBinder("courseKpoint")
	public void teacherListBinder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("courseKpoint.");
	}

	@Autowired
	private CourseKpointService courseKpointService;

	/**
	 * updateKpoint 更新节点
	 */
	@RequestMapping("/updateKpoint")
	@ResponseBody
	public Map<String, Object> updateKpoint(@ModelAttribute("courseKpoint") CourseKpoint kpoint) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			boolean update = courseKpointService.updateKpoint(kpoint);
			System.out.println("////////////updateKpoint/////////////////");
			if (update) {
				json.put("success", true);
				json.put("entity", kpoint);
			} else {
				json.put("success", false);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json;
	}

	/**
	 * 拖拽修改父节点
	 */
    @RequestMapping("/updateparentid/{parentId}/{kpointId}")
    @ResponseBody
    public Map<String,Object> updateKpointParentId(@PathVariable("parentId") int parentId,@PathVariable("kpointId") int kpointId){
    	Map<String,Object> json = new HashMap<String,Object>();
    	try{
    		boolean update = courseKpointService.updateKpointParentId(kpointId, parentId);
    		if (update) {
    			json.put("success", true);
			}else {
				json.put("success", false);
			}
    	}catch (Exception e) {
			logger.error("updateKpointParentId()---error",e);
		}
    	return json;
    }
	
	/**
	 * 删除节点
	 */
	@RequestMapping("deletekpoint/{ids}")
	@ResponseBody
	public Map<String, Object> deleteKpoint(@PathVariable("ids") String ids) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			boolean delete = courseKpointService.deleteKpointByIds(ids);
			if (delete) {
				json.put("success", true);

			} else {
				json.put("success", false);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json;
	}

	// //////////////////////////////判断节点然后添加节点///////////////////////////////////////////////
	@RequestMapping("/getkpoint/{kpointId}")
	// //////////////getkpoint判断节点类型
	@ResponseBody
	public Map<String, Object> queryCourseKpointById(@PathVariable("kpointId") int kpointId) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			CourseKpointDto kpoint = courseKpointService.queryCourseKpointById(kpointId);
			json.put("success", true);
			json.put("entity", kpoint);
		} catch (Exception e) {
			json.put("success", false);
			logger.error("getkpoint()--error", e);
		}
		return json;
	}

	@RequestMapping("/addkpoint")
	// //////////addkpoint创建节点
	@ResponseBody
	public Map<String, Object> addKpoint(@ModelAttribute("courseKpoint") CourseKpoint courseKpoint) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			courseKpoint.setAddTime(new Date());
			courseKpoint.setFree(1);// 1免费
			int add = courseKpointService.addCourseKpoint(courseKpoint);
			if (add > 0) {
				json.put("success", true);
				json.put("entity", courseKpoint);
			} else {
				json.put("success", false);
			}
		} catch (Exception e) {
			logger.error("addKpoint()--error", e);
		}
		return json;
	}

	/**
	 * 课程的视频列表
	 */
	@RequestMapping("/list/{courseId}")
	public ModelAndView showKpointList(HttpServletRequest request, @PathVariable("courseId") int courseId) {
		ModelAndView model = new ModelAndView();
		model.setViewName("modules/course/kpoint_list");
		try {
			List<CourseKpoint> kpointList = courseKpointService.queryCourseKpointByCourseId(courseId);
			model.addObject("kpointList", GsonUtils.GsonString(kpointList));

		} catch (Exception e) {
			// TODO: handle exception
		}

		return model;
	}

}
