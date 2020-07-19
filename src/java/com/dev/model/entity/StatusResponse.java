package com.dev.model.entity;

public class StatusResponse {
    private String jobName;
    private String jobStatus;
    private String dateModified;

    public StatusResponse(String jobName, String jobStatus, String dateModified) {
        this.jobName = jobName;
        this.jobStatus = jobStatus;
        this.dateModified = dateModified;
    }
}
