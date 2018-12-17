package me.kafeitu.activiti.web.chapter13;

import me.kafeitu.activiti.chapter13.Page;
import me.kafeitu.activiti.chapter13.PageUtil;
import me.kafeitu.activiti.chapter6.util.UserUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.NativeExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运行中的执行实例Execution
 * User: henryyan
 */
@Controller
@RequestMapping("/chapter13")
public class ExecutionController {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    /**
     * 用户参与过的流程
     *
     * @return
     */
    @RequestMapping("execution/list")
    public ModelAndView list(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("chapter13/execution-list");
    /* 标准查询
    List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().list();
    List<Execution> list = runtimeService.createExecutionQuery().list();
    mav.addObject("list", list);
    */

        User user = UserUtil.getUserFromSession(request.getSession());
        Page<Execution> page = new Page<Execution>(PageUtil.PAGE_SIZE);
        int[] pageParams = PageUtil.init(page, request);
        NativeExecutionQuery nativeExecutionQuery = runtimeService.createNativeExecutionQuery();

        // native query
        String sql = "select RES.* from ACT_RU_EXECUTION RES left join ACT_HI_TASKINST ART on ART.PROC_INST_ID_ = RES.PROC_INST_ID_ "
                + " where ART.ASSIGNEE_ = #{userId} and ACT_ID_ is not null and IS_ACTIVE_ = 'TRUE' order by START_TIME_ desc";

        nativeExecutionQuery.parameter("userId", user.getId());

      //记录了执行ID(表中字段：EXECUTION_ID_)为2726和2725的两条记录；这个executionList[ConcurrentExecution[2726], ConcurrentExecution[2725]]
        List<Execution> executionList = nativeExecutionQuery.sql(sql).listPage(pageParams[0], pageParams[1]);

        // 查询流程定义对象：bmp对象
        Map<String, ProcessDefinition> definitionMap = new HashMap<String, ProcessDefinition>();

        // 
        Map<String, Task> taskMap = new HashMap<String, Task>();

        // 每个Execution的当前活动ID，可能为多个
        Map<String, List<String>> currentActivityMap = new HashMap<String, List<String>>();

        // 设置每个Execution对象的当前活动节点
        for (Execution execution : executionList) {
            ExecutionEntity executionEntity = (ExecutionEntity) execution;
            String processInstanceId = executionEntity.getProcessInstanceId();//流程实例id，比如：2703 一个流程实例不管有多少条分支实例，这个ID都是一致的；一个流程实例ID只对应一个实例
            String processDefinitionId = executionEntity.getProcessDefinitionId();//流程定义ID，比如：leave-countersign:1:19 开启了多少个bmp

            // 将流程定义ID和其对应的bmp对象缓存到Map   {leave-countersign:1:19=ProcessDefinitionEntity[leave-countersign:1:19]}
            definitionCache(definitionMap, processDefinitionId);

            // 查询当前流程的所有处于活动状态的活动ID，如果并行的活动则会有多个
            //当前活动状态活动id为：countersign,在表ACT_RU_TASK 中的TASK_DEF_KEY存储。<userTask id="countersign" name="[部门/人事]联合会签" activiti:assignee="${user}">
            //TASK_DEF_KEY用于定义项目中TaskAction中的任务办理方法名
            List<String> activeActivityIds = runtimeService.getActiveActivityIds(execution.getId());//循环遍历出EXECUTION_ID_为2726和2725的记录
            currentActivityMap.put(execution.getId(), activeActivityIds);

            for (String activityId : activeActivityIds) {

                // 查询处于活动状态的任务(根据TASK_DEF_KEY和对应的EXECUTION_ID_查询)
                Task task = taskService.createTaskQuery().taskDefinitionKey(activityId).executionId(execution.getId()).singleResult();

                // 调用活动
                if (task == null) {
                    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                            .superProcessInstanceId(processInstanceId).singleResult();
                    task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
                    definitionCache(definitionMap, processInstance.getProcessDefinitionId());
                }
                taskMap.put(activityId, task);//EXECUTION_ID_和task放在map中 
                if (task.getDueDate()==null) {
                	taskMap.put(activityId, task);//EXECUTION_ID_和task放在map中 
					
				}
            }
        }

        mav.addObject("taskMap", taskMap);//{countersign=Task[id=2729, name=[部门/人事]联合会签]}
        mav.addObject("definitions", definitionMap);//{leave-countersign:1:19=ProcessDefinitionEntity[leave-countersign:1:19]}
        mav.addObject("currentActivityMap", currentActivityMap);//{2725=[countersign], 2726=[countersign]}
		
//      //////////////////////////////  显示dmp的相关基本信息/////////////////////////////////////////////////
        System.out.println(definitionMap.get("leave-countersign:1:19").getDiagramResourceName());
		System.out.println(definitionMap.get("leave-countersign:1:19").getDescription());
		System.out.println(definitionMap.get("leave-countersign:1:19").getKey());
		
//      //////////////////////////////  显示ACT_RU_TASK 的相关基本信息/////////////////////////////////////////////////
		System.out.println(taskMap.get("countersign").getDueDate());
		System.out.println(taskMap.get("countersign").getAssignee());
		System.out.println(taskMap.get("countersign").getOwner());
		System.out.println(taskMap.get("countersign").getDescription());
		System.out.println(taskMap.get("countersign").getCategory());
		
        page.setResult(executionList);
        page.setTotalCount(nativeExecutionQuery.sql("select count(*) from (" + sql + ")").count());
        mav.addObject("page", page);

        return mav;
    }

    /**
     * 流程定义对象缓存
     *使用流程定义ID（leave-countersign:1:19）查询，返回唯一结果集（bmp表对象），将结果集缓存
     * @param definitionMap
     * @param processDefinitionId
     */
    private void definitionCache(Map<String, ProcessDefinition> definitionMap, String processDefinitionId) {
        if (definitionMap.get(processDefinitionId) == null) {
            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId);
            ProcessDefinition processDefinition = processDefinitionQuery.singleResult();
            // 放入缓存
            definitionMap.put(processDefinitionId, processDefinition);
        }
    }
}
