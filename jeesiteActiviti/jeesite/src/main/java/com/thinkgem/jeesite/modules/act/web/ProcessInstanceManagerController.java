package com.thinkgem.jeesite.modules.act.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.utils.Page_;
import com.thinkgem.jeesite.modules.act.utils.Page_Util;

/**
 * 流程实例管理控制器 User: henryyan
 */
@Controller
@RequestMapping(value = "${adminPath}/act/processinstance")
public class ProcessInstanceManagerController extends BaseController {

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	RepositoryService repositoryService;

	/**
	 * 流程实例列表
	 */
	@RequestMapping(value = "list")
	public ModelAndView processInstanceList(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("chapter6/processinstance-list");

		Page_<ProcessInstance> page = new Page_<ProcessInstance>(Page_Util.PAGE_SIZE);
		int[] pageParams = Page_Util.init(page, request);

		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
		List<ProcessInstance> processInstanceList = processInstanceQuery.listPage(pageParams[0], pageParams[1]);

		// 缓存流程定义
		Map<String, ProcessDefinition> definitionMap = new HashMap<String, ProcessDefinition>();
		for (ProcessInstance processInstance : processInstanceList) {
			definitionCache(definitionMap, processInstance.getProcessDefinitionId());
		}
		page.setResult(processInstanceList);
		page.setTotalCount(processInstanceQuery.count());
		mav.addObject("page", page);
		mav.addObject("definitions", definitionMap);

		return mav;
	}

	/**
	 * 更改流程实例的状态
	 * 
	 * @return
	 */
	@RequestMapping(value = "{state}/{processInstanceId}")
	public String changeState(@PathVariable("state") String state, @PathVariable("processInstanceId") String processInstanceId) {
		if (StringUtils.equals("active", state)) {
			runtimeService.activateProcessInstanceById(processInstanceId);
		} else {
			runtimeService.suspendProcessInstanceById(processInstanceId);
		}

		return "redirect:" + adminPath + "/act/processinstance/list";
	}

	/**
	 * 删除流程实例
	 * 
	 * @return
	 */
	@RequestMapping(value = "delete/{processInstanceId}")
	@ResponseBody
	public boolean delete(@PathVariable("processInstanceId") String processInstanceId,
			@RequestParam("deleteReason") String deleteReason) {
		runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
		return true;
	}

	/**
	 * 流程定义对象缓存
	 * 
	 * @param definitionMap
	 * @param processDefinitionId
	 */
	private void definitionCache(Map<String, ProcessDefinition> definitionMap, String processDefinitionId) {
		if (definitionMap.get(processDefinitionId) == null) {
			ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
			processDefinitionQuery.processDefinitionId(processDefinitionId);
			ProcessDefinition processDefinition = processDefinitionQuery.singleResult();

			// 放入缓存
			definitionMap.put(processDefinitionId, processDefinition);
		}
	}

}