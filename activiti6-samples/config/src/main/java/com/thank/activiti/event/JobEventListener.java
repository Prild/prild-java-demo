package com.thank.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EventListener: 监听定时作业任务并打印
 */
public class JobEventListener implements ActivitiEventListener {

    private static final Logger logger = LoggerFactory.getLogger(JobEventListener.class);

    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        ActivitiEventType eventType = activitiEvent.getType();
        String typeName = eventType.name();

        if (typeName.startsWith("TIMER") || typeName.startsWith("JOB")) {
            logger.debug("监听到JOB事件: {} \t {}", eventType, activitiEvent.getProcessInstanceId());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
