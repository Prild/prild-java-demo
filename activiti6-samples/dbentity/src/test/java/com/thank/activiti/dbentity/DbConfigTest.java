package com.thank.activiti.dbentity;

import com.google.common.collect.Lists;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ComponentAdapter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * description: none
 *
 * @author xiefayang
 * 2018/6/22 15:55
 */
public class DbConfigTest {

    private static final Logger logger = LoggerFactory.getLogger(DbConfigTest.class);

    /**
     * 查询Activiti表和数据数量
     */
    @Test
    public void testDbConfig() {
        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-mysql.cfg.xml").buildProcessEngine();
        ManagementService managementService = processEngine.getManagementService();
        Map<String, Long> tableCountMap = managementService.getTableCount();

        List<String> tableNameList = Lists.newArrayList(tableCountMap.keySet());
        Collections.sort(tableNameList);

        tableNameList.forEach(tableName -> logger.debug("tableName: {}, count: {}", tableName, tableCountMap.get(tableName)));
    }


    @Test
    public void testDropTable() {
        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-mysql.cfg.xml").buildProcessEngine();
        ManagementService managementService = processEngine.getManagementService();

        managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
                commandContext.getDbSqlSession().dbSchemaDrop();
                logger.debug("表删除成功");
                return null;
            }
        });
    }
}
