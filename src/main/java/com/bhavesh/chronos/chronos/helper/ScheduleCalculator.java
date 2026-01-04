package com.bhavesh.chronos.chronos.helper;

import com.bhavesh.chronos.chronos.entities.Job;
import com.bhavesh.chronos.chronos.entities.JobExecution;
import com.bhavesh.chronos.chronos.entities.JobLog;
import com.bhavesh.chronos.chronos.entities.repo.JobLogRepository;
import com.bhavesh.chronos.chronos.enums.RecurringSchedule;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.logging.Logger;


@Component
public class ScheduleCalculator {

    private final JobLogRepository logRepository;

    public ScheduleCalculator(JobLogRepository logRepository) {
        this.logRepository = logRepository;
    }
    public LocalDateTime calculateNextRun(
            RecurringSchedule schedule,
            LocalDateTime fromTime) {

        return switch (schedule) {
            case HOURLY -> fromTime.plusHours(1);
            case DAILY -> fromTime.plusDays(1);
            case WEEKLY -> fromTime.plusWeeks(1);
            case MONTHLY -> fromTime.plusMonths(1);
        };
    }

    private void executeEmailViaPython(Job job, JobExecution execution) throws Exception {

        ProcessBuilder pb = new ProcessBuilder(
                "python",
                "send_mail.py",
                job.getToEmail(),
                job.getSubject(),
                job.getBody()
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                log(execution, "PYTHON: " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Email sending failed");
        }
    }

    private void log(JobExecution execution, String message) {
        JobLog log = new JobLog();
        log.setJobExecution(execution);
        log.setLogMessage(message);
        logRepository.save(log);
    }
}
