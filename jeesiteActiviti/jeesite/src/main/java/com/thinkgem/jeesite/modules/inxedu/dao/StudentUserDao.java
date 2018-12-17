package com.thinkgem.jeesite.modules.inxedu.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.QueryStudentUser;
import com.thinkgem.jeesite.modules.inxedu.entity.StudentUser;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

@MyBatisDao
public interface StudentUserDao {
	/**
	 * 创建用户 返回用户ID
	 */
	public int createStudentUser(StudentUser user);

	/**
	 * 通过用户ID查询用户
	 */
	public StudentUser queryUserById(Integer userId);

	/**
	 * 检测手机是否存在
	 */
	public boolean checkMobile(@Param("mobile") String mobile);

	/**
	 * 检测邮箱号是否存在
	 */
	public boolean checkEmail(String email);

	/**
	 * 获取登录用户 帐号（邮箱或手机号）
	 */
	public StudentUser getLoginUser(@Param("account") String account, @Param("password") String password);

	/**
	 * 修改用户密码
	 */
	public boolean updateUserPwd(@Param("e")StudentUser user);

	/**
	 * 分页查询用户
	 */
	public List<StudentUser> queryUserListPage(@Param("e") QueryStudentUser query);

	/**
	 * 冻结或解冻用户
	 */
	public boolean updateUserStates(StudentUser user);

	/**
	 * 修改用户信息
	 */
	public void updateStudentUser(StudentUser user);

	/**
	 * 修改用户头像
	 */
	public void updateImg(StudentUser user);

	/**
	 * 查询所有学员记录数
	 * 
	 * @return 返回所有的记录数
	 */
	public int queryAllUserCount();

	/**
	 * 通过手机号或邮箱号查询用户信息
	 */
	public StudentUser queryUserByEmailOrMobile(String emailOrMobile);

	/**
	 * 根据用户uid获取用户信息
	 */
	public Map<String, StudentUser> getUserExpandByUids(String uids) throws Exception;

	/**
	 * 通过集合cusId 查询customer 返回map
	 */
	public Map<String, StudentUser> queryCustomerInCusIds(List<Long> cusIds) throws Exception;

	/**
	 * 通过标识更新未读数加一
	 */
	public void updateUnReadMsgNumAddOne(@Param("falg") String falg, @Param("cusId") Integer cusId);// 使用这种方式传参更直观，取代map

	/**
	 * 通过标识更新未读数清零
	 */
	public void updateUnReadMsgNumReset(@Param("falg") String falg, @Param("cusId") Integer cusId);

	/**
	 * 更新用户的上传统计系统消息时间
	 */
	public void updateCusForLST(@Param("cusId") Integer cusId, @Param("date") Date date);

	/**
	 * excel批量开通用户 shanXinYing
	 */
	public String updateImportExcel(HttpServletRequest request, MultipartFile myFile, Integer mark) throws Exception;

	/**
	 * 缓存用户信息
	 * 
	 * @param request
	 * @param userId
	 * @param autoThirty
	 *            是否自动登录
	 */
	public void setLoginInfo(HttpServletRequest request, Integer userId, String autoThirty);

	/**
	 * 根据条件获取User列表 带课程名称
	 */
	public List<StudentUser> getUserListPage(StudentUser user, PageEntity page);

}
