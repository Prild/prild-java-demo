package com.thank.workflow.model;

public class ActivitiNode {
    private String id;
    private String name;
    private String users;
    private String groups;
    private String assignee;

    public ActivitiNode() {
    }

    public ActivitiNode(String id, String name, String users ,String groups,String assignee) {
        this.id = id;
        this.name = name;
        this.users = users;
        this.groups = groups;
        this.assignee = assignee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
}
