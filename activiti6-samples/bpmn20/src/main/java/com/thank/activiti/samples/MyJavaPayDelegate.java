package com.thank.activiti.samples;

import com.google.common.base.Objects;
import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 模拟确认支付Java Service Task
 * @author Administrator
 */
public class MyJavaPayDelegate implements JavaDelegate, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(MyJavaPayDelegate.class);
    private static final long serialVersionUID = -5065637892005000519L;

    @Override
    public void execute(DelegateExecution execution) {

        logger.debug("run my java pay delegate: {}", this);
        logger.debug("variables: {}", execution.getVariables());

        // 设置变量和本地变量, 测试不通子流程在触发异常之后的获取变量情况
        execution.setVariable("key1","value1");
        execution.setVariable("key2","value2");
        execution.getParent().setVariableLocal("localKey1", "localValue1");

        // 模拟一个错误事件(传errorFlag=true触发)
        Object errorFlag = execution.getVariable("errorFlag");
        if (errorFlag!=null && Objects.equal(true, errorFlag)) {
            throw new BpmnError("bpmnError");
        }
    }
}
