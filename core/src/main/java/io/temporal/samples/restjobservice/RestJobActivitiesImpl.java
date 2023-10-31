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

package io.temporal.samples.restjobservice;

import com.example.job.service.dataclasses.JobData;
import com.example.job.service.dataclasses.JobRunStatus;
import com.example.job.service.dataclasses.JobState;
import com.example.job.service.web.ServerInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;
import java.io.IOException;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestJobActivitiesImpl implements RestJobActivities {
  private static final Logger log = LoggerFactory.getLogger(RestJobActivitiesImpl.class);

  @Override
  public String createJob(JobData jobData) {
    String url = ServerInfo.getWebServerURL() + "/job";

    // Create a FormBody with the parameters
    FormBody formBody =
        new FormBody.Builder()
            .add("type", jobData.getType())
            .add("stepLength", String.valueOf(jobData.getStepLength()))
            .add("steps", String.valueOf(jobData.getSteps()))
            .build();

    // Build the request with POST method and form body
    Request request = new Request.Builder().url(url).post(formBody).build();

    try (Response response = new OkHttpClient().newCall(request).execute()) {
      String jobId = response.body().string();

      return jobId;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public JobState awaitJobCompletion(String id) {
    String url = ServerInfo.getWebServerURL() + "/job/" + id;
    log.info("\n\nWaiting for job " + id + " to complete. URL: " + url + "\n");

    // Build the request with GET method
    Request request = new Request.Builder().url(url).get().build();

    ActivityExecutionContext context = Activity.getExecutionContext();

    boolean isRunning = true;
    JobState jobStateResponse = null;

    while (isRunning) {

      try (Response response = new OkHttpClient().newCall(request).execute()) {
        // Get JSON JobState from response and convert to JobState object
        String jsonStr = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
        jobStateResponse = objectMapper.readValue(jsonStr, JobState.class);

        isRunning = jobStateResponse.getJobRunStatus() == JobRunStatus.RUNNING;

        //        System.out.println("Job Status: " + jobStateResponse);
        //        System.out.println("Is Running: " + isRunning);

        // heartbeat to prevent activity timeout then sleep
        context.heartbeat(jobStateResponse);
        Thread.sleep((long) 2000);
      } catch (IOException e) {
        throw new RuntimeException(e);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    return jobStateResponse;
  }
}
