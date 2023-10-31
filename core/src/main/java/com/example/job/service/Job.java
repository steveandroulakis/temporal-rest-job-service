package com.example.job.service;

import com.example.job.service.dataclasses.JobData;
import com.example.job.service.dataclasses.JobState;
import java.util.UUID;

public class Job {
  private JobData jobData;
  private JobState jobState;
  private int stepState;

  public Job(String type, int stepLength, int steps) {
    this.jobData = new JobData(generateId(), type, stepLength, steps);
    this.jobState = JobState.Running;
    this.stepState = 0;
  }

  private String generateId() {
    return "job-" + UUID.randomUUID().toString().substring(0, 6);
  }

  public JobData getJobData() {
    return this.jobData;
  }

  public void run() throws InterruptedException {
    System.out.println("Running job " + jobData.getId());
    System.out.println("Data " + this.getJobDataAsJson());

    for (int i = 0; i < jobData.getSteps(); i++) {
      // get job status
      stepState = i;
      Thread.sleep((long) jobData.getStepLength() * 1000);
      System.out.println("Job Status " + this.getStateAsJson());
    }
    jobState = JobState.Finished;
    System.out.println("Job Status " + this.getStateAsJson());
  }

  // get jobData as JSON
  public String getJobDataAsJson() {
    return "{\"id\":\""
        + this.jobData.getId()
        + "\",\"type\":\""
        + this.jobData.getType()
        + "\",\"stepLength\":"
        + this.jobData.getStepLength()
        + ",\"steps\":"
        + this.jobData.getSteps()
        + "}";
  }

  // get jobState and stepState as JSON
  public String getStateAsJson() {
    return "{\"jobState\":\"" + this.jobState + "\",\"stepState\":" + this.stepState + "}";
  }
}
