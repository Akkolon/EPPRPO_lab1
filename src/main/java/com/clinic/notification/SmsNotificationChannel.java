package com.clinic.notification;

import com.clinic.domain.Patient;

public final class SmsNotificationChannel implements NotificationChannel {

    @Override
    public void send(Patient patient, String message) {
        System.out.printf("[SMS -> %s] %s%n", patient.getContactInfo().getPhone(), message);
    }
}
