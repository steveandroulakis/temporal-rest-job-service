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
import com.example.job.service.dataclasses.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestJobActivitiesImpl implements RestJobActivities {
  private static final Logger log = LoggerFactory.getLogger(RestJobActivitiesImpl.class);

  @Override
  public JobStatus createJob(JobData jobData) {
    log.info("createJob: " + jobData.getId() + "\n");
    return null;
  }

  //  private static String simulateDelay(int seconds) {
  //    String url = ServerInfo.getWebServerURL() + "/simulateDelay?s=" + seconds;
  //    log.info("\n\n/API/simulateDelay URL: " + url + "\n");
  //    Request request = new Request.Builder().url(url).build();
  //    try (Response response = new OkHttpClient().newCall(request).execute()) {
  //      return response.body().string();
  //    } catch (IOException e) {
  //      throw new RuntimeException(e);
  //    }
  //  }
}
