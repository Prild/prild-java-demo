package com.thank.activiti.event;

import com.thank.activiti.DemoMain;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EventListener:
 *   实现一个简单的功能, 在流程启动和结束的时候打印一句话
 */
public class ProcessEventListener implements ActivitiEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ProcessEventListener.class);

    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        ActivitiEventType eventType = activitiEvent.getType();
        if (ActivitiEventType.PROCESS_STARTED.equals(eventType)) {
            logger.debug("流程启动: {} \t {}", eventType, activitiEvent.getProcessInstanceId());
        } else if (ActivitiEventType.PROCESS_COMPLETED.equals(eventType)) {
            logger.debug("流程完成: {} \t {}", eventType, activitiEvent.getProcessInstanceId());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
