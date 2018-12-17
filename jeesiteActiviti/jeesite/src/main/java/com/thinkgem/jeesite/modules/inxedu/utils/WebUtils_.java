package com.thinkgem.jeesite.modules.inxedu.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.WebUtils;

import com.thinkgem.jeesite.common.utils.CookieUtils;
import com.thinkgem.jeesite.common.utils.ValidatorsAll;

/**
 *  扩展spring webutils工具类
 * @author cwx
 *
 */
public class WebUtils_ extends WebUtils {
	
//start:*******************************验证*******************************************************
	/**
	 * 1.是否是邮箱 2.邮箱长度<=i
	 * 
	 * @param email
	 * @param i
	 *            总长度
	 * @return
	 */
	public static boolean checkEmail(String email, int i) {
		boolean isEmail = ValidatorsAll.isEmail(email);
		if (isEmail) {
			if (email.length() <= i) {
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * 是否是手机号
	 * 
	 * @param mobile
	 */
	public static boolean checkMobile(String mobile) {
		boolean isMobile = ValidatorsAll.isMobile(mobile);
		return isMobile;
	}

	/**
	 * 密码有字母和数字组合且≥6位≤16位
	 * 
	 * @param password
	 * @return
	 */
	public static boolean isPasswordAvailable(String password) {
		// TODO Auto-generated method stub
		boolean isPassword = ValidatorsAll.isPassword(password);
		return isPassword;
	}

//end:*******************************验证*******************************************************
	/**
	 * 设置cookie
	 * 
	 * @param response
	 * @param webUserLoginPrefix
	 *            名称
	 * @param uuid
	 *            值
	 * @param i
	 *            生存时间
	 */
	public static void setCookie(HttpServletResponse response, String webUserLoginPrefix, String uuid, int i) {
		CookieUtils.setCookie(response, webUserLoginPrefix, uuid, i);
	}

	/**
	 * 获得指定Cookie的值
	 * 
	 * @param request
	 * @param webUserLoginPrefix
	 *            指定名称
	 * @return
	 */

	public static String getCookie_(HttpServletRequest request, String webUserLoginPrefix) {
		return CookieUtils.getCookie(request, webUserLoginPrefix);

	}

}
