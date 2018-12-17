package com.thinkgem.jeesite.modules.inxedu.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteProfile;

/**
 * 网站配置
 * @author www.inxedu.com
 */
@MyBatisDao
public interface WebsiteProfileDao {
	/**
	 * 根据type查询网站配置
	 */
	public WebsiteProfile getWebsiteProfileByType(@Param("type")String type);
	/**
	 * 添加查询网站配置
	 */
	public boolean addWebsiteProfileByType(WebsiteProfile websiteProfile);
	/**
	 * 更新网站配置管理
	 */
	public boolean updateWebsiteProfile(@Param("e")WebsiteProfile websiteProfile);

	/**
	 * 查询网站配置
	 */
	public List<WebsiteProfile> getWebsiteProfileList();
}