package com.prild.activiti.helloworld;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoMain.class);

    public static void main(String[] args) {
        LOGGER.info("启动了");
        //创建流程引擎
        //创建默认的 基于内存数据库的流程引擎对象
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        ProcessEngine processEngine = cfg.buildProcessEngine();

        String name = processEngine.getName();
        String version = ProcessEngine.VERSION;
        LOGGER.info("名称{},版本{}",name,version);

        //部署流程定义文件
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("second_approve.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();//部署
        String deployId = deploy.getId();//部署id
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deployId)
                .singleResult();//根据部署id获取流程定义对象

        LOGGER.info("流程定义文件{},流程id{}",processDefinition.getName(),processDefinition.getId());

        //启动运行流程
        RuntimeService runtimeService = processEngine.getRuntimeService();  // 运行时对象
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById(processDefinition.getId());//基于id启动流程定义对象
        LOGGER.info("启动流程{}",processInstance.getProcessDefinitionKey());

        //处理流程任务



        LOGGER.info("结束了");
    }
}
