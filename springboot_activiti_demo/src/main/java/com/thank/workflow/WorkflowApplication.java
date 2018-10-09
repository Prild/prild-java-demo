package com.thank.workflow;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class WorkflowApplication {

	@Autowired
	private TaskService taskService;

	@Autowired
	private ProcessEngine processEngine;


	@GetMapping("/get")
	public void getTask() {
		System.out.println("taskService:" + this.taskService);
		System.out.println("processEngine:" + this.processEngine);
	}

	public static void main(String[] args) {
		SpringApplication.run(WorkflowApplication.class, args);
	}
}
