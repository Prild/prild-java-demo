package com.thank.activiti.bpmn20;

import com.google.common.collect.ImmutableMap;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

import java.util.List;
import java.util.Map;

/**
 * 评审销售领导的申请流程
 *   测试: 错误事件的较为复杂的例子
 *   其中包括了错误事件, 边界事件, 并行网关, 排他网关等知识点
 */
public class BoundaryErrorEventTest extends PluggableActivitiTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Normally the UI will do this automatically for us
        Authentication.setAuthenticatedUserId("kermit");
    }

    @Override
    protected void tearDown() throws Exception {
        Authentication.setAuthenticatedUserId(null);
        super.tearDown();
    }

    @Deployment(resources = { "reviewSalesLead.bpmn20.xml" })
    public void testReviewSalesLeadProcess() {
        // After starting the process, a task should be assigned to the 'initiator' (normally set by GUI)
        Map<String, Object> variables = ImmutableMap.of("customerName", "thank", "details", "very nice");
        String processInstanceId = runtimeService.startProcessInstanceByKey("reviewSaledLead", variables).getId();

        Task task = taskService.createTaskQuery().taskAssignee("kermit").singleResult();
        assertEquals("Provide new sales lead", task.getName());

        // 完成task后, 评审的子流程将会被激活
        taskService.complete(task.getId());

        // 接下来, 经过并行网关, 会有两个并行的task
        // 1.reviewCustomerRating (评审客户等级) 对应候选组: accountancy(会计组)
        // 2.reviewProfitability (评审盈利状况) 对应侯选组: management(管理组)
        Task ratingTask = taskService.createTaskQuery().taskCandidateGroup("accountancy").singleResult();
        Task profitabilityTask = taskService.createTaskQuery().taskCandidateGroup("management").singleResult();
        assertEquals("Review customer rating", ratingTask.getName());
        assertEquals("Review profitability", profitabilityTask.getName());

        // 在profitabilityTask中, 会判断参数notEnoughInformation
        // true: 表示Not enough info, 会抛出一个错误事件(error event), 并关闭该子流程
        // false: 表示Enough info, 可以正常完成
        // 这里我们传true, 模拟抛出错误
        variables = ImmutableMap.of("notEnoughInformation", true);
        taskService.complete(profitabilityTask.getId(), variables);

        // 这时边界任务catchNotEnoughInformationError会捕捉到这个事件错误
        // 进而激活任务: provideAdditionalDetails(提供更多details)
        Task provideDetailsTask = taskService.createTaskQuery().taskAssignee("kermit").singleResult();
        assertEquals("Provide additional details", provideDetailsTask.getName());

        // 当provideDetailsTask完成后, 将会再次激活评审的子流程
        taskService.complete(provideDetailsTask.getId());
        List<Task> reviewTaskList = taskService.createTaskQuery().orderByTaskName().asc().list();
        assertEquals("Review customer rating", reviewTaskList.get(0).getName());
        assertEquals("Review profitability", reviewTaskList.get(1).getName());

        // 下面我们传notEnoughInformation=false来正常结束这两个并行任务
        taskService.complete(reviewTaskList.get(0).getId());
        taskService.complete(reviewTaskList.get(1).getId(), ImmutableMap.of("notEnoughInformation", false));

        // 并行任务结束后, 经过storeLeadInCrmSystem(保存该领导到CRM系统)任务, 整个流程就结束了
        List<Task> finalTaskList = taskService.createTaskQuery().list();
        assertEquals(0, finalTaskList.size());
        assertProcessEnded(processInstanceId);
    }

}
