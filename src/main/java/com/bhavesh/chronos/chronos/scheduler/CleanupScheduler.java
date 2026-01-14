package com.bhavesh.chronos.chronos.scheduler;

import com.bhavesh.chronos.chronos.entities.repo.JobExecutionRepository;
import com.bhavesh.chronos.chronos.entities.repo.JobLogRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CleanupScheduler {

    private final JobExecutionRepository executionRepo;
    private final JobLogRepository logRepo;

    public CleanupScheduler(JobExecutionRepository executionRepo,
                            JobLogRepository logRepo) {
        this.executionRepo = executionRepo;
        this.logRepo = logRepo;
    }

    @Scheduled(cron = "0 0 2 * * ?") // daily at 2 AM
    @Transactional
    public void cleanup() {

        logRepo.deleteOldLogs(
                LocalDateTime.now().minusDays(7));

        executionRepo.deleteOldExecutions(
                LocalDateTime.now().minusDays(30));
    }
}
