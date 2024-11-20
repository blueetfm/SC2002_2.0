package Models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private List<Appointment> appointments;

    private static final AtomicInteger counter = new AtomicInteger(0);

    private AppointmentManager() {
        this.appointments = initializeObjects();
    }

    public static synchronized AppointmentManager getInstance() {
        if (instance == null) {
            instance = new AppointmentManager();
        }
        return instance;
    }

    // Function to initialize objects from CSVHandler's readCSVLines
    public List<Appointment> initializeObjects() {
        List<Appointment> appointments = new ArrayList<>();
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
                Enums.Status status = Enums.Status.valueOf(record.get(5).toUpperCase());
    
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
            }
        }
        return appointments;
    }

    // Function to transform objects back into CSV lines
    public List<List<String>> initializeCSVLines(List<Appointment> appointments){
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

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public String generateAppointmentID(){
        int uniqueNumber = counter.incrementAndGet();
        String uniquePart = String.format("%03d", uniqueNumber);

        return "APT-" + "-" + uniquePart;
    }


    /*Patient Menu Stuff*/
    // View available appointments
        // public List<TimeSlot> viewAvailableAppointmentSlots(LocalDate date, String doctorID) {
        //     // return TimeSlotList.getTimeSlotList().getAvailableSlots(date, doctorID);
        // }

    // Schedule an appointment
        // public boolean scheduleAppointment(String patientID, String doctorID, LocalDate date, LocalTime timeSlot) {
        // }
    
    // Reschedule an appointment
        // public boolean rescheduleAppointment(String appointmentID, LocalDate newDate, LocalTime newTimeSlot) {
        // }
    
    // Cancel appointment
        // public boolean cancelAppointment(String appointmentID) {
        // }
    
    // View scheduled appointment
    public List<Appointment> viewScheduledAppointments(String patientID) {
        return AppointmentQuery.getAppointmentsByPatientID(appointments, patientID);
    }


    /*Doctor Menu Stuff*/
    public List<Appointment> viewUpcomingAppointments(String doctorID){
        return AppointmentQuery.getAppointmentsByDoctorID(appointments, doctorID);
    }

    public boolean recordAppointmentOutcomeRecord(String appointmentID, String patientID, LocalDate outcomeDate, Service service, String medication, PrescriptionStatus prescriptionStatus, String notes){
        Appointment appointment = AppointmentQuery.getAppointmentById(appointments, appointmentID);

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



    /*Pharmacist Menu Stuff*/
    public List<AppointmentOutcomeRecord> viewAppointmentOutcomeRecords(){
        List<AppointmentOutcomeRecord> appointmentOutcomeRecords = new ArrayList<>();

        for (Appointment appointment : appointments){
            appointmentOutcomeRecords.add(appointment.outcomeRecord);
        }

        return appointmentOutcomeRecords;
    }

    public AppointmentOutcomeRecord viewAppointmentOutcomeRecord(String appointmentID){
        Appointment appointment = AppointmentQuery.getAppointmentById(appointments, appointmentID);

        return appointment.outcomeRecord;
    }

    /*Administrator Menu Stuff*/
    public List<Appointment> viewAppointments(){
        return appointments;
    }

    
}
