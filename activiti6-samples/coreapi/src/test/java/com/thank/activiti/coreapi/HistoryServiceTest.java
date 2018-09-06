package com.thank.activiti.coreapi;

import com.google.common.collect.ImmutableMap;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class HistoryServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(HistoryServiceTest.class);

    // 为了展示历史数据的全貌, 这里的配置文件中设置了流程历史的级别为: FULL
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg.historyAll.xml");

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testHistoryService() {
        Map<String, Object> normalVariables = ImmutableMap.of("key1", "value1", "key2", "value2");
        Map<String, Object> transientVariables = ImmutableMap.of("tsKey", "tsValue");
        ProcessInstance processInstance = activitiRule.getRuntimeService().createProcessInstanceBuilder()
                .processDefinitionKey("my-process")
                // 设置普通变量和瞬时变量(瞬时变量不会记录在历史库中)
                .variables(normalVariables).transientVariables(transientVariables)
                .start();

        // 修改变量
        activitiRule.getRuntimeService().setVariable(processInstance.getId(), "key1", "value1_update");

        Task task = activitiRule.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        Map<String, String> formVariables = ImmutableMap.of("formKey1", "formValue1");
        activitiRule.getFormService().submitTaskFormData(task.getId(), formVariables);

        // 以上先跑完整个流程, 下面来查看历史数据
        HistoryService historyService = activitiRule.getHistoryService();

        // 查询: 历史流程实例实体类
        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().list();
        historicProcessInstanceList.forEach(historicProcessInstance -> logger.debug("historicProcessInstance: {}", ToStringBuilder.reflectionToString(historicProcessInstance, ToStringStyle.JSON_STYLE)));

        // 查询: 单个活动节点执行的信息
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().list();
        historicActivityInstanceList.forEach(historicActivityInstance -> logger.debug("historicActivityInstance: {}", historicActivityInstance));

        // 查询: 用户任务实例的信息
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().list();
        historicTaskInstanceList.forEach(historicTaskInstance -> logger.debug("historicTaskInstance: {}", ToStringBuilder.reflectionToString(historicTaskInstance,ToStringStyle.JSON_STYLE)));

        // 查询: 流程或任务变量值的实体
        List<HistoricVariableInstance> historicVariableInstanceList = historyService.createHistoricVariableInstanceQuery().list();
        historicVariableInstanceList.forEach(historicVariableInstance -> logger.debug("historicVariableInstance: {}", historicVariableInstance));

        // 查询: 历史流程活动任务的详细信息
        List<HistoricDetail> historicDetailList = historyService.createHistoricDetailQuery().list();
        historicDetailList.forEach(historicDetail -> logger.debug("historicDetail: {}", historicDetail));

        ProcessInstanceHistoryLog historyLog = historyService.createProcessInstanceHistoryLogQuery(processInstance.getId())
                .includeActivities()
                .includeComments()
                .includeFormProperties()
                .includeTasks()
                .includeVariables()
                .includeVariableUpdates()
                .singleResult();

        List<HistoricData> historicDataList = historyLog.getHistoricData();
        historicDataList.forEach(historicData -> logger.debug("historicData: {}", historicData));

        // 删除历史
        historyService.deleteHistoricProcessInstance(processInstance.getId());

        HistoricProcessInstance result = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
        Assert.assertNull(result);
    }


}



