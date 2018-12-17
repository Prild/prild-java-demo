package com.thinkgem.jeesite.modules.inxedu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.course.CourseStudyhistory;


/**
 * CourseStudyhistory管理接口
 */
@MyBatisDao
public interface CourseStudyhistoryDao {

    /**
     * 添加CourseStudyhistory
     */
    public int addCourseStudyhistory(CourseStudyhistory courseStudyhistory);


    /**
     * 根据id删除一个CourseStudyhistory
     */
    public boolean deleteCourseStudyhistoryById(int id);

    /**
     * 修改CourseStudyhistory
     */
    public boolean updateCourseStudyhistory(CourseStudyhistory courseStudyhistory);

    /**
     * 根据条件获取CourseStudyhistory列表
     */
    public List<CourseStudyhistory> getCourseStudyhistoryList(@Param("e")CourseStudyhistory courseStudyhistory);
    
    
	/**
	 * 查看 课程下的 所有的学习记录
	 */
	public List<CourseStudyhistory> getCourseStudyhistoryListByCouId(int courseId);
	
	/**
	 * 查看 课程下的学习记录 总人数
	 */
	public int getCourseStudyhistoryCountByCouId(int courseId);
    
}
