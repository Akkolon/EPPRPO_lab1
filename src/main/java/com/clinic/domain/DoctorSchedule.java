package com.clinic.domain;

import com.clinic.domain.exception.SlotNotAvailableException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DoctorSchedule {

    private final List<TimeSlot> availableSlots = new ArrayList<>();

    public void addSlot(TimeSlot slot) {
        if (!availableSlots.contains(slot)) {
            availableSlots.add(slot);
        }
    }

    public boolean isAvailable(TimeSlot slot) {
        return availableSlots.contains(slot);
    }

    public void reserve(TimeSlot slot) {
        if (!availableSlots.remove(slot)) {
            throw new SlotNotAvailableException("Слот %s уже занят или не существует".formatted(slot));
        }
    }

    public void release(TimeSlot slot) {
        addSlot(slot);
    }

    public List<TimeSlot> getAvailableSlots() {
        return Collections.unmodifiableList(availableSlots);
    }
}
