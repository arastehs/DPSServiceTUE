package com.dev.service;

import com.dev.common.exceptions.JobNameNotFoundException;
import com.dev.model.entity.StatusResponse;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public interface DpsService {
    public String runJob(String jobName) throws SQLException, JobNameNotFoundException, InterruptedException, ExecutionException;
    public StatusResponse getJobStatus(String jobName) throws SQLException;
}
