package com.thank.workflow.model;

import lombok.Data;
import org.activiti.engine.repository.Deployment;

import java.util.Date;

/**
 * description: none
 *
 * @author thank
 * 2017/12/19 13:37
 */
@Data
public class DeploymentBo {

    private String id;

    private String name;

    private Date deploymentTime;

    private String category;

    private String tenantId;

}
