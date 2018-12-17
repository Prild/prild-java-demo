package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.WebsiteCourseDao;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteCourse;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteCourseDetailDTO;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;
@Service
@Transactional
public class WebsiteCourseDaoService implements WebsiteCourseDao{
@Autowired
private WebsiteCourseDao websiteCourseDao;
	@Override
	public List<WebsiteCourse> queryWebsiteCourseList() {
		// TODO Auto-generated method stub
		return websiteCourseDao.queryWebsiteCourseList();
	}

	@Override
	public WebsiteCourse queryWebsiteCourseById(int id) {
		// TODO Auto-generated method stub
		return websiteCourseDao.queryWebsiteCourseById(id);
	}

	@Override
	public boolean updateWebsiteCourseById(WebsiteCourse websiteCourse) {
		return websiteCourseDao.updateWebsiteCourseById(websiteCourse);
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addWebsiteCourse(WebsiteCourse websiteCourse) {
		return websiteCourseDao.addWebsiteCourse(websiteCourse);
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean deleteWebsiteCourseDetailById(int id) {
		return websiteCourseDao.deleteWebsiteCourseDetailById(id);
		// TODO Auto-generated method stub
		
	}

}
