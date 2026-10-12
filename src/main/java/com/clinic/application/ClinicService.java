package com.clinic.application;

import com.clinic.domain.Appointment;
import com.clinic.domain.AppointmentStatus;
import com.clinic.domain.Clinic;
import com.clinic.domain.ContactInfo;
import com.clinic.domain.Diagnosis;
import com.clinic.domain.Doctor;
import com.clinic.domain.MedicalRecord;
import com.clinic.domain.Patient;
import com.clinic.domain.TimeSlot;
import com.clinic.observer.AppointmentObserver;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class ClinicService {

    private final Clinic clinic;
    private final List<AppointmentObserver> observers = new ArrayList<>();

    public ClinicService(Clinic clinic) {
        this.clinic = Objects.requireNonNull(clinic, "Клиника обязательна");
    }

    public void addObserver(AppointmentObserver observer) {
        observers.add(observer);
    }

    public void registerDoctor(Doctor doctor) {
        clinic.registerDoctor(doctor);
    }

    public Patient registerPatient(String patientId, String fullName, ContactInfo contactInfo) {
        Patient patient = new Patient(patientId, fullName, contactInfo);
        clinic.registerPatient(patient);
        return patient;
    }

    public Appointment scheduleAppointment(String patientId, String doctorId, TimeSlot slot) {
        Patient patient = clinic.findPatient(patientId);
        Doctor doctor = clinic.findDoctor(doctorId);
        doctor.getSchedule().reserve(slot);
        Appointment appointment = new Appointment(UUID.randomUUID().toString(), patient, doctor, slot);
        clinic.registerAppointment(appointment);
        notifyObservers(appointment, null);
        return appointment;
    }

    public Appointment completeAppointment(String appointmentId, String diagnosisDescription) {
        Appointment appointment = clinic.findAppointment(appointmentId);
        AppointmentStatus previousStatus = appointment.getStatus();
        appointment.complete(new Diagnosis(diagnosisDescription, LocalDate.now()));
        notifyObservers(appointment, previousStatus);
        return appointment;
    }

    public Appointment cancelAppointment(String appointmentId) {
        Appointment appointment = clinic.findAppointment(appointmentId);
        AppointmentStatus previousStatus = appointment.getStatus();
        appointment.cancel();
        appointment.getDoctor().getSchedule().release(appointment.getTimeSlot());
        notifyObservers(appointment, previousStatus);
        return appointment;
    }

    public MedicalRecord getMedicalRecord(String patientId) {
        return clinic.findPatient(patientId).getMedicalRecord();
    }

    public List<Doctor> listDoctors() {
        return clinic.listDoctors();
    }

    public List<Patient> listPatients() {
        return clinic.listPatients();
    }

    public List<Appointment> listAppointments() {
        return clinic.listAppointments();
    }

    private void notifyObservers(Appointment appointment, AppointmentStatus previousStatus) {
        for (AppointmentObserver observer : observers) {
            observer.onStatusChanged(appointment, previousStatus);
        }
    }
}
