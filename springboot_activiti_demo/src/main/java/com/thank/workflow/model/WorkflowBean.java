package com.thank.workflow.model;

import lombok.Data;

import java.io.File;

/**
 * description: none
 *
 * @author thank
 * 2017/12/18 15:58
 */
@Data
public class WorkflowBean {

    /**
     * 业务对象ID
     */
    private Long id;

    /**
     * 部署对象ID
     */
    private String deploymentId;

    /**
     * 资源文件名称
     */
    private String imageName;

    /**
     * 任务名称
     */
    private String taskId;

    /**
     * 连线名称
     */
    private String outcome;

    /**
     * 备注
     */
    private String comment;

    /**
     * 流程定义名称
     */
    private String filename;

    /**
     * 流程定义文件
     */
    private File file;

}
