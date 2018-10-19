/*
package com.thank.workflow.service;

import java.io.File;
import java.io.InputStream;
import java.util.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import com.thank.workflow.dao.SimpleDao;
import com.thank.workflow.dao.SysRoleDao;
import com.thank.workflow.dao.SysWorkflowDao;
import com.thank.workflow.dao.SysWorkflowStepDao;
import com.thank.workflow.model.SysWorkflow;
import com.thank.workflow.model.SysWorkflowStep;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

*/
/**
 *
 * @project cb-console-service
 * @author suruozhong
 * @description (工作流管理)
 * @date 2017年10月19日
 *//*


@Service
@Transactional
public class SysWorkflowServiceImpl {

    @Resource
    private SysRoleDao sysRoleDao;
    @Resource
    private SysWorkflowDao sysWorkflowDao;
    @Resource
    private SysWorkflowStepDao sysWorkflowStepDao;
    @Resource
    private SimpleDao simpleDao;
    //获取角色下所有用户
    private List<String> getUserRole(String rolePkno){
        List<String> list = simpleDao.getDelegate()
                .createSQLQuery("select user_pkno from sys_user_role_mapping where role_pkno=:rolePkno")
                .setParameter("rolePkno",rolePkno)
                .list();
        return list;
    }


    public RetKit save(SysWorkflow model, String stepArr) {

        model.setCreateTime(new Date());
        sysWorkflowDao.getDelegate().save(model);
        JSONArray arr = JSONObject.parseArray(stepArr);
        List<SysWorkflowStep> stepList = new ArrayList<SysWorkflowStep>();
        for(int i=0; i<arr.size();i++) {
            SysWorkflowStep step = new SysWorkflowStep();
            JSONObject obj = JSONObject.parseObject(arr.get(i).toString());
            step.setId(obj.getLong("id"));
            step.setCreateTime(new Date());
            step.setWorkflowId(model.getId());
            step.setRolePkno(obj.getString("rolePkno"));
            step.setType(obj.getIntValue("type"));
            sysWorkflowStepDao.merge(step);
            stepList.add(step);
        }
        //部署工作流
        addFlowDeployment(model,stepList);
        return RetKit.ok();
    }

    */
/**
     *@Date：2017/11/24
     *@Description：创建流程并部署
     *
     *//*

    protected void addFlowDeployment(SysWorkflow workflow,List<SysWorkflowStep> stepList){
        System.out.println(".........start...");
        ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();

        // 1. 建立模型
        BpmnModel model = new BpmnModel();
        Process process=new Process();
        model.addProcess(process);
        process.setId("news");
        process.setName(workflow.getName());
        process.setDocumentation(workflow.getContent());
        //添加流程
        //开始节点
        process.addFlowElement(createStartEvent());
        for(int i=0; i<stepList.size(); i++){
            SysWorkflowStep step = stepList.get(i);
            //判断是否会签
            if(step.getType()==1){
                //会签
                //加入并行网关-分支
                process.addFlowElement(createParallelGateway("parallelGateway-fork"+i,"并行网关-分支"+i));
                //获取角色下所有用户
                List<String> userList = getUserRole(step.getRolePkno());
                for(int u=0; u<userList.size();u++){
                    //并行网关分支的审核节点
                    process.addFlowElement(createUserTask("userTask"+i+u,"并行网关分支用户审核节点"+i+u,userList.get(u)));
                }
                //并行网关-汇聚
                process.addFlowElement(createParallelGateway("parallelGateway-join"+i,"并行网关到-汇聚"+i));

            }else{
                //普通流转
                //审核节点
                process.addFlowElement(createGroupTask("task"+i,"组审核节点"+i,step.getRolePkno()));
                //回退节点
                process.addFlowElement(createUserTask("repulse"+i,"回退节点"+i,"${startUserId}"));
            }
        }
        //结束节点
        process.addFlowElement(createEndEvent());

        //连线
        for(int y=0; y<stepList.size(); y++){
            SysWorkflowStep step = stepList.get(y);
            //是否会签
            if(step.getType()==1){
                //会签
                //判断是否第一个节点
                if(y==0){
                    //开始节点和并行网关-分支连线
                    process.addFlowElement(createSequenceFlow("startEvent", "parallelGateway-fork"+y, "开始节点到并行网关-分支"+y, ""));
                }else{
                    //审核节点或者并行网关-汇聚到并行网关-分支
                    //判断上一个节点是否是会签
                    if(stepList.get(y-1).getType()==1){
                        process.addFlowElement(createSequenceFlow("parallelGateway-join"+(y-1),"parallelGateway-fork"+y,"并行网关-汇聚到并行网关-分支"+y,""));
                    }else{
                        process.addFlowElement(createSequenceFlow("task"+(y-1),"parallelGateway-fork"+y,"上一个审核节点到并行网关-分支"+y,""));
                    }
                }
                //并行网关-分支和会签用户连线，会签用户和并行网关-汇聚连线
                List<String> userList = getUserRole(step.getRolePkno());
                for(int u=0; u<userList.size(); u++){
                    process.addFlowElement(createSequenceFlow("parallelGateway-fork"+y,"userTask"+y+u,"并行网关-分支到会签用户"+y+u,""));
                    process.addFlowElement(createSequenceFlow("userTask"+y+u,"parallelGateway-join"+y,"会签用户到并行网关-汇聚",""));
                }
                //最后一个节点  并行网关-汇聚到结束节点
                if(y==(stepList.size()-1)){
                    process.addFlowElement(createSequenceFlow("parallelGateway-join"+y,"endEvent","并行网关-汇聚到结束节点",""));
                }
            }else{
                //普通流转
                //第一个节点
                if(y==0){
                    //开始节点和审核节点1
                    process.addFlowElement(createSequenceFlow("startEvent", "task"+y, "开始节点到审核节点"+y, ""));
                }else{
                    //判断上一个节点是否会签
                    if(stepList.get(y-1).getType()==1){
                        //会签
                        //并行网关-汇聚到审核节点
                        process.addFlowElement(createSequenceFlow("parallelGateway-join"+(y-1),"task"+y,"并行网关-汇聚到审核节点"+y,""));
                    }else{
                        //普通
                        process.addFlowElement(createSequenceFlow("task"+(y-1),"task"+y,"审核节点"+(y-1)+"到审核节点"+y,"${flag=='true'}"));
                    }
                }
                //是否最后一个节点
                if(y==(stepList.size()-1)){
                    //审核节点到结束节点
                    process.addFlowElement(createSequenceFlow("task"+y,"endEvent","审核节点"+y+"到结束节点","${flag=='true'}"));
                }
                //审核节点到回退节点
                process.addFlowElement(createSequenceFlow("task"+y,"repulse"+y,"审核不通过-打回"+y,"${flag=='false'}"));
                process.addFlowElement(createSequenceFlow("repulse"+y,"task"+y,"回退节点到审核节点"+y,""));
            }
        }

        // 2. 生成的图形信息
        new BpmnAutoLayout(model).execute();

        // 3. 部署流程
        Deployment deployment = processEngine.getRepositoryService().createDeployment().addBpmnModel(process.getId()+".bpmn", model).name(process.getId()+"_deployment").deploy();

        //        // 4. 启动一个流程实例
//        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(process.getId());
//
//        // 5. 获取流程任务
//        List<Task> tasks = processEngine.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).list();
//        try{
//            // 6. 将流程图保存到本地文件
//            InputStream processDiagram = processEngine.getRepositoryService().getProcessDiagram(processInstance.getProcessDefinitionId());
//            FileUtils.copyInputStreamToFile(processDiagram, new File("/deployments/"+process.getId()+".png"));
//
//            // 7. 保存BPMN.xml到本地文件
//            InputStream processBpmn = processEngine.getRepositoryService().getResourceAsStream(deployment.getId(), process.getId()+".bpmn");
//            FileUtils.copyInputStreamToFile(processBpmn,new File("/deployments/"+process.getId()+".bpmn"));
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        System.out.println(".........end...");
    }


    //任务节点-组
    protected UserTask createGroupTask(String id, String name, String candidateGroup) {
        List<String> candidateGroups=new ArrayList<String>();
        candidateGroups.add(candidateGroup);
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setCandidateGroups(candidateGroups);
        return userTask;
    }

    //任务节点-用户
    protected UserTask createUserTask(String id, String name, String userPkno) {
        List<String> candidateUsers=new ArrayList<String>();
        candidateUsers.add(userPkno);
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setCandidateUsers(candidateUsers);
        return userTask;
    }

    //任务节点-锁定者
    protected UserTask createAssigneeTask(String id, String name, String assignee) {
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setAssignee(assignee);
        return userTask;
    }

    */
/*连线*//*

    protected SequenceFlow createSequenceFlow(String from, String to,String name,String conditionExpression) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        flow.setName(name);
        if(StringUtils.isNotEmpty(conditionExpression)){
            flow.setConditionExpression(conditionExpression);
        }
        return flow;
    }

    //排他网关
    protected ExclusiveGateway createExclusiveGateway(String id,String name) {
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(id);
        exclusiveGateway.setName(name);
        return exclusiveGateway;
    }

    //并行网关
    protected ParallelGateway createParallelGateway(String id,String name){
        ParallelGateway gateway = new ParallelGateway();
        gateway.setId(id);
        gateway.setName(name);
        return gateway;
    }

    //开始节点
    protected StartEvent createStartEvent() {
        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        return startEvent;
    }

    */
/*结束节点*//*

    protected EndEvent createEndEvent() {
        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        return endEvent;
    }

}
*/
