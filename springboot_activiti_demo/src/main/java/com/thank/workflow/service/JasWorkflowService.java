package com.thank.workflow.service;

import org.activiti.engine.*;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * description: none
 *
 * @author thank
 * 2017/12/26 10:06
 */
@Service
public class JasWorkflowService {

    @Resource
    private ProcessEngine processEngine;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private FormService formService;
    @Resource
    private HistoryService historyService;
    @Resource
    private IdentityService identityService;


    /**
     * 根据业务ID查询是否存在流程(--查询该条业务数据是否已经发起了流程)
     */
    public boolean isExistsWorkflow(String bussinessEventId) {

        HistoricProcessInstance historicProcessInstance = this.historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(bussinessEventId).singleResult();
        return historicProcessInstance!=null ;
    }


    /**
     * 判断给定用户是否可以发起给定流程名称的流程
     */
    public boolean isCanInitializeWorkflow(String userId, String bussinessId, String workflowName) {

        if(this.isExistsWorkflow(bussinessId)) {
            return false;
        }

        TaskDefinition firstTaskDefinition = this.getFirstTaskDefinition(workflowName);
        Set<User> candidateUserList = this.getCandidateUserList(firstTaskDefinition);

        for (User user: candidateUserList) {
            if (userId.equals(user.getId())) {
                return true;
            }
        }
        return false;
    }



    public String initializeWorkflow(String workflowName, String userId, String businessEventId, Map<String, Object> initializeVariables,
                                     boolean completeFirstNode) {

        if (this.isExistsWorkflow(businessEventId)) {
            return "-1";
        }

        User user = this.getUser(userId);
        initializeVariables.put("unit", "#".concat(user.getLastName()));
        this.identityService.setAuthenticatedUserId(userId);

        ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery().processDefinitionKey(workflowName).latestVersion().singleResult();
        String processCategory = processDefinition.getCategory();
        Map<String, Object> flowVariables = new HashMap<>();
        flowVariables.put("processCategory", processCategory);
        flowVariables.put("workflowSubject", initializeVariables.get("workflowSubject"));

        ProcessInstance processInstance = null;
        if (businessEventId!=null) {
            processInstance = this.runtimeService.startProcessInstanceByKey(workflowName, businessEventId, flowVariables);
        } else {
            processInstance = this.runtimeService.startProcessInstanceByKey(workflowName, flowVariables);
        }

        String flowInstanceId = processInstance.getId();

        if (completeFirstNode) {

        }

        return null;

    }





/*********************************************************************************************************/

    /**
     * 根据流程名称获取第一个任务节点的定义
     * @param workflowName: 流程名称, 流程定义的KEY
     */
    private TaskDefinition getFirstTaskDefinition(String workflowName) {

        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) this.repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(workflowName).latestVersion().singleResult();

        processDefinitionEntity = this.getProcessDefinitionEntity(processDefinitionEntity.getId());

        Map<String, TaskDefinition> taskDefinitionMap = processDefinitionEntity.getTaskDefinitions();
        ActivityImpl activityImpl = processDefinitionEntity.getInitial();
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        PvmTransition pvmTransition = pvmTransitionList.get(0);
        ActivityImpl activity = (ActivityImpl)pvmTransition.getDestination();
        return taskDefinitionMap.get(activity.getId());
    }


    /**
     * 根据流程定义ID获取流程定义实体对象
     */
    private ProcessDefinitionEntity getProcessDefinitionEntity(String processDefinitionId) {

        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)((RepositoryServiceImpl)this.repositoryService)
                .getDeployedProcessDefinition(processDefinitionId);

        return processDefinitionEntity;
    }


    /**
     * 获取给定任务定义中的任务办理候选人
     */
    private Set<User> getCandidateUserList(TaskDefinition taskDefinition) {

        Set<User> userSet = new HashSet<>();
        Set<Expression> userIdExpressions = taskDefinition.getCandidateUserIdExpressions();

        for (Expression expression: userIdExpressions) {
            String expressionText = expression.getExpressionText();
            User user = this.getUser(expressionText);
            if (null != user) {
                userSet.add(user);
            }
        }

        Set<Group> candidateGroupList = this.getCandidateGroupList(taskDefinition);
        for (Group group: candidateGroupList) {
            String groupId = group.getId();
            List<User> userList = this.identityService.createUserQuery().memberOfGroup(groupId).list();
            userSet.addAll(userList);
        }

        return userSet;
    }


    /**
     * 获取给定任务定义中定义的任务候选角色
     */
    private Set<Group> getCandidateGroupList(TaskDefinition taskDefinition) {

        Set<Group> groupSet = new HashSet<>();
        Set<Expression> expressionSet = taskDefinition.getCandidateGroupIdExpressions();

        for (Expression expression: expressionSet) {
            String expressionText = expression.getExpressionText().replace("#{unit}", "");
            Group group = this.identityService.createGroupQuery().groupId(expressionText).singleResult();
            if (null != group) {
                groupSet.add(group);
            }
        }

        return groupSet;
    }


    /**
     * 查询工作流用户
     */
    private User getUser(String userId) {
        return this.identityService.createUserQuery().userId(userId).singleResult();
    }
}
