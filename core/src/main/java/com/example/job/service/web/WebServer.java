/*
 *  Copyright (c) 2020 Temporal Technologies, Inc. All Rights Reserved
 *
 *  Copyright 2012-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *  Modifications copyright (C) 2017 Uber Technologies, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"). You may not
 *  use this file except in compliance with the License. A copy of the License is
 *  located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 *  or in the "license" file accompanying this file. This file is distributed on
 *  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *  express or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */

package com.example.job.service.web;

import static io.temporal.samples.restjobservice.RestJobServiceRequester.*;

import com.example.job.service.Job;
import com.example.job.service.dataclasses.JobData;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {

  private static final HashMap<String, Job> jobMap = new HashMap<>();
  private static final ExecutorService executorService = Executors.newCachedThreadPool();

  @SuppressWarnings("FutureReturnValueIgnored")
  private static void createJob(Context ctx) {
    String type = ctx.formParam("type");
    int stepLength = Integer.parseInt(ctx.formParam("stepLength")); // Providing a default value
    int steps = Integer.parseInt(ctx.formParam("steps")); // Providing a default value

    // Create a new JobData object with these parameters
    JobData jobData = new JobData(type, stepLength, steps);

    Job newJob = new Job(jobData.getType(), jobData.getStepLength(), jobData.getSteps());
    jobMap.put(newJob.getJobData().getId(), newJob);
    executorService.submit(
        () -> {
          try {
            newJob.run();
          } catch (InterruptedException e) {
            ctx.status(500);
          }
        });
    ctx.json(newJob.getJobData().getId()); // Respond with the Job ID
  }

  private static void getJobStatus(Context ctx) {
    String id = ctx.pathParam("id");
    Job job = jobMap.get(id);
    if (job != null) {
      ctx.json(job.getJobState()); // Return the Job status as JSON
    } else {
      ctx.status(404); // Job not found
    }
  }

  public static void main(String[] args) {
    Javalin app = Javalin.create();

    app.get(
        "/",
        ctx -> {
          ctx.html(
              "<!DOCTYPE html>"
                  + "<html>"
                  + "<body>"
                  + "<h2>Create Job</h2>"
                  + "<form action=\"/job\" method=\"post\">"
                  + "  Job Name:<br>"
                  + "  <input type=\"text\" name=\"type\"><br><br>"
                  + "  Number of Steps:"
                  + "  <input type=\"number\" name=\"steps\" name=\"Total Steps\">"
                  + "  Step Length (seconds):"
                  + "  <input type=\"number\" name=\"stepLength\"><br><br>"
                  + "  <input type=\"submit\" value=\"Submit\">"
                  + "</form>"
                  + "</body>"
                  + "</html>");
        });

    app.get(
        "/serverinfo",
        ctx -> {
          // some code
          ctx.json(ServerInfo.getServerInfo());
        });

    app.post("/job", WebServer::createJob);
    app.get("/job/{id}", WebServer::getJobStatus);

    app.start(7070);
  }
}
