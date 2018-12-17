package com.thinkgem.jeesite.modules.inxedu.service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.thinkgem.jeesite.common.constants.CacheConstans;
import com.thinkgem.jeesite.common.utils.EhCacheUtils;
import com.thinkgem.jeesite.common.utils.MD5Utiles_;
import com.thinkgem.jeesite.common.utils.ValidatorsAll;
import com.thinkgem.jeesite.modules.inxedu.dao.StudentUserDao;
import com.thinkgem.jeesite.modules.inxedu.entity.QueryStudentUser;
import com.thinkgem.jeesite.modules.inxedu.entity.StudentUser;
import com.thinkgem.jeesite.modules.inxedu.utils.WebUtils_;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

/**
 * 前台用户
 * 
 * @author www.inxedu.com
 * 
 */
@Service
@Transactional
public class StudentUserService implements StudentUserDao {
	@Autowired
	private StudentUserDao studentUserDao;

	/**
	 * 创建用户
	 * 
	 * @return 返回用户ID
	 */
	@Override
	public int createStudentUser(StudentUser user) {
		return studentUserDao.createStudentUser(user);
	}

	/**
	 * 通过用户ID查询用户
	 */
	@Override
	public StudentUser queryUserById(Integer userId) {
		return studentUserDao.queryUserById(userId);
	}

	/**
	 * 检测手机是否存在
	 */
	@Override
	public boolean checkMobile(String mobile) {
		return studentUserDao.checkMobile(mobile);
	}

	/**
	 * 检测邮箱号是否存在
	 */
	@Override
	public boolean checkEmail(String email) {
		return studentUserDao.checkEmail(email);
	}

	/**
	 * 获取登录用户(邮箱或手机号)
	 */
	@Override
	public StudentUser getLoginUser(String account, String password) {
		return studentUserDao.getLoginUser(account, password);
	}

	/**
	 * 修改用户密码
	 */
	@Override
	public boolean updateUserPwd(StudentUser user) {
		return studentUserDao.updateUserPwd(user);
	}

	/**
	 * 分页查询用户
	 */
	@Override
	public List<StudentUser> queryUserListPage(@Param("e") QueryStudentUser query) {
		return studentUserDao.queryUserListPage(query);
	}

	/**
	 * 冻结或解冻用户
	 */
	@Override
	public boolean updateUserStates(StudentUser user) {
		return studentUserDao.updateUserStates(user);
	}

	/**
	 * 修改用户信息
	 */
	@Override
	public void updateStudentUser(StudentUser user) {
		studentUserDao.updateStudentUser(user);
	}

	/**
	 * 修改用户头像
	 */
	@Override
	public void updateImg(StudentUser user) {
	}

	/**
	 * 查询所有学员记录数
	 * 
	 * @return 返回所有的记录数
	 */
	@Override
	public int queryAllUserCount() {
		return studentUserDao.queryAllUserCount();
	}

	/**
	 * 通过手机号或邮箱号查询用户信息
	 */
	@Override
	public StudentUser queryUserByEmailOrMobile(String emailOrMobile) {
		return studentUserDao.queryUserByEmailOrMobile(emailOrMobile);
	}

	/**
	 * 根据用户uid获取用户信息
	 */
	@Override
	public Map<String, StudentUser> getUserExpandByUids(String uids) throws Exception {
		return studentUserDao.getUserExpandByUids(uids);
	}

	/**
	 * 通过集合cusId 查询customer 返回map
	 */
	@Override
	public Map<String, StudentUser> queryCustomerInCusIds(List<Long> cusIds) throws Exception {
		return studentUserDao.queryCustomerInCusIds(cusIds);
	}

	/**
	 * 通过标识更新未读数加一
	 */
	@Override
	public void updateUnReadMsgNumAddOne(String falg, Integer cusId) {
		studentUserDao.updateUnReadMsgNumAddOne(falg, cusId);
	}

	/**
	 * excel批量开通用户 shanXinYing
	 */

	/**
	 * 后台缓存用户信息
	 */
	@Override
	public void setLoginInfo(HttpServletRequest request, Integer userId, String autoThirty) {
		StudentUser user =this.queryUserById(userId);
		//用户密码不能让别人看到
		user.setPassword("");
		//用户cookie key
		String uuid = WebUtils_.getCookie_(request, CacheConstans.WEB_USER_LOGIN_PREFIX);
		//缓存用户的登录时间
		user.setLoginTimeStamp(Long.parseLong( EhCacheUtils.get(CacheConstans.USER_CURRENT_LOGINTIME+user.getUserId()).toString()));
		if(autoThirty!=null&&autoThirty.equals("true")){//自动登录
			EhCacheUtils.set(uuid, user, CacheConstans.USER_TIME);
		}else{
			EhCacheUtils.set(uuid, user, 86400);
		}
	}

	/**
	 * 根据条件获取User列表 带课程名称
	 */
	@Override
	public List<StudentUser> getUserListPage(StudentUser user, PageEntity page) {
		return studentUserDao.getUserListPage(user, page);
	}

	/**
	 * 通过标识更新未读数清零
	 */
	@Override
	public void updateUnReadMsgNumReset(@Param("falg") String falg, @Param("cusId") Integer cusId) {
		// TODO Auto-generated method stub

	}

	/**
	 * 更新用户的上传统计系统消息时间
	 */
	@Override
	public void updateCusForLST(@Param("cusId") Integer cusId, @Param("date") Date date) {
		// TODO Auto-generated method stub

	}

	// start:******************************************************************************************************
	// 上传excel
	@Override
	public String updateImportExcel(HttpServletRequest request, MultipartFile myFile, Integer mark) throws Exception {
		// 1.出现异常跳过保存
		// 2.出现错误全部不保存
		String msg = "";
		Workbook wookbook = WorkbookFactory.create(myFile.getInputStream());
		Sheet sheet = wookbook.getSheet("Sheet1");

		int rows = sheet.getLastRowNum();// 指的行数，一共有多少行+
		if (rows == 0) {
			throw new Exception("请填写数据");
		}
		for (int i = 1; i <= rows + 1; i++) {
			// 读取左上端单元格
			Row row = sheet.getRow(i);
			// 行不为空
			if (row != null) {
				// **读取cell**
				String email = getCellValue(row.getCell((short) 0));// 邮箱
				String mobile = getCellValue(row.getCell((short) 1));// 手机
				String pwd = getCellValue(row.getCell((short) 2));// 获得密码

				// 邮箱
				if (StringUtils.isEmpty(email)) {// excel里邮箱为空 ，mark1 跳过继续执行
													// mark2不执行了
					if (mark == 1) {
						msg += "第" + i + "行邮箱错误<br/>";
						continue;
					} else {
						throw new Exception("第" + i + "行邮箱错误<br/>");
					}
				}
				if (StringUtils.isNotEmpty(email)) {
					// if (WebUtils.checkEmail(email, 50)==false) {
					if (!(ValidatorsAll.isEmail(email) && email.length() < 50)) {// 如果邮箱超过50个字符，不是正常的邮箱
																					// mark1
																					// 跳过继续执行
																					// mark2不执行了
						if (mark == 1) {
							msg += "第" + i + "行邮箱格式错误<br/>";
							continue;
						} else {
							throw new Exception("第" + i + "行邮箱格式错误<br/>");
						}
					}
				}
				boolean b = checkEmail(email.toLowerCase());// 重复导入邮箱
				if (b == true) {
					if (mark == 1) {
						msg += "第" + i + "行邮箱已存在<br/>";
						continue;
					} else {
						throw new Exception("第" + i + "行邮箱已存在<br/>");
					}
				}
				// 手机
				if (StringUtils.isEmpty(mobile)) {// 手机号为空
					if (mark == 1) {
						msg += "第" + i + "行手机错误<br/>";
						continue;
					} else {
						throw new Exception("第" + i + "行手机错误<br/>");
					}
				}
				if (StringUtils.isNotEmpty(mobile)) {
					if (!ValidatorsAll.isMobile(mobile)) {// 手机号不对
						if (mark == 1) {
							System.out.println("//////////////////////////mobile->" + mobile + "////////////////////////////////////");
							msg += "第" + i + "行手机格式错误<br/>";
							continue;
						} else {
							throw new Exception("第" + i + "行手机格式错误<br/>");
						}
					}
				}
				// 密码
				if (StringUtils.isNotEmpty(pwd)) {
					if (pwd.length() < 6 || pwd.length() > 20) {
						if (mark == 1) {
							msg += "第" + i + "行密码错误<br/>";
							continue;
						} else {
							throw new Exception("第" + i + "行密码错误<br/>");
						}
					}
				}
				if (StringUtils.isEmpty(pwd)) {
					pwd = "111111";
				}
				StudentUser studentUser = new StudentUser();
				// studentUser.setUserId(IdGen.uuid());
				studentUser.setEmail(email);// 用户学员邮箱
				studentUser.setMobile(mobile);// 用户学员手机
				studentUser.setPassword(MD5Utiles_.MD5(pwd));// 用户学员密码
				studentUser.setCreateTime(new Date());// 注册时间
				studentUser.setLastSystemTime(new Date());// 上传统计系统消息时间
				studentUserDao.createStudentUser(studentUser);

			}
		}
		return msg;
	}

	/**
	 * 获得Hsscell内容
	 */
	public String getCellValue(Cell cell) {
		String value = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_FORMULA:
				break;
			case Cell.CELL_TYPE_NUMERIC:
				DecimalFormat df = new DecimalFormat("0");
				value = df.format(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				value = cell.getStringCellValue().trim();
				break;
			default:
				value = "";
				break;
			}
		}
		return value.trim();
	}

//	public static boolean checkEmail_(String email) {
//		boolean flag = false;
//		try {
//			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
//			Pattern regex = Pattern.compile(check);
//			Matcher matcher = regex.matcher(email);
//			flag = matcher.matches();
//		} catch (Exception e) {
//			flag = false;
//		}
//		return flag;
//	}
	// end:******************************************************************************************************

}