package Models;

import java.util.*;
import java.util.stream.Collectors;

public class AppointmentQuery {

    public static Appointment getAppointmentById(List<Appointment> appointments, String appointmentID) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                return appointment;
            }
        }
        return null;
    }

    public static List<Appointment> getAppointmentsByPatientID(List<Appointment> appointments, String patientID) {
        return appointments.stream()
                .filter(appointment -> appointment.getPatientID().equals(patientID))
                .collect(Collectors.toList());
    }

    public static List<Appointment> getAppointmentsByDoctorID(List<Appointment> appointments, String doctorID) {
        return appointments.stream()
                .filter(appointment -> appointment.getDoctorID().equals(doctorID))
                .collect(Collectors.toList());
    }

    /* Appointment Outcome Records */
    public static AppointmentOutcomeRecord getAppointmentOutcomeRecordByID(List<Appointment> appointments, String appointmentID){
        Appointment appointment = AppointmentQuery.getAppointmentById(appointments, appointmentID);

        return appointment.outcomeRecord;
    }

    public static List<AppointmentOutcomeRecord> getAppointmentOutcomeRecordByPatientID(List<Appointment> appointments, String patientID){
        return appointments.stream()
                .filter(appointment -> appointment.getPatientID().equals(patientID)) // Filter by patientID
                .map(Appointment::getOutcomeRecord) // Extract the outcomeRecord
                .collect(Collectors.toList()); // Collect as a list
    }
}

