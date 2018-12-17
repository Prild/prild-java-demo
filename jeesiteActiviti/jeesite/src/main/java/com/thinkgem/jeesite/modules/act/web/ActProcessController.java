/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.act.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.apache.commons.io.FilenameUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.service.ActProcessService;
import com.thinkgem.jeesite.modules.act.utils.Page_;
import com.thinkgem.jeesite.modules.act.utils.Page_Util;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.jeesite.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.act.service.ProcessDefinitionService;
import com.thinkgem.jeesite.modules.act.service.ext.ActGroupEntityService;
import com.thinkgem.jeesite.modules.act.service.ext.ActUserEntityService;

/**
 * 流程定义相关Controller
 * 
 * @author ThinkGem
 * @version 2013-11-03
 */
@Controller
@RequestMapping(value = "${adminPath}/act/process")
public class ActProcessController extends BaseController {

	@Autowired
	private ActProcessService actProcessService;
	@Autowired
	RepositoryService repositoryService;
	@Autowired
	IdentityService identityService;
	@Autowired
	SystemService systemService;
	@Autowired
	private ProcessDefinitionService processDefinitionService;
	@Autowired
	private ActUserEntityService actUserEntityService;
	@Autowired
	private ActGroupEntityService actGroupEntityService;

	/**
	 * 设置流程定义对象的候选人、候选组
	 * 
	 * @return
	 */
	@RequestMapping(value = "startable/set/{processDefinitionId}", method = RequestMethod.POST)
	@ResponseBody
	public String addStartables(@PathVariable("processDefinitionId") String processDefinitionId,
			@RequestParam(value = "users_[]", required = false) String[] users,
			@RequestParam(value = "groups_[]", required = false) String[] groups) {
		processDefinitionService.setStartables(processDefinitionId, users, groups);
		
		
		return "true";
	}

	/**
	 * 流程列表 谁来负责启动流程的
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/process-list-view")
	public ModelAndView processListReadonly(HttpServletRequest request) {

		// 对应WEB-INF/views/chapter5/process-list.jsp
		String viewName = "chapter5/process-list-view";

		Page_<ProcessDefinition> page = new Page_<ProcessDefinition>(Page_Util.PAGE_SIZE);
		int[] pageParams = Page_Util.init(page, request);

		ModelAndView mav = new ModelAndView(viewName);
		// User user = UserUtil.getUserFromSession(request.getSession());
		// ProcessDefinitionQuery processDefinitionQuery =
		// repositoryService.createProcessDefinitionQuery().startableByUser(user.getId());
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

		processDefinitionQuery.suspended().active();

		List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(pageParams[0], pageParams[1]);

		page.setResult(processDefinitionList);
		page.setTotalCount(processDefinitionQuery.count());
		mav.addObject("page", page);

		// 读取所有人员
		List<User> users = systemService.findAllListUser();
		mav.addObject("users", users);

		// 读取所有组
		List<Role> groups = systemService.findAllListRole();
		mav.addObject("groups", groups);

		return mav;
	}

	/**
	 * 读取流程定义的相关候选启动人、组，根据link信息转换并封装为User、Group对象
	 * 
	 * @param processDefinitionList
	 * @return
	 */
	private Map<String, Map<String, List<? extends Object>>> setCandidateUserAndGroups(
			List<ProcessDefinition> processDefinitionList) {
		Map<String, Map<String, List<? extends Object>>> linksMap = new HashMap<String, Map<String, List<? extends Object>>>();
		for (ProcessDefinition processDefinition : processDefinitionList) {
			List<IdentityLink> identityLinks = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());

			Map<String, List<? extends Object>> single = new Hashtable<String, List<? extends Object>>();
			List<UserEntity> linkUsers = new ArrayList<UserEntity>();
			List<Role> linkRoles = new ArrayList<Role>();

			for (IdentityLink link : identityLinks) {
				if (StringUtils.isNotBlank(link.getUserId())) {
//					linkUsers.add((User) identityService.createUserQuery().userId(link.getUserId()).singleResult());
					UserEntity user = actUserEntityService.findUserById(link.getUserId());
					linkUsers.add(user);
				} else if (StringUtils.isNotBlank(link.getGroupId())) {
//					linkGroups.add((Role) identityService.createGroupQuery().groupId(link.getGroupId()).singleResult());
					System.out.println("/////////////////////////"+link.getGroupId()+"//////////////////////");
					Role role  = systemService.getRoleByEnname(link.getGroupId());//根据角色登录名查询，不是id
					System.out.println("/////////////////////////"+role.getName()+"//////////////////////");
					linkRoles.add(role);
				}
			}

			single.put("users", linkUsers);
			single.put("groups", linkRoles);

			linksMap.put(processDefinition.getId(), single);

		}
		return linksMap;
	}

	/**
	 * 流程管理
	 * @param category 类别
	 * @param request
	 * @param response
	 * @param model (页面: category users groups linksMap(某一条流程定义的负责人/组))
	 * @return
	 */
	@RequiresPermissions("act:process:edit")
	@RequestMapping(value = { "list", "" })
	public String processList(String category, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		// 保存两个对象，一个是ProcessDefinition（流程定义），一个是Deployment（流程部署）
		Page<Object[]> page = actProcessService.processList(new Page<Object[]>(request, response), category);
		model.addAttribute("page", page);
		model.addAttribute("category", category);
		
		List<User> users = systemService.findAllListUser();// 读取所有人员
		model.addAttribute("users_", users);
		
		List<Role> groups = systemService.findAllListRole();// 读取所有组
		model.addAttribute("groups_", groups);
		
		Page_<ProcessDefinition> page_ = new Page_<ProcessDefinition>(Page_Util.PAGE_SIZE);
		int[] pageParams = Page_Util.init(page_, request);
		
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().suspended().active();
		List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(pageParams[0], pageParams[1]);
		
		Map<String, Map<String, List<? extends Object>>> linksMap = setCandidateUserAndGroups(processDefinitionList);// 读取每个流程定义的候选属性
		model.addAttribute("linksMap", linksMap);
		return "modules/act/actProcessList";
	}

	/**
	 * 运行中的实例列表
	 */
	@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "running")
	public String runningList(String procInsId, String procDefKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<ProcessInstance> page = actProcessService.runningList(new Page<ProcessInstance>(request, response), procInsId,
				procDefKey);
		model.addAttribute("page", page);
		model.addAttribute("procInsId", procInsId);
		model.addAttribute("procDefKey", procDefKey);
		return "chapter6/processRunningList";
	}

	/**
	 * 读取资源，通过部署ID
	 * 
	 * @param processDefinitionId
	 *            流程定义ID
	 * @param processInstanceId
	 *            流程实例ID
	 * @param resourceType
	 *            资源类型(xml|image)
	 * @param response
	 * @throws Exception
	 */
	@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "resource/read")
	public void resourceRead(String procDefId, String proInsId, String resType, HttpServletResponse response) throws Exception {
		InputStream resourceAsStream = actProcessService.resourceRead(procDefId, proInsId, resType);
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}

	/**
	 * 部署流程
	 */
	@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "/deploy", method = RequestMethod.GET)
	public String deploy(Model model) {
		return "modules/act/actProcessDeploy";
	}

	/**
	 * 部署流程 - 保存
	 * 
	 * @param file
	 * @return
	 */
	@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "/deploy", method = RequestMethod.POST)
	public String deploy(@Value("#{APP_PROP['activiti.export.diagram.path']}") String exportDir, String category,
			MultipartFile file, RedirectAttributes redirectAttributes) {

		String fileName = file.getOriginalFilename();
		DeploymentBuilder deployment = null;
		if (StringUtils.isBlank(fileName) || StringUtils.isBlank(category)) // 双重验证
		{
			redirectAttributes.addFlashAttribute("message", "不能为空");
			return "redirect:" + adminPath + "/act/process";// 还是本页
		}
		// ///////////////////////////////////////原来代码//////////////////////////////////////////////////////////
		//
		// if (StringUtils.isBlank(fileName)){
		// redirectAttributes.addFlashAttribute("message", "请选择要部署的流程文件");
		// }else{
		// String message = actProcessService.deploy(exportDir, category, file);
		// redirectAttributes.addFlashAttribute("message", message);
		// }
		//
		// return "redirect:" + adminPath + "/act/process";

		try {
			InputStream fileInputStream = file.getInputStream();

			String extension = FilenameUtils.getExtension(fileName);// 文件的扩展名

			deployment = repositoryService.createDeployment();// zip或者bar类型的文件用ZipInputStream方式部署
			if (extension.equals("zip") || extension.equals("bar")) {
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				deployment.addZipInputStream(zip);
			} else {// 其他类型的文件直接部署/////////////////////////////////////////////待优化部分/////////////////////////////////////
				deployment.addInputStream(fileName, fileInputStream);
			}
			Deployment depl = deployment.deploy();
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(depl.getId()).list();

			for (ProcessDefinition processDefinition : list) {// 设置流程分类
				repositoryService.setProcessDefinitionCategory(processDefinition.getId(), category);
				redirectAttributes.addFlashAttribute("message", "部署成功");
			}
			if (list.size() == 0) {
				redirectAttributes.addFlashAttribute("message", "分类部署失败");
			}
		} catch (Exception e) {
			logger.error("error on deploy process, because of file input stream");
		}

		return "redirect:" + adminPath + "/act/process";// 还是本页

	}

	/**
	 * 设置流程分类
	 */
	@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "updateCategory")
	public String updateCategory(String procDefId, String category, RedirectAttributes redirectAttributes) {
		actProcessService.updateCategory(procDefId, category);
		return "redirect:" + adminPath + "/act/process";
	}

	/**
	 * 挂起、激活流程实例
	 */
	@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "update/{state}")
	public String updateState(@PathVariable("state") String state, String procDefId, RedirectAttributes redirectAttributes) {
		String message = actProcessService.updateState(state, procDefId);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:" + adminPath + "/act/process";
	}

	/**
	 * 将部署的流程转换为模型
	 * 
	 * @param procDefId
	 * @param redirectAttributes
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws XMLStreamException
	 */
	@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "convert/toModel")
	public String convertToModel(String procDefId, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException,
			XMLStreamException {
		org.activiti.engine.repository.Model modelData = actProcessService.convertToModel(procDefId);
		redirectAttributes.addFlashAttribute("message", "转换模型成功，模型ID=" + modelData.getId());
		return "redirect:" + adminPath + "/act/model";
	}

	/**
	 * 导出图片文件到硬盘
	 */
	@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "export/diagrams")
	@ResponseBody
	public List<String> exportDiagrams(@Value("#{APP_PROP['activiti.export.diagram.path']}") String exportDir) throws IOException {
		List<String> files = actProcessService.exportDiagrams(exportDir);
		;
		return files;
	}

	/**
	 * 删除部署的流程，级联删除流程实例
	 * 
	 * @param deploymentId
	 *            流程部署ID
	 */
	@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "delete")
	public String delete(String deploymentId) {
		actProcessService.deleteDeployment(deploymentId);
		return "redirect:" + adminPath + "/act/process";
	}

	/**
	 * 删除流程实例
	 * 
	 * @param procInsId
	 *            流程实例ID
	 * @param reason
	 *            删除原因
	 */
	@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "deleteProcIns")
	public String deleteProcIns(String procInsId, String reason, RedirectAttributes redirectAttributes) {
		if (StringUtils.isBlank(reason)) {
			addMessage(redirectAttributes, "请填写删除原因");
		} else {
			actProcessService.deleteProcIns(procInsId, reason);
			addMessage(redirectAttributes, "删除流程实例成功，实例ID=" + procInsId);
		}
		return "redirect:" + adminPath + "/act/process/running/";
	}

}
