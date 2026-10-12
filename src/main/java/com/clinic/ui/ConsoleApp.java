package com.clinic.ui;

import com.clinic.application.ClinicService;
import com.clinic.domain.Appointment;
import com.clinic.domain.AppointmentStatus;
import com.clinic.domain.ContactInfo;
import com.clinic.domain.ContactPreference;
import com.clinic.domain.Doctor;
import com.clinic.domain.MedicalRecordEntry;
import com.clinic.domain.Patient;
import com.clinic.domain.TimeSlot;
import com.clinic.domain.exception.DomainException;

import java.util.List;
import java.util.Scanner;

public final class ConsoleApp {

    private final ClinicService clinicService;
    private final Scanner scanner;
    private int patientCounter;

    public ConsoleApp(ClinicService clinicService, Scanner scanner, int initialPatientCounter) {
        this.clinicService = clinicService;
        this.scanner = scanner;
        this.patientCounter = initialPatientCounter;
    }

    public void run() {
        System.out.println("=== Информационная система клиники ===");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readIntChoice("Выберите пункт меню: ", 0, 8);
            try {
                running = handleChoice(choice);
            } catch (DomainException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
        System.out.println("Работа программы завершена.");
    }

    private boolean handleChoice(int choice) {
        switch (choice) {
            case 1 -> printDoctors();
            case 2 -> printPatients();
            case 3 -> registerPatient();
            case 4 -> scheduleAppointment();
            case 5 -> completeAppointment();
            case 6 -> cancelAppointment();
            case 7 -> printMedicalRecord();
            case 8 -> printAppointments();
            case 0 -> {
                return false;
            }
            default -> System.out.println("Неизвестный пункт меню.");
        }
        return true;
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Показать врачей");
        System.out.println("2. Показать пациентов");
        System.out.println("3. Зарегистрировать нового пациента");
        System.out.println("4. Записать пациента на приём");
        System.out.println("5. Провести приём (указать диагноз)");
        System.out.println("6. Отменить приём");
        System.out.println("7. Показать медицинскую карту пациента");
        System.out.println("8. Показать все приёмы");
        System.out.println("0. Выход");
    }

    private void printDoctors() {
        List<Doctor> doctors = clinicService.listDoctors();
        if (doctors.isEmpty()) {
            System.out.println("Врачи не зарегистрированы.");
            return;
        }
        for (int i = 0; i < doctors.size(); i++) {
            Doctor doctor = doctors.get(i);
            System.out.printf("%d) %s | %s | %s | свободных слотов: %d%n",
                    i + 1, doctor.getDoctorId(), doctor.getFullName(),
                    doctor.getSpecialization().getDisplayName(),
                    doctor.getSchedule().getAvailableSlots().size());
        }
    }

    private void printPatients() {
        List<Patient> patients = clinicService.listPatients();
        if (patients.isEmpty()) {
            System.out.println("Пациенты не зарегистрированы.");
            return;
        }
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            System.out.printf("%d) %s | %s | %s%n",
                    i + 1, patient.getPatientId(), patient.getFullName(), patient.getContactInfo());
        }
    }

    private void registerPatient() {
        String fullName = readNonBlankLine("Введите ФИО пациента: ");
        String phone = readNonBlankLine("Введите телефон: ");
        String email = readNonBlankLine("Введите email: ");
        int prefChoice = readIntChoice("Предпочитаемый канал связи (1 - SMS, 2 - EMAIL): ", 1, 2);
        ContactPreference preference = prefChoice == 1 ? ContactPreference.SMS : ContactPreference.EMAIL;
        patientCounter++;
        String patientId = "P" + patientCounter;
        ContactInfo contactInfo = new ContactInfo(phone, email, preference);
        Patient patient = clinicService.registerPatient(patientId, fullName, contactInfo);
        System.out.println("Пациент зарегистрирован с идентификатором " + patient.getPatientId());
    }

    private void scheduleAppointment() {
        List<Patient> patients = clinicService.listPatients();
        if (patients.isEmpty()) {
            System.out.println("Сначала зарегистрируйте пациента.");
            return;
        }
        printPatients();
        Patient patient = patients.get(readIntChoice("Выберите пациента (номер): ", 1, patients.size()) - 1);

        List<Doctor> doctors = clinicService.listDoctors();
        if (doctors.isEmpty()) {
            System.out.println("Нет доступных врачей.");
            return;
        }
        printDoctors();
        Doctor doctor = doctors.get(readIntChoice("Выберите врача (номер): ", 1, doctors.size()) - 1);

        List<TimeSlot> slots = doctor.getSchedule().getAvailableSlots();
        if (slots.isEmpty()) {
            System.out.println("У выбранного врача нет свободных слотов.");
            return;
        }
        for (int i = 0; i < slots.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, slots.get(i));
        }
        TimeSlot slot = slots.get(readIntChoice("Выберите слот (номер): ", 1, slots.size()) - 1);

        Appointment appointment = clinicService.scheduleAppointment(
                patient.getPatientId(), doctor.getDoctorId(), slot);
        System.out.println("Приём создан, идентификатор: " + appointment.getAppointmentId());
    }

    private void completeAppointment() {
        Appointment appointment = pickAppointmentByStatus(AppointmentStatus.SCHEDULED, "запланированных");
        if (appointment == null) {
            return;
        }
        String diagnosis = readNonBlankLine("Введите диагноз: ");
        clinicService.completeAppointment(appointment.getAppointmentId(), diagnosis);
        System.out.println("Приём отмечен как проведённый.");
    }

    private void cancelAppointment() {
        Appointment appointment = pickAppointmentByStatus(AppointmentStatus.SCHEDULED, "запланированных");
        if (appointment == null) {
            return;
        }
        clinicService.cancelAppointment(appointment.getAppointmentId());
        System.out.println("Приём отменён.");
    }

    private Appointment pickAppointmentByStatus(AppointmentStatus status, String statusLabel) {
        List<Appointment> matching = clinicService.listAppointments().stream()
                .filter(a -> a.getStatus() == status)
                .toList();
        if (matching.isEmpty()) {
            System.out.println("Нет " + statusLabel + " приёмов.");
            return null;
        }
        for (int i = 0; i < matching.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, describeAppointment(matching.get(i)));
        }
        int index = readIntChoice("Выберите приём (номер): ", 1, matching.size());
        return matching.get(index - 1);
    }

    private void printMedicalRecord() {
        List<Patient> patients = clinicService.listPatients();
        if (patients.isEmpty()) {
            System.out.println("Пациенты не зарегистрированы.");
            return;
        }
        printPatients();
        Patient patient = patients.get(readIntChoice("Выберите пациента (номер): ", 1, patients.size()) - 1);
        List<MedicalRecordEntry> entries = clinicService.getMedicalRecord(patient.getPatientId()).getEntries();
        if (entries.isEmpty()) {
            System.out.println("Медицинская карта пуста.");
            return;
        }
        entries.forEach(entry -> System.out.println(" - " + entry));
    }

    private void printAppointments() {
        List<Appointment> appointments = clinicService.listAppointments();
        if (appointments.isEmpty()) {
            System.out.println("Приёмы отсутствуют.");
            return;
        }
        appointments.forEach(appointment -> System.out.println(" - " + describeAppointment(appointment)));
    }

    private String describeAppointment(Appointment appointment) {
        return "%s | пациент %s | врач %s | %s | статус %s".formatted(
                appointment.getAppointmentId(),
                appointment.getPatient().getFullName(),
                appointment.getDoctor().getFullName(),
                appointment.getTimeSlot(),
                appointment.getStatus());
    }

    private String readNonBlankLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (!line.isBlank()) {
                return line.strip();
            }
            System.out.println("Значение не может быть пустым, попробуйте снова.");
        }
    }

    private int readIntChoice(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().strip();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Введите число от %d до %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Нужно ввести целое число, попробуйте снова.");
            }
        }
    }
}
