package com.thank.activiti.coreapi;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RepositoryServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void testRepository() {

        // 两次部署, 每次部署两个流程文件
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.name("测试部署资源1")
                .addClasspathResource("my-process.bpmn20.xml")
                .addClasspathResource("second_approve.bpmn20.xml");
        Deployment deployment = deploymentBuilder.deploy();     //deploy之后，将一个部署对象 两个流程定义文件，部署到数据库
        logger.debug("deployment: ", deployment);

        DeploymentBuilder deploymentBuilder1 = repositoryService.createDeployment();
        deploymentBuilder1.name("测试部署资源1")
                .addClasspathResource("my-process.bpmn20.xml")
                .addClasspathResource("second_approve.bpmn20.xml");
        deploymentBuilder1.deploy();

        // 流程部署对象查询
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        List<Deployment> deploymentList = deploymentQuery.orderByDeploymenTime().asc().listPage(0, 100);
        logger.debug("deploymentList.size: {}", deploymentList.size());
        Assert.assertEquals(2, deploymentList.size());
        deploymentList.forEach(deploymentEntity -> logger.debug("deployment: {}", deploymentEntity));

        // 流程定义对象查询
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionKey().asc().listPage(0, 100);
        logger.debug("processDefinitionList.size: {}", processDefinitionList.size());
        Assert.assertEquals(4, processDefinitionList.size());
        for (ProcessDefinition processDefinition : processDefinitionList) {
            logger.debug("processDefinition = {} , version = {} , key = {} ,id = {}",
                    processDefinition, processDefinition.getVersion(),
                    processDefinition.getKey(), processDefinition.getId());
        }
    }


    /**
     * 测试流程暂停和启动激活
     */
    @Test
    // 帮我们自动部署
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testSuspend() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();

        // 因为只部署有一个流程定义文件, 所以可以拿单一结果来接收
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        String processDefinitionId = processDefinition.getId();
        logger.debug("流程定义ID: {}", processDefinitionId);

        // 暂停该流程
        repositoryService.suspendProcessDefinitionById(processDefinitionId);
        logger.debug("--开始启动流程--");
        try {
            activitiRule.getRuntimeService().startProcessInstanceById(processDefinitionId);
        } catch (Exception e) {
            logger.debug("--启动失败--");
            logger.debug(e.getMessage());
        }

        logger.debug("--激活后重新启动流程--");
        // 激活该流程
        repositoryService.activateProcessDefinitionById(processDefinitionId);
        activitiRule.getRuntimeService().startProcessInstanceById(processDefinitionId);
        logger.debug("--启动成功--");
    }


    /**
     * 测试设置流程的启动用户和组
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testCandidateStarter(){

        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        String processDefinitionId = processDefinition.getId();
        logger.debug("流程定义ID: {}", processDefinitionId);

        // 设置流程启动的用户组和用户
        repositoryService.addCandidateStarterUser(processDefinitionId, "thank");
        repositoryService.addCandidateStarterGroup(processDefinitionId, "thankGroup");

        // 取出关系
        List<IdentityLink> identityLinks = repositoryService.getIdentityLinksForProcessDefinition(processDefinitionId);
        logger.debug("identityLinks.size: {}", identityLinks.size());
        Assert.assertEquals(2, identityLinks.size());
        identityLinks.forEach(identityLink -> logger.debug("processDefId: {}, userId: {}, groupId: {}",
                identityLink.getProcessDefinitionId(), identityLink.getUserId(), identityLink.getGroupId()));

        /**
         * 注意: 就算上面设置了用户和组, 这里没有指定用户和组我们依然能正常启动
         * 是因为这个设置只是activiti给我们设置了这个关系, 具体的判断还是需要在我们的业务逻辑中去取出关系后判断
         */
        activitiRule.getRuntimeService().startProcessInstanceById(processDefinitionId);

        // 取消流程启动的用户组和用户
        repositoryService.deleteCandidateStarterGroup(processDefinition.getId(),"thankGroup");
        repositoryService.deleteCandidateStarterUser(processDefinition.getId(),"thank");
        List<IdentityLink> identityLinks2 = repositoryService.getIdentityLinksForProcessDefinition(processDefinitionId);
        logger.debug("identityLinks.size: {}", identityLinks2.size());
        Assert.assertEquals(0, identityLinks2.size());
    }
}
