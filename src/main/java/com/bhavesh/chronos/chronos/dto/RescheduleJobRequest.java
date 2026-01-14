package com.bhavesh.chronos.chronos.dto;

import com.bhavesh.chronos.chronos.enums.ScheduleType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RescheduleJobRequest {

    private ScheduleType scheduleType;
    private LocalDateTime runAt;
    private String cronExpression;
}