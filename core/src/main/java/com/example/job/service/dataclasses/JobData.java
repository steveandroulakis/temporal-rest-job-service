package com.example.job.service.dataclasses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JobData {
  private String id;
  private String type;
  private int stepLength;
  private int steps;

  public JobData(String id, String type, int stepLength, int steps) {
    this.id = id;
    this.type = type;
    this.stepLength = stepLength;
    this.steps = steps;
  }

  // getters and setters

  public String getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public int getStepLength() {
    return stepLength;
  }

  public int getSteps() {
    return steps;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setStepLength(int stepLength) {
    this.stepLength = stepLength;
  }

  public void setSteps(int steps) {
    this.steps = steps;
  }
}
