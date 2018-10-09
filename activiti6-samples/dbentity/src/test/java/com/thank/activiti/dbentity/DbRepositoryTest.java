package com.thank.activiti.dbentity;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 * description: 测试资源文件表
 *
 * @author xiefayang
 * 2018/6/22 11:25
 */
public class DbRepositoryTest {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DbRepositoryTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testDeploy() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        repositoryService.createDeployment()
                .name("二级审批流程")
                .addClasspathResource("second_approve.bpmn20.xml").deploy();
    }


    @Test
    public void testSuspend() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        repositoryService.suspendProcessDefinitionById("second_approve:1:5004");

        Assert.assertTrue(repositoryService.isProcessDefinitionSuspended("second_approve:1:5004"));
    }

}
