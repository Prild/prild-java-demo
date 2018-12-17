package com.thinkgem.jeesite.modules.inxedu.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.kpoint.CourseKpoint;
import com.thinkgem.jeesite.modules.inxedu.entity.kpoint.CourseKpointDto;


/**
 * CourseKpoint管理接口
 * @author www.inxedu.com
 */
@MyBatisDao
public interface CourseKpointDao {
    /**
     * 添加视频节点(一个课程对应多个视频节点，并且视频节点必须依赖课程而存在的)
     */
    public int addCourseKpoint(CourseKpoint courseKpoint);
    
    /**
     * 通过课程ID，查询课程所属视频
     * @param courseId 课程ID
     * @return List<CourseKpoint>
     */
    public List<CourseKpoint> queryCourseKpointByCourseId(int courseId);
    
    /**
     * 通过ID，查询视频详情
     * @param kpointId 视频ID
     * @return CourseKpointDto
     */
    public CourseKpointDto queryCourseKpointById(int kpointId);
    
    /**
     * 修改视频节点
     * @param kpoint
     */
    public boolean updateKpoint(CourseKpoint kpoint);
    
    /**
     * 删除视频节点
     * @param ids ID串
     */
    public boolean deleteKpointByIds(String ids);
    
    /**
     * 修改视频节点父ID
     * @param map
     */
//    public boolean updateKpointParentId(Map<String,Integer> map);
    public boolean updateKpointParentId(int kpointId, int parentId);
    
    /**
     * 获取课程的 二级视频节点总数(只支持二级)
     */
    public int getSecondLevelKpointCount(@Param("value")Long courseId);

}