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

import com.example.job.service.web.ServerInfo;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.samples.restjobservice.dataclasses.WorkflowParameterObj;
import java.io.FileNotFoundException;
import javax.net.ssl.SSLException;

public class RestJobServiceRequester {

  public static String runWorkflow(WorkflowParameterObj workflowParameterObj)
      throws FileNotFoundException, SSLException {
    // generate a random reference number
    String referenceNumber = generateReferenceNumber(); // random reference number

    // Workflow execution code

    WorkflowClient client = TemporalClient.get();
    final String TASK_QUEUE = ServerInfo.getTaskqueue();

    WorkflowOptions options =
        WorkflowOptions.newBuilder()
            .setWorkflowId(referenceNumber)
            .setTaskQueue(TASK_QUEUE)
            .build();
    RestJobServiceWorkflow transferWorkflow =
        client.newWorkflowStub(RestJobServiceWorkflow.class, options);

    WorkflowClient.start(transferWorkflow::restJobService, workflowParameterObj);
    System.out.printf("\n\nJob Created: $%s\n", workflowParameterObj.getJobData().getId());

    return referenceNumber;
  }

  @SuppressWarnings("CatchAndPrintStackTrace")
  public static void main(String[] args) throws Exception {

    //    WorkflowParameterObj params =
    //        new WorkflowParameterObj(amountCents, ExecutionScenarioObj.HAPPY_PATH);
    //
    //    runWorkflow(params);

    System.exit(0);
  }

  private static String generateReferenceNumber() {
    return String.format(
        "TRANSFER-%s-%03d",
        (char) (Math.random() * 26 + 'A')
            + ""
            + (char) (Math.random() * 26 + 'A')
            + ""
            + (char) (Math.random() * 26 + 'A'),
        (int) (Math.random() * 999));
  }
}
