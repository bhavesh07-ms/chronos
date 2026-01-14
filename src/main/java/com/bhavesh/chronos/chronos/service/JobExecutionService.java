package com.bhavesh.chronos.chronos.service;

import com.bhavesh.chronos.chronos.entities.Job;

public interface JobExecutionService {
    void execute(Job job);
}
