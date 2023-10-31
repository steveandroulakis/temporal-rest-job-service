package com.example.job.service;

import com.example.job.service.dataclasses.JobData;
import com.example.job.service.dataclasses.JobState;
import com.example.job.service.dataclasses.JobStatus;
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
    for (int i = 0; i < jobData.getSteps(); i++) {
      stepState = i;
      Thread.sleep((long) jobData.getStepLength() * 1000);
    }
    jobState = JobState.Finished;
  }

  public JobStatus getStatus() {
    return new JobStatus(jobState, stepState);
  }
}
