package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.WebsiteProfileDao;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteImages;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteProfile;
@Service
@Transactional
public class WebsiteProfileDaoService implements WebsiteProfileDao{

	@Autowired
	private WebsiteProfileDao websiteProfileDao;
	

	@Override
	public  WebsiteProfile  getWebsiteProfileByType(@Param("type")String type) {
		return websiteProfileDao.getWebsiteProfileByType(type);
	}
	/**
	 * 根据类型从edu_website_profile表中获取管理员配置的值
	 */
	public Map<String,Object>  getWebsiteProfileByType_(String type) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(type, getWebsiteProfileByType(type).getDesciption());
		return map;
	}

	
	@Override
	public boolean addWebsiteProfileByType(WebsiteProfile websiteProfile) {
		return websiteProfileDao.addWebsiteProfileByType(websiteProfile);
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateWebsiteProfile(@Param("e")WebsiteProfile websiteProfile) {
		return websiteProfileDao.updateWebsiteProfile(websiteProfile);
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<WebsiteProfile> getWebsiteProfileList() {
		// TODO Auto-generated method stub
		return websiteProfileDao.getWebsiteProfileList();
	}
	
	public Map<String, List<WebsiteImages>> getWebsiteProfileList_() {
		return null;
		// TODO Auto-generated method stub
	}

}
