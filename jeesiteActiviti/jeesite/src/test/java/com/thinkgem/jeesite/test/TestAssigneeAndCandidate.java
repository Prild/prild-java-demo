package com.thinkgem.jeesite.test;

import java.util.List;

import org.junit.Test;

import com.thinkgem.jeesite.base.TestBase;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;

import javax.annotation.Resource;

public class TestAssigneeAndCandidate extends TestBase {

    @Resource(name = "assetActTaskService")
    private ActTaskService assetActTaskService;

    @Test
    public void testList(){
    	Act act = null;
    	List<Act> list = assetActTaskService.todoList(act);
    	for (Act act2 : list) {
    		System.out.println("///getAssigneeName////"+act2.getAssigneeName()+"//////getBusinessId//////"+act2.getBusinessId());
			
		}
    }
}
