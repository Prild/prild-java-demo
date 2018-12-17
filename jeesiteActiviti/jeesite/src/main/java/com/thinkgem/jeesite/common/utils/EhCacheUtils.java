/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Cache工具类
 * 
 * @author ThinkGem
 * @version 2013-5-29
 */
public class EhCacheUtils {

	private static CacheManager cacheManager = ((CacheManager) SpringContextHolder.getBean("cacheManager"));

	private static final String SYS_CACHE = "sysCache";

	// start:***********************************************************************************
	// 从ehcache的指定容器中取值
	public static Object get(String key) {
		return get(SYS_CACHE, key);
	}

	/**
	 * 从ehcache的指定容器中取值
	 * 
	 * @param cacheName
	 *            容器名
	 * @param key
	 *            键
	 * @return
	 */
	public static Object get(String cacheName, String key) {
		Element element = getCache(cacheName).get(key);
		return element == null ? null : element.getObjectValue();
	}

	// end:***********************************************************************************

	// start:***********************************************************************************
	// 向指定容器中设置值
	public static void put(String key, Object value) {
		put(SYS_CACHE, key, value);
	}

	/**
	 * 向指定容器中设置值
	 * 
	 * @param cacheName
	 *            容器名
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public static void put(String cacheName, String key, Object value) {
		Element element = new Element(key, value);
		getCache(cacheName).put(element);
	}

	// end:***********************************************************************************

	// start:***********************************************************************************
	// 向指定容器中设置值,并设置存活时间(如果cache/ehcache-local.xml配置过,这里配置会报警告,并且无效)
	/**
	 * 设置缓存
	 * @param key 
	 * @param value
	 * @param timeToLiveSeconds 存活时间
	 */
	public static void set(String key, Object value, Integer timeToLiveSeconds) {
		set(SYS_CACHE, key, value, timeToLiveSeconds);
	}

	public static void set(String cacheName, String key, Object value, Integer timeToLiveSeconds) {
		// Element element = new Element(key, value);
		// key value 是否会死亡 缓存的间隔时间 缓存存活时间
		Element element = new Element(key, value, timeToLiveSeconds);
		getCache(cacheName).put(element);
	}

	// end:***********************************************************************************

	// start:***********************************************************************************
	// 删除EHCache容器中的元素
	public static void remove(String key) {
		remove(SYS_CACHE, key);
	}

	/**
	 * 删除EHCache容器中的元素
	 * 
	 * @param cacheName
	 *            容器名
	 * @param key
	 *            键
	 */
	public static void remove(String cacheName, String key) {
		getCache(cacheName).remove(key);
	}

	// end:***********************************************************************************

	/**
	 * 根据cacheName获得一个Cache，没有则创建一个。
	 */
	private static Cache getCache(String cacheName) {// 公共方法
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			cacheManager.addCache(cacheName);
			cache = cacheManager.getCache(cacheName);
			cache.getCacheConfiguration().setEternal(true);
		}
		return cache;
	}

	public static CacheManager getCacheManager() {
		return cacheManager;
	}
}
