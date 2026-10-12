package com.clinic.notification;

import com.clinic.domain.Patient;

public interface NotificationChannel {

    void send(Patient patient, String message);
}
