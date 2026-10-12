package com.clinic.notification;

import com.clinic.domain.Patient;

public final class EmailNotificationChannel implements NotificationChannel {

    @Override
    public void send(Patient patient, String message) {
        System.out.printf("[EMAIL -> %s] %s%n", patient.getContactInfo().getEmail(), message);
    }
}
