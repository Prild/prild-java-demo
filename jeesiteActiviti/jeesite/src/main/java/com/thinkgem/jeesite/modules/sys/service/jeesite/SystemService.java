/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service.jeesite;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.security.Digests;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.Encodes;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.dao.MenuDao;
import com.thinkgem.jeesite.modules.sys.dao.RoleDao;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.Menu;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * 
 * @author ThinkGem
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService implements InitializingBean {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private SessionDAO sessionDao;
	@Autowired
	private SystemAuthorizingRealm systemRealm;

	public SessionDAO getSessionDao() {
		return sessionDao;
	}

	@Autowired
	private IdentityService identityService;

	// //////////////////////////////User Service
	// begin/////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 获取用户
	 * 
	 * @param id
	 * @return
	 */
	public User getUser(String id) {
		return UserUtils.get(id);
	}

	public List<User> findAllListUser() {
		return userDao.findAllListUser();

	}

	/**
	 * 根据登录名获取用户
	 * 
	 * @param loginName
	 * @return
	 */
	public User getUserByLoginName(String loginName) {
		return UserUtils.getByLoginName(loginName);
	}

	public Page<User> findUser(Page<User> page, User user) {
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用
																						// ${sqlMap.dsf}调用权限SQL）
		user.setPage(page);// 设置分页参数
		page.setList(userDao.findList(user));// 执行分页查询
		return page;
	}

	/**
	 * 无分页查询人员列表
	 * 
	 * @param user
	 * @return
	 */
	public List<User> findUser(User user) {
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用
																						// ${sqlMap.dsf}调用权限SQL）
		List<User> list = userDao.findList(user);
		return list;
	}

	/**
	 * 通过部门ID获取用户列表，仅返回用户id和name（树查询用户时用）
	 * 
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUserByOfficeId(String officeId) {
		List<User> list = (List<User>) CacheUtils.get(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId);
		if (list == null) {
			User user = new User();
			user.setOffice(new Office(officeId));
			list = userDao.findUserByOfficeId(user);
			CacheUtils.put(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId, list);
		}
		return list;
	}

	@Transactional(readOnly = false)
	public void saveUser(User user) {
		if (StringUtils.isBlank(user.getId())) {
			user.preInsert();
			userDao.insert(user);
		} else {
			User oldUser = userDao.get(user.getId());
			if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null) {
				CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());// 清除原用户机构用户缓存
			}
			user.preUpdate();// 更新用户数据
			userDao.update(user);
		}
		if (StringUtils.isNotBlank(user.getId())) {

			userDao.deleteUserRole(user);// 更新用户与角色关联
			if (user.getRoleList() != null && user.getRoleList().size() > 0) {
				userDao.insertUserRole(user);
			} else {
				throw new ServiceException(user.getLoginName() + "没有设置角色！");
			}
			saveActivitiUser(user);// 将当前用户同步到Activiti
			UserUtils.clearCache(user);// 清除用户缓存
			// systemRealm.clearAllCachedAuthorizationInfo();// 清除权限缓存
		}
	}

	@Transactional(readOnly = false)
	public void updateUserInfo(User user) {
		user.preUpdate();
		userDao.updateUserInfo(user);
		UserUtils.clearCache(user);// 清除用户缓存
		// systemRealm.clearAllCachedAuthorizationInfo();// 清除权限缓存
	}

	@Transactional(readOnly = false)
	public void deleteUser(User user) {
		userDao.delete(user);
		deleteActivitiUser(user);// 同步到Activiti
		UserUtils.clearCache(user);// 清除用户缓存
		// systemRealm.clearAllCachedAuthorizationInfo();// 清除权限缓存
	}

	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		User user = new User(id);
		user.setPassword(entryptPassword(newPassword));
		userDao.updatePasswordById(user);
		user.setLoginName(loginName);// 清除用户缓存
		UserUtils.clearCache(user);
		// systemRealm.clearAllCachedAuthorizationInfo();// 清除权限缓存
	}

	@Transactional(readOnly = false)
	public void updateUserLoginInfo(User user) {

		user.setOldLoginIp(user.getLoginIp());// 保存上次登录信息
		user.setOldLoginDate(user.getLoginDate());

		user.setLoginIp(StringUtils.getRemoteAddr(Servlets.getRequest()));// 更新本次登录信息
		user.setLoginDate(new Date());
		userDao.updateLoginInfo(user);
	}

	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
	}

	/**
	 * 验证密码
	 * 
	 * @param plainPassword
	 *            明文密码
	 * @param password
	 *            密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Encodes.decodeHex(password.substring(0, 16));
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
	}

	/**
	 * 获得活动会话
	 * 
	 * @return
	 */
	public Collection<Session> getActiveSessions() {
		return sessionDao.getActiveSessions(false);
	}

	// //////////////////////////////User Service
	// end/////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////Role Service
	// begin/////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Role getRole(String id) {
		return roleDao.get(id);
	}

	public Role getRoleByName(String name) {
		Role r = new Role();
		r.setName(name);
		return roleDao.getByName(r);
	}

	public Role getRoleByEnname(String enname) {
		Role r = new Role();
		r.setEnname(enname);
		return roleDao.getByEnname(r);
	}

	public List<Role> findRole(Role role) {
		return roleDao.findList(role);
	}

	public List<Role> findAllRole() {
		return UserUtils.getRoleList();
	}
	public List<Role> findAllListRole() {
		return roleDao.findAllListRole();
	}
	public List<Role> findAllList() {
		return roleDao.findAllListRole();
	}

	
	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		if (StringUtils.isBlank(role.getId())) {
			role.preInsert();
			roleDao.insert(role);
			saveActivitiGroup(role);// 同步到Activiti
		} else {
			role.preUpdate();
			roleDao.update(role);
		}
		roleDao.deleteRoleMenu(role);// 更新角色与菜单关联
		if (role.getMenuList().size() > 0) {
			roleDao.insertRoleMenu(role);
		}
		roleDao.deleteRoleOffice(role);// 更新角色与部门关联
		if (role.getOfficeList().size() > 0) {
			roleDao.insertRoleOffice(role);
		}

		saveActivitiGroup(role);// 同步到Activiti

		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);// 清除用户角色缓存
		// systemRealm.clearAllCachedAuthorizationInfo();// 清除权限缓存
	}

	@Transactional(readOnly = false)
	public void deleteRole(Role role) {
		roleDao.delete(role);

		deleteActivitiGroup(role);// 同步到Activiti

		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);// 清除用户角色缓存

		// systemRealm.clearAllCachedAuthorizationInfo();// 清除权限缓存
	}

	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, User user) {
		List<Role> roles = user.getRoleList();
		for (Role e : roles) {
			if (e.getId().equals(role.getId())) {
				roles.remove(e);
				saveUser(user);
				return true;
			}
		}
		return false;
	}

	@Transactional(readOnly = false)
	public User assignUserToRole(Role role, User user) {
		if (user == null) {
			return null;
		}
		List<String> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getRoleList().add(role);
		saveUser(user);
		return user;
	}

	// //////////////////////////////Role Service
	// end/////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////Menu Service
	// begin/////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Menu getMenu(String id) {
		return menuDao.get(id);
	}

	public List<Menu> findAllMenu() {
		return UserUtils.getMenuList();
	}

	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {

		menu.setParent(this.getMenu(menu.getParent().getId()));// 获取父节点实体

		String oldParentIds = menu.getParentIds();// 获取修改前的parentIds，用于更新子节点的parentIds

		menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");// 设置新的父节点串

		if (StringUtils.isBlank(menu.getId())) {// 保存或更新实体
			menu.preInsert();
			menuDao.insert(menu);
		} else {
			menu.preUpdate();
			menuDao.update(menu);
		}

		// 更新子节点 parentIds
		Menu m = new Menu();
		m.setParentIds("%," + menu.getId() + ",%");
		List<Menu> list = menuDao.findByParentIdsLike(m);
		for (Menu e : list) {
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
			menuDao.updateParentIds(e);
		}
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);// 清除用户菜单缓存
		// systemRealm.clearAllCachedAuthorizationInfo();// 清除权限缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void updateMenuSort(Menu menu) {
		menuDao.updateSort(menu);
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);// 清除用户菜单缓存
		// systemRealm.clearAllCachedAuthorizationInfo();// 清除权限缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);// 清除日志相关缓存
	}

	@Transactional(readOnly = false)
	public void deleteMenu(Menu menu) {
		menuDao.delete(menu);
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// systemRealm.clearAllCachedAuthorizationInfo();
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	/**
	 * 获取Key加载信息
	 */
	public static boolean printKeyLoadMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n    欢迎使用 " + Global.getConfig("productName") + "  - Powered By http://jeesite.com\r\n");
		sb.append("\r\n======================================================================\r\n");
		System.out.println(sb.toString());
		return true;
	}

	// //////////////////////////////Menu Service
	// end/////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////Activiti Service begin 同步用户
	// 角色 信息到 /////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 以下已废弃，同步见：ActGroupEntityServiceFactory.java、ActUserEntityServiceFactory.java

	private static boolean isSynActivitiIndetity = true;// 是需要同步Activiti数据，如果从未同步过，则同步数据。

	public void afterPropertiesSet() throws Exception {// afterPropertiesSet这个方法将在所有的属性被初始化后调用。但是会在init前调用。但是主要的是如果是延迟加载的话，则马上执行。所以可以在类上加上注解：import
														// org.springframework.context.annotation.Lazy;@Lazy(false)这样spring容器初始化的时候afterPropertiesSet就会被调用。只需要实现InitializingBean接口就行。
		if (!Global.isSynActivitiIndetity()) {
			return;
		}
		if (isSynActivitiIndetity) {// 系统启动，从这里执行
			isSynActivitiIndetity = false;
			List<Group> groupList = identityService.createGroupQuery().list();// 同步角色数据
			if (groupList.size() == 0) {
				Iterator<Role> roles = roleDao.findAllList(new Role()).iterator();
				while (roles.hasNext()) {
					Role role = roles.next();
					saveActivitiGroup(role);
				}
			}
			List<org.activiti.engine.identity.User> userList = identityService.createUserQuery().list();// 同步用户数据
			if (userList.size() == 0) {
				Iterator<User> users = userDao.findAllList(new User()).iterator();
				while (users.hasNext()) {
					saveActivitiUser(users.next());
				}
			}
		}
	}

	/**
	 * role表对应activiti 中的group表
	 * 
	 * @param role
	 *            角色
	 */
	private void saveActivitiGroup(Role role) {
		if (!Global.isSynActivitiIndetity()) {
			return;
		}
		String groupId = role.getEnname();
		if (StringUtils.isNotBlank(role.getOldEnname()) && !role.getOldEnname().equals(role.getEnname())) {
			identityService.deleteGroup(role.getOldEnname());// 如果修改了英文名，则删除原Activiti角色
		}

		Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
		if (group == null) {
			group = identityService.newGroup(groupId);
		}
		group.setName(role.getName());
		group.setType(role.getRoleType());
		identityService.saveGroup(group);

		List<org.activiti.engine.identity.User> activitiUserList = identityService.createUserQuery().memberOfGroup(groupId)
				.list();
		for (org.activiti.engine.identity.User activitiUser : activitiUserList) {
			identityService.deleteMembership(activitiUser.getId(), groupId);// 删除用户与用户组关系
		}

		List<User> userList = findUser(new User(new Role(role.getId())));// 创建用户与用户组关系
		for (User e : userList) {
			String userId = e.getLoginName();// ObjectUtils.toString(user.getId());
			org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
			if (activitiUser == null) {// 如果该用户不存在，则创建一个
				activitiUser = identityService.newUser(userId);
				activitiUser.setFirstName(e.getName());
				activitiUser.setLastName(StringUtils.EMPTY);
				activitiUser.setEmail(e.getEmail());
				activitiUser.setPassword(StringUtils.EMPTY);
				identityService.saveUser(activitiUser);
			}
			identityService.createMembership(userId, groupId);
		}
	}

	public void deleteActivitiGroup(Role role) {
		if (!Global.isSynActivitiIndetity()) {
			return;
		}
		if (role != null) {
			String groupId = role.getEnname();
			identityService.deleteGroup(groupId);
		}
	}

	private void saveActivitiUser(User user) {
		if (!Global.isSynActivitiIndetity()) {
			return;
		}
		String userId = user.getLoginName();// ObjectUtils.toString(user.getId());
		org.activiti.engine.identity.User activitiUser = null;
		try {
			activitiUser = identityService.createUserQuery().userId(userId).singleResult();// 没有数据不应该抛异常
		} catch (Exception e) {
			// TODO: handle exception
			if (activitiUser == null) {
				activitiUser = identityService.newUser(userId);// 创建用户对象
			}
			activitiUser.setFirstName(user.getName());
			// activitiUser.setLastName(StringUtils.EMPTY);
			activitiUser.setEmail(user.getEmail());
			// activitiUser.setPassword(StringUtils.EMPTY);
			try {
				identityService.saveUser(activitiUser);// 同步用户角色关联数据

			} catch (Exception e2) {
				// TODO: handle exception
				logger.info(e2.toString());
			}
		}

		List<Group> activitiGroups = identityService.createGroupQuery().groupMember(userId).list();
		for (Group group : activitiGroups) {
			identityService.deleteMembership(userId, group.getId());// 删除用户与用户组关系
		}

		for (Role role : user.getRoleList()) {// 创建用户与用户组关系
			String groupId = role.getEnname();
			Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
			if (group == null) {// 如果该用户组不存在，则创建一个
				group = identityService.newGroup(groupId);
				group.setName(role.getName());
				group.setType(role.getRoleType());
				identityService.saveGroup(group);
			}
			identityService.createMembership(userId, role.getEnname());
		}
	}

	private void deleteActivitiUser(User user) {
		if (!Global.isSynActivitiIndetity()) {
			return;
		}
		if (user != null) {
			String userId = user.getLoginName();// ObjectUtils.toString(user.getId());
			identityService.deleteUser(userId);
		}
	}
	// /////////////////////////////////////////////Activiti Service end 同步用户 角色
	// 信息到 Activiti /////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////
}
