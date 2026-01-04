package com.bhavesh.chronos.chronos.enums;

public enum RecurringSchedule {

    HOURLY("hourly"),
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly");
    private final String label;
    RecurringSchedule(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }

}