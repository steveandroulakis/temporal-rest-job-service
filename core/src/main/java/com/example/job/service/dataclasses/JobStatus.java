package com.example.job.service.dataclasses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JobStatus {
  private JobState jobState;
  private int stepState;

  public JobStatus(JobState jobState, int stepState) {
    this.jobState = jobState;
    this.stepState = stepState;
  }

  public JobState getJobState() {
    return jobState;
  }

  public int getStepState() {
    return stepState;
  }

  public void setJobState(JobState jobState) {
    this.jobState = jobState;
  }

  public void setStepState(int stepState) {
    this.stepState = stepState;
  }
}
