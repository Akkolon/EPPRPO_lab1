package com.clinic.domain;

import com.clinic.domain.exception.InvalidStatusTransitionException;

import java.util.Objects;

public final class Appointment {

    private final String appointmentId;
    private final Patient patient;
    private final Doctor doctor;
    private final TimeSlot timeSlot;
    private AppointmentStatus status;
    private Diagnosis diagnosis;

    public Appointment(String appointmentId, Patient patient, Doctor doctor, TimeSlot timeSlot) {
        this.appointmentId = Objects.requireNonNull(appointmentId, "Идентификатор приёма обязателен");
        this.patient = Objects.requireNonNull(patient, "Пациент обязателен");
        this.doctor = Objects.requireNonNull(doctor, "Врач обязателен");
        this.timeSlot = Objects.requireNonNull(timeSlot, "Слот приёма обязателен");
        this.status = AppointmentStatus.SCHEDULED;
    }

    public void complete(Diagnosis diagnosis) {
        changeStatus(AppointmentStatus.COMPLETED);
        this.diagnosis = Objects.requireNonNull(diagnosis, "Диагноз обязателен при завершении приёма");
    }

    public void cancel() {
        changeStatus(AppointmentStatus.CANCELLED);
    }

    private void changeStatus(AppointmentStatus target) {
        if (!status.canTransitionTo(target)) {
            throw new InvalidStatusTransitionException(
                    "Переход из статуса %s в %s недопустим".formatted(status, target));
        }
        this.status = target;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }
}
