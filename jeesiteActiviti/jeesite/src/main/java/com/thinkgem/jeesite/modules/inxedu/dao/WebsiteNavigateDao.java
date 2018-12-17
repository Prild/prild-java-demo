package com.thinkgem.jeesite.modules.inxedu.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteNavigate;

/**
 * WebsiteNavigateTbl管理接口
 * @author www.inxedu.com
 */
@MyBatisDao
public interface WebsiteNavigateDao {
	
	/**
	 * 导航列表
	 */
	public List<WebsiteNavigate> getWebsiteNavigate(WebsiteNavigate websiteNavigate);
	/**
	 * 添加导航
	 */
	public boolean addWebsiteNavigate(WebsiteNavigate websiteNavigate);
	/**
	 * 冻结、解冻导航
	 */
	public boolean freezeWebsiteNavigate(WebsiteNavigate websiteNavigate);
	/**
	 * 删除导航
	 */
	public boolean delWebsiteNavigate(int id);
	/**
	 * 更新导航
	 */
	public boolean updateWebsiteNavigate(WebsiteNavigate websiteNavigate);
	/**
	 * id查询导航
	 */
	public WebsiteNavigate getWebsiteNavigateById(int id);
	/**
	 * 查询未冻结的导航列表
	 */
	public List<WebsiteNavigate> getWebNavigate();
	
}