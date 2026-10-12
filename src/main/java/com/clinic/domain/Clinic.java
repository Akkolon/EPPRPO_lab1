package com.clinic.domain;

import com.clinic.domain.exception.NotFoundException;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Clinic {

    private final Map<String, Doctor> doctors = new LinkedHashMap<>();
    private final Map<String, Patient> patients = new LinkedHashMap<>();
    private final Map<String, Appointment> appointments = new LinkedHashMap<>();

    public void registerDoctor(Doctor doctor) {
        doctors.put(doctor.getDoctorId(), doctor);
    }

    public void registerPatient(Patient patient) {
        patients.put(patient.getPatientId(), patient);
    }

    public void registerAppointment(Appointment appointment) {
        appointments.put(appointment.getAppointmentId(), appointment);
    }

    public Doctor findDoctor(String doctorId) {
        Doctor doctor = doctors.get(doctorId);
        if (doctor == null) {
            throw new NotFoundException("Врач с идентификатором %s не найден".formatted(doctorId));
        }
        return doctor;
    }

    public Patient findPatient(String patientId) {
        Patient patient = patients.get(patientId);
        if (patient == null) {
            throw new NotFoundException("Пациент с идентификатором %s не найден".formatted(patientId));
        }
        return patient;
    }

    public Appointment findAppointment(String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment == null) {
            throw new NotFoundException("Приём с идентификатором %s не найден".formatted(appointmentId));
        }
        return appointment;
    }

    public List<Doctor> listDoctors() {
        return Collections.unmodifiableList(List.copyOf(doctors.values()));
    }

    public List<Patient> listPatients() {
        return Collections.unmodifiableList(List.copyOf(patients.values()));
    }

    public List<Appointment> listAppointments() {
        return Collections.unmodifiableList(List.copyOf(appointments.values()));
    }
}
