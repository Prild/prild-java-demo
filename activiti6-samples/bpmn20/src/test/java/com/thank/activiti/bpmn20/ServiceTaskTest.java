package com.thank.activiti.bpmn20;

import com.google.common.collect.ImmutableMap;
import org.activiti.engine.ActivitiEngineAgenda;
import org.activiti.engine.ManagementService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ServiceTaskTest {

    private static final Logger logger = LoggerFactory.getLogger(ServiceTaskTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * 测试JavaDelegate方式的ServiceTask
     */
    @Test
    @Deployment(resources = {"my-process-servicetask1.bpmn20.xml"})
    public void testServiceTask() {
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        List<HistoricActivityInstance> activityInstanceList = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().orderByHistoricActivityInstanceEndTime().asc().list();
        activityInstanceList.forEach(historicActivityInstance -> logger.debug("activity: {}", historicActivityInstance));
        Assert.assertEquals(3, activityInstanceList.size());
        Assert.assertEquals("Service task", activityInstanceList.get(1).getActivityName());
    }


    /**
     * 测试ActivityBehavior方式的ServiceTask
     */
    @Test
    @Deployment(resources = {"my-process-servicetask2.bpmn20.xml"})
    public void testServiceTask2() {
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        List<HistoricActivityInstance> activityInstanceList = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().orderByHistoricActivityInstanceEndTime().asc().list();
        activityInstanceList.forEach(historicActivityInstance -> logger.debug("activity: {}", historicActivityInstance));

        Assert.assertEquals(2, activityInstanceList.size());
        Assert.assertNotEquals("Service task", activityInstanceList.get(1).getActivityName());

        // 该任务正在执行中
        Execution execution = activitiRule.getRuntimeService().createExecutionQuery().activityId("someTask").singleResult();
        Assert.assertNotNull(execution);

        // 不能像taskService.complete那样完成, 那该怎么向后驱动完成这个任务呢
        ManagementService managementService = activitiRule.getManagementService();
        managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
                ActivitiEngineAgenda agenda = commandContext.getAgenda();
                agenda.planTakeOutgoingSequenceFlowsOperation((ExecutionEntity) execution, false);
                return null;
            }
        });

        Execution someTaskExecution = activitiRule.getRuntimeService().createExecutionQuery().activityId("someTask").singleResult();
        Assert.assertNull(someTaskExecution);
    }


    /**
     * 测试JavaDelegate方式的ServiceTask 并传值表达式
     */
    @Test
    @Deployment(resources = {"my-process-servicetask3.bpmn20.xml"})
    public void testServiceTask3() {
        Map<String, Object> variables = ImmutableMap.of("desc", "hello world");
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);
    }
}
