package com.thank.activiti.dbentity;

import com.google.common.collect.Maps;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * description:
 */
public class DbRuntimeTest {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DbRuntimeTest.class);
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testRuntime() {
        activitiRule.getRepositoryService().createDeployment()
                .name("二次审批流程")
                .addClasspathResource("second_approve.bpmn20.xml")
                .deploy();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        activitiRule.getRuntimeService().startProcessInstanceByKey("second_approve", variables);
    }

    /**
     * 测试设置拥有人(ACT_RU_IDENTITYLINK: 参与者信息表)
     */
    @Test
    public void testSetOwner() {
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionId("second_approve").singleResult();
        taskService.setOwner(task.getId(), "thank");
    }

    /**
     * 设置开始节点中message事件订阅(ACT_RU_EVENT_SUBSCR: 事件订阅/监听信息表)
     * 接受到这个信号, 就可以启动流程实例
     */
    @Test
    public void testMessage() {
        activitiRule.getRepositoryService().createDeployment().addClasspathResource("my-process-message.bpmn20.xml").deploy();
    }

    /**
     * 设置了节点中的message事件订阅
     * 只有当流程定义的实例启动后, 才能监听到这个订阅信号, 所以在ACT_RU_EVENT_SUBSCR会有对应的流程实例ID和执行ID
     */
    @Test
    public void testMessageReceived() {
        activitiRule.getRepositoryService().createDeployment().addClasspathResource("my-process-message-received.bpmn20.xml").deploy();
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
    }

    @Test
    public void testJob() throws InterruptedException {
        activitiRule.getRepositoryService().createDeployment()
                .addClasspathResource("my-process-job.bpmn20.xml")
                .deploy();

        Thread.sleep(1000*30L);
    }
}
