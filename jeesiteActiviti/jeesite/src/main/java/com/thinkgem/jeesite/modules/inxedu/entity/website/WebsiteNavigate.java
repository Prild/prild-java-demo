package com.thinkgem.jeesite.modules.inxedu.entity.website;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 导航
 * @author www.inxedu.com
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WebsiteNavigate extends DataEntity<WebsiteNavigate> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id_;
	/**
	 * 导航的名称
	 */
	private String name;
	/**
	 * 跳转链接
	 */
	private String url;
	/**
	 * 是否在新页面打开0是1否
	 */
	private int newPage;
	/**
	 *  INDEX首页、USER个人中心、FRIENDLINK尾部友链、TAB尾部标签
	 */
	private String type;
	/**
	 * 显示排序
	 */
	private int orderNum;
	/**
	 * 0正常1冻结
	 */
	private int status ;
	public int getId_() {
		return id_;
	}
	public void setId_(int id_) {
		this.id_ = id_;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getNewPage() {
		return newPage;
	}
	public void setNewPage(int newPage) {
		this.newPage = newPage;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
