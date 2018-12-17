package com.thinkgem.jeesite.test;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

public class TestLeave
{
	static void log(Object obj)
    {
    	System.out.println(obj);
    }
	
	static void run(ProcessEngine processEngine) throws Exception
	{
		RepositoryService repositoryService = processEngine.getRepositoryService();
		
        repositoryService.createDeployment().addClasspathResource("leave.bpmn20.xml").deploy();
        
        RuntimeService runtimeService = processEngine.getRuntimeService();
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("userid", "10010");
        variables.put("day", 3);
        
        runtimeService.startProcessInstanceByKey("leave_process", variables);
        
        TaskService taskService = processEngine.getTaskService();
        
        TaskQuery query = taskService.createTaskQuery();
        
        List<Task> tasks = query.list();

        for(Task task : tasks)
        {
        	log(task.getId() + "," + task.getName());
        	taskService.complete(task.getId(), variables);
        }
        
        log("--------------------");
        
        tasks = query.list();

        for(Task task : tasks)
        {
        	log(task.getId() + "," + task.getName());
        	taskService.complete(task.getId(), variables);
        }
        
        log("--------------------");
        
        tasks = query.list();

        for(Task task : tasks)
        {
        	log(task.getId() + "," + task.getName());
        	taskService.complete(task.getId(), variables);
        }
	}
	public static void main(String[] args)
	{
		ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
	
		try {
			run(processEngine);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		processEngine.close();
	}
}
