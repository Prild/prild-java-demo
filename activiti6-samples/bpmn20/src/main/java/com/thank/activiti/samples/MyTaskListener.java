package com.thank.activiti.samples;

import com.google.common.collect.ImmutableList;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过监听方式的自定义配置用户任务
 */
public class MyTaskListener implements TaskListener {

    private static final Logger logger = LoggerFactory.getLogger(MyTaskListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if ("create".equals(eventName)) {
            logger.debug("Listener: config task info");
            delegateTask.addCandidateUsers(ImmutableList.of("user1", "user2"));
            delegateTask.addCandidateGroup("group1");
            delegateTask.setDueDate(DateTime.now().plusDays(3).toDate());
        } else if ("complete".equals(eventName)) {
            logger.debug("Listener: complete task");
        }
    }
}
