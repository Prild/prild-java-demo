package com.thinkgem.jeesite.modules.act.web;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.act.service.ext.ActGroupEntityService;
import com.thinkgem.jeesite.modules.act.service.ext.ActUserEntityService;
import com.thinkgem.jeesite.modules.act.utils.Page_;
import com.thinkgem.jeesite.modules.act.utils.Page_Util;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.service.jeesite.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * 任务控制器
 * 
 * @author henryyan
 */
@Controller
@RequestMapping(value = "${adminPath}/act/task2")
public class TaskController extends BaseController {

	private String redirect = "redirect:/";
	private String TASK_LIST = "/act/task2/list";

	@Autowired
	TaskService taskService;

	@Autowired
	FormService formService;

	@Autowired
	IdentityService identityService;

	@Autowired
	HistoryService historyService;
	// Principal principal = UserUtils.getPrincipal();// 授权用户
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private ActUserEntityService actUserEntityService;
	@Autowired
	private ActGroupEntityService actGroupEntityService;
	@Autowired
	private SystemService systemService;
	@Autowired
	UserDao userDao;

	/**
	 * 读取启动流程的表单字段
	 */
	@RequestMapping(value = { "list", "" })
	public ModelAndView todoTasks(Act act, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes)
			throws Exception {
		String viewName = "chapter6/task-list";
		ModelAndView mav = new ModelAndView(viewName);
		List<Act> list = actTaskService.todoList(act);
		mav.addObject("list", list);
		return mav;
	}

	/**
	 * 签收任务
	 */
	@RequestMapping(value = "claim/{id}")
	public String claim(@PathVariable("id") String taskId, @RequestParam(value = "nextDo", required = false) String nextDo,
			HttpSession session, RedirectAttributes redirectAttributes) {
		String userId = UserUtils.getUser().getLoginName();// 统一用用户的登录名(唯一)
															// 这样更直观
		taskService.claim(taskId, userId);
		redirectAttributes.addFlashAttribute("message", "任务已签收");
		if (StringUtils.equals(nextDo, "handle")) {//处理直接查看进来 还没有签收的任务
			return "redirect:/" + adminPath + "/act/task2/getform/" + taskId;
		} else {
			return "redirect:/" + adminPath + "/act/task2/list";
		}
	}

	/**
	 * 反签收任务
	 */
	@RequestMapping(value = "unclaim/{id}")
	public String unclaim(@PathVariable("id") String taskId, HttpSession session, RedirectAttributes redirectAttributes) {
		// 反签收条件过滤
		List<IdentityLink> links = taskService.getIdentityLinksForTask(taskId);
		for (IdentityLink identityLink : links) {
			// 如果一个任务有相关的候选人、组就可以反签收
			if (StringUtils.equals(IdentityLinkType.CANDIDATE, identityLink.getType())) {
				taskService.claim(taskId, null);
				redirectAttributes.addFlashAttribute("message", "任务已反签收");
				return redirect + adminPath + TASK_LIST;
			}
		}
		redirectAttributes.addFlashAttribute("error", "该任务不允许反签收！");// 不允许签收的情况：比如：上级同意之后
		return redirect + adminPath + TASK_LIST;// 层级多了用两个斜杠？
	}

	/**
	 * 读取用户任务的表单字段
	 */
	@RequestMapping(value = "getform/{taskId}")
	public ModelAndView readTaskForm(@PathVariable("taskId") String taskId) throws Exception {
		String viewName = "chapter6/task-form";
		ModelAndView mav = new ModelAndView(viewName);
		TaskFormData taskFormData = formService.getTaskFormData(taskId);
		Task task = null;

		// 外置表单
		if (taskFormData != null && taskFormData.getFormKey() != null) {
			Object renderedTaskForm = formService.getRenderedTaskForm(taskId);// 字符串
			task = taskService.createTaskQuery().taskId(taskId).singleResult();
			mav.addObject("taskFormData", renderedTaskForm);
			mav.addObject("hasFormKey", true);
		} else if (taskFormData != null) { // 动态表单
			mav.addObject("taskFormData", taskFormData);
			task = taskFormData.getTask();
		} else { // 手动创建的任务（包括子任务）
			task = taskService.createTaskQuery().taskId(taskId).singleResult();
			mav.addObject("manualTask", true);
		}
		mav.addObject("task", task);

		// 读取任务参与人(组)列表
		List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
		mav.addObject("identityLinksForTask", identityLinksForTask);

		// 读取所有人员
		// List<User> users = identityService.createUserQuery().list();
		// mav.addObject("users", users);

		List<User> users = systemService.findAllListUser();
		mav.addObject("users", users);

		// 读取所有组
		// List<Group> groups = identityService.createGroupQuery().list();
		// mav.addObject("groups", groups);
		List<Role> groups = systemService.findAllListRole();
		mav.addObject("groups", groups);

		// 读取子任务
		List<HistoricTaskInstance> subTasks = historyService.createHistoricTaskInstanceQuery().taskParentTaskId(taskId).list();
		mav.addObject("subTasks", subTasks);

		// 读取上级任务
		if (task != null && task.getParentTaskId() != null) {
			HistoricTaskInstance parentTask = historyService.createHistoricTaskInstanceQuery().taskId(task.getParentTaskId())
					.singleResult();
			mav.addObject("parentTask", parentTask);
		}

		// 读取附件
		List<Attachment> attachments = null;
		if (task.getTaskDefinitionKey() != null) {
			attachments = taskService.getTaskAttachments(taskId);
		} else {
			attachments = taskService.getProcessInstanceAttachments(task.getProcessInstanceId());
		}
		mav.addObject("attachments", attachments);

		String curruntLoginUser = UserUtils.getUser().getLoginName();
		mav.addObject("curruntLoginUser", curruntLoginUser);
		return mav;
	}

	/**
	 * 查看已结束任务
	 */
	@RequestMapping(value = "archived/{taskId}")
	public ModelAndView viewHistoryTask(@PathVariable("taskId") String taskId) throws Exception {
		String viewName = "chapter6/task-form-archived";
		ModelAndView mav = new ModelAndView(viewName);
		HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		if (task.getParentTaskId() != null) {
			HistoricTaskInstance parentTask = historyService.createHistoricTaskInstanceQuery().taskId(task.getParentTaskId())
					.singleResult();
			mav.addObject("parentTask", parentTask);
		}
		mav.addObject("task", task);

		// 读取子任务
		List<HistoricTaskInstance> subTasks = historyService.createHistoricTaskInstanceQuery().taskParentTaskId(taskId).list();
		mav.addObject("subTasks", subTasks);

		// 读取附件
		List<Attachment> attachments = null;
		if (task.getTaskDefinitionKey() != null) {
			attachments = taskService.getTaskAttachments(taskId);
		} else {
			attachments = taskService.getProcessInstanceAttachments(task.getProcessInstanceId());
		}
		mav.addObject("attachments", attachments);

		return mav;
	}

	/**
	 * complete/{taskId}办理完成 读取启动流程的表单字段
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "complete/{taskId}", method = RequestMethod.POST)
	public String completeTask(@PathVariable("taskId") String taskId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception {

		System.out.println("////////////////jeesite/a/act/task2/complete///////////////");
		// 设置当前操作人，对于调用活动可以获取到当前操作人
		String currentUserId = UserUtils.getUser().getLoginName();
		identityService.setAuthenticatedUserId(currentUserId);

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 如果任务的流程定义任务Key为空则认为是手动创建的任务
		if (StringUtils.isBlank(task.getTaskDefinitionKey())) {
			taskService.complete(taskId);
			return redirect + adminPath + TASK_LIST;
		}

		// 权限检查-任务的办理人和当前人不一致不能完成任务
		if (!task.getAssignee().equals(currentUserId)) {
			redirectAttributes.addFlashAttribute("error", "没有权限，不能完成该任务！");
			return "redirect:"+adminPath+"/act/task2/getform/" + taskId;
		}

		// 单独处理被委派的任务
		if (task.getDelegationState() == DelegationState.PENDING) {
			taskService.resolveTask(taskId);
			return redirect + adminPath + TASK_LIST;
		}

		TaskFormData taskFormData = formService.getTaskFormData(taskId);
		String formKey = taskFormData.getFormKey();
		// 从请求中获取表单字段的值
		List<FormProperty> formProperties = taskFormData.getFormProperties();
		Map<String, String> formValues = new HashMap<String, String>();

		if (StringUtils.isNotBlank(formKey)) { // formkey表单
			Map<String, String[]> parameterMap = request.getParameterMap();
			Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
			for (Entry<String, String[]> entry : entrySet) {
				String key = entry.getKey();
				formValues.put(key, entry.getValue()[0]);
			}
		} else { // 动态表单
			for (FormProperty formProperty : formProperties) {
				if (formProperty.isWritable()) {
					String value = request.getParameter(formProperty.getId());
					formValues.put(formProperty.getId(), value);
				}
			}
		}
		formService.submitTaskFormData(taskId, formValues);
		return redirect + adminPath + TASK_LIST;
	}

	/**
	 * 更改任务属性
	 * 
	 * @throws ParseException
	 */
	@RequestMapping("property/{taskId}")
	// a/act/task2/property/{taskId}
	@ResponseBody
	public String changeTaskProperty(@PathVariable("taskId") String taskId, @RequestParam("propertyName") String propertyName,
			@RequestParam("value") String value) throws ParseException {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 更改到期日
		if (StringUtils.equals(propertyName, "dueDate")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date parse = sdf.parse(value);
			task.setDueDate(parse);
			taskService.saveTask(task);
		} else if (StringUtils.equals(propertyName, "priority")) {
			// 更改任务优先级
			task.setPriority(Integer.parseInt(value));
			taskService.saveTask(task);
		} else if (StringUtils.equals(propertyName, "owner")) {
			// 更改拥有人
			task.setOwner(value);
			taskService.saveTask(task);
		} else if (StringUtils.equals(propertyName, "assignee")) {
			// 更改办理人
			task.setAssignee(value);
			taskService.saveTask(task);
		} else {
			return "不支持[" + propertyName + "]属性！";
		}
		return "success";
	}

	/**
	 * 添加参与人
	 */
	@RequestMapping("participant/add/{taskId}")
	@ResponseBody
	public String addParticipants(@PathVariable("taskId") String taskId, @RequestParam("userId[]") String[] userIds,
			@RequestParam("type[]") String[] types, HttpServletRequest request) {

		String currentUserId = UserUtils.getUser().getLoginName();
		identityService.setAuthenticatedUserId(currentUserId);// 设置当前操作人，对于调用活动可以获取到当前操作人

		for (int i = 0; i < userIds.length; i++) {
			taskService.addUserIdentityLink(taskId, userIds[i], types[i]);
		}
		return "success";
	}

	/**
	 * 删除参与人
	 */
	@RequestMapping("participant/delete/{taskId}")
	@ResponseBody
	public String deleteParticipant(@PathVariable("taskId") String taskId,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "groupId", required = false) String groupId, @RequestParam("type") String type) {
		/*
		 * 区分用户、组，使用不同的处理方式
		 */
		if (StringUtils.isNotBlank(groupId)) {
			taskService.deleteCandidateGroup(taskId, groupId);
		} else {
			taskService.deleteUserIdentityLink(taskId, userId, type);
		}
		return "success";
	}

	/**
	 * 添加候选人
	 */
	@RequestMapping("candidate/add/{taskId}")
	@ResponseBody
	public String addCandidates(@PathVariable("taskId") String taskId, @RequestParam("userOrGroupIds[]") String[] userOrGroupIds,
			@RequestParam("type[]") String[] types, HttpServletRequest request) {
		// 设置当前操作人，对于调用活动可以获取到当前操作人
		String currentUserId = UserUtils.getUser().getLoginName();
		identityService.setAuthenticatedUserId(currentUserId);

		for (int i = 0; i < userOrGroupIds.length; i++) {
			String type = types[i];
			if (StringUtils.equals("user", type)) {
				taskService.addCandidateUser(taskId, userOrGroupIds[i]);
			} else if (StringUtils.equals("group", type)) {
				taskService.addCandidateGroup(taskId, userOrGroupIds[i]);
			}
		}
		return "success";
	}

	/**
	 * 添加子任务
	 */
	@RequestMapping("subtask/add/{taskId}")
	public String addSubTask(@PathVariable("taskId") String parentTaskId, @RequestParam("taskName") String taskName,
			@RequestParam(value = "description", required = false) String description, HttpSession session) {
		Task newTask = taskService.newTask();
		newTask.setParentTaskId(parentTaskId);
		String userId = UserUtils.getUser().getLoginName();
		newTask.setOwner(userId);
		newTask.setAssignee(userId);
		newTask.setName(taskName);
		newTask.setDescription(description);

		taskService.saveTask(newTask);
		return "redirect:" + adminPath + "/act/task2/getform/" + parentTaskId;
		// return "redirect:/chapter6/task/getform/" + parentTaskId;
	}

	/**
	 * 删除子任务（子任务不能彻底删除，只能代表完成）
	 */
	@RequestMapping("delete/{taskId}")
	public String deleteSubTask(@PathVariable("taskId") String taskId, HttpSession session) {
		String userId = UserUtils.getUser().getLoginName();
		taskService.deleteTask(taskId, "deleteByUser" + userId);
		return "redirect:" + adminPath + "/act/task2/archived/" + taskId;
	}

	/**
	 * 新任务
	 */
	@RequestMapping("new")
	public String newTask(@RequestParam("taskName") String taskName,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "priority", required = false) int priority,
			@RequestParam(value = "dueDate", required = false) String dueDate, HttpSession session) {
		Task newTask = taskService.newTask();
		String userId = UserUtils.getUser().getLoginName();
		newTask.setOwner(userId);
		newTask.setAssignee(userId);
		newTask.setName(taskName);
		newTask.setDescription(description);
		if (StringUtils.isNotBlank(dueDate)) {
			try {
				newTask.setDueDate(java.sql.Date.valueOf(dueDate));
			} catch (Exception e) {
			}
		}
		newTask.setPriority(priority);

		taskService.saveTask(newTask);
		return "redirect:" + adminPath +"/act/task2/getform/" + newTask.getId();
	}

	/**
	 * 任务委派
	 * 
	 * @param taskId
	 * @param delegateUserId
	 */
	@RequestMapping("delegate/{taskId}")
	@ResponseBody
	public String delegate(@PathVariable("taskId") String taskId, @RequestParam("delegateUserId") String delegateUserId) {
		taskService.delegateTask(taskId, delegateUserId);
		return "success";
	}

}
