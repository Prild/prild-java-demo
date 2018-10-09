package com.thank.workflow.service;

import com.thank.workflow.WorkflowApplication;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * description: none
 *
 * @author thank
 * 2017/12/15 10:09
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WorkflowApplication.class)
public class ResumeServiceTest {


    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void storeResume() throws Exception {

        Map<String, Object> var = new HashMap<>();
        var.put("name", "谢发扬");
        var.put("age", 23);
        var.put("email", "coderthank@163.com");

        this.runtimeService.startProcessInstanceByKey("task", var);
    }


    @Test
    public void deploy() {
        Deployment deployment = this.processEngine.getRepositoryService().createDeployment().name("deplname")
                .addClasspathResource("processes/task.bpmn")
                .addClasspathResource("processes/task.png")
                .deploy();
    }


}