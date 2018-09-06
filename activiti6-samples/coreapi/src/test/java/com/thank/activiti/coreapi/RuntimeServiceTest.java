package com.thank.activiti.coreapi;

import com.google.common.collect.ImmutableMap;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 一: 测试流程启动的几种方式
 * 二: 测试流程实例和执行流的查询
 * 三: 测试几种流程触发
 */
public class RuntimeServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(RuntimeServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * 测试启动流程的方式: 根据流程定义key
     * 区别: 每次部署流程, 流程定义ID都会变, 而Key不会变, 根据Key启动时每次使用的是最新版本
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testStartProcess(){
        Map<String, Object> params = ImmutableMap.of("key1", "value1");

        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", params);
        logger.debug("processInstance: {}", processInstance);
    }


    /**
     * 测试启动流程的方式: 根据流程定义ID
     * 区别: 每次部署流程, 流程定义ID都会变, 而Key不会变, 根据Key启动时每次使用的是最新版本
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testStartProcessById() {
        // 先获取processDefinitionId
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessDefinition processDefinition = activitiRule.getRepositoryService().createProcessDefinitionQuery().singleResult();
        Map<String, Object> params = ImmutableMap.of("key1", "value1");

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), params);
        logger.debug("processInstance: {}", processInstance);
    }


    /**
     * 测试启动流程的方式: 根据ProcessInstanceBuilder
     * 第三种启动方式
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testProcessInstanceBuilder() {
        Map<String, Object> params = ImmutableMap.of("key1", "value1");
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
        ProcessInstance processInstance = processInstanceBuilder.businessKey("businessKey001")
                .processDefinitionKey("my-process")
                .variables(params).start();
        logger.debug("processInstance: {}", processInstance);
    }


    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testVariables() {
        Map<String, Object> params = ImmutableMap.of("key1", "value1", "key2", "value2");
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", params);
        logger.debug("processInstance = {}", processInstance);

        // 新增一个
        runtimeService.setVariable(processInstance.getId(),"key3", "value3");

        // 修改一个
        runtimeService.setVariable(processInstance.getId(),"key2", "value2_update");

        Map<String, Object> variables = runtimeService.getVariables(processInstance.getId());
        logger.debug("variables: {}", variables);
    }


    /**
     * 测试流程实例的查询
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testProcessInstanceQuery() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        logger.debug("processInstance = {}", processInstance);

        ProcessInstance processInstance1 = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
        Assert.assertEquals(processInstance.getId(), processInstance1.getId());
    }


    /**
     * 测试执行对象查询
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testExecutionQuery() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        logger.debug("processInstance = {}", processInstance);

        List<Execution> executionList = runtimeService.createExecutionQuery().listPage(0, 10);
        logger.debug("executionList.size: {}", executionList.size());
        executionList.forEach(execution -> logger.debug("execution: {}", execution));
        Assert.assertEquals(2, executionList.size());
    }


    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-trigger.bpmn20.xml"})
    public void testTrigger() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        Execution execution = runtimeService.createExecutionQuery().activityId("someTask").singleResult();
        logger.debug("execution: {}", execution);
        Assert.assertNotNull(execution);

        runtimeService.trigger(execution.getId());

        execution = runtimeService.createExecutionQuery().activityId("someTask").singleResult();
        logger.debug("execution: {}", execution);
        Assert.assertNull(execution);
    }


    /**
     * 测试信号事件SignalEventReceived事件触发
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-signal-received.bpmn20.xml"})
    public void testSignalEventReceived(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        runtimeService.startProcessInstanceByKey("my-process");

        Execution execution = runtimeService.createExecutionQuery().signalEventSubscriptionName("my-signal").singleResult();
        logger.debug("execution: {}", execution);
        Assert.assertNotNull(execution);

        runtimeService.signalEventReceived("my-signal");

        execution = runtimeService.createExecutionQuery().signalEventSubscriptionName("my-signal").singleResult();
        logger.debug("execution: {}", execution);
        Assert.assertNull(execution);
    }


    /**
     * 测试消息事件MessageEventReceived事件触发
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-message-received.bpmn20.xml"})
    public void testMessageEventReceived(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        runtimeService.startProcessInstanceByKey("my-process");

        Execution execution = runtimeService.createExecutionQuery().messageEventSubscriptionName("my-message").singleResult();
        logger.debug("execution: {}", execution);
        Assert.assertNotNull(execution);

        // 注意: 与signalEventReceived的区别在这里
        runtimeService.messageEventReceived("my-message", execution.getId());

        execution = runtimeService.createExecutionQuery().messageEventSubscriptionName("my-message").singleResult();
        logger.debug("execution: {}", execution);
        Assert.assertNull(execution);
    }


    /**
     * 测试通过messageEventReceived通过message启动流程
     * tip: 它最终其实也是找key去启动的
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-message.bpmn20.xml"})
    public void testMessageStart(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByMessage("my-message");
//                .startProcessInstanceByKey("my-process");
        Assert.assertNotNull(processInstance);
    }
}
