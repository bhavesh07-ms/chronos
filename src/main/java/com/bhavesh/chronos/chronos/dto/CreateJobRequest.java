package com.bhavesh.chronos.chronos.dto;

import com.bhavesh.chronos.chronos.enums.JobType;
import com.bhavesh.chronos.chronos.enums.RecurringSchedule;
import com.bhavesh.chronos.chronos.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreateJobRequest {

    private String name;

    // FIX job type
    private JobType jobType; // EMAIL

    private ScheduleType scheduleType;

    // Email-specific fields
    private String toEmail;
    private String subject;
    private String body;

    private LocalDateTime runAt; // only for ONE_TIME
    private RecurringSchedule recurringSchedule; // only for RECURRING

    private int maxRetries;
}
