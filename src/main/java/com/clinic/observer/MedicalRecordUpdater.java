package com.clinic.observer;

import com.clinic.domain.Appointment;
import com.clinic.domain.AppointmentStatus;
import com.clinic.domain.MedicalRecordEntry;

public final class MedicalRecordUpdater implements AppointmentObserver {

    @Override
    public void onStatusChanged(Appointment appointment, AppointmentStatus previousStatus) {
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            return;
        }
        MedicalRecordEntry entry = new MedicalRecordEntry(
                appointment.getTimeSlot().getDate(),
                appointment.getDoctor().getFullName(),
                appointment.getDiagnosis());
        appointment.getPatient().getMedicalRecord().addEntry(entry);
    }
}
