package com.clinic.observer;

import com.clinic.domain.Appointment;
import com.clinic.domain.AppointmentStatus;

public interface AppointmentObserver {

    void onStatusChanged(Appointment appointment, AppointmentStatus previousStatus);
}
