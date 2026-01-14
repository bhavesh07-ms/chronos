package com.bhavesh.chronos.chronos.service.impl;

import com.bhavesh.chronos.chronos.dto.CreateJobRequest;
import com.bhavesh.chronos.chronos.dto.JobResponse;
import com.bhavesh.chronos.chronos.dto.RescheduleJobRequest;
import com.bhavesh.chronos.chronos.entities.Job;
import com.bhavesh.chronos.chronos.entities.JobExecution;
import com.bhavesh.chronos.chronos.entities.repo.JobExecutionRepository;
import com.bhavesh.chronos.chronos.entities.repo.JobRepository;
import com.bhavesh.chronos.chronos.enums.JobStatus;
import com.bhavesh.chronos.chronos.enums.ScheduleType;
import com.bhavesh.chronos.chronos.helper.ScheduleCalculator;
import com.bhavesh.chronos.chronos.service.JobService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobExecutionRepository executionRepository;
    private final ScheduleCalculator scheduleCalculator;

    public JobServiceImpl(JobRepository jobRepository,
                          JobExecutionRepository executionRepository,
                          ScheduleCalculator scheduleCalculator) {
        this.jobRepository = jobRepository;
        this.executionRepository = executionRepository;
        this.scheduleCalculator = scheduleCalculator;
    }

    @Override
    public JobResponse createJob(CreateJobRequest request) {

        Job job = new Job();
        job.setName(request.getName());
        job.setJobType(request.getJobType());
        job.setScheduleType(request.getScheduleType());
        job.setMaxRetries(request.getMaxRetries());
        job.setToEmail(request.getToEmail());
        job.setSubject(request.getSubject());
        job.setBody(request.getBody());
        job.setRetryCount(0);
        job.setStatus(JobStatus.ACTIVE);

        if (request.getScheduleType() == ScheduleType.IMMEDIATE) {
            job.setNextRunAt(LocalDateTime.now());

        } else if (request.getScheduleType() == ScheduleType.ONE_TIME) {
            job.setNextRunAt(request.getRunAt());

        } else {
            job.setRecurringSchedule(request.getRecurringSchedule());
            job.setNextRunAt(
                    scheduleCalculator.calculateNextRun(
                            request.getRecurringSchedule(),
                            LocalDateTime.now()
                    )
            );
        }


        Job saved = jobRepository.save(job);
        return mapToResponse(saved);
    }

    @Override
    public JobResponse getJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return mapToResponse(job);
    }

    @Override
    public List<JobResponse> listJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void cancelJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setStatus(JobStatus.CANCELLED);
        job.setTerminalFailureReason("Job cancelled by user");
        jobRepository.save(job);
    }

    @Override
    public List<JobExecution> getExecutions(Long jobId) {
        return executionRepository.findByJobId(jobId);
    }

    @Override
    @Transactional
    public Job pauseJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow();
        job.setStatus(JobStatus.PAUSED);
        return job;
    }

    @Override
    @Transactional
    public Job resumeJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow();

        if (job.getStatus() == JobStatus.PAUSED) {
            job.setStatus(JobStatus.ACTIVE);
        }
        return job;
    }


    private JobResponse mapToResponse(Job job) {

        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setName(job.getName());
        response.setStatus(job.getStatus());
        response.setScheduleType(job.getScheduleType());
        response.setNextRunAt(job.getNextRunAt());
        response.setRetryCount(job.getRetryCount());
        response.setMaxRetries(job.getMaxRetries());
        response.setTerminalFailureReason(job.getTerminalFailureReason());
        return response;
    }

    @Override
    @Transactional
    public JobResponse rescheduleJob(Long id, RescheduleJobRequest request) {

        Job job = jobRepository.findById(id)
                .orElseThrow();

        //  Safety checks
        if (job.getStatus() == JobStatus.RUNNING) {
            throw new IllegalStateException("Cannot reschedule a running job");
        }

        if (job.getStatus() == JobStatus.CANCELLED) {
            throw new IllegalStateException("Cannot reschedule a cancelled job");
        }

        // Allowed even if FAILED due to max retries
        job.setRetryCount(0);
        job.setTerminalFailureReason(null);

        job.setScheduleType(request.getScheduleType());

        if (request.getScheduleType() == ScheduleType.IMMEDIATE) {
            job.setNextRunAt(LocalDateTime.now());

        } else if (request.getScheduleType() == ScheduleType.ONE_TIME) {
            job.setNextRunAt(request.getRunAt());

        } else {
            job.setRecurringSchedule(job.getRecurringSchedule());
            job.setNextRunAt(
                    scheduleCalculator.calculateNextRun(
                            job.getRecurringSchedule(),
                            LocalDateTime.now()
                    )
            );
        }

        job.setStatus(JobStatus.ACTIVE);

        jobRepository.save(job);

        return mapToResponse(job);
    }


}
