package com.thinkgem.jeesite.modules.inxedu.dao;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface CourseTeacherDao  {
	   /**
     * 添加课程与讲师的关联数
     */
    public boolean addCourseTeacher(String value);
    
    /**
     * 删除课程与讲师的关联数据
     * @param courseId 课程ID
     */
    public boolean deleteCourseTeacher(int courseId);
    
}
