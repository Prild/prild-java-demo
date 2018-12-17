package com.thinkgem.jeesite.modules.inxedu.interceptor;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.thinkgem.jeesite.common.constants.WebSiteProfileType;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.GsonUtils;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteImages;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteNavigate;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteProfile;
import com.thinkgem.jeesite.modules.inxedu.service.WebsiteImagesDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.WebsiteNavigateDaoService;
import com.thinkgem.jeesite.modules.inxedu.service.WebsiteProfileDaoService;


public class LimitIntercepterForWebsite extends HandlerInterceptorAdapter {
	 Logger logger = LoggerFactory.getLogger(LimitIntercepterForWebsite.class);
	 @Autowired
	 private WebsiteProfileDaoService websiteProfileDaoService;
	 @Autowired
	 private WebsiteImagesDaoService websiteImagesDaoService;
	 @Autowired
	 private WebsiteNavigateDaoService websiteNavigateDaoService;
	/**
	 * preHandle方法是进行处理器拦截用的，顾名思义，该方法将在Controller处理之前进行调用，
	 * SpringMVC中的Interceptor拦截器是链式的，可以同时存在
	 * 多个Interceptor，然后SpringMVC会根据声明的前后顺序一个接一个的执行
	 * ，而且所有的Interceptor中的preHandle方法都会在
	 * Controller方法调用之前调用。SpringMVC的这种Interceptor链式结构也是可以进行中断的
	 * ，这种中断方式是令preHandle的返 回值为false，当preHandle的返回值为false的时候整个请求就结束了。
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
    		// 获得banner图
			Map<String, List<WebsiteImages>> websiteImages = websiteImagesDaoService.queryImagesByType_();
			request.setAttribute("websiteImages", websiteImages);
			// 获得网站配置
			Map<String,Object>  websitemap = websiteProfileDaoService.getWebsiteProfileByType_(WebSiteProfileType.web.toString());
			request.setAttribute("websitemap", websitemap);
			// 获得LOGO配置
			Map<String,Object>  logomap = websiteProfileDaoService.getWebsiteProfileByType_(WebSiteProfileType.logo.toString());
			request.setAttribute("logomap",  logomap);
			// 网站统计代码
			Map<String,Object>  tongjiemap = websiteProfileDaoService.getWebsiteProfileByType_(WebSiteProfileType.censusCode.toString());
			request.setAttribute("tongjiemap", tongjiemap);
			// 咨询链接
			Map<String,Object>  onlinemap = websiteProfileDaoService.getWebsiteProfileByType_(WebSiteProfileType.online.toString());
			request.setAttribute("onlinemap", onlinemap);

			// 网站导航配置
			Map<String,Object>  navigatemap = websiteNavigateDaoService.getWebNavigate_();
			request.setAttribute("navigatemap", navigatemap);
		} catch (Exception e) {
			logger.error("LimitIntercepterForWebsite.preHandle 网站配置出错", e);
		}

		return super.preHandle(request, response, handler);
	}

	/**
	 * 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行。该方法将在整个请求完成之后，
	 * 也就是DispatcherServlet渲染了视图执行，
	 * 这个方法的主要作用是用于清理资源的，当然这个方法也只能在当前这个Interceptor的preHandle方法的返回值为true时才会执行。
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// TODO Auto-generated method stub
		super.afterCompletion(request, response, handler, ex);
	}

	/**
	 * 这个方法只会在当前这个Interceptor的preHandle方法返回值为true的时候才会执行。postHandle是进行处理器拦截用的，
	 * 它的执行时间是在处理器进行处理之
	 * 后，也就是在Controller的方法调用之后执行，但是它会在DispatcherServlet进行视图的渲染之前执行
	 * ，也就是说在这个方法中你可以对ModelAndView进行操
	 * 作。这个方法的链式结构跟正常访问的方向是相反的，也就是说先声明的Interceptor拦截器该方法反而会后调用
	 * ，这跟Struts2里面的拦截器的执行过程有点像，
	 * 只是Struts2里面的intercept方法中要手动的调用ActionInvocation的invoke方法
	 * ，Struts2中调用ActionInvocation的invoke方法就是调用下一个Interceptor
	 * 或者是调用action，然后要在Interceptor之前调用的内容都写在调用invoke之前
	 * ，要在Interceptor之后调用的内容都写在调用invoke方法之后。
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}
}
