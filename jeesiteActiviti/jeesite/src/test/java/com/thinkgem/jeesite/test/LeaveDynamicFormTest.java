package com.thinkgem.jeesite.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:test-spring-context22222222.xml") 
public class LeaveDynamicFormTest extends AbstractJUnit4SpringContextTests {
    @Autowired // 注入运行服务类
    private RuntimeService runtimeService;
    
    @Autowired // 注入任务服务类
    private TaskService taskService;
    
    @Autowired
    private  RepositoryService repositoryService;
    
    @Autowired
    private FormService formService;
    
    @Autowired
    private ProcessEngine processEngine;
    
    @Autowired
    @Rule // 注入一些规则
    public ActivitiRule activitiSpringRule;
    
	@Test
//	@Deployment(resources = "act/designs/oa/leave.bpmn")
	public void testJavascriptFormType() throws Exception {

//		// 验证是否部署成功
//		long count = repositoryService.createProcessDefinitionQuery().count();
//		assertEquals(1, count);
//		
//		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave").singleResult();
//        StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
//        List<FormProperty> formProperties = startFormData.getFormProperties();
//        for (FormProperty formProperty : formProperties) {
//            System.out.println(formProperty.getId() + "，value=" + formProperty.getValue());
//        }
		
		String deploymentId = "f78a0c2814c54699b0a2c84382e89c11";
		processEngine.getRepositoryService().deleteDeployment(deploymentId, true);
		System.out.println("删除成功!");
	}
}
