package com.clinic.domain;

import java.time.LocalDate;
import java.util.Objects;

public final class Diagnosis {

    private final String description;
    private final LocalDate issuedOn;

    public Diagnosis(String description, LocalDate issuedOn) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Описание диагноза не может быть пустым");
        }
        this.description = description.strip();
        this.issuedOn = Objects.requireNonNull(issuedOn, "Дата постановки диагноза обязательна");
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getIssuedOn() {
        return issuedOn;
    }

    @Override
    public String toString() {
        return "%s (от %s)".formatted(description, issuedOn);
    }
}
