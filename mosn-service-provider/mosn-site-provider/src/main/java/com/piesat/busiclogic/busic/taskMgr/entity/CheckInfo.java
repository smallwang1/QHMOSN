package com.piesat.busiclogic.busic.taskMgr.entity;


public class CheckInfo {

    private String id;

    private String taskid;

    private String check_time;

    private String comments;

    private String status;

    private String userid;


    public String getId() {
        return id;
    }

    public String getTaskid() {
        return taskid;
    }

    public String getCheck_time() {
        return check_time;
    }

    public String getComments() {
        return comments;
    }

    public String getStatus() {
        return status;
    }

    public String getUserid() {
        return userid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public void setCheck_time(String check_time) {
        this.check_time = check_time;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
