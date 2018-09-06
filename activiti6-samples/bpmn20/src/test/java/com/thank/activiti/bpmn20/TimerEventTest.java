package com.thank.activiti.bpmn20;


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

public class TimerEventTest {

    private static final Logger logger = LoggerFactory.getLogger(TimerEventTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * 测试TimeTask 触发边界事件
     * 触发条件: commonTask持续5秒(<timeDuration>PT5S</timeDuration>)
     * 以上就会触发边界事件 -> timeout task
     */
    @Test
    @Deployment(resources = {"my-process-timer-boundary.bpmn20.xml"})
    public void testTimerBoundary() throws InterruptedException {
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");

        List<Task> taskList = activitiRule.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).list();
        logger.debug("taskList.size: {}", taskList.size());
        taskList.forEach(task -> {
            logger.debug("task.name: {}", task.getName());
            Assert.assertEquals("common task", task.getName());
        });

        // 睡眠>5s, 就能触发边界事件啦
        Thread.sleep(1000 * 10);

        taskList = activitiRule.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).list();
        logger.debug("taskList.size: {}", taskList.size());
        taskList.forEach(task -> {
            logger.debug("task.name: {}", task.getName());
            Assert.assertEquals("timeout task", task.getName());
        });
    }
}
