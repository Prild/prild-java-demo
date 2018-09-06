package com.thank.activiti.bpmn20;

import com.google.common.collect.ImmutableMap;
import com.thank.activiti.samples.MyJavaBean;
import com.thank.activiti.samples.MyJavaDelegate;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:activiti-context.xml")
public class ServiceTaskSpringTest {

    private static final Logger logger = LoggerFactory.getLogger(ServiceTaskSpringTest.class);

    @Autowired
    @Rule
    public ActivitiRule activitiRule;

    /**
     * 测试用Spring容器实例化的bean: JavaDelegate
     * JavaDelegate方式的ServiceTask
     */
    @Test
    @Deployment(resources = {"my-process-servicetask4.bpmn20.xml"})
    public void testServiceTask() {
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        List<HistoricActivityInstance> activityInstanceList = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().orderByHistoricActivityInstanceEndTime().asc().list();
        activityInstanceList.forEach(historicActivityInstance -> logger.debug("activity: {}", historicActivityInstance));
        Assert.assertEquals("3", activityInstanceList.size()+"");
        Assert.assertEquals("Service task", activityInstanceList.get(1).getActivityName());
    }


    /**
     * 测试用流程启动时传入的参入bean: JavaDelegate
     * JavaDelegate方式的ServiceTask
     *
     * 可以看到, JavaDelegate执行的时的哈希和我们这里传入的一样, 也就是说并没有使用Spring容器中配置的JavaDelegate Bean
     * 说以这两种方式是有优先级的
     */
    @Test
    @Deployment(resources = {"my-process-servicetask4.bpmn20.xml"})
    public void testServiceTask2() {
        MyJavaDelegate myJavaDelegate = new MyJavaDelegate();
        logger.debug("myJavaDelegate: {}", myJavaDelegate);
        Map<String, Object> variables = ImmutableMap.of("myJavaDelegate", myJavaDelegate);
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);

        List<HistoricActivityInstance> activityInstanceList = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().orderByHistoricActivityInstanceEndTime().asc().list();
        activityInstanceList.forEach(historicActivityInstance -> logger.debug("activity: {}", historicActivityInstance));
        Assert.assertEquals("3", activityInstanceList.size()+"");
        Assert.assertEquals("Service task", activityInstanceList.get(1).getActivityName());
    }


    /**
     * 测试不实现JavaDelegate, 执行调用方法表达式和值表达式
     */
    @Test
    @Deployment(resources = {"my-process-servicetask5.bpmn20.xml"})
    public void testServiceTask5() {
        Map<String, Object> variables = ImmutableMap.of("myJavaBean", new MyJavaBean("thank"));
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);
        List<HistoricActivityInstance> activityInstanceList = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().orderByHistoricActivityInstanceEndTime().asc().list();
        activityInstanceList.forEach(historicActivityInstance -> logger.debug("activity: {}", historicActivityInstance));
        Assert.assertEquals("4", activityInstanceList.size()+"");
        Assert.assertEquals("Service task", activityInstanceList.get(1).getActivityName());
    }
}