package com.thank.workflow.utils;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * description: none
 *
 * @author thank
 * 2017/12/18 16:31
 */
public class ManagerTaskHandler implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {

        //数据库查询, 设置个人任务的办理人
        String assignee = "";
        delegateTask.setAssignee(assignee);
    }
}
