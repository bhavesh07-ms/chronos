package com.bhavesh.chronos.chronos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SystemHealthResponse {

    private boolean schedulerRunning;
    private long activeJobs;
    private long runningJobs;
    private long pendingJobs;
    private long failedJobsLast24h;

    // getters & setters
}
