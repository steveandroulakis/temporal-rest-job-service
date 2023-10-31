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
import com.example.job.service.dataclasses.JobState;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestJobServiceWorkflowImpl implements RestJobServiceWorkflow {

  private static final Logger log = LoggerFactory.getLogger(RestJobServiceWorkflowImpl.class);
  private final ActivityOptions options =
      ActivityOptions.newBuilder()
          .setStartToCloseTimeout(Duration.ofHours(1))
          .setHeartbeatTimeout(Duration.ofSeconds(10))
          .build();

  private final RestJobActivities restJobActivities =
      Workflow.newActivityStub(RestJobActivities.class, options);

  @Override
  public List<JobState> restJobService() {

    List<JobState> jobStates = new ArrayList<>();

    List<Promise<JobState>> parallelJobs = new ArrayList<>();

    JobData jobAData = new JobData("AAA", 1, 10);

    // Parallel execution of Job A and B

    String jobAid = restJobActivities.createJob(jobAData);
    log.info("\n\nJob created: " + jobAid + "\n");

    JobData jobBData = new JobData("BBB", 1, 10);

    // run activity createJob
    String jobBid = restJobActivities.createJob(jobBData);
    log.info("\n\nJob created: " + jobAid + "\n");

    // Job A await
    Promise<JobState> jobAactivity = Async.function(restJobActivities::awaitJobCompletion, jobAid);
    parallelJobs.add(jobAactivity);

    // Job B await
    Promise<JobState> jobBactivity = Async.function(restJobActivities::awaitJobCompletion, jobBid);
    parallelJobs.add(jobBactivity);

    // Wait for both Job A and Job B to complete
    Promise.allOf(parallelJobs).get();

    for (Promise<JobState> jobResult : parallelJobs) {
      JobState jobResultState = jobResult.get();
      log.info("\n\nJob " + jobResultState.getId() + " COMPLETE\n");
      jobStates.add(jobResultState);
    }

    log.info("Workflow Complete\n");
    return jobStates;
  }
}
