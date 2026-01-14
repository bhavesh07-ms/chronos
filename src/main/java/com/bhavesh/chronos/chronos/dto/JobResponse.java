package com.bhavesh.chronos.chronos.dto;

import com.bhavesh.chronos.chronos.enums.JobStatus;
import com.bhavesh.chronos.chronos.enums.ScheduleType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobResponse {

    private Long id;
    private String name;
    private JobStatus status;
    private ScheduleType scheduleType;
    private LocalDateTime nextRunAt;
    private String TerminalFailureReason;
    private int retryCount;
    private int maxRetries;
}
