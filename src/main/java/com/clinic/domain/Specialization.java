package com.clinic.domain;

public enum Specialization {
    THERAPIST("Терапевт"),
    CARDIOLOGIST("Кардиолог"),
    DENTIST("Стоматолог"),
    SURGEON("Хирург");

    private final String displayName;

    Specialization(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
