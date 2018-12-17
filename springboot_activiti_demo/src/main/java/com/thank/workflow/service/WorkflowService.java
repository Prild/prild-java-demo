package com.thank.workflow.service;

import com.thank.workflow.model.ActivitiNode;
import com.thank.workflow.model.WorkflowBean;
import com.thank.workflow.utils.ActivitiNodeHelper;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.apache.commons.io.FileUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: none
 *
 * @author thank
 * 2017/12/15 23:06
 */
@Service
public class WorkflowService {

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

    private static final String RESOUTCE_PATH = "processes/";

    /*public String saveNewDeploy(String fileName, String deploymentName) {

        Deployment deployment = this.processEngine.getRepositoryService().createDeployment().name(deploymentName)
                .addClasspathResource(RESOUTCE_PATH.concat(fileName).concat(".bpmn"))
                .addClasspathResource(RESOUTCE_PATH.concat(fileName).concat(".png"))
                .deploy();

        return deployment.toString();
    }*/

    public String saveNewDeploy(){

        List<ActivitiNode> activitiNodeList = new ArrayList<>();
        activitiNodeList.add(new ActivitiNode("empTask", "Emp Task", "2001,2002,2003","","2101,2102,2103"));
        activitiNodeList.add(new ActivitiNode("ldTask", "Ld Task", "1001,1002","","1101,1102"));
        activitiNodeList.add(new ActivitiNode("hrTask", "Hr Task", "3001","","3101"));
        activitiNodeList.add(new ActivitiNode("acTask", "Ac Task", "4001","","4101"));


        BpmnModel model = new BpmnModel();
        Process process=new Process();
        model.addProcess(process);
        final String PROCESSKEY ="qj_process";
        final String PROCESSNAME ="请假审批";
        process.setId(PROCESSKEY);
        process.setName(PROCESSNAME);

        int index = 0;
        for (ActivitiNode activitiNode : activitiNodeList) {
            if (index == 0){
                createFirstNode(process, activitiNode);
            }else {
                createMidNode(activitiNodeList, process, activitiNode , index);
            }
            if (index == 1){
                process.addFlowElement(ActivitiNodeHelper.createSequenceFlow(activitiNodeList.get(index - 1).getId(), activitiNode.getId(), "", ""));
            }else if (index > 1){
                process.addFlowElement(ActivitiNodeHelper.createSequenceFlow("exclusiveGateway" + (index - 1), activitiNode.getId(), "同意", "${submitType=='1'}"));
            }
            index ++;
        }


        // 2. Generate graphical information
        new BpmnAutoLayout(model).execute();

        // 3. Deploy the process to the engine
        Deployment deployment = processEngine.getRepositoryService().createDeployment().addBpmnModel(PROCESSKEY+".bpmn", model).name(PROCESSKEY+"_deployment").deploy();

        return deployment.toString();
    }

    private void createMidNode(List<ActivitiNode> activitiNodeList, Process process, ActivitiNode activitiNode ,int index) {
        String exclusiveGateway = "exclusiveGateway" + index;
        //String endEvent = "endEvent" + index;

        process.addFlowElement(ActivitiNodeHelper.createUserTask(activitiNode.getId(), activitiNode.getName(), activitiNode.getUsers(),activitiNode.getAssignee()));
        process.addFlowElement(ActivitiNodeHelper.createExclusiveGateway(exclusiveGateway));
        //process.addFlowElement(ActivitiNodeHelper.createEndEvent(endEvent));

        process.addFlowElement(ActivitiNodeHelper.createSequenceFlow(activitiNode.getId(), exclusiveGateway, "", ""));
        if (index == activitiNodeList.size() - 1){
            //最终 节点
            process.addFlowElement(ActivitiNodeHelper.createSequenceFlow(exclusiveGateway, "endEvent", "同意或不同意", "${submitType=='1' || submitType=='-1'}"));
        }else {
            //中间 节点
            process.addFlowElement(ActivitiNodeHelper.createSequenceFlow(exclusiveGateway, "endEvent", "不同意", "${submitType=='-1'}"));
        }
        process.addFlowElement(ActivitiNodeHelper.createSequenceFlow(exclusiveGateway, activitiNodeList.get(0).getId(), "回退", "${submitType=='0'}"));
    }

    private void createFirstNode(Process process, ActivitiNode activitiNode) {
        //第一个节点  开始--节点一
        process.addFlowElement(ActivitiNodeHelper.createStartEvent());
        process.addFlowElement(ActivitiNodeHelper.createUserTask(activitiNode.getId(), activitiNode.getName(), activitiNode.getUsers(),activitiNode.getAssignee()));
        process.addFlowElement(ActivitiNodeHelper.createSequenceFlow("startEvent", activitiNode.getId(), "", ""));

        process.addFlowElement(ActivitiNodeHelper.createEndEvent());
    }


    public List<Deployment> findDeploymentList() {
        List<Deployment> list = this.repositoryService.createDeploymentQuery().list();
        for(Deployment deployment:list){

        }
        return list;
    }

    public List<ProcessDefinition> findProcessDefinition() {
        return this.repositoryService.createProcessDefinitionQuery().list();
    }

    public void deleteDeploy(String deploymentId) {
        this.repositoryService.deleteDeployment(deploymentId,true);
    }

    public InputStream findImageInputStream(String deploymentId, String imageName) {
        return this.repositoryService.getResourceAsStream(deploymentId, imageName);
    }

    public ProcessInstance startProcess(String processDefinitionKey, String bussinessKey, Map<String, Object> variables) {
        ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey(processDefinitionKey, bussinessKey, variables);
        List<Task> list = this.taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        System.out.println("流程" + processInstance.getId() + "：待处理任务数量" + list.size());
        for (Task task:list) {
            taskService.complete(task.getId(),variables);
        }
        return processInstance;
    }

    public ProcessInstance submit(String processInstanceId, Map<String, Object> variables) {
        List<Task> list = this.taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        System.out.println("待处理任务数量" + list.size());
        for (Task task:list) {
            taskService.complete(task.getId(),variables);
        }
        return this.runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
    }

    public List<Task> findTaskList(String processInstanceId,String userId) {
        //List<Task> taskList = this.taskService.createTaskQuery().taskAssignee(userName).list();
        List<Task> taskList = this.taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        List<Task> taskList0 = this.taskService.createTaskQuery().taskCandidateOrAssigned(userId).list();
        List<Task> taskList1 = this.taskService.createTaskQuery().taskAssignee(userId).list();
        List<Task> taskList2 = this.taskService.createTaskQuery().taskCandidateGroup(userId).list();
        List<Task> taskList3 = this.taskService.createTaskQuery().taskCandidateUser(userId).list();

        return taskList;
    }

    public String findTaskFormKeyByTaskId(String taskId) {
        TaskFormData taskFormData = this.formService.getTaskFormData(taskId);
        String url = taskFormData.getFormKey();
        return url;
    }


    /**
     * 1. 使用任务ID, 获取业务对象的ID, 从而获取业务对象
     */
    public Object findBussinessObjectByTaskId(String taskId) {

        //1. 使用任务ID, 获取任务对象
        Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();

        //2. 使用任务对象, 获取流程实例ID
        String processInstanceId = task.getProcessInstanceId();

        //3. 使用流程实例ID, 获取流程实例对象(查询正在执行的执行对象表)
        ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery().processDefinitionId(processInstanceId).singleResult();

        //4. 获取BUSSINESS_KEY
        String bussinessKey = processInstance.getBusinessKey();

        //5. 获取BUSSINESS_KEY对应的的业务对象主键ID
        //获取规则...
        String buddinessId = bussinessKey;

        //6. 查询业务对象, 返回
        //BussinessObject = bussinessDao.findById(buddinessId);
        return null;
    }

    /**
     * 2. 使用任务ID, 查询ProcessDefinitionEntity对象, 从而获取当前任务完成后的连线名称, 放置到List集合中
     */
    public List<String> findOutComeListByTaskId(String taskId) {

        List<String> list = new ArrayList<>();

        //使用任务ID 查询任务对象
        Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();

        //获取流程定义ID
        String processDefinitionId = task.getProcessDefinitionId();

        //获取流程定义实体ProcessDefinitionEntity
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) this.repositoryService.getProcessDefinition(processDefinitionId);

        //获取流程实例对象
        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery().processDefinitionId(processInstanceId).singleResult();

        //获取当前活动
        String activityId = processInstance.getActivityId();
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);

        //获取当前活动完成之后的连线名称
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        for (PvmTransition pvmTransition: pvmTransitionList) {
            String name = (String) pvmTransition.getProperty("name");
            if (StringUtils.hasText(name)) {
                list.add(name);
            } else {
                list.add("default commit");
            }
        }

        return list;
    }

    /**
     * 3. 返回所有历史审核人的审核信息列表, 帮助当前人完成审核
     *
     *      获取批注信息, 传递的事当前任务ID, 获取历史任务ID对应的批注
     */
    public List<Comment> findCommentByTaskId(String taskId) {

        List<Comment> commentList = new ArrayList<>();

        Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

//        List<HistoricTaskInstance> historicTaskInstanceList = this.historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();
//
//        for (HistoricTaskInstance historicTaskInstance: historicTaskInstanceList) {
//            String hisTaskId = historicTaskInstance.getId();
//            List<Comment> taskComments = this.taskService.getTaskComments(hisTaskId);
//            commentList.addAll(taskComments);
//        }

        //另一种简单方法
        commentList = this.taskService.getProcessInstanceComments(processInstanceId);
        return commentList;
    }


    public void saveSubmitTask(WorkflowBean workflowBean) {

        // 业务对象ID
        Long id = workflowBean.getId();
        String taskId = workflowBean.getTaskId();
        String outcome = workflowBean.getOutcome();
        String message = workflowBean.getComment();

        /**
         * 1：在完成之前，添加一个批注信息，向act_hi_comment表中添加数据，用于记录对当前申请人的一些审核信息
         */
        Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();

        String processInstanceId = task.getProcessInstanceId();

        /**
         * 注意, 添加批注的时候, activiti的底层是使用:
         *      //String userId = Authentication.getAuthenticatedUserId();
         *      //CommentEntity comment = new CommentEntity();
         *      //comment.setUserId(userId);
         * 所以, 在这之前需要获取当前登录人, 作为该任务的办理人(审核人). 对应 act_hi_comment中的user_id字段, 如果不添加审核人, 该字段为null
         * 获取当前登录人之后setAuthenticatedUserId(); 添加当前任务的审核人
         */
        String userId = "xxx";
        Authentication.setAuthenticatedUserId(userId);
        this.taskService.addComment(taskId,processInstanceId, message);


        /**
         * 2. 如果连线的名称是"default" 则不需要设置, 如果不是就需要设置流程变量
         * 在完成任务之前, 设置流程变量, 按照连线的名称去完成任务
         * 流程变量的名称: outcome
         * 流程变量的值: 连线的名称
         */
        Map<String, Object> variables = new HashMap<>();
        if (outcome!=null && !"default".equals(outcome)) {
            variables.put("outcome", outcome);
        }

        //3. 完成当前人的个人任务(并设置流程变量)
        this.taskService.complete(taskId, variables);

        //4. 当任务完成后, 需要指定下一个任务的办理人(这里通过类的方式设置 ManagerTaskHandler)
        //xxxxhandler

        //5. 完成任务后, 判断流程是否结束, 如果结束了, 就可以更新业务对象的状态啦
        ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if(null == processInstance) {
            System.out.println("流程结束了, 更新业务信息表状态吧");
        }
    }


    /**
     * 通过业务对象ID查询历史批注信息
     * @param id 业务对象ID
     */
    public List<Comment> findCommentListByBussinessId(Long id) {

        /**
         * 首先根据业务对象ID查询出业务对象
         * 找出businessKey
         */
        //.....
        String businessKey = "";

        //方式一. 使用历史的流程实例查询, 返回历史的流程实例对象, 获取流程实例ID
//        HistoricProcessInstance historicProcessInstance = this.historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
//        String processInstanceId = historicProcessInstance.getId();

        //方式二: 使用历史的流程变量查询, 返回历史的流程变量对象, 获取流程实例ID
        HistoricVariableInstance historicVariableInstance = this.historyService.createHistoricVariableInstanceQuery().variableValueEquals("objId", businessKey).singleResult();
        String processInstanceId = historicVariableInstance.getProcessInstanceId();

        List<Comment> commentList = this.taskService.getProcessInstanceComments(processInstanceId);
        return commentList;
    }


    /**
     * 查询流程定义对象
     */
    public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {

        Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
        String processDefinitionId = task.getProcessDefinitionId();

        // 对应表: 对应表act_re_procdef
        ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        return processDefinition;
    }


    public Map<String, Object> findCoordingByTask(String taskId) {

        Map<String, Object> returnMap = new HashMap<>(16);

        Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
        String processDefinitionId = task.getProcessDefinitionId();
        String processInstanceId = task.getProcessInstanceId();

        // 获取流程定义的实体对象（对应.bpmn文件中的数据）
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) this.repositoryService.getProcessDefinition(processDefinitionId);

        // 获取当前活动对应的流程实例对象
        ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

        // 获取当前活动对象
        String activityId = processInstance.getActivityId();
        ActivityImpl activity = processDefinitionEntity.findActivity(activityId);

        //获取坐标
        returnMap.put("x", activity.getX());
        returnMap.put("y", activity.getY());
        returnMap.put("width", activity.getWidth());
        returnMap.put("height", activity.getHeight());

        return returnMap;
    }


}
