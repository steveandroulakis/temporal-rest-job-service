package com.example.job.service;

import com.example.job.service.dataclasses.JobData;
import com.example.job.service.dataclasses.JobRunStatus;
import com.example.job.service.dataclasses.JobState;

public class Job {
  private JobData jobData;
  private JobState jobState;
  private int stepState;

  public Job(String type, int stepLength, int totalSteps) {
    this.jobData = new JobData(type, stepLength, totalSteps);
    this.jobData.setId(JobData.generateId(type));
    this.jobState = new JobState(this.jobData.getId(), 0, totalSteps);
    this.stepState = 0;
  }

  public JobData getJobData() {
    return this.jobData;
  }

  public JobState getJobState() {
    return this.jobState;
  }

  public void run() throws InterruptedException {
    System.out.println(
        "Running Job: "
            + jobData.getId()
            + " over "
            + (jobData.getSteps() * jobData.getStepLength())
            + " seconds");
    System.out.println("Data: " + this.getJobData());

    for (int i = 0; i < jobData.getSteps(); i++) {
      // get job status
      stepState = i;
      Thread.sleep((long) jobData.getStepLength() * 1000);
      this.getJobState().setStepState(stepState);
      System.out.println("Job Status: " + this.getJobState());
    }
    this.getJobState().setJobRunStatus(JobRunStatus.FINISHED);
    this.jobState = this.getJobState();
    System.out.println("Job Status: " + this.getJobState());
  }
}
