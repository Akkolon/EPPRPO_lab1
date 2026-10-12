package com.clinic.domain;

import java.util.Objects;

public final class ContactInfo {

    private final String phone;
    private final String email;
    private final ContactPreference preference;

    public ContactInfo(String phone, String email, ContactPreference preference) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Телефон не может быть пустым");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        this.phone = phone.strip();
        this.email = email.strip();
        this.preference = Objects.requireNonNull(preference, "Не указан предпочитаемый канал связи");
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public ContactPreference getPreference() {
        return preference;
    }

    @Override
    public String toString() {
        return "тел. %s, email %s".formatted(phone, email);
    }
}
