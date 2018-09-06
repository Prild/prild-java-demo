package com.thank.activiti.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试让流程出错的service task
 */
public class MDCErrorDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(MDCErrorDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) {
        logger.info("run MDCErrorDelegate->execute()");
        throw new RuntimeException("test exception!");
    }
}
