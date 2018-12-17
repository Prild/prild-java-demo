package com.thinkgem.jeesite.common.constants;

import com.thinkgem.jeesite.common.utils_.PropertyUtil;


/**
 * @description cache缓存相关常量
 * @author www.inxedu.com
 */
public class CacheConstans {
	/** memtimes.properties中配置这些常量 **/
	public static PropertyUtil webPropertyUtil = PropertyUtil.getInstance("memtimes");
	
	/** ->inxedu **/
	public static final String MEMFIX = webPropertyUtil.getProperty("memfix");
	
	/** inxedurecommend_course **/
	public static final String RECOMMEND_COURSE = MEMFIX + "recommend_course";
	
	/** 3600秒 1h **/
	public static final int RECOMMEND_COURSE_TIME = Integer.parseInt(webPropertyUtil.getProperty("RECOMMEND_COURSE_TIME"));
	
	/** inxedubanner_images **/
	public static final String BANNER_IMAGES = MEMFIX + "banner_images";
	
	/** 21600秒 6h **/
	public static final int BANNER_IMAGES_TIME = Integer.parseInt(webPropertyUtil.getProperty("BANNER_IMAGES_TIME"));
	
	/** inxeduwebsite_profile **/
	public static final String WEBSITE_PROFILE = MEMFIX + "website_profile";
	
	/** 21600秒 6h **/
	public static final int WEBSITE_PROFILE_TIME = Integer.parseInt(webPropertyUtil.getProperty("WEBSITE_PROFILE_TIME"));
	
	/** inxeduwebsite_navigate **/
	public static final String WEBSITE_NAVIGATE = MEMFIX + "website_navigate";
	
	/** 43200秒 12h **/
	public static final int WEBSITE_NAVIGATE_TIME = Integer.parseInt(webPropertyUtil.getProperty("WEBSITE_NAVIGATE_TIME"));
	
	/** inxeduindex_student_dynamic **/
	public static final String INDEX_STUDENT_DYNAMIC = MEMFIX + "index_student_dynamic";

	/** 前台登录用户ehcache前缀 inxeduweb_user_login_ */
	public static final String WEB_USER_LOGIN_PREFIX = MEMFIX + "web_user_login_";//inxeduweb_user_login_
	
	/** 前台登录用户缓存6小时  **/
	public static final int USER_TIME = Integer.parseInt(webPropertyUtil.getProperty("USER_TIME"));
	
	/** 记录当前用户当前的登录时间，下次登录时会更新此缓存  inxeduUSER_CURRENT_LOGINTIME_ **/
	public static final String USER_CURRENT_LOGINTIME = MEMFIX+"USER_CURRENT_LOGINTIME_";

	/** 缓存后台登录用户ehcache前缀  inxedulogin_sys_user_*/
	public static final String LOGIN_MEMCACHE_PREFIX = MEMFIX + "login_sys_user_";
	
	/** 后台所有用户权限缓存名前缀  inxeduSYS_USER_ALL_FUNCTION_**/
	public static final String SYS_ALL_USER_FUNCTION_PREFIX = MEMFIX + "SYS_USER_ALL_FUNCTION_";
	
	/** 登录用户权限缓存名前缀 inxeduUSER_ALL_FUNCTION **/
	public static final String USER_FUNCTION_PREFIX = MEMFIX + "USER_ALL_FUNCTION";
	
	/** 前台首页 网校名师 缓存  inxeduINDEX_TEACHER_RECOMMEND**/
	public static final String INDEX_TEACHER_RECOMMEND = MEMFIX + "INDEX_TEACHER_RECOMMEND";
	
	/** 文章 好文推荐 缓存  inxeduARTICLE_GOOD_RECOMMEND**/
	public static final String ARTICLE_GOOD_RECOMMEND = MEMFIX + "ARTICLE_GOOD_RECOMMEND";
	
	/** 问答 热门问答推荐 缓存  inxeduQUESTIONS_HOT_RECOMMEND**/
	public static final String QUESTIONS_HOT_RECOMMEND = MEMFIX + "QUESTIONS_HOT_RECOMMEND";

	/** 网站统计 inxeduweb_statistics*/
	public static final String WEB_STATISTICS = MEMFIX + "web_statistics";
	
	/** 网站最近30条活跃统计 inxeduweb_statistics_thirty*/
	public static final String WEB_STATISTICS_THIRTY = MEMFIX + "web_statistics_thirty";
	
	/** 缓存1小时  */
	public static final int WEB_STATISTICS_TIME = Integer.parseInt(webPropertyUtil.getProperty("WEB_STATISTICS_TIME"));

	/** 后台统计 inxeduWEB_COUNT*/
	public static final String WEB_COUNT = MEMFIX + "WEB_COUNT";
	
	/** 缓存1小时 */
	public static final int WEB_COUNT_TIME = Integer.parseInt(webPropertyUtil.getProperty("WEB_STATISTICS_TIME"));

	/**帮助页面左侧菜单key:inxeduhelp_center **/
	public static final String HELP_CENTER = MEMFIX + "help_center";
	
	/** 缓存1小时; **/
	public static final int HELP_CENTER_TIME = Integer.parseInt(webPropertyUtil.getProperty("HELP_CENTER_TIME"));
}
