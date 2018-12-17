package com.thinkgem.jeesite.common.constants;

import com.thinkgem.jeesite.common.utils_.PropertyUtil;

//import com.inxedu.os.common.util.PropertyUtil;

/**
 * 常量  // PropertyUtil 属性文件读写工具类
 * 
 * @author www.inxedu.com
 */
public class CommonConstants {

	public static String propertyFile = "jeesite";// jeesite.properties
	
	public static PropertyUtil propertyUtil = PropertyUtil.getInstance(propertyFile);
	public static String contextPath = propertyUtil.getProperty("contextPath");// http://127.0.0.1:18080/jeesite
	public static String staticServer = propertyUtil.getProperty("contextPath");// http://127.0.0.1:18080/jeesite
	public static String uploadImageServer = propertyUtil.getProperty("contextPath");// http://127.0.0.1:18080/jeesite
	public static String staticImage = propertyUtil.getProperty("contextPath");// http://127.0.0.1:18080/jeesite
	public static String projectName = propertyUtil.getProperty("projectName");// jeesite
	public static final String MYDOMAIN = propertyUtil.getProperty("mydomain");// Cookie域   配置文件没有配置

	/** 邮箱正则表达式 */
	public static String emailRegex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	/** 电话号码正则表达式 */
	public static String telRegex = "^1[0-9]{10}$";
	/** 后台用户登录名正则表达式 */
	public static String loginRegex = "^(?=.*[a-zA-Z])[a-zA-Z0-9]{6,20}$";
	/** 图片验证码Session的K */
	public static final String RAND_CODE = "COMMON_RAND_CODE";
}
