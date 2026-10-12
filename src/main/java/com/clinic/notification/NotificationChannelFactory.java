package com.clinic.notification;

import com.clinic.domain.ContactPreference;

public final class NotificationChannelFactory {

    public NotificationChannel create(ContactPreference preference) {
        return switch (preference) {
            case SMS -> new SmsNotificationChannel();
            case EMAIL -> new EmailNotificationChannel();
        };
    }
}
