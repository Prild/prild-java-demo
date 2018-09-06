package com.thank.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EventListener:
 *   实现一个简单的功能, 在流程启动和结束的时候打印一句话
 */
public class CustomEventListener implements ActivitiEventListener {

    private static final Logger logger = LoggerFactory.getLogger(CustomEventListener.class);

    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        ActivitiEventType eventType = activitiEvent.getType();
        if (ActivitiEventType.CUSTOM.equals(eventType)) {
            logger.debug("监听到自定义事件: {} \t {}", eventType, activitiEvent.getProcessInstanceId());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
