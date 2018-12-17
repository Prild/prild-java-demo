package com.thinkgem.jeesite.modules.inxedu.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteImages;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;


/**
 *广告图dao层接口
 *@author www.inxedu.com
 */
@MyBatisDao
public interface WebsiteImagesDao {
	/**
	 * 添加图片
	 * @param image
	 * @return 返回图片ID
	 */
	public int creasteImage(WebsiteImages image);
	
	/**
	 * 分页查询广告图片
	 * @param image 查询条件
	 * @param page 分页条件
	 * @return List<WebsiteImages>
	 */
	public List<Map<String,Object>> queryImagePage(@Param("e")WebsiteImages image,@Param("page")PageEntity page);
	
	/**
	 * 通过图片ID，查询图片详情信息
	 * @param imageId
	 * @return
	 */
	public WebsiteImages queryImageById(int imageId);
	
	/**
	 * 删除图片
	 * @param imageIds
	 */
	public boolean deleteImages(String imageIds);
	
	/**
	 * 修改图片
	 * @param image
	 */
	public boolean updateImage(WebsiteImages image);
	
	/**
	 * 查询所有图片
	 * @return List<WebsiteImages>
	 */
	public List<WebsiteImages> queryImagesByType();
}
