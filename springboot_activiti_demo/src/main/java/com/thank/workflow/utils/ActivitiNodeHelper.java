package com.thank.workflow.utils;

import org.activiti.bpmn.model.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivitiNodeHelper {

    /*任务节点*/
    public static UserTask createUserTask(String id, String name, String candidateUsers,String assignee) {
        List<String> users =new ArrayList<String>();
        String[] usersStr = candidateUsers.split(",");
        for (String user:usersStr) {
            users.add(user);
        }
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setCandidateGroups(users);
        userTask.setCandidateUsers(users);
        userTask.setAssignee(assignee);
        return userTask;
    }

    /*连线*/
    public static SequenceFlow createSequenceFlow(String from, String to, String name, String conditionExpression) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        flow.setName(name);
        if(StringUtils.isNotEmpty(conditionExpression)){
            flow.setConditionExpression(conditionExpression);
        }
        return flow;
    }

    /*排他网关*/
    public static ExclusiveGateway createExclusiveGateway(String id) {
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(id);
        return exclusiveGateway;
    }

    /*开始节点*/
    public static StartEvent createStartEvent() {
        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        return startEvent;
    }

    /*结束节点*/
    public static EndEvent createEndEvent() {
        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        return endEvent;
    }
}
