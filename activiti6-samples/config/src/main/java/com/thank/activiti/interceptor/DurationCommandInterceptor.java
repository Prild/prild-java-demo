package com.thank.activiti.interceptor;

import com.thank.activiti.DemoMain;
import org.activiti.engine.impl.interceptor.AbstractCommandInterceptor;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 打印执行时间的拦截器
 */
public class DurationCommandInterceptor extends AbstractCommandInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(DemoMain.class);

    /**
     * 啥也不做, 就打印下执行时长
     */
    @Override
    public <T> T execute(CommandConfig commandConfig, Command<T> command) {
        long start = System.currentTimeMillis();
        try {
            return this.getNext().execute(commandConfig, command);
        } finally {
            long duration = System.currentTimeMillis() - start;
            logger.debug("{} 执行时长: {}", command.getClass().getSimpleName(), duration);
        }
    }
}
