package com.thinkgem.jeesite.modules.inxedu.dao;


import java.util.List;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteCourse;
import com.thinkgem.jeesite.modules.inxedu.entity.website.WebsiteCourseDetailDTO;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

/**
 * WebsiteCourse管理接口
 * @author www.inxedu.com
 */
@MyBatisDao
public interface WebsiteCourseDao {

    /**
     * 推荐课程分类列表
     */
    public List<WebsiteCourse> queryWebsiteCourseList();

    /**
     * 查询推荐课程分类
     */
    public WebsiteCourse queryWebsiteCourseById(int id);
    /**
     * 修改推荐课程分类
     */
    public boolean updateWebsiteCourseById(WebsiteCourse websiteCourse);
    /**
     * 添加推荐课程分类
     */
    public boolean addWebsiteCourse(WebsiteCourse websiteCourse);

    /**
     * 删除推荐课程分类及分类下推荐课程
     */
    public boolean deleteWebsiteCourseDetailById(int id);
}