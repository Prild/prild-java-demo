package com.thank.activiti.bpmn20;

import com.google.common.collect.ImmutableMap;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class GatewayTest {

    private static final Logger logger = LoggerFactory.getLogger(GatewayTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * 测试单一网关
     */
    @Test
    @Deployment(resources = {"my-process-exclusiveGateway1.bpmn20.xml"})
    public void testExclusiveGateway1() {
        Map<String, Object> variables = ImmutableMap.of("score", 91);
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        Assert.assertEquals("精英", task.getName());

        variables = ImmutableMap.of("score", 77);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);
        task = activitiRule.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        Assert.assertEquals("优秀", task.getName());
    }


    /**
     * 测试并行网关
     */
    @Test
    @Deployment(resources = {"my-process-parallelGateway1.bpmn20.xml"})
    public void testParallelGateway1() {
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");

        TaskService taskService = activitiRule.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).orderByTaskId().asc().list();

        Assert.assertEquals(2, taskList.size());
        Assert.assertEquals("确认支付", taskList.get(0).getName());
        Assert.assertEquals("确认收货", taskList.get(1).getName());

        taskService.complete(taskList.get(0).getId());
        taskService.complete(taskList.get(1).getId());

        Task task3 = taskService.createTaskQuery().singleResult();
        Assert.assertEquals("订单完成", task3.getName());
    }

}
