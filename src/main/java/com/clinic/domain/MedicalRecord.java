package com.clinic.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MedicalRecord {

    private final List<MedicalRecordEntry> entries = new ArrayList<>();

    public void addEntry(MedicalRecordEntry entry) {
        entries.add(entry);
    }

    public List<MedicalRecordEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}
