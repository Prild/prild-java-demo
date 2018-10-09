package com.thank.activiti.coreapi;

import com.google.common.collect.ImmutableMap;
import org.activiti.engine.FormService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
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

public class FormServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(FormServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-form.bpmn20.xml"})
    public void testFormService(){
        FormService formService = activitiRule.getFormService();
        ProcessDefinition processDefinition = activitiRule.getRepositoryService().createProcessDefinitionQuery().singleResult();

        // 查询startFormKey
        String startFormKey = formService.getStartFormKey(processDefinition.getId());
        logger.debug("startFormKey: {}", startFormKey);

        // 查询startFormData
        StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
        List<FormProperty> startFormPropertyList = startFormData.getFormProperties();
        startFormPropertyList.forEach(formProperty -> logger.debug("start formProperty = {}", ToStringBuilder.reflectionToString(formProperty,ToStringStyle.JSON_STYLE)));

        Map<String, String> startFormParams = ImmutableMap.of("message", "hello world");
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), startFormParams);

        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();

        String taskFormKey = formService.getTaskFormKey(processDefinition.getId(), task.getTaskDefinitionKey());
        logger.debug("taskFormKey: {}", taskFormKey);

        // 查询taskFormData
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormProperty> taskFormPropertyList = taskFormData.getFormProperties();
        taskFormPropertyList.forEach(formProperty -> logger.debug("task formProperty = {}", ToStringBuilder.reflectionToString(formProperty,ToStringStyle.JSON_STYLE)));

        Map<String, String> eventFormParams = ImmutableMap.of("yesOrNo", "yes");
        formService.submitTaskFormData(task.getId(), eventFormParams);

        Task result = activitiRule.getTaskService().createTaskQuery().taskId(task.getId()).singleResult();
        Assert.assertNull(result);
    }

}



