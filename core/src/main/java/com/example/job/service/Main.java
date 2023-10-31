package com.example.job.service;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    Job myJob = new Job("type1", 1, 5);
    myJob.run();
  }
}
