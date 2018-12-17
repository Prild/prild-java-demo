/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.web;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.DateUtils;

/**
 * 控制器支持类
 * 
 * @author ThinkGem
 * @version 2013-3-23
 */
public abstract class BaseController {
	public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 管理基础路径
	 */
	@Value("${adminPath}")
	protected String adminPath;

	/**
	 * 前端基础路径
	 */
	@Value("${frontPath}")
	protected String frontPath;

	/**
	 * 前端URL后缀
	 */
	@Value("${urlSuffix}")
	protected String urlSuffix;

	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	//start:**********************验证相关********************************************

	/**
	 * 服务端参数有效性验证(object 验证的实体对象; groups 验证组;  验证成功：返回true、失败：将错误信息添加到message中 )
	 */
	protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
		try {
			BeanValidators.validateWithException(validator, object, groups);
		} catch (ConstraintViolationException ex) {
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(model, list.toArray(new String[] {}));
			return false;
		}
		return true;
	}
	//页面循环输出model信息
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		model.addAttribute("message", sb.toString());
	}
	/**
	 * 服务端参数有效性验证(object 验证的实体对象; groups 验证组;  验证成功：返回true、失败：将错误信息添加到 flash message中 )
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
		try {
			BeanValidators.validateWithException(validator, object, groups);
		} catch (ConstraintViolationException ex) {
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addFlashMessage(redirectAttributes, list.toArray(new String[] {}));
			return false;
		}
		return true;
	}
	//页面动画形式循环输出model信息
	protected void addFlashMessage(RedirectAttributes redirectAttributes, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}
	/**
	 * 服务端参数有效性验证(object 验证的实体对象; groups 验证组,不传入此参数时,同@Valid注解验证;验证成功：继续执行、验证失败：抛出异常跳转400页面)
	 */
	protected void beanValidator(Object object, Class<?>... groups) {
		BeanValidators.validateWithException(validator, object, groups);
	}

	//start:**********************客户端信息处理********************************************
	/**
	 * 设置ajax请返回结果
	 */
	public Map<String,Object> setJson(boolean success, String message, Object entity) {
		Map<String,Object> json = new HashMap<String,Object>();
		json.put("success", Boolean.valueOf(success));
		json.put("message", message);
		json.put("entity", entity);
		return json;
	}
	/**
	 * 对象转换成json字符串返回给客户端
	 */
	protected String renderString(HttpServletResponse response, Object object) {
		return renderString(response, JsonMapper.toJsonString(object), "application/json");
	}
	/**
	 * 返回客户端经过编码的字符串
	 */
	protected String renderString(HttpServletResponse response, String string, String type) {
		try {
			response.reset();
			response.setContentType(type);
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
			return null;
		} catch (IOException e) {
			return null;
		}
	}
	//end:**********************客户端信息处理*****************************************************

	
	
	//start:**********************异常处理***********************************
	//自动：参数绑定异常
	@ExceptionHandler({ BindException.class, ConstraintViolationException.class, ValidationException.class })
	public String bindException() {
		return "error/400";
	}
	//自动：授权登录异常
	@ExceptionHandler({ AuthenticationException.class })
	public String authenticationException() {
		return "error/403";
	}
	//手动设置：ajax请异常
	public Map<String,Object> setAjaxException(Map<String,Object> map){
		map.put("success", false);
		map.put("message", "系统繁忙，请稍后再操作！");
		map.put("entity", null);
		return map;
	}
	//end:**********************异常处理***********************************

	//start:*****************************@InitBinder*************************************************
	//分页
    @InitBinder({"page"})
    public void initBinderPage(WebDataBinder binder) {
      binder.setFieldDefaultPrefix("page.");
    }
    //double Date String Integer处理
	@InitBinder
	protected void initBinder(WebDataBinder binder) {

		binder.registerCustomEditor(Double.class, new DoubleEditor());//double

		binder.registerCustomEditor(Date.class, new DateEditor());// Date

		//初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
		binder.registerCustomEditor(String.class, new StringEditor());// String

		binder.registerCustomEditor(Integer.class, new IntegerEditor());// Integer
	}

	public class DoubleEditor extends PropertiesEditor {
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (text == null || text.equals("")) {
				text = "0";
			}
			setValue(Double.parseDouble(text));
		}

		@Override
		public String getAsText() {
			return getValue().toString();
		}
	}

	private class DateEditor extends PropertyEditorSupport {
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = null;
			try {
				date = format.parse(text);
			} catch (ParseException e) {
				format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					date = format.parse(text);
				} catch (ParseException e1) {
				}
			}
			setValue(date);
		}
	}

	// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
	public class StringEditor extends PropertiesEditor {
		@Override
		public void setAsText(String text) {
			setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
		}

		@Override
		public String getAsText() {
			Object value = getValue();
			return value != null ? value.toString() : "";
		}
	}

	public class IntegerEditor extends PropertiesEditor {
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (text == null || text.equals("")) {
				text = "0";
			}
			setValue(Integer.parseInt(text));
		}

		@Override
		public String getAsText() {
			return getValue().toString();
		}
	}
	//end:*****************************@InitBinder*************************************************

}
