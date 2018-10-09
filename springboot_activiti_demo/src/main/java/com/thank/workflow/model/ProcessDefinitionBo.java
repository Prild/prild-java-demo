package com.thank.workflow.model;

import lombok.Data;

/**
 * description: none
 *
 * @author thank
 * 2017/12/19 13:56
 */
@Data
public class ProcessDefinitionBo {

    private String id;

    private String category;

    private String name;

    private String key;

    private String description;

    private Integer version;

    private String resourceName;

    private String deploymentId;

    private String diagramResourceName;

    private boolean hasStartFormKey;

    private boolean hasGraphicalNotation;

    private boolean isSuspended;

    private String tenantId;
}
