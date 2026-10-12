package com.clinic.domain;

import java.util.Objects;

public final class Doctor extends Person {

    private final String doctorId;
    private final Specialization specialization;
    private final DoctorSchedule schedule;

    public Doctor(String doctorId, String fullName, ContactInfo contactInfo, Specialization specialization) {
        super(fullName, contactInfo);
        if (doctorId == null || doctorId.isBlank()) {
            throw new IllegalArgumentException("Идентификатор врача не может быть пустым");
        }
        this.doctorId = doctorId;
        this.specialization = Objects.requireNonNull(specialization, "Специализация обязательна");
        this.schedule = new DoctorSchedule();
    }

    public String getDoctorId() {
        return doctorId;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public DoctorSchedule getSchedule() {
        return schedule;
    }
}
