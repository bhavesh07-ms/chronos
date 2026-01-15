package com.bhavesh.chronos.chronos.service;

import com.bhavesh.chronos.chronos.dto.SystemHealthResponse;

public interface MonitoringService {
    public SystemHealthResponse getHealth();
}
