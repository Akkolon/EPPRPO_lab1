package com.clinic.observer;

import com.clinic.domain.Appointment;
import com.clinic.domain.AppointmentStatus;
import com.clinic.notification.NotificationChannel;
import com.clinic.notification.NotificationChannelFactory;

import java.util.Objects;

public final class PatientNotifier implements AppointmentObserver {

    private final NotificationChannelFactory channelFactory;

    public PatientNotifier(NotificationChannelFactory channelFactory) {
        this.channelFactory = Objects.requireNonNull(channelFactory, "Фабрика каналов обязательна");
    }

    @Override
    public void onStatusChanged(Appointment appointment, AppointmentStatus previousStatus) {
        String message = buildMessage(appointment, previousStatus);
        NotificationChannel channel = channelFactory.create(
                appointment.getPatient().getContactInfo().getPreference());
        channel.send(appointment.getPatient(), message);
    }

    private String buildMessage(Appointment appointment, AppointmentStatus previousStatus) {
        String doctorName = appointment.getDoctor().getFullName();
        return switch (appointment.getStatus()) {
            case SCHEDULED -> "Вы записаны на приём к врачу %s на %s"
                    .formatted(doctorName, appointment.getTimeSlot());
            case COMPLETED -> "Приём у врача %s завершён. Диагноз: %s"
                    .formatted(doctorName, appointment.getDiagnosis());
            case CANCELLED -> "Приём у врача %s на %s отменён"
                    .formatted(doctorName, appointment.getTimeSlot());
        };
    }
}
