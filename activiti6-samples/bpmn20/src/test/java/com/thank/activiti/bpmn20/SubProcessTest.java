package com.thank.activiti.bpmn20;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SubProcessTest {

    private static final Logger logger = LoggerFactory.getLogger(SubProcessTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * 测试普通子流程正常流转
     */
    @Test
    @Deployment(resources = {"my-process-subprocess1.bpmn20.xml"})
    public void testSubProcess1() {
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        Assert.assertEquals("订单完成", task.getName());
    }


    /**
     * 测试普通子流程触发异常边界(触发条件写在了MyJavaPayDelegate)
     */
    @Test
    @Deployment(resources = {"my-process-subprocess1.bpmn20.xml"})
    public void testSubProcess2() {

        // 触发一个错误事件(传errorFlag=true触发)
        Map<String, Object> variables = ImmutableMap.of("errorFlag", true);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);

        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        Assert.assertEquals("异常处理", task.getName());

        // 测试变量获取情况
        Map<String, Object> result = activitiRule.getRuntimeService().getVariables(processInstance.getId());
        Assert.assertTrue(result.keySet().contains("key1"));
        Assert.assertFalse(result.keySet().contains("localKey1"));
    }


    /**
     * 测试事件子流程
     */
    @Test
    @Deployment(resources = {"my-process-subprocess2.bpmn20.xml"})
    public void testSubProcess3() {
        Map<String, Object> variables = ImmutableMap.of("errorFlag", true);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        Assert.assertEquals("异常处理", task.getName());

        // 测试变量获取情况
        Map<String, Object> result = activitiRule.getRuntimeService().getVariables(processInstance.getId());
        Assert.assertTrue(result.keySet().contains("key1"));
        Assert.assertTrue(result.keySet().contains("localKey1"));
    }


    /**
     * 测试调用式子流程
     */
    @Test
    @Deployment(resources = {"my-process-subprocess3.bpmn20.xml", "my-process-subprocess4.bpmn20.xml"})
    public void testSubProcess4() {
        // 发生异常情况
        Map<String, Object> variables = ImmutableMap.of("errorFlag", true, "key0", "value0");
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        Assert.assertEquals("异常处理", task.getName());

        // 测试变量获取情况
        Map<String, Object> result = activitiRule.getRuntimeService().getVariables(processInstance.getId());
        logger.debug("result: {}", result);
        Assert.assertTrue(result.keySet().contains("key0"));
        Assert.assertFalse(result.keySet().contains("key1"));
        Assert.assertFalse(result.keySet().contains("localKey1"));


        // 不发生异常, 正常情况
        variables = ImmutableMap.of("errorFlag", false, "key0", "value0");
        processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);
        task = activitiRule.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        Assert.assertEquals("订单完成", task.getName());

        // 测试变量获取情况
        result = activitiRule.getRuntimeService().getVariables(processInstance.getId());
        logger.debug("result: {}", result);
        Assert.assertTrue(result.keySet().contains("key0"));
        Assert.assertTrue(result.keySet().contains("key1"));
        Assert.assertFalse(result.keySet().contains("key2"));
        Assert.assertFalse(result.keySet().contains("localKey1"));

    }
}
