package com.bhavesh.chronos.chronos.entities;

import com.bhavesh.chronos.chronos.enums.JobStatus;
import com.bhavesh.chronos.chronos.enums.JobType;
import com.bhavesh.chronos.chronos.enums.RecurringSchedule;
import com.bhavesh.chronos.chronos.enums.ScheduleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Getter
@Setter
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @Enumerated(EnumType.STRING)
    private RecurringSchedule recurringSchedule;

    private LocalDateTime nextRunAt;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private int maxRetries;

    private int retryCount;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String TerminalFailureReason;

    private String toEmail;
    private String subject;

    @Column(length = 4000)
    private String body;
    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // getters & setters
}
