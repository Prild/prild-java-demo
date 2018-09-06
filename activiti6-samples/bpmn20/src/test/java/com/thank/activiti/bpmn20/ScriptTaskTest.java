package com.thank.activiti.bpmn20;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScriptTaskTest {

    private static final Logger logger = LoggerFactory.getLogger(ScriptTaskTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * 测试groovy脚本设置变量
     */
    @Test
    @Deployment(resources = {"my-process-scripttask1.bpmn20.xml"})
    public void testScriptTask() {
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        HistoryService historyService = activitiRule.getHistoryService();

        List<HistoricVariableInstance> variableList = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId()).list();
        Assert.assertEquals(1, variableList.size());
        Assert.assertEquals("key1", variableList.get(0).getVariableName());
        Assert.assertEquals("value1", variableList.get(0).getValue());
    }


    /**
     * 测试juel脚本传入参数和接收脚本结果
     */
    @Test
    @Deployment(resources = {"my-process-scripttask2.bpmn20.xml"})
    public void testScriptTask2() {

        Map<String, Object> variables = ImmutableMap.of("key1", 1, "key2", 2);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> variableList = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId()).list();

        Map<String, Object> variableMap = variableList.stream().collect(Collectors.toMap(HistoricVariableInstance::getVariableName, HistoricVariableInstance::getValue, (a, b) -> b));
        Assert.assertEquals(3, variableMap.size());
        Assert.assertTrue(variableMap.get("mySum") instanceof Long);
        Assert.assertEquals((long) (2 + 1), variableMap.get("mySum"));
    }


    /**
     * 测试javascript脚本传入参数和接收脚本结果
     */
    @Test
    @Deployment(resources = {"my-process-scripttask3.bpmn20.xml"})
    public void testScriptTask3() {
        Map<String, Object> variables = ImmutableMap.of("key1", 1, "key2", 2);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);
        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> variableList = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId()).orderByVariableName().asc().list();

        Map<String, Object> variableMap = variableList.stream().collect(Collectors.toMap(HistoricVariableInstance::getVariableName, HistoricVariableInstance::getValue, (a, b) -> b));
        Assert.assertEquals(3, variableMap.size());
        Assert.assertTrue(variableMap.get("mySum") instanceof Double);
        Assert.assertEquals((double) (2 + 1), variableMap.get("mySum"));
    }


    /**
     * 测试直接执行一个脚本
     */
    @Test
    public void testScriptEngine() throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("juel");
        String script = "${1 + 2}";
        Object result = scriptEngine.eval(script);
        Assert.assertTrue(result instanceof Long);
        Assert.assertEquals((long) (2 + 1), result);
    }
}
