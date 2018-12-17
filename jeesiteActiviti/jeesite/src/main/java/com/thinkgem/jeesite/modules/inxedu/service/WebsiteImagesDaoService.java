package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.constants.CacheConstans;
import com.thinkgem.jeesite.common.utils.EhCacheUtils;
import com.thinkgem.jeesite.modules.inxedu.dao.WebsiteImagesDao;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteImages;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

@Service
@Transactional
public class WebsiteImagesDaoService implements WebsiteImagesDao{
@Autowired
private WebsiteImagesDao websiteImagesDao;
	@Override
	public int creasteImage(WebsiteImages image) {
		// TODO Auto-generated method stub
		return websiteImagesDao.creasteImage(image);
	}

	@Override
	public List<Map<String, Object>> queryImagePage(WebsiteImages image, PageEntity page) {
		// TODO Auto-generated method stub
		return websiteImagesDao.queryImagePage(image, page);
	}

	@Override
	public WebsiteImages queryImageById(int imageId) {
		// TODO Auto-generated method stub
		return websiteImagesDao.queryImageById(imageId);
	}

	@Override
	public boolean deleteImages(String imageIds) {
		return websiteImagesDao.deleteImages(imageIds);
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateImage(WebsiteImages image) {
		return websiteImagesDao.updateImage(image);
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<WebsiteImages> queryImagesByType() {
		// TODO Auto-generated method stub
		return websiteImagesDao.queryImagesByType();
	}
	public Map<String,List<WebsiteImages>> queryImagesByType_() {
		//从缓存中查询图片数据
		@SuppressWarnings("unchecked")
		Map<String,List<WebsiteImages>> imageMapList = (Map<String,List<WebsiteImages>>) EhCacheUtils.get(CacheConstans.BANNER_IMAGES);
		//如果缓存中不存在则重新查询
		if(imageMapList==null){
			List<WebsiteImages> imageList = websiteImagesDao.queryImagesByType();
			if(imageList!=null && imageList.size()>0){
				imageMapList = new HashMap<String, List<WebsiteImages>>();
//				将表中type_id进行相同的数分组，并给于key，比如：type_1->type_id=1的list type_11->type_id=11的list type_12->type_id=12的list
				List<WebsiteImages> _list = new ArrayList<WebsiteImages>();//
				int typeId =imageList.get(0).getTypeId();
				for(WebsiteImages _wi : imageList){
					if(typeId==_wi.getTypeId()){
						_list.add(_wi);//
					}else{
						imageMapList.put("type_"+typeId, _list);//{type_1=[WebsiteImages(imageId=274, imagesUr。。)],type_2=[]...}
						 _list = new ArrayList<WebsiteImages>();
						 _list.add(_wi);
					}
					typeId = _wi.getTypeId();
				}
				//添加最后一条记录
				imageMapList.put("type_"+typeId, _list);//type_17-> type_id=17的所有行组成的list
				EhCacheUtils.set(CacheConstans.BANNER_IMAGES, imageMapList,CacheConstans.BANNER_IMAGES_TIME);
			}
		}
		return imageMapList;
	}
	
}
