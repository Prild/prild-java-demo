package com.thank.activiti.config;

import org.activiti.engine.event.EventLogEntry;
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
 * 测试开启EventLog
 */
public class ConfigEventLogTest {

    private static final Logger logger = LoggerFactory.getLogger(ConfigEventLogTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_eventlog.cfg.xml");

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testEnableEventLog() {
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        activitiRule.getTaskService().complete(task.getId());

        logger.info("{} = {}", processInstance.getId(), processInstance.getProcessInstanceId());
        List<EventLogEntry> eventLogEntries = activitiRule.getManagementService()
                .getEventLogEntriesByProcessInstanceId(processInstance.getProcessInstanceId());

        logger.debug("eventLogEntries.size: ", eventLogEntries.size());
        eventLogEntries.forEach(eventLogEntry -> logger.debug(" \neventLog.type: {} \neventLog.data: {}",
                eventLogEntry.getType(), new String(eventLogEntry.getData())));
    }

}
