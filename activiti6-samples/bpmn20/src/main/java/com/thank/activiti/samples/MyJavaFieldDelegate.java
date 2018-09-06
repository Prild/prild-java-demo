package com.thank.activiti.samples;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.Serializable;

public class MyJavaFieldDelegate implements JavaDelegate, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(MyJavaFieldDelegate.class);

    private Expression name;

    private Expression desc;

    @Override
    public void execute(DelegateExecution execution) {
        Assert.notNull(name, "name should not be null!");
        Assert.notNull(desc, "desc should not be null!");
        logger.debug("run my java delete: {}, name:{}, desc: {}", this, name.getValue(execution), desc.getValue(execution));
    }
}
