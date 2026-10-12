package com.clinic;

import com.clinic.application.ClinicService;
import com.clinic.domain.Clinic;
import com.clinic.domain.ContactInfo;
import com.clinic.domain.ContactPreference;
import com.clinic.domain.Doctor;
import com.clinic.domain.Patient;
import com.clinic.domain.Specialization;
import com.clinic.domain.TimeSlot;
import com.clinic.notification.NotificationChannelFactory;
import com.clinic.observer.MedicalRecordUpdater;
import com.clinic.observer.PatientNotifier;
import com.clinic.ui.ConsoleApp;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public final class 1Main {

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        Clinic clinic = new Clinic();
        ClinicService clinicService = new ClinicService(clinic);

        NotificationChannelFactory channelFactory = new NotificationChannelFactory();
        clinicService.addObserver(new PatientNotifier(channelFactory));
        clinicService.addObserver(new MedicalRecordUpdater());

        seedData(clinicService);

        ConsoleApp app = new ConsoleApp(clinicService, new Scanner(System.in, StandardCharsets.UTF_8), 2);
        app.run();
    }

    private static void seedData(ClinicService clinicService) {
        Doctor ivanov = new Doctor("D1", "Иванов Иван Иванович",
                new ContactInfo("+375291112233", "ivanov@clinic.by", ContactPreference.EMAIL),
                Specialization.CARDIOLOGIST);
        TimeSlot ivanovTodaySlot = new TimeSlot(LocalDate.now(), LocalTime.of(15, 0), LocalTime.of(15, 30));
        ivanov.getSchedule().addSlot(ivanovTodaySlot);
        ivanov.getSchedule().addSlot(new TimeSlot(LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(9, 30)));
        ivanov.getSchedule().addSlot(new TimeSlot(LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(10, 30)));
        clinicService.registerDoctor(ivanov);

        Doctor petrova = new Doctor("D2", "Петрова Анна Сергеевна",
                new ContactInfo("+375291114455", "petrova@clinic.by", ContactPreference.SMS),
                Specialization.THERAPIST);
        petrova.getSchedule().addSlot(new TimeSlot(LocalDate.now().plusDays(1), LocalTime.of(11, 0), LocalTime.of(11, 30)));
        petrova.getSchedule().addSlot(new TimeSlot(LocalDate.now().plusDays(2), LocalTime.of(14, 0), LocalTime.of(14, 30)));
        clinicService.registerDoctor(petrova);

        Patient sidorov = clinicService.registerPatient("P1", "Сидоров Пётр Максимович",
                new ContactInfo("+375291556677", "sidorov@example.com", ContactPreference.SMS));
        clinicService.registerPatient("P2", "Кузнецова Мария Олеговна",
                new ContactInfo("+375291778899", "kuznetsova@example.com", ContactPreference.EMAIL));

        clinicService.scheduleAppointment(sidorov.getPatientId(), ivanov.getDoctorId(), ivanovTodaySlot);
    }
}
