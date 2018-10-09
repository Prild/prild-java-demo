package com.thank.activiti.dbentity;

import com.google.common.collect.ImmutableMap;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DbHistoryTest {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DbHistoryTest.class);
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");
    /**
     * 测试: 分别对应ACT_HI_*中的多张表
     */
    @Test
    public void testDeploy() {
        activitiRule.getRepositoryService().createDeployment()
                .name("测试部署").addClasspathResource("my-process.bpmn20.xml").deploy();

        RuntimeService runtimeService = activitiRule.getRuntimeService();

        Map<String, Object> variables = ImmutableMap.of("key1", "value1", "key2", "value2", "key3", "value3");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        
        runtimeService.setVariable(processInstance.getId(), "key1", "value1_update");

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        taskService.setOwner(task.getId(), "thank");
        taskService.addComment(task.getId(), processInstance.getId(), "comment 1");
        taskService.addComment(task.getId(), processInstance.getId(), "comment 2");
        taskService.createAttachment("url", task.getId(),processInstance.getId(), "name","desc", "/url/test.png");

        Map<String, String> formVariables = ImmutableMap.of("key2", "value2_update", "key3", "value3_update");

        // 通过form方式提交的任务表单会存储在ACT_HI_DETAIL(历史变更)中, 而task方式不会
        activitiRule.getFormService().submitTaskFormData(task.getId(), formVariables);
    }
}
