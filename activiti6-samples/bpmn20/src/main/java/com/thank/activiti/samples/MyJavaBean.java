package com.thank.activiti.samples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class MyJavaBean implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(MyJavaBean.class);

    public MyJavaBean() {
    }

    public MyJavaBean(String name) {
        this.name = name;
    }

    private String name;

    public void sayHello() {
        logger.debug("Run MyJavaBean.sayHello()...");
    }

    public String getName() {
        logger.debug("Run getName(), name:{}", name);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
