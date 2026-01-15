package com.bhavesh.chronos.chronos.entities;

import com.bhavesh.chronos.chronos.enums.ExecutionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_executions")
@Getter
@Setter
public class JobExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;

    private int attemptNumber;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Column(length = 2000)
    private String errorMessage;

    private LocalDateTime endAt;


    // NEW
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
