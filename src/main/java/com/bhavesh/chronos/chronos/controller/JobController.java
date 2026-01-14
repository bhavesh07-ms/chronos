package com.bhavesh.chronos.chronos.controller;

import com.bhavesh.chronos.chronos.dto.CreateJobRequest;
import com.bhavesh.chronos.chronos.dto.JobResponse;
import com.bhavesh.chronos.chronos.dto.RescheduleJobRequest;
import com.bhavesh.chronos.chronos.entities.Job;
import com.bhavesh.chronos.chronos.entities.JobExecution;
import com.bhavesh.chronos.chronos.service.JobService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public JobResponse createJob(@RequestBody CreateJobRequest request) {
        return jobService.createJob(request);
    }

    @GetMapping("/{id}")
    public JobResponse getJob(@PathVariable Long id) {
        return jobService.getJob(id);
    }

    @GetMapping
    public List<JobResponse> listJobs() {
        return jobService.listJobs();
    }

    @DeleteMapping("/{id}")
    public void cancelJob(@PathVariable Long id) {
        jobService.cancelJob(id);
    }

    @GetMapping("/{id}/executions")
    public List<JobExecution> getExecutions(@PathVariable Long id) {
        return jobService.getExecutions(id);
    }

    @PatchMapping("/{id}/pause")
    public Job pauseJob(@PathVariable Long id) {
        return jobService.pauseJob(id);

    }

    @PatchMapping("/{id}/resume")
    public Job resumeJob(@PathVariable Long id) {
        return jobService.resumeJob(id);
    }

    @PatchMapping("/{id}/reschedule")
    public JobResponse reschedule(
            @PathVariable Long id,
            @RequestBody RescheduleJobRequest request) {

        return jobService.rescheduleJob(id, request);
    }

}
