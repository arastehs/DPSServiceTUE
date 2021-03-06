This project is a restful API with 2 kinds of methods for DpsService:(DPS stands for data preparation service)
1.runJob: (http://localhost:8085/DpsService/runJob)
an on-demand command for running two type of jobs
2.showJobStatus (http://localhost:8085/DpsService/showJobStatus)
it queries the last status of the desired job from the data base

A short introduction of the business and the data model:
It is a simplified model of the DPS(Data Pre-processing Service)we have used for fuel allocation. We have a profile of
vehicles and their fuel remains which is updated regularly based on some rules. Also we have a quota table that is used by
the switch service at gas stations. So, our first job is to first obtain the list of vehicles that have consumed their quota
bulk and next to calculate and put a quota bulk on the quota table based on some simplified rules and relations
with the admin user request.
The second job is to update a charged table which is responsible for maintaining all the quota bulks that we are sure that
are consumed.(when we receive the acknowledge of a quota bulk it is put in a temp table). So our job is to join this
temp table with the sent bulks and update the main charged records base on that information.

According to mentioned model, we have 2 request of running jobs, one named Quota for the first job, and the second one
is called Charged for putting the acknowledged bulks into the charged table.

sample json request for the runJob method:(jobName can be quota/charged)
{
"userName":"ana",                   //admin user
"pass":"ana_pass",                  //admin password, hashed by SHA
"jobName": "quota"                //job name
}

json response:
{
    "responseCode": "success"    //everything is ok or other responses according to what happened.
}
##################################################################################################
sample json request for the showJobStatus method:(jobName can be quota/charged)
{
"userName":"parisa",            //report user
"pass":"par_pass",              //report pass, hashed by MD5
"jobName": "charged"           // job name
}

json response:
{
    "responseCode": "SUCCESS",
    "statusResponse": {
        "jobName": "quota",
        "jobStatus": "Running was Finished for 2 records",
        "dateModified": "2020-05-18 14:56:05.0"
    }
}

##################################################################################################
I put a uml class diagram file besides my project files. I put only the contorller, service and dao classes in order to be readable
