package com.clinic.domain;

public final class Patient extends Person {

    private final String patientId;
    private final MedicalRecord medicalRecord;

    public Patient(String patientId, String fullName, ContactInfo contactInfo) {
        super(fullName, contactInfo);
        if (patientId == null || patientId.isBlank()) {
            throw new IllegalArgumentException("Идентификатор пациента не может быть пустым");
        }
        this.patientId = patientId;
        this.medicalRecord = new MedicalRecord();
    }

    public String getPatientId() {
        return patientId;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }
}
