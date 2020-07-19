package com.dev.service.workers;

import com.dev.common.exceptions.JobNameNotFoundException;
import com.dev.model.dao.ChargedVehicleListProvider;
import com.dev.model.dao.QuotaVehicleListProvider;
import com.dev.model.dao.VehicleListProvider;
import org.apache.log4j.Logger;

public class JobFactory {
    private static Logger log = Logger.getLogger(JobFactory.class);


    public Job getJob(String jobName) throws JobNameNotFoundException {
        if (jobName == null) {
            return null;
        }
        if (jobName.equalsIgnoreCase("charged")) {
            return new ChargedJob();

        } else if (jobName.equalsIgnoreCase("quota")) {
            return new QuotaJob();

        } else {
            throw new JobNameNotFoundException(jobName);
        }

    }

    //get the appropriate list provider class according to jobName
    public VehicleListProvider getVehicleProvider(String jobName) throws JobNameNotFoundException {
        if (jobName == null) {
            return null;
        }
        if (jobName.equalsIgnoreCase("charged")) {
            return new ChargedVehicleListProvider();

        } else if (jobName.equalsIgnoreCase("quota")) {
            return new QuotaVehicleListProvider();

        } else {
            throw new JobNameNotFoundException(jobName);
        }

    }
}
