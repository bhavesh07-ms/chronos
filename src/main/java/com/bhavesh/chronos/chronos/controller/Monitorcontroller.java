package com.bhavesh.chronos.chronos.controller;

import com.bhavesh.chronos.chronos.dto.SystemHealthResponse;
import com.bhavesh.chronos.chronos.service.impl.MonitoringServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/monitoring")
public class Monitorcontroller {

    MonitoringServiceImpl monitoringService;

    Monitorcontroller(MonitoringServiceImpl monitoringService) {
        this.monitoringService = monitoringService;
    }
    @GetMapping("/health")
    public SystemHealthResponse health() {
        return monitoringService.getHealth();
    }

}
