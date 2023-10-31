package com.example.job.service.dataclasses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JobData {
  private String id;

  @JsonProperty("type")
  private String type;

  @JsonProperty("stepLength")
  private int stepLength;

  @JsonProperty("steps")
  private int steps;

  @JsonCreator
  public JobData(
      @JsonProperty("type") String type,
      @JsonProperty("stepLength") int stepLength,
      @JsonProperty("steps") int steps) {
    this.type = type;
    this.stepLength = stepLength;
    this.steps = steps;
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

  // generate ID static method
  public static String generateId(String type) {
    return type + "-" + UUID.randomUUID().toString().substring(0, 3);
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
