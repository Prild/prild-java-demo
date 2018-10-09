package com.thank.workflow.controller;

import com.thank.workflow.model.DeploymentBo;
import com.thank.workflow.model.ProcessDefinitionBo;
import com.thank.workflow.model.WorkflowBean;
import com.thank.workflow.service.WorkflowService;
import com.thank.workflow.utils.ActivitiEntity2BoConverter;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: none
 *
 * @author thank
 * 2017/12/15 22:54
 */
@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    @Resource
    private WorkflowService workflowService;

    /**
     * 发布流程
     */
    @GetMapping("/newDeploy")
    public String newDeploy(@RequestParam String fileName, @RequestParam String deploymentName) {

        return this.workflowService.saveNewDeploy(fileName, deploymentName);
    }

    /**
     * 查询流程对象(act_re_deployment) - 查询全部
     * 查询流程定义(act_re_procdef) - 查询全部
     */
    @GetMapping("/getProcessInfo")
    public Map<String, List<?>> getProcessInfo() {

        Map<String, List<?>> resultMap = new HashMap<>();

        List<DeploymentBo> deploymentBoList = ActivitiEntity2BoConverter.toDeploymentBo(this.workflowService.findDeploymentList());
        List<ProcessDefinitionBo> processDefinitionBoList = ActivitiEntity2BoConverter.toProcessDefinitionBo(this.workflowService.findProcessDefinition());
        resultMap.put("deploymentBoList", deploymentBoList);
        resultMap.put("processDefinitionBoList", processDefinitionBoList);
        return  resultMap;
    }


    /**
     * 删除部署信息
     * @param deploymentId 流程部署ID
     */
    @GetMapping("/deleteDeploy")
    public void deleteDeploy(@RequestParam String deploymentId) {
        this.workflowService.deleteDeploy(deploymentId);

    }


    /**
     * 查看流程图
     * @param deploymentId 流程部署ID
     * @param imageName 资源名称: 查看流程图(例如: processes/task.png)
     * @param response 图片字节输出流
     */
    @GetMapping("/viewImpage")
    public void viewImpage(@RequestParam String deploymentId, @RequestParam String imageName, HttpServletResponse response) {
        InputStream inputStream = this.workflowService.findImageInputStream(deploymentId, imageName);
        response.setContentType("image/gif");
        try {
            OutputStream out = response.getOutputStream();
            byte[] bytes = new byte[inputStream.available()];
            int i = inputStream.read(bytes);
            out.write(bytes);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 启动流程实例
     * @param processDefinitionKey 使用流程定义的Key启动
     * @param bussinessKey 向正在执行的执行对象表中的BUSINESS_KEY字段添加业务数据, 同时就让流程关联业务
     * @param variables 设置流程变量
     */
    @PostMapping("/startProcess")
    public Object startProcess(@RequestParam String processDefinitionKey, @RequestParam String bussinessKey, @RequestBody Map<String, Object> variables) {

        ProcessInstance processInstance = this.workflowService.startProcess(processDefinitionKey, bussinessKey, variables);
        Map<String, Object> returnMap = new HashMap<>(16);

        returnMap.put("processInstanceId", processInstance.getId());
        returnMap.put("bussinessKey", processInstance.getBusinessKey());
        return returnMap;
    }


    /**
     * 获取任务列表
     */
    @GetMapping("/getTaskList")
    public Object getTaskList(@RequestParam String userName) {
        return this.workflowService.findTaskList(userName);
    }


    /**
     * 打开任务表单
     */
    @GetMapping("/viewTaskForm")
    public String viewTaskForm(@RequestParam String taskId) {
        String url = this.workflowService.findTaskFormKeyByTaskId(taskId);
        url += "?taskId=" + taskId;
        return url;
    }


    /**
     * 准备表单数据, 包括三个部分:
     */
    @GetMapping("/getFormData")
    public Object getFormData(@RequestParam String taskId) {

        //1. 使用任务ID, 获取业务对象的ID, 从而获取业务对象
        Object bussinessObejct = this.workflowService.findBussinessObjectByTaskId(taskId);

        //2. 使用任务ID, 查询ProcessDefinitionEntity对象, 从而获取当前任务完成后的连线名称, 放置到List集合中
        List<String> outcomeList = this.workflowService.findOutComeListByTaskId(taskId);

        //3. 返回所有历史审核人的审核信息列表, 帮助当前人完成审核
        List<Comment> commentList = this.workflowService.findCommentByTaskId(taskId);

        return null;
    }


    /**
     * 提交任务
     */
    @PostMapping("/submitTask")
    public WorkflowBean submitTask(@RequestBody WorkflowBean workflowBean) {

        this.workflowService.saveSubmitTask(workflowBean);
        return null;
    }


    /**
     * 查询历史的批注信息(根据业务ID)
     */
    @GetMapping("/viewHistoryComment")
    public String viewHistoryComment(@RequestParam Long id) {
        this.workflowService.findCommentListByBussinessId(id);
        return null;
    }


    /**
     * 查看当前任务的流程图(查看当前活动节点, 并用红色框标注)
     */
    @GetMapping("/viewCurrentImage")
    public void viewCurrentImage(@RequestParam String taskId) {

        /** 1. 查看流程图 */
        // 获取任务ID，获取任务对象，使用任务对象获取流程定义ID，查询流程定义对象
        ProcessDefinition processDefinition = this.workflowService.findProcessDefinitionByTaskId(taskId);

        String deploymentId = processDefinition.getDeploymentId();
        String imageName = processDefinition.getDiagramResourceName();

        /** 2. 查看当前活动, 获取当前活动对应的坐标x,y,width,height 将这四个值放到Map中 */
    }
}
