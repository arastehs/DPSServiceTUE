package com.dev.service;

import com.dev.common.exceptions.JobNameNotFoundException;
import com.dev.common.threads.ThreadPool;
import com.dev.common.utils.Constants;
import com.dev.model.dao.LogInfo;
import com.dev.model.dao.StageLogInfoDao;
import com.dev.model.dao.VehicleListProvider;
import com.dev.model.entity.StatusResponse;
import com.dev.service.workers.Job;
import com.dev.service.workers.JobFactory;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
/*
the implementation of DpsService interface
it has a runJob method that
 */

public class DpsServiceImpl implements DpsService {
    private static Logger log = Logger.getLogger(DpsServiceImpl.class);


    @Override
    public String runJob(String jobName) throws SQLException, JobNameNotFoundException, InterruptedException, ExecutionException {

        LogInfo stageLog = new StageLogInfoDao();

        JobFactory factory = new JobFactory();

        /*
        creates the appropriate class base on the requested jobName
         */
        VehicleListProvider vehicleListProvider = factory.getVehicleProvider(jobName);

        //get collection of dao should be processed in every job
        Vector<String> processList = vehicleListProvider.VehicleProvider();

        //it means that there is s.th to be processed
        if (processList.size() > 0) {
            //log the staring moment
            stageLog.insertLogInfo(jobName, "Running was Started");

            Iterator it = processList.iterator();

            //get a thread pool with fix numbers
            ExecutorService pool = ThreadPool.getThreadPool();

            List<Future<?>> futures = new ArrayList<>();

            while (it.hasNext()) {

                /*
                dispatch the jobs to different threads, according to the jobName it creates a QuotaJob or ChargedJob clss
                */
                Job job = factory.getJob(jobName);

                /*
                id here is either a cardId wants a bulk or a concat of cardId with seqNo
                that represents an acknowledged charged record for a cardId
                */
                job.setId(it.next().toString());

                Future<?> future = pool.submit(job);
                futures.add(future);
            }


            pool.shutdown(); //disable new tasks from being submitted
            // wait a while for existing tasks to terminate
            if (!pool.awaitTermination(300, TimeUnit.SECONDS)) {

                pool.shutdownNow();
                log.error("error while shutdown the thread pool");
            }

            boolean isAllComplete = true; //making sure all threads are finished whether successfully or not
            int isOk = 0; // counting the successful numbers
            for (Future<?> future : futures) {
                isAllComplete &= future.isDone();
                isOk += (Integer) future.get(); //successful count of processed records

            }



            if (isAllComplete) {
                log.info("all tasks finished");
                stageLog.insertLogInfo(jobName, "Running was Finished for " + isOk + " records");
                return Constants.RESPONSE_CODE_SUCCESS;

            } else {
                log.error("some tasks remained uncompleted");
                stageLog.insertLogInfo(jobName, "Incomplete Run");
                return Constants.RESPONSE_CODE_INCOMPLETE;
            }


        } else {
            log.info("nothing to do!");
            stageLog.insertLogInfo(jobName, "No candidates for this job");
            return Constants.RESPONSE_CODE_EMPTY_VOLUNTEER;
        }

    }

    /*
    GET THE LAST STATUS OF THE DESIRED JOB FROM DB
     */
    @Override
    public StatusResponse getJobStatus(String jobName) throws SQLException {
        StageLogInfoDao logInfoDao = new StageLogInfoDao();
        StatusResponse statusResponse = logInfoDao.reportStageLogInfo(jobName);

        return statusResponse;
    }


}
