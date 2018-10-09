package com.thank.workflow.controller;

import com.thank.workflow.service.JasWorkflowService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * description: none
 *
 * @author thank
 * 2017/12/26 10:37
 */
@RestController
@RequestMapping("/workflow")
public class JasWorkflowController {

    @Resource
    private JasWorkflowService jasWorkflowService;

    /**
     * 根据业务主键查询流程实例是否已存在（即：该条业务数据是否已经发起流程）
     * @param bussinessEventId 业务数据ID
     */
    @RequestMapping("/isExistsWorkflow")
    public boolean isExistsWorkflow(@RequestParam String bussinessEventId) {

        return this.jasWorkflowService.isExistsWorkflow(bussinessEventId);
    }



    @RequestMapping("/isCanInitializeWorkflow")
    public boolean isCanInitializeWorkflow(@RequestParam String bussinessEventId, String workflowName) {

        String userId = "";
        return this.jasWorkflowService.isCanInitializeWorkflow(userId, bussinessEventId, workflowName);
    }


    @RequestMapping("initializeWorkflow")
    public void initializeWorkflow() {

        String workflowName = "";
        String userId = "";
        String businessEventId = "";
        boolean autoPassFirstNode = false;
        Map<String, Object> flowVarsMap = new HashMap<>(16);


    }

}
