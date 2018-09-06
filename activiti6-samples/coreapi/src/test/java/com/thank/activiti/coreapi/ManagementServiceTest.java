package com.thank.activiti.coreapi;

import com.thank.activiti.mapper.MyCustomMapper;
import org.activiti.engine.ManagementService;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.management.TablePage;
import org.activiti.engine.runtime.DeadLetterJobQuery;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.runtime.SuspendedJobQuery;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ManagementServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ManagementServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg.job.xml");


    /**
     * 测试几种job的查询
     */
    @Test
    @Deployment(resources = {"my-process-job.bpmn20.xml"})
    public void testJobQuery(){

        ManagementService managementService = activitiRule.getManagementService();
        List<Job> timeJobList = managementService.createTimerJobQuery().list();
        timeJobList.forEach(job -> logger.debug("timeJob: {}", job));

        Assert.assertTrue(timeJobList.size() == 1);
        JobQuery jobQuery = managementService.createJobQuery();
        SuspendedJobQuery suspendedJobQuery = managementService.createSuspendedJobQuery();
        DeadLetterJobQuery deadLetterJobQuery = managementService.createDeadLetterJobQuery();
    }


    /**
     * 测试tablePageQuery 查询流程定义表
     */
    @Test
    @Deployment(resources = {"my-process-job.bpmn20.xml"})
    public void testTablePageQuery() {
        ManagementService managementService = activitiRule.getManagementService();
        
        String tableName = managementService.getTableName(ProcessDefinitionEntity.class);
        TablePage tablePage = managementService.createTablePageQuery().tableName(tableName).listPage(0, 100);
        List<Map<String, Object>> tablePageRows = tablePage.getRows();

        tablePageRows.forEach(row -> logger.debug("row: {}", row));
        Assert.assertTrue(tablePageRows.size()==1);
    }


    /**
     * 测试自定义SQL查询Task
     */
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testCustomSql() {
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        ManagementService managementService = activitiRule.getManagementService();

        List<Map<String, Object>> resultList = managementService.executeCustomSql(new AbstractCustomSqlExecution<MyCustomMapper, List<Map<String, Object>>>(MyCustomMapper.class) {
            @Override
            public List<Map<String, Object>> execute(MyCustomMapper myCustomMapper) {
                return myCustomMapper.findAll();
            }
        });


        resultList.forEach(result -> logger.info("result: {}", result));
        Assert.assertTrue(resultList.size() == 1);
    }


    /**
     * 测试command查询
     */
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testCommand(){
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        ManagementService managementService = activitiRule.getManagementService();

        ProcessDefinitionEntity processDefinitionEntity = managementService.executeCommand(new Command<ProcessDefinitionEntity>() {
            @Override
            public ProcessDefinitionEntity execute(CommandContext commandContext) {
                ProcessDefinitionEntity processDefinitionEntity = commandContext.getProcessDefinitionEntityManager().findLatestProcessDefinitionByKey("my-process");
                return processDefinitionEntity;
            }
        });

        logger.debug("processDefinitionEntity: {}", ToStringBuilder.reflectionToString(processDefinitionEntity, ToStringStyle.JSON_STYLE));
    }
}



