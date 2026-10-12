package com.clinic.domain;

import java.util.Objects;

public abstract class Person {

    private final String fullName;
    private final ContactInfo contactInfo;

    protected Person(String fullName, ContactInfo contactInfo) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("ФИО не может быть пустым");
        }
        this.fullName = fullName.strip();
        this.contactInfo = Objects.requireNonNull(contactInfo, "Контактные данные обязательны");
    }

    public String getFullName() {
        return fullName;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }
}
