package com.thank.activiti.samples;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class MyJavaDelegate implements JavaDelegate, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(MyJavaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        logger.debug("run my java delete: {}", this);
    }
}
