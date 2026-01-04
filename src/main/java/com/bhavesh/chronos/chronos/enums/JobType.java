package com.bhavesh.chronos.chronos.enums;

public enum  JobType {

    EMAIL("email"),
    DUMMY("dummy");
    private final String type;
    JobType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
