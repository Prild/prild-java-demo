package com.thank.activiti.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 使用脚手架工程中的demo
 */
public class ConfigHistoryLevelTest {

    private static final Logger logger = LoggerFactory.getLogger(ConfigHistoryLevelTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_history.cfg.xml");

    /**
     * 流程历史记录级别测试
     */
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void test() {

        // 1. 启动流程
        System.out.println("--------------启动流程-----------------");
        startProcessInstance();


        // 2. 修改变量
        System.out.println("--------------修改变量-----------------");
        modifyVariable();


        // 3. 提交表单 task
        System.out.println("--------------提交表单task-----------------");
        submitTaskFormData();

        // 4. 输出历史内容

        // 4.1 输出历史活动
        System.out.println("--------------历史活动-----------------");
        showHistoryActivity();

        // 4.2 输出历史变量
        System.out.println("--------------历史变量-----------------");
        showHistoryVariables();

        // 4.3 输出历史用户任务
        System.out.println("--------------历史任务-----------------");
        showHistoryTask();

        // 4.4 输出历史表单
        System.out.println("--------------历史表单-----------------");
        showHistoryForm();

        // 4.5 输出历史详情
        System.out.println("--------------历史详情-----------------");
        showHistoryDetail();
    }

    private void showHistoryDetail() {
        List<HistoricDetail> historicDetails = activitiRule.getHistoryService().createHistoricDetailQuery().listPage(0, 100);
        logger.debug("historicDetails size: {}", historicDetails.size());
        historicDetails.forEach(historicDetail -> logger.debug("historicDetail: {}", historicDetail));
    }

    private void showHistoryForm() {
        List<HistoricDetail> historicDetailsForm = activitiRule.getHistoryService().createHistoricDetailQuery().formProperties().listPage(0, 100);
        logger.debug("historicDetails size: {}", historicDetailsForm.size());
        historicDetailsForm.forEach(historicDetail -> logger.debug("historicDetail: {}", toString(historicDetail)));
    }

    private void showHistoryTask() {
        List<HistoricTaskInstance> historicTaskInstances = activitiRule.getHistoryService().createHistoricTaskInstanceQuery().listPage(0, 100);
        logger.debug("historicTaskInstances size: {}", historicTaskInstances.size());
        historicTaskInstances.forEach(historicTaskInstance -> logger.debug("historicTaskInstance: {}", historicTaskInstance));
    }

    private void showHistoryVariables() {
        List<HistoricVariableInstance> historicVariableInstances = activitiRule.getHistoryService().createHistoricVariableInstanceQuery().listPage(0, 100);
        logger.debug("historicVariableInstance size: {}", historicVariableInstances.size());
        historicVariableInstances.forEach(historicVariableInstance -> logger.debug("historicVariableInstance: {}", historicVariableInstance));
    }

    private void showHistoryActivity() {
        List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService()
                .createHistoricActivityInstanceQuery().listPage(0, 100);
        logger.debug("historicActivityInstances size: {}", historicActivityInstances.size());
        historicActivityInstances.forEach(historicActivityInstance -> logger.debug("historicActivityInstance: {}", historicActivityInstance));
    }

    private void submitTaskFormData() {
        // 通过表单提交完成Task
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        Map<String, String> formProperties = ImmutableMap.of("formKey1", "formValue1", "formKey2", "formValue2");
        activitiRule.getFormService().submitTaskFormData(task.getId(), formProperties);
    }

    private void modifyVariable() {
        // 执行对象集合
        List<Execution> executions = activitiRule.getRuntimeService().createExecutionQuery().listPage(0, 100);
        executions.forEach(execution -> logger.debug("execution={}", execution));
        logger.debug("executions size is : {}", executions.size());
        String executionId = executions.iterator().next().getId();
        activitiRule.getRuntimeService().setVariable(executionId, "keyStart1", "value1_update");
    }

    private void startProcessInstance() {
        // 构造参数
        Map<String, Object> params = ImmutableMap.of("keyStart1", "value1", "keyStart2", "value2");
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", params);
    }

    static String toString(HistoricDetail historicDetail){
        return ToStringBuilder.reflectionToString(historicDetail, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
