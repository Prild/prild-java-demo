package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.WebsiteNavigateDao;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteNavigate;

@Service
@Transactional
public class WebsiteNavigateDaoService implements WebsiteNavigateDao {
	@Autowired
	private WebsiteNavigateDao websiteNavigateDao;

	@Override
	public List<WebsiteNavigate> getWebsiteNavigate(WebsiteNavigate websiteNavigate) {
		// TODO Auto-generated method stub
		return websiteNavigateDao.getWebsiteNavigate(websiteNavigate);
	}

	@Override
	public boolean addWebsiteNavigate(WebsiteNavigate websiteNavigate) {
		// TODO Auto-generated method stub
		return websiteNavigateDao.addWebsiteNavigate(websiteNavigate);
	}

	@Override
	public boolean freezeWebsiteNavigate(WebsiteNavigate websiteNavigate) {
		// TODO Auto-generated method stub
		return websiteNavigateDao.freezeWebsiteNavigate(websiteNavigate);
	}

	@Override
	public boolean delWebsiteNavigate(int id) {
		// TODO Auto-generated method stub
		return websiteNavigateDao.delWebsiteNavigate(id);
	}

	@Override
	public boolean updateWebsiteNavigate(WebsiteNavigate websiteNavigate) {
		// TODO Auto-generated method stub
		return websiteNavigateDao.updateWebsiteNavigate(websiteNavigate);
	}

	@Override
	public WebsiteNavigate getWebsiteNavigateById(int id) {
		// TODO Auto-generated method stub
		return websiteNavigateDao.getWebsiteNavigateById(id);
	}

	@Override
	public List<WebsiteNavigate> getWebNavigate() {
		// TODO Auto-generated method stub
		return websiteNavigateDao.getWebNavigate();
	}

	public Map<String, Object> getWebNavigate_() {
		Map<String, Object> map = new HashMap<>();
		// 根据type对应的list放入map中
		List<WebsiteNavigate> list = getWebNavigate();
		List<Object> list_ = new ArrayList<Object>();
		List<Object> list_1 = new ArrayList<Object>();
		List<Object> list_2 = new ArrayList<Object>();
		for (WebsiteNavigate navigate : list) {
			if (navigate.getType().equals("INDEX")) {
				list_.add(navigate);
			}
			if (navigate.getType().equals("FRIENDLINK")) {
				list_1.add(navigate);
			}
			if (navigate.getType().equals("TAB")) {
				list_2.add(navigate);
			}
		}
		map.put("INDEX", list_);
		map.put("FRIENDLINK", list_1);
		map.put("TAB", list_2);
		return map;
	}

}
