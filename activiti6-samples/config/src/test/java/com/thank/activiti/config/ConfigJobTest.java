package com.thank.activiti.config;

import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 测试作业执行器Job Executor
 */
public class ConfigJobTest {

    private static final Logger logger = LoggerFactory.getLogger(ConfigJobTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_job.cfg.xml");

    @Test
    @Deployment(resources = {"my-process_job.bpmn20.xml"})
    public void test() throws InterruptedException {

        logger.debug("---------开始----------");
        List<Job> jobs = activitiRule.getManagementService().createTimerJobQuery().listPage(0, 100);
        logger.debug("jobs.size:{}", jobs.size());
        jobs.forEach(job -> logger.debug("定时任务: {}, 默认重试次数: {}", job, job.getRetries()));
        Thread.sleep(1000 * 100);
        logger.debug("---------结束----------");
    }

}
