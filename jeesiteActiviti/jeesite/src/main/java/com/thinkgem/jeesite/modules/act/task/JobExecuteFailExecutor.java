package com.thinkgem.jeesite.modules.act.task;

import org.activiti.engine.ManagementService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.runtime.Job;

public class JobExecuteFailExecutor implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		ManagementService managementService = execution.getEngineServices().getManagementService();// 操作控制台中使用
		System.out.println("////////////////////////////////流程实例：" + execution.getProcessInstanceId());
		Job job = managementService.createJobQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
		if (job.getRetries() > 0) {
			throw new RuntimeException("本次作业执行失败，再次执行可以成功！");
		}

	}

}