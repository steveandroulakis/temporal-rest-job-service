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

package io.temporal.samples.restjobservice.dataclasses;

import com.example.job.service.dataclasses.JobData;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkflowParameterObj {
  // in cents
  private JobData jobData;

  // No-arg constructor
  public WorkflowParameterObj() {}

  // Constructor
  @JsonCreator
  public WorkflowParameterObj(@JsonProperty("jobData") JobData jobData) {
    this.jobData = jobData;
  }

  public JobData getJobData() {
    return jobData;
  }
}
