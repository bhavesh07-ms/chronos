package com.bhavesh.chronos.chronos.service;

import com.bhavesh.chronos.chronos.dto.CreateJobRequest;
import com.bhavesh.chronos.chronos.dto.JobResponse;
import com.bhavesh.chronos.chronos.dto.RescheduleJobRequest;
import com.bhavesh.chronos.chronos.entities.Job;
import com.bhavesh.chronos.chronos.entities.JobExecution;

import java.util.List;

public interface JobService {

    JobResponse createJob(CreateJobRequest request);

    JobResponse getJob(Long id);

    List<JobResponse> listJobs();

    void cancelJob(Long id);

    List<JobExecution> getExecutions(Long jobId);

    Job pauseJob(Long id);

    Job resumeJob(Long id);
    public JobResponse rescheduleJob(Long id, RescheduleJobRequest request);
}

