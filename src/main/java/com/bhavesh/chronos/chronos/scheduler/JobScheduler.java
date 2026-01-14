package com.bhavesh.chronos.chronos.scheduler;

import com.bhavesh.chronos.chronos.entities.Job;
import com.bhavesh.chronos.chronos.entities.repo.JobRepository;
import com.bhavesh.chronos.chronos.enums.JobStatus;
import com.bhavesh.chronos.chronos.service.JobExecutionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class JobScheduler {

    private final JobRepository jobRepository;
    private final JobExecutionService executionService;

    public JobScheduler(JobRepository jobRepository,
                        JobExecutionService executionService) {
        this.jobRepository = jobRepository;
        this.executionService = executionService;
    }

    @Scheduled(fixedDelay = 10000) // every 10 seconds
    public void pollAndExecuteJobs() {

        List<Job> jobs = jobRepository.findDueJobs(LocalDateTime.now());

        for (Job job : jobs) {

            // STOP conditions
            if (job.getStatus() != JobStatus.ACTIVE) continue;

            if (job.getEndTime() != null &&
                    LocalDateTime.now().isAfter(job.getEndTime())) {
                job.setStatus(JobStatus.COMPLETED);
                jobRepository.save(job);
                continue;
            }

            executionService.execute(job);
        }
    }


}

