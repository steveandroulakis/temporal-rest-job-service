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
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestJobServiceChildWorkflowImpl implements RestJobServiceChildWorkflow {

  private static final Logger log = LoggerFactory.getLogger(RestJobServiceChildWorkflowImpl.class);
  private final ActivityOptions options =
      ActivityOptions.newBuilder()
          .setStartToCloseTimeout(Duration.ofHours(1))
          .setHeartbeatTimeout(Duration.ofSeconds(10))
          .build();

  private final RestJobActivities restJobActivities =
      Workflow.newActivityStub(RestJobActivities.class, options);

  @Override
  public List<JobState> restJobServiceChild() {

    // Collecting final job states to put in the workflow result
    List<JobState> jobStates = new ArrayList<>();

    // all jobs will use this data class
    JobData jobData = new JobData("CHILD-EEE", 1, 10);

    // Create Job CHILD-EEE
    String jobid = restJobActivities.createJob(jobData);
    log.info("\n\nCreated: " + jobid + "\n");

    // Await Job AAA completion
    log.info("\n\nAwaiting completion: " + jobid + "\n");
    jobStates.add(restJobActivities.awaitJobCompletion(jobid));
    log.info("\n\n" + jobData.getType() + " COMPLETE\n");

    log.info("\n\nChild Workflow Complete\n");
    return jobStates;
  }
}
