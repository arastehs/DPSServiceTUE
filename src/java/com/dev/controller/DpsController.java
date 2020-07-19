/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dev.controller;

import com.dev.common.exceptions.JobNameNotFoundException;
import com.dev.common.security.AdminUser;
import com.dev.common.security.ReportUser;
import com.dev.common.utils.Constants;
import com.dev.model.entity.StatusResponse;
import com.dev.model.entity.DpsResponse;
import com.dev.model.entity.Request;
import com.dev.service.DpsService;
import com.dev.service.DpsServiceImpl;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author arasteh.s
 */

//Version : 1.0.0
/*
This is the starting point of the project, 2 methods: 1 for running a job with an admin user, the
other for getting the last status of a particular job from DB(successful run, incomplete run , ...
 */
@Path("/")
public class DpsController {

    private static Logger log = Logger.getLogger(DpsController.class);

    private DpsService dpsService = new DpsServiceImpl();


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/runJob")
    public String runJob(Request request) {

        Gson gson = new Gson();
        DpsResponse response = new DpsResponse();

        try {

            AdminUser adminUser = new AdminUser(request.getUserName(), request.getPass(), "admin");
            //valid user
            if (adminUser.getUserInfo(request.getUserName(), request.getPass()) > 0) {

                //give control to service layer to dispatch to some threads and then deliver it to dao one
                String resultRun = dpsService.runJob(request.getJobName().toLowerCase()); //QUOTA OR CHARGED

                response.setResponseCode(resultRun);
            }
            //invalid user
            else {
                response.setResponseCode(Constants.RESPONSE_CODE_INVALID_USER);
            }


            return gson.toJson(response);

        } catch (Exception e) {
            log.error("Internal Error:" + e.getMessage());
            if (e instanceof JobNameNotFoundException)
                response.setResponseCode(Constants.RESPONSE_CODE_INVALID_JOB);
            else
                response.setResponseCode(Constants.RESPONSE_CODE_INTERNAL_ERROR);

            return gson.toJson(response);
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/showJobStatus")
    public String showJobStatus(Request request) {

        Gson gson = new Gson();
        DpsResponse response = new DpsResponse();

        try {

            ReportUser reportUser = new ReportUser(request.getUserName(), request.getPass(), "report");
            //valid user
            //if it is actually a valid report user, it must be hashed by md5
            if (reportUser.getUserInfo(request.getUserName(), request.getPass()) > 0) {

                //give control to service layer to deliver it to dao one
                StatusResponse statusResponse = dpsService.getJobStatus(request.getJobName().toLowerCase()); //QUOTA OR CHARGED STATUS

                if (statusResponse != null) {
                    response.setStatusResponse(statusResponse);
                    response.setResponseCode(Constants.RESPONSE_CODE_SUCCESS);
                }
                //no record of this job in db,invalid job name
                else
                    response.setResponseCode(Constants.RESPONSE_CODE_INVALID_JOB);
            }
            //invalid user
            else {
                response.setResponseCode(Constants.RESPONSE_CODE_INVALID_USER);
            }

            return gson.toJson(response);

        } catch (Exception e) {
            log.error("Internal Error:" + e.getMessage());
            response.setResponseCode(Constants.RESPONSE_CODE_INTERNAL_ERROR);
            return gson.toJson(response);
        }

    }

}
