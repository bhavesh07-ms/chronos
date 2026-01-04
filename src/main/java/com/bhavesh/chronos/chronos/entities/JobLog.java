package com.bhavesh.chronos.chronos.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_logs")
@Getter
@Setter
public class JobLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_execution_id", nullable = false)
    private JobExecution jobExecution;

    @Column(nullable = false, length = 4000)
    private String logMessage;

    private LocalDateTime timestamp;

    @PrePersist
    void onCreate() {
        timestamp = LocalDateTime.now();
    }

    // getters & setters
}
