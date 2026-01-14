package com.bhavesh.chronos.chronos.service.impl;

import com.bhavesh.chronos.chronos.entities.Job;
import com.bhavesh.chronos.chronos.entities.JobExecution;
import com.bhavesh.chronos.chronos.entities.JobLog;
import com.bhavesh.chronos.chronos.entities.repo.JobExecutionRepository;
import com.bhavesh.chronos.chronos.entities.repo.JobLogRepository;
import com.bhavesh.chronos.chronos.entities.repo.JobRepository;
import com.bhavesh.chronos.chronos.enums.ExecutionStatus;
import com.bhavesh.chronos.chronos.enums.JobStatus;
import com.bhavesh.chronos.chronos.enums.JobType;
import com.bhavesh.chronos.chronos.enums.ScheduleType;
import com.bhavesh.chronos.chronos.helper.ScheduleCalculator;
import com.bhavesh.chronos.chronos.service.JobExecutionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;

@Service
public class JobExecutionServiceImpl implements JobExecutionService {

    private final JobRepository jobRepository;
    private final JobExecutionRepository executionRepository;
    private final JobLogRepository logRepository;
    private final Executor executor;
    private final ScheduleCalculator scheduleCalculator;

    public JobExecutionServiceImpl(
            JobRepository jobRepository,
            JobExecutionRepository executionRepository,
            JobLogRepository logRepository,
            Executor executor,
            ScheduleCalculator scheduleCalculator
    ) {
        this.jobRepository = jobRepository;
        this.executionRepository = executionRepository;
        this.logRepository = logRepository;
        this.executor = executor;
        this.scheduleCalculator = scheduleCalculator;
    }

    @Override
    @Transactional
    public void execute(Job job) {

        // LOCK job
        if (job.getStatus() != JobStatus.ACTIVE) {
            return;
        }

        jobRepository.save(job);

        executor.execute(() -> runJob(job));
    }

    @Transactional
    protected void runJob(Job job) {

        JobExecution execution = new JobExecution();
        execution.setJob(job);
        execution.setAttemptNumber(job.getRetryCount() + 1);
        execution.setStatus(ExecutionStatus.RUNNING);
        execution.setStartTime(LocalDateTime.now());
        executionRepository.save(execution);

        try {
            log(execution, "Job started");

            if (JobType.EMAIL.equals(job.getJobType())) {
                scheduleCalculator.executeEmail(job, execution);
            } else {
                throw new IllegalArgumentException("Unsupported job type");
            }

            execution.setStatus(ExecutionStatus.SUCCESS);
            execution.setEndTime(LocalDateTime.now());
            executionRepository.save(execution);

            log(execution, "Job completed successfully");
            handleSuccess(job);

        } catch (Exception ex) {

            execution.setStatus(ExecutionStatus.FAILED);
            execution.setErrorMessage(ex.getMessage());
            execution.setEndTime(LocalDateTime.now());
            executionRepository.save(execution);

            log(execution, "Job failed: " + ex.getMessage());
            handleFailure(job, ex.getMessage());
        }
    }

    private void handleSuccess(Job job) {

        if (job.getScheduleType() == ScheduleType.RECURRING) {

            LocalDateTime nextRun =
                    scheduleCalculator.calculateNextRun(
                            job.getRecurringSchedule(),
                            LocalDateTime.now()
                    );

            job.setNextRunAt(nextRun);
            job.setStatus(JobStatus.ACTIVE);

        } else {
            job.setStatus(JobStatus.COMPLETED);
            job.setNextRunAt(null);
        }

        jobRepository.save(job);
    }


    private void handleFailure(Job job, String errorMessage) {

        if (job.getRetryCount() < job.getMaxRetries()) {

            job.setRetryCount(job.getRetryCount() + 1);
            job.setNextRunAt(LocalDateTime.now().plusSeconds(10));
            job.setStatus(JobStatus.ACTIVE);

        } else {
            // TERMINAL FAILURE
            job.setStatus(JobStatus.FAILED);
            job.setTerminalFailureReason("MAX_RETRIES_EXHAUSTED");
            job.setNextRunAt(null);
        }

        jobRepository.save(job);
    }


    private void log(JobExecution execution, String message) {
        JobLog log = new JobLog();
        log.setJobExecution(execution);
        log.setLogMessage(message);
        logRepository.save(log);
    }
}
