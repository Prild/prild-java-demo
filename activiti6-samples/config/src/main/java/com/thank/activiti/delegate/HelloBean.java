package com.thank.activiti.delegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义bean, 测试在和Spring集成后在定义文件表达式中使用Spring bean
 */
public class HelloBean {

    private static final Logger logger = LoggerFactory.getLogger(HelloBean.class);

    public void sayHello() {
        logger.debug("-----HelloBean.sayHello(): hello!-----");
    }
}
