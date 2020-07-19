package com.dev.common.exceptions;

import org.apache.log4j.Logger;

public class JobNameNotFoundException extends Exception{
    private static Logger log = Logger.getLogger(JobNameNotFoundException.class);
    public JobNameNotFoundException(String jobName) {
        log.error("No job found for \"" + jobName + "\" job");
    }
}
