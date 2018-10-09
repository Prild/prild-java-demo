package com.thank.workflow.utils;

import com.thank.workflow.model.DeploymentBo;
import com.thank.workflow.model.ProcessDefinitionBo;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * description: none
 *
 * @author thank
 * 2017/12/19 14:01
 */
public class ActivitiEntity2BoConverter {

    public static List<DeploymentBo> toDeploymentBo(List<Deployment> deploymentList) {

        List<DeploymentBo> deploymentBoList = new ArrayList<>();
        for (Deployment deployment:deploymentList){
            DeploymentBo deploymentBo = new DeploymentBo();
            BeanUtils.copyProperties(deployment, deploymentBo);
            deploymentBoList.add(deploymentBo);
        }

        return deploymentBoList;
    }


    public static List<ProcessDefinitionBo> toProcessDefinitionBo(List<ProcessDefinition> processDefinitionList) {

        List<ProcessDefinitionBo> definitionBoList = new ArrayList<>();
        for (ProcessDefinition processDefinition: processDefinitionList) {
            ProcessDefinitionBo processDefinitionBo = new ProcessDefinitionBo();
            BeanUtils.copyProperties(processDefinition, processDefinitionBo);
            definitionBoList.add(processDefinitionBo);
        }

        return definitionBoList;
    }
}
