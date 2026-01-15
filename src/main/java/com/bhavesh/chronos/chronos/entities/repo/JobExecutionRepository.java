package com.bhavesh.chronos.chronos.entities.repo;

import com.bhavesh.chronos.chronos.entities.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JobExecutionRepository
        extends JpaRepository<JobExecution, Long> {

    List<JobExecution> findByJobId(Long jobId);

    @Modifying
    @Query("DELETE FROM JobExecution e WHERE e.createdAt < :cutoff")
    void deleteOldExecutions(@Param("cutoff") LocalDateTime cutoff);

    @Query("""
SELECT COUNT(e) FROM JobExecution e
WHERE e.status = 'FAILED'
AND e.createdAt >= :since
""")
    long countRecentFailures(@Param("since") LocalDateTime since);
}
