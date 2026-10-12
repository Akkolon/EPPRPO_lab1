package com.clinic.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public final class TimeSlot {

    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = Objects.requireNonNull(date, "Дата обязательна");
        this.startTime = Objects.requireNonNull(startTime, "Время начала обязательно");
        this.endTime = Objects.requireNonNull(endTime, "Время окончания обязательно");
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Время начала должно быть раньше времени окончания");
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TimeSlot timeSlot)) {
            return false;
        }
        return date.equals(timeSlot.date)
                && startTime.equals(timeSlot.startTime)
                && endTime.equals(timeSlot.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, startTime, endTime);
    }

    @Override
    public String toString() {
        return "%s %s-%s".formatted(date, startTime, endTime);
    }
}
