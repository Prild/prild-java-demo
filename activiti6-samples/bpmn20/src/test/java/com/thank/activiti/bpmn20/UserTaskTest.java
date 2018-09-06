package com.thank.activiti.bpmn20;

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

public class UserTaskTest {

    private static final Logger logger = LoggerFactory.getLogger(UserTaskTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * 测试xml中定义任务候选人, 侯选组, 代办人
     */
    @Test
    @Deployment(resources = {"my-process-usertask.bpmn20.xml"})
    public void testUserTask() {
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        TaskService taskService = activitiRule.getTaskService();

        Task user1Task = taskService.createTaskQuery().taskCandidateUser("user1").singleResult();
        Task user2Task = taskService.createTaskQuery().taskCandidateUser("user2").singleResult();
        Task group1Task = taskService.createTaskQuery().taskCandidateGroup("group1").singleResult();
        Assert.assertEquals(user1Task.getId(), user2Task.getId());
        Assert.assertEquals(user1Task.getId(), group1Task.getId());

        // 设置代办人
//        taskService.setAssignee(user1Task.getId(), "user1"); // 不推荐
        taskService.claim(user1Task.getId(), "user1");
        logger.debug("task:{} claim assignee user: {}", user1Task.getName(), "user01");

        // 当设置完代办人后, 就会清空候选人和侯选组, 不信来看
        user1Task = taskService.createTaskQuery().taskCandidateOrAssigned("user1").singleResult();
        user2Task = taskService.createTaskQuery().taskCandidateOrAssigned("user2").singleResult();
        group1Task = taskService.createTaskQuery().taskCandidateGroup("group1").singleResult();
        Assert.assertNotNull(user1Task);
        Assert.assertNull(user2Task);
        Assert.assertNull(group1Task);
    }


    /**
     * 测试实现监听方式配置
     *    task create阶段: 设置任务候选人, 侯选组, 限制时长
     *    task complete阶段: 打印
     */
    @Test
    @Deployment(resources = {"my-process-usertask2.bpmn20.xml"})
    public void testUserTask2() {
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().taskCandidateUser("user1").singleResult();
        logger.debug("find by user1 task ={}", task);
        taskService.complete(task.getId());
    }
}
