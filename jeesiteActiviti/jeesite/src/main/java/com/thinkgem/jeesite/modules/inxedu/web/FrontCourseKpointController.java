package com.thinkgem.jeesite.modules.inxedu.web;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.thinkgem.jeesite.common.utils.GsonUtils;
import com.thinkgem.jeesite.common.utils.ObjectUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inxedu.entity.course.Course;
import com.thinkgem.jeesite.modules.inxedu.entity.kpoint.CourseKpoint;
import com.thinkgem.jeesite.modules.inxedu.entity.kpoint.CourseKpointDto;
import com.thinkgem.jeesite.modules.inxedu.service.CourseDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.CourseKpointService;
import com.thinkgem.jeesite.modules.inxedu.service.WebsiteProfileDaoService;
import com.thinkgem.jeesite.modules.inxedu.utils.VideoPlayTokenUtils;
@Controller
@RequestMapping("${frontPath}/front")
public class FrontCourseKpointController extends BaseController{

	@Autowired
	private CourseKpointService courseKpointService;
	@Autowired
	private CourseDaoService courseDaoService;
	@Autowired
	private WebsiteProfileDaoService websiteProfileDaoService;
	//getKopintHtml
	@RequestMapping("/ajax/getKopintHtml")
	public ModelAndView getKopintHtml(@RequestParam("kpointId") Integer kpointId,@RequestParam("courseId") Integer courseId,HttpServletRequest request,HttpServletResponse response){
		ModelAndView view = new ModelAndView();
//		view.setViewName("modules/web/course/videocode");
		
		try {
			
			//知识点
			CourseKpoint  courseKpoint  = courseKpointService.queryCourseKpointById(kpointId);
			PrintWriter out = response.getWriter();
			if (ObjectUtils.isNull(courseKpoint)) {// 当传入数据不正确时直接返回
				out.println("<script>var noCover=true; dialog dialog('提示','参数错误！',1);</script>");
				return null;
			}
			view.addObject("courseKpoint", courseKpoint);
			
			//课程
			Course course = courseDaoService.queryCourseById(courseId);
			if (ObjectUtils.isEmpty(course)) {
				return view;
			}
			view.addObject("course", course);
			
			//类型
			String videoType = null;
			String videoUrl = null;
			String type = courseKpoint.getFileType();
			//**************************************视频类型****************************************************
			if ("VIDEO".equals(type)) {
				videoType = courseKpoint.getVideoType();
				videoUrl = courseKpoint.getVideoUrl();
				if ("INXEDUVIDEO".equalsIgnoreCase(videoType)) {//需要处理
					Map<String, Object> inxeduMap =websiteProfileDaoService.getWebsiteProfileByType_("INXEDUVIDEO");
//					Map<String, Object> inxeduMap_ = (Map<String, Object>) inxeduMap.get("INXEDUVIDEO");
					Map<String, Object> inxeduMap_ = GsonUtils.GsonToMaps(inxeduMap.get("INXEDUVIDEO").toString());
					String playUrl = "http://vod.baofengcloud.com/" + inxeduMap_.get("UserId") + "/player/cloud.swf";
					String url = "servicetype=1&uid=" + inxeduMap_.get("UserId") + "&fid=" + videoUrl;
					playUrl += "?" + url;
					// 如果因酷云的key不为空则按加密播放如果为空则不加密
					if (StringUtils.isNotEmpty(inxeduMap_.get("SecretKey").toString())&& StringUtils.isNotEmpty(inxeduMap_.get("AccessKey").toString())) {//{"AccessKey":"asf","SecretKey":"adf","UserId":"33197041"}
						String token = VideoPlayTokenUtils.createPlayToken(videoUrl, inxeduMap_.get("SecretKey").toString(), inxeduMap_.get("AccessKey").toString());
						playUrl += "&tk=" + token;
					}
					playUrl += "&auto=" + 1;
					videoUrl = playUrl;
					//http://vod.baofengcloud.com/33197041/player/cloud.swf?servicetype=1&uid=33197041&fid=C5AA1229B53780843FE4A38371334C96&tk=asf%3Adx7RqWfzzEK5ATrieS2fbSMsHq8%3D%3AaWQ9QzVBQTEyMjlCNTM3ODA4NDNGRTRBMzgzNzEzMzRDOTYmZGVhZGxpbmU9MTUxNTc0ODI2NyZlbmFibGVodG1sNT0x&auto=1
					//http://vod.baofengcloud.com/33197041/player/cloud.swf?servicetype=1&uid=33197041&fid=C5AA1229B53780843FE4A38371334C96&tk=asf%3AXsf21mW9ew5EmkBZ9%2FmaHHC554Q%3D%3AaWQ9QzVBQTEyMjlCNTM3ODA4NDNGRTRBMzgzNzEzMzRDOTYmZGVhZGxpbmU9MTUxNTc0OTE2NSZlbmFibGVodG1sNT0x&auto=1
				}
//				if ("uploadVideo".equalsIgnoreCase(videoType)) {//不处理
//					
//				}
				if ("CC".equalsIgnoreCase(videoType)) {//从数据库读取url
					Map<String, Object> ccwebsitemap = websiteProfileDaoService.getWebsiteProfileByType_("cc");//直接从表读取数据
					view.addObject("ccwebsitemap", ccwebsitemap);
				}
//				if ("IFRAME".equalsIgnoreCase(videoType)) {//不处理
//					
//				}
				view.addObject("videotype", videoType);
				view.addObject("videourl", videoUrl);
				view.setViewName("modules/web/course/videocode");
				return view;
			}
			//**************************************文本类型****************************************************
			if ("TEXT".equalsIgnoreCase(type)||"TXT".equalsIgnoreCase(type)) {
				view.setViewName("modules/web/playCourse/play_txt_ajax");
				return view;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return view;
	}
}
