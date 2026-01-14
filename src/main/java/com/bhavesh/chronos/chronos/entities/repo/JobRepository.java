package com.bhavesh.chronos.chronos.entities.repo;

import com.bhavesh.chronos.chronos.entities.Job;
import com.bhavesh.chronos.chronos.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("""
        SELECT j FROM Job j
        WHERE j.nextRunAt <= :now
          AND j.status = 'ACTIVE'
    """)
    List<Job> findDueJobs(@Param("now") LocalDateTime now);

    @Query(
        "SELECT COUNT(j) FROM Job j WHERE j.status = :jobStatus"
    )
    long countByStatus(JobStatus jobStatus);

    @Query(
        "SELECT COUNT(j) FROM Job j WHERE j.nextRunAt <= :now AND j.status = 'ACTIVE'"
    )
    long countPendingJobs(LocalDateTime now);
}
