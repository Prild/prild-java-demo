package com.thinkgem.jeesite.modules.act.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.utils.Page_;
import com.thinkgem.jeesite.modules.act.utils.Page_Util;

/**
 * 已归档流程实例 User: henryyan
 */
@Controller
@RequestMapping(value = "${adminPath}/act/history")
public class HistoryProcessInstanceController extends BaseController {

	@Autowired
	HistoryService historyService;

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	RuntimeService runtimeService;

	/**
	 * 查询已结束流程实例
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ModelAndView finishedProcessInstanceList(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("chapter6/finished-process");
		Page_<HistoricProcessInstance> page = new Page_<HistoricProcessInstance>(Page_Util.PAGE_SIZE);
		int[] pageParams = Page_Util.init(page, request);
		/**
		 * 1、使用HistoryService的createHistoricProcessInstanceQuery（）
		 * 方法就可以得到HistoricProcessInstanceQuery对象，
		 * 该对象主要用于流程实例的历史数据查询。对于流程实例，不管流程是否完成，都会保存到ACT_HI_PROCINST表中。
		 * 2、如果使用finished()则查询已经完成的流程实例，条件有可能是查看是否 有值：完成时间 END_TIME_ END_ACT_ID_
		 */
		HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
				.finished();// ACT_HI_PROCINST历史流程实例查询
		List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery.listPage(pageParams[0],
				pageParams[1]);// listPage前面是从第几条开始，后面是一共几条

		// 查询流程定义对象
		Map<String, ProcessDefinition> definitionMap = new HashMap<String, ProcessDefinition>();

		for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
			definitionCache(definitionMap, historicProcessInstance.getProcessDefinitionId());
		}

		page.setResult(historicProcessInstances);// 设置记录列表list
		page.setTotalCount(historicProcessInstanceQuery.count());
		mav.addObject("page", page);
		mav.addObject("definitions", definitionMap);

		return mav;
	}

	/**
	 * 查询已结束流程实例
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "finished/manager")
	public ModelAndView finishedProcessInstanceListForManager(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("chapter6/finished-process-manager");
		Page_<HistoricProcessInstance> page = new Page_<HistoricProcessInstance>(Page_Util.PAGE_SIZE);
		int[] pageParams = Page_Util.init(page, request);
		HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
				.finished();
		List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery.listPage(pageParams[0],
				pageParams[1]);

		// 查询流程定义对象
		Map<String, ProcessDefinition> definitionMap = new HashMap<String, ProcessDefinition>();

		for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
			definitionCache(definitionMap, historicProcessInstance.getProcessDefinitionId());
		}

		page.setResult(historicProcessInstances);
		page.setTotalCount(historicProcessInstanceQuery.count());
		mav.addObject("page", page);
		mav.addObject("definitions", definitionMap);

		return mav;
	}

	/**
	 * 查询历史相关信息
	 * 
	 * @param processInstanceId
	 * @return
	 */
	@RequestMapping(value = "finished/view/{processInstanceId}")
	public ModelAndView historyDatas(@PathVariable("processInstanceId") String processInstanceId) {
		ModelAndView mav = new ModelAndView("chapter6/view-finished-process");

		List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId).list();

		// 查询历史流程实例
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();

		// 查询流程有关的变量
		List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery()
				.processInstanceId(processInstanceId).list();

		List<HistoricDetail> formProperties = historyService.createHistoricDetailQuery().processInstanceId(processInstanceId)
				.formProperties().list();

		// 查询流程定义对象
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();

		mav.addObject("historicProcessInstance", historicProcessInstance);
		mav.addObject("variableInstances", variableInstances);
		mav.addObject("activities", activityInstances);
		mav.addObject("formProperties", formProperties);
		mav.addObject("processDefinition", processDefinition);

		return mav;
	}

	/**
	 * 删除历史流程数据
	 * 
	 * @param processInstanceId
	 * @return
	 */
	@RequestMapping(value = "finished/delete/{processInstanceId}")
	public String deleteProcessInstance(@PathVariable("processInstanceId") String processInstanceId,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", "ID为" + processInstanceId + "的历史流程已删除！");
		historyService.deleteHistoricProcessInstance(processInstanceId);
		return "redirect:" + adminPath + "/act/history/finished/manager";
	}

	/**
	 * 流程定义对象缓存
	 * 
	 * @param definitionMap
	 * @param processDefinitionId
	 */
	private void definitionCache(Map<String, ProcessDefinition> definitionMap, String processDefinitionId) {
		if (definitionMap.get(processDefinitionId) == null) {
			// 通过流程定义ID查询出流程定义对象
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(processDefinitionId).singleResult();

			// 放入缓存
			definitionMap.put(processDefinitionId, processDefinition);
		}
	}

}
