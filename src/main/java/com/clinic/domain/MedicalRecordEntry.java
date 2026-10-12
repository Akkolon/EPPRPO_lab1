package com.clinic.domain;

import java.time.LocalDate;
import java.util.Objects;

public final class MedicalRecordEntry {

    private final LocalDate date;
    private final String doctorFullName;
    private final Diagnosis diagnosis;

    public MedicalRecordEntry(LocalDate date, String doctorFullName, Diagnosis diagnosis) {
        this.date = Objects.requireNonNull(date, "Дата записи обязательна");
        if (doctorFullName == null || doctorFullName.isBlank()) {
            throw new IllegalArgumentException("ФИО врача не может быть пустым");
        }
        this.doctorFullName = doctorFullName.strip();
        this.diagnosis = Objects.requireNonNull(diagnosis, "Диагноз обязателен для записи");
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDoctorFullName() {
        return doctorFullName;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    @Override
    public String toString() {
        return "%s — врач %s, диагноз: %s".formatted(date, doctorFullName, diagnosis);
    }
}
