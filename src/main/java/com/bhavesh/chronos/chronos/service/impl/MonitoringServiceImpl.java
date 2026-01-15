package com.bhavesh.chronos.chronos.service.impl;

import com.bhavesh.chronos.chronos.dto.SystemHealthResponse;
import com.bhavesh.chronos.chronos.entities.repo.JobExecutionRepository;
import com.bhavesh.chronos.chronos.entities.repo.JobRepository;
import com.bhavesh.chronos.chronos.enums.JobStatus;
import com.bhavesh.chronos.chronos.service.MonitoringService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MonitoringServiceImpl implements MonitoringService {

    private final JobRepository jobRepository;
    private final JobExecutionRepository executionRepository;

    public MonitoringServiceImpl(JobRepository jobRepository,
                             JobExecutionRepository executionRepository) {
        this.jobRepository = jobRepository;
        this.executionRepository = executionRepository;
    }

    public SystemHealthResponse getHealth() {

        SystemHealthResponse response = new SystemHealthResponse();

        response.setSchedulerRunning(true); // app is up
        response.setActiveJobs(jobRepository.countByStatus(JobStatus.ACTIVE));
        response.setRunningJobs(jobRepository.countByStatus(JobStatus.RUNNING));
        response.setPendingJobs(jobRepository.countPendingJobs(LocalDateTime.now()));
        response.setFailedJobsLast24h(
                executionRepository.countRecentFailures(
                        LocalDateTime.now().minusHours(24)
                )
        );

        return response;
    }
}
