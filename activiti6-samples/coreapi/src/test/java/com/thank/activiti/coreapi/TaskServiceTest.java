package com.thank.activiti.coreapi;

import com.google.common.collect.ImmutableMap;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.*;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class TaskServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();


    /**
     * 测试任务中几种变量的区别, complete任务
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskService(){
        Map<String, Object> message = ImmutableMap.of("message", "hello world");
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", message);

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        logger.debug("task: {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        logger.debug("task description: {}", task.getDescription());
        Assert.assertTrue(task.getDescription().contains("hello world"));

        taskService.setVariable(task.getId(), "key1", "value1");
        taskService.setVariableLocal(task.getId(), "localKey1", "localValue1");

        Map<String, Object> taskVariables = taskService.getVariables(task.getId());
        Map<String, Object> taskLocalVariables = taskService.getVariablesLocal(task.getId());
        Map<String, Object> processVariables = activitiRule.getRuntimeService().getVariables(task.getExecutionId());
        logger.debug("taskVariables: {}", taskVariables);
        logger.debug("taskLocalVariables: {}", taskLocalVariables);
        logger.debug("processVariables: {}", processVariables);
        Assert.assertTrue(taskVariables.containsKey("key1") && taskVariables.containsKey("localKey1") && taskVariables.containsKey("message"));
        Assert.assertTrue(taskLocalVariables.containsKey("localKey1"));
        Assert.assertTrue(processVariables.containsKey("key1") && processVariables.containsKey("message"));

        Map<String, Object> completeVar = ImmutableMap.of("cKey", "cValue");
        taskService.complete(task.getId(), completeVar);

        Task result = taskService.createTaskQuery().taskId(task.getId()).singleResult();
        logger.debug("result: {}", result);
        Assert.assertNull(result);
    }


    /**
     * 测试任务的候选人, 办理人和办理任务
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskServiceUser(){
        Map<String, Object> message = ImmutableMap.of("message", "hello world");
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", message);

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        logger.debug("task: {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        logger.debug("task description: {}", task.getDescription());
        Assert.assertTrue(task.getDescription().contains("hello world"));

        taskService.setOwner(task.getId(), "thank");

        // 可以设置, 但是不建议在程序中这样设置, 不安全, 不如使用claim来指定
//        taskService.setAssignee(task.getId(), "thank");

        // 查询出候选人有thank但是未指定办理人的任务
        List<Task> taskList = taskService.createTaskQuery().taskCandidateUser("thank").taskUnassigned()
                .listPage(0, 100);
        logger.debug("taskList.size: {}", taskList.size());

        for (Task task1 : taskList) {
            try {
                // 如果有这个, 会进catch异常
//                taskService.claim(task1.getId(), "thank222");

                // 指定任务办理人(代办人)
                taskService.claim(task1.getId(), "thank");
            } catch (Exception e) {
                logger.error("办理人已经指定了, 不能再指定");
            }
        }


        // 查询该任务的所有用户权限信息: 包括任务的拥有者, 候选人, 代办人
        List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(task.getId());
        identityLinkList.forEach(identityLink -> logger.debug("identityLink = {}", identityLink));

        // 查询办理人是thank的任务
        List<Task> thankTaskList = taskService.createTaskQuery().taskAssignee("thank").listPage(0, 100);

        // complete task
        ImmutableMap<String, Object> remark = ImmutableMap.of("remark", "i agree!");
        thankTaskList.forEach(thankTask -> taskService.complete(thankTask.getId(), remark));

        thankTaskList = taskService.createTaskQuery().taskAssignee("thank").list();
        Assert.assertTrue(thankTaskList==null || thankTaskList.size()== 0);
    }


    /**
     * 测试任务附件的创建&查询
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskServiceAttachment() {
        Map<String, Object> message = ImmutableMap.of("message", "hello world");
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", message);

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();

        // 两种方式: 1.字符串URL, 2.文件流
        taskService.createAttachment("url", task.getId(), task.getProcessInstanceId(),
                "beautiful picture" , "desc", "http://xxx.jpg");

        List<Attachment> attachmentList = taskService.getTaskAttachments(task.getId());
        attachmentList.forEach(attachment -> logger.debug("taskAttachment = {}", ToStringBuilder.reflectionToString(logger, ToStringStyle.JSON_STYLE)));
    }


    /**
     * 测试任务评论的创建&查询
     * 测试事件记录和任务评论的区别
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskServiceCommentAndEventRecord() {
        Map<String, Object> message = ImmutableMap.of("message", "hello world");
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", message);

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();

        // 随便设置一下任务, 为了看事件记录
        taskService.setOwner(task.getId(),"user1");
        taskService.setAssignee(task.getId(),"jimmy");

        // create
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "my comment 1");
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "my comment 2");

        // query comment
        List<Comment> commentList = taskService.getTaskComments(task.getId());
        commentList.forEach(comment ->  logger.debug("taskComment = {}",ToStringBuilder.reflectionToString(comment,ToStringStyle.JSON_STYLE)));

        // query event record
        List<Event> eventList = taskService.getTaskEvents(task.getId());
        eventList.forEach(event -> logger.debug("event = {}",ToStringBuilder.reflectionToString(event,ToStringStyle.JSON_STYLE)));
    }
}

