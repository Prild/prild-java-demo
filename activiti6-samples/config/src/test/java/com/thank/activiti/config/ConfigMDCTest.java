package com.thank.activiti.config;

import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 使用脚手架工程中的demo
 */
public class ConfigMDCTest {

    private static final Logger logger = LoggerFactory.getLogger(ConfigMDCTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * MDC默认是只有在发生异常时才会打印MDC信息,
     * 所以这里模拟了一个发生异常的流程文件, 可以看到ERROR日志中打印了MDC信息
     * MDC信息在logback.xml中配置具体打印哪些信息
     *
     * 但是这种方式也许不太好, 如果我们需要所有的(包括没发生异常的)都打印MDC信息,
     * 可以使用拦截器的方式来实现 {@link ConfigMDCCustomTest}
     */
    @Test
    @Deployment(resources = {"my-process_mdc_error.bpmn20.xml"})
    public void testMDCError() {
        LogMDC.setMDCEnabled(true);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        assertNotNull(processInstance);
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        assertEquals("Activiti is awesome!", task.getName());
        activitiRule.getTaskService().complete(task.getId());
    }

}
