package com.bhavesh.chronos.chronos.entities.repo;

import com.bhavesh.chronos.chronos.entities.JobLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JobLogRepository
        extends JpaRepository<JobLog, Long> {

    List<JobLog> findByJobExecutionId(Long executionId);

    @Modifying
    @Query("DELETE FROM JobLog l WHERE l.timestamp < :cutoff")
    void deleteOldLogs(@Param("cutoff") LocalDateTime cutoff);

}

