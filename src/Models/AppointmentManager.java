package Models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.swing.text.View;

import Enums.*;
import Utils.CSVHandler;
import Utils.DateTimeFormatUtils;

public class AppointmentManager {
    private static AppointmentManager instance;
    private static List<Appointment> appointments;
    private static List<AppointmentOutcomeRecord> appointmentOutcomeRecords;

    private static final AtomicInteger counter = new AtomicInteger(0);

    private AppointmentManager() {
        if (appointments == null || appointments.isEmpty()) {
            initializeObjects();
        }
    }

    public static synchronized AppointmentManager getInstance() {
        if (instance == null) {
            instance = new AppointmentManager();
            if (appointments == null) {
                appointments = initializeObjects();
            }
        }
        return instance;
    }

    // Function to initialize objects from CSVHandler's readCSVLines
    public static List<Appointment> initializeObjects() {
        appointments = new ArrayList<Appointment>();
        appointmentOutcomeRecords = new ArrayList<AppointmentOutcomeRecord>();

        List<List<String>> records = CSVHandler.readCSVLines("data/Appointment_List.csv");

        if (!records.isEmpty()) {
            // skip headers
            for (int i = 1; i < records.size(); i++) {
                List<String> record = records.get(i);
                
                // parsing appointment
                String appointmentID = record.get(0);
                String patientID = record.get(1);
                String doctorID = record.get(2);
                LocalDate date = LocalDate.parse(record.get(3), DateTimeFormatUtils.DATE_FORMATTER);
                LocalTime timeSlot = LocalTime.parse(record.get(4), DateTimeFormatUtils.TIME_FORMATTER);
                Enums.AppointmentStatus status = Enums.AppointmentStatus.valueOf(record.get(5).toUpperCase());
    
                // parsing appointment outcome record
                LocalDate outcomeDate = LocalDate.parse(record.get(6), DateTimeFormatUtils.DATE_FORMATTER); 
                Enums.Service service = Enums.Service.valueOf(record.get(7).toUpperCase()); 
                String medication = record.get(8);
                Enums.PrescriptionStatus prescriptionStatus = Enums.PrescriptionStatus.valueOf(record.get(9).toUpperCase());
                String notes = record.get(10);

                AppointmentOutcomeRecord outcomeRecord = new AppointmentOutcomeRecord(
                    appointmentID, patientID, outcomeDate, service, medication, prescriptionStatus, notes
                );

                Appointment appointment = new Appointment(
                    appointmentID, patientID, doctorID, date, timeSlot, status, outcomeRecord
                );

                appointments.add(appointment);
                appointmentOutcomeRecords.add(outcomeRecord);
                System.out.println("Initialized!");
            }
        }
        return appointments;
    }

    // Function to transform objects back into CSV lines
    public static List<List<String>> initializeCSVLines(List<Appointment> appointments){
        List<List<String>> records = new ArrayList<>();

        records.add(Arrays.asList("Appointment ID","Patient ID","Doctor ID","Date","Time Slot","Status","Outcome Date","Service","Medication","Prescription Status","Notes"));

        for (Appointment appointment : appointments){
            if (appointment != null){
                records.add(Arrays.asList(
                    appointment.getAppointmentID(), appointment.getPatientID(), appointment.getDoctorID(),
                    appointment.getDate().format(DateTimeFormatUtils.DATE_FORMATTER),
                    appointment.getTimeSlot().format(DateTimeFormatUtils.TIME_FORMATTER),
                    appointment.getStatus().name(),
                    appointment.getOutcomeRecord().getDate().format(DateTimeFormatUtils.DATE_FORMATTER),
                    appointment.getOutcomeRecord().getService().name(),
                    appointment.getOutcomeRecord().getMedication(),
                    appointment.getOutcomeRecord().getPrescriptionStatus().name(),
                    appointment.getOutcomeRecord().getNotes()
                ));

            }
        }
        return records;
    }

    public String generateAppointmentID(){
        int uniqueNumber = counter.incrementAndGet();
        String uniquePart = String.format("%03d", uniqueNumber);

        return "APT-" + "-" + uniquePart;
    }

    public static List<Appointment> getAppointments() {
        return appointments;
    }

    public static List<AppointmentOutcomeRecord> getAppointmentOutcomeRecords(){
        return appointmentOutcomeRecords;
    }

    /* Appointment Queries */
    public static Appointment getAppointmentByID(String appointmentID) {
        String upperCaseAppointmentID = appointmentID.toUpperCase();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(upperCaseAppointmentID)) {
                return appointment;
            }
        }
        return null;
    }

    public static List<Appointment> getAppointmentsByPatientID(String patientID) {
        String upperCasePatientID = patientID.toUpperCase();
        return appointments.stream()
                .filter(appointment -> appointment.getPatientID().equals(upperCasePatientID))
                .collect(Collectors.toList());
    }

    public static List<Appointment> getAppointmentsByDoctorID(String doctorID) {
        String upperCaseDoctorID = doctorID.toUpperCase();
        return appointments.stream()
                .filter(appointment -> appointment.getDoctorID().equals(upperCaseDoctorID))
                .collect(Collectors.toList());
    }

    /* Appointment Outcome Records */
    public static AppointmentOutcomeRecord getAppointmentOutcomeRecordByID(String appointmentID){
        String upperCaseAppointmentID = appointmentID.toUpperCase();
        Appointment appointment = getAppointmentByID(upperCaseAppointmentID);

        return appointment.outcomeRecord;
    }

    public static List<AppointmentOutcomeRecord> getAppointmentOutcomeRecordsByPatientID(String patientID){
        String upperCasePatientID = patientID.toUpperCase();
        return appointments.stream()
                .filter(appointment -> appointment.getPatientID().equals(upperCasePatientID)) 
                .map(Appointment::getOutcomeRecord) 
                .collect(Collectors.toList()); 
    }


    /*Patient Menu Stuff*/
    // Schedule appointment
    public static boolean scheduleAppointment(){
        List<Appointment> appointments = getAppointments();
        // List<TimeSlot> timeslots = TimeSlotList.getTimeSlotList();
        // Appointment appointment = 

        return false;
    }

    // Cancel appointment
    public static boolean cancelAppointment(String appointmentID){
        List<Appointment> appointments = getAppointments();
        Iterator<Appointment> iterator = appointments.iterator();

        while (iterator.hasNext()) {
            Appointment appointment = iterator.next();
            if (appointment.getAppointmentID().equals(appointmentID)) {
                appointment.setStatus(AppointmentStatus.CANCELED);
                iterator.remove(); 
                return true; 
            }
        }    
        return false; 
    }
    

    public static boolean recordAppointmentOutcomeRecord(String appointmentID, String patientID, LocalDate outcomeDate, Service service, String medication, PrescriptionStatus prescriptionStatus, String notes){
        Appointment appointment = getAppointmentByID(appointmentID);

        if (appointment.outcomeRecord == null){
            AppointmentOutcomeRecord outcomeRecord = new AppointmentOutcomeRecord(
                    appointmentID, patientID, outcomeDate, service, medication, prescriptionStatus, notes
                );
            appointment.outcomeRecord = outcomeRecord;

            List<List<String>> updatedCSVLines = initializeCSVLines(appointments);
            String[] headers = updatedCSVLines.get(0).toArray(new String[0]);
            String[] lines = updatedCSVLines.stream()
                .skip(1) 
                .map(row -> String.join(",", row)) 
                .toArray(String[]::new);

            CSVHandler.writeCSVLines(headers, lines, "data/Appointment_List.csv");
            return true;

        } else {
            return false;
        }
    }    
}
