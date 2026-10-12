package com.clinic.domain;

public enum AppointmentStatus {

    SCHEDULED,
    COMPLETED,
    CANCELLED;

    public boolean canTransitionTo(AppointmentStatus target) {
        return this == SCHEDULED && (target == COMPLETED || target == CANCELLED);
    }
}
