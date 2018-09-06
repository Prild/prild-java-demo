package com.thank.activiti.config;

import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigTest {

    private static final Logger logger = LoggerFactory.getLogger(ConfigTest.class);

    @Test
    public void testConfig1() {
        // 进入源码可以看到: 默认resources: "activiti.cfg.xml", beanName: "processEngineConfiguration"
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        logger.info("configuration={}", configuration);
        // StandaloneProcessEngineConfiguration
    }

    @Test
    public void testConfig2() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        logger.info("configuration={}", configuration);
        // StandaloneInMemProcessEngineConfiguration
    }
}
