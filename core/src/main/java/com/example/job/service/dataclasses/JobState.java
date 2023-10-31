package com.example.job.service.dataclasses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JobState {

  private String id;
  private JobRunStatus jobRunStatus;
  private int stepState;
  private int stepTotal;

  @JsonCreator
  public JobState(
      @JsonProperty("id") String id,
      @JsonProperty("stepState") int stepState,
      @JsonProperty("stepTotal") int stepTotal) {
    this.id = id;
    this.jobRunStatus = JobRunStatus.RUNNING;
    this.stepState = stepState;
    this.stepTotal = stepTotal;
  }

  @Override
  public String toString() {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return super.toString();
    }
  }

  // getters
  public String getId() {
    return this.id;
  }

  public JobRunStatus getJobRunStatus() {
    return this.jobRunStatus;
  }

  public int getStepState() {
    return this.stepState;
  }

  public int getStepTotal() {
    return this.stepTotal;
  }

  // setters
  public void setId(String id) {
    this.id = id;
  }

  public void setJobRunStatus(JobRunStatus jobRunStatus) {
    this.jobRunStatus = jobRunStatus;
  }

  public void setStepState(int stepState) {
    this.stepState = stepState;
  }

  public void setStepTotal(int stepTotal) {
    this.stepTotal = stepTotal;
  }
}
