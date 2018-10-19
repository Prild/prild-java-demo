package com.thank.workflow.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@Entity
@Table(name="sys_workflow_step")
public class SysWorkflowStep implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @JSONField(format = DateUtils.YYYY_MM_DD_HH_MM_SS)
    @JsonSerialize(using = DateJsonSerializer.class)
    @Column(name="create_time")
    private Date createTime;

    @Column(name="workflow_id")
    private Long workflowId;
    //该步骤审核的角色
    @Column(name="role_pkno")
    private String rolePkno;
    /**
     * type==1  会签
     * type==2  普通流转
     */
    @Column(name="type")
    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public String getRolePkno() {
        return rolePkno;
    }

    public void setRolePkno(String rolePkno) {
        this.rolePkno = rolePkno;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


}
