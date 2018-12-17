package me.kafeitu.activiti;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

/**
 * 自定义表单引擎
 * 
 * @author henryyan
 */
public class MyFormEngineTest extends AbstractTest {
	org.activiti.engine.repository.Deployment deployment;

	@Test
	@Deployment(resources = { "chapter6/leave-formkey/leave-formkey.bpmn", "chapter6/leave-formkey/leave-start.form" })
	public void allPass() throws Exception {
		// repositoryService的主要作用是管理流程仓库，例如部署，删除，读取流程资源等
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();// 查询的是ACT_RE_PROCDEF表
		// 查询部署记录
		deployment = repositoryService.createDeploymentQuery().singleResult();// 查询的是ACT_RE_DEPLOYMENT表

		// 读取启动表单  formService渲染外置表单模版为HTML的函数
		Object renderedStartForm = formService.getRenderedStartForm(processDefinition.getId(), "myformengine");
		System.out.println("////////////////////////////////" + processDefinition.getId() + "////////////////////////////////");
		// 验证表单对象
		assertNotNull(renderedStartForm);
		assertTrue(renderedStartForm instanceof javax.swing.JButton);
		javax.swing.JButton button = (JButton) renderedStartForm;
		assertEquals("My Start Form Button", button.getName());

	}
}
