package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.WebsiteCourseDetailDao;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteCourseDetail;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteCourseDetailDTO;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

@Service
@Transactional
public class WebsiteCourseDetailService implements WebsiteCourseDetailDao{
@Autowired
private WebsiteCourseDetailDao websiteCourseDetailDao;
	@Override
	public boolean createWebsiteCourseDetail(String detail) {
		return websiteCourseDetailDao.createWebsiteCourseDetail(detail);
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<WebsiteCourseDetailDTO> queryCourseDetailPage(WebsiteCourseDetailDTO dto) {
		// TODO Auto-generated method stub
		return websiteCourseDetailDao.queryCourseDetailPage(dto);
	}

	@Override
	public boolean deleteDetailById(int id) {
		return websiteCourseDetailDao.deleteDetailById(id);
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateSort(int id,int sort) {
		return websiteCourseDetailDao.updateSort(id,sort);
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<WebsiteCourseDetail> queryDetailListByrecommendId(int recommendId) {
		// TODO Auto-generated method stub
		return websiteCourseDetailDao.queryDetailListByrecommendId(recommendId);
	}

}
