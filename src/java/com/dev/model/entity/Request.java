package com.dev.model.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Request {


    private String userName;
    private String pass;
    private String jobName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
