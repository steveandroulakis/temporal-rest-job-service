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
import io.temporal.workflow.ChildWorkflowOptions;
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

    String workflowId = Workflow.getInfo().getWorkflowId();

    //    Collecting final job states to put in the workflow result
    List<JobState> jobStates = new ArrayList<>();

    // all jobs will use this data class
    JobData jobData = new JobData("AAA", 1, 10);

    // Create Job AAA
    String jobid = restJobActivities.createJob(jobData);
    log.info("\n\nCreated: " + jobid + "\n");

    // Await Job AAA completion
    log.info("\n\nAwaiting completion: " + jobid + "\n");
    jobStates.add(restJobActivities.awaitJobCompletion(jobid));
    log.info("\n\n" + jobData.getType() + " COMPLETE\n");

    // Create Job BBB
    jobData.setType("BBB");
    jobid = restJobActivities.createJob(jobData);
    log.info("\n\nCreated: " + jobid + "\n");

    // Await Job BBB completion
    log.info("\n\nAwaiting completion: " + jobid + "\n");
    jobStates.add(restJobActivities.awaitJobCompletion(jobid));
    log.info("\n\n" + jobData.getType() + " COMPLETE\n");

    // Create Job CCC1 and CCC2 for parallel execution
    jobData.setType("CCC1");
    String jobidCCC1 = restJobActivities.createJob(jobData);
    log.info("\n\nCreated: " + jobid + "\n");

    jobData.setType("CCC2");
    String jobidCCC2 = restJobActivities.createJob(jobData);
    log.info("\n\nCreated: " + jobid + "\n");

    // Job CCC1 and Job CCC2 (parallel execution)
    List<Promise<JobState>> parallelJobs = new ArrayList<>();

    Promise<JobState> jobAactivity =
        Async.function(restJobActivities::awaitJobCompletion, jobidCCC1);
    parallelJobs.add(jobAactivity);

    jobData.setType("CCC2");
    Promise<JobState> jobBactivity =
        Async.function(restJobActivities::awaitJobCompletion, jobidCCC2);
    parallelJobs.add(jobBactivity);

    // Wait for both Job CCC1 and CCC2 to complete
    Promise.allOf(parallelJobs).get();

    for (Promise<JobState> jobResult : parallelJobs) {
      JobState jobResultState = jobResult.get();
      log.info("\n\n" + jobResultState.getId() + " COMPLETE\n");
      jobStates.add(jobResultState);
    }

    // Create Job DDD
    jobData.setType("DDD");
    jobid = restJobActivities.createJob(jobData);
    log.info("\n\nCreated: " + jobid + "\n");

    // Await Job DDD completion
    log.info("\n\nAwaiting completion: " + jobid + "\n");
    jobStates.add(restJobActivities.awaitJobCompletion(jobid));
    log.info("\n\n" + jobData.getType() + " COMPLETE\n");

    // Create Child Workflow containing Job EEE
    ChildWorkflowOptions options =
        ChildWorkflowOptions.newBuilder().setWorkflowId(workflowId + "-CHILD").build();

    RestJobServiceChildWorkflow workflowChild =
        Workflow.newChildWorkflowStub(RestJobServiceChildWorkflow.class, options);
    Promise<List<JobState>> childJob = Async.function(workflowChild::restJobServiceChild);

    // wait for child workflow to complete
    for (JobState jobState : childJob.get()) {
      log.info("\n\nChild " + jobState.getId() + " COMPLETE\n");
      jobStates.add(jobState);
    }

    log.info("\n\nWorkflow Complete\n");
    return jobStates;
  }
}
