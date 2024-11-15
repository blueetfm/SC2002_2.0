package Models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import Enums.*;
import Utils.CSVHandler;
import Utils.DateTimeFormatUtils;

public class AppointmentManager {
    private static AppointmentManager instance;
    private AppointmentList appointmentList;


    private AppointmentManager(String appointmentFilePath, String outcomeFilePath) {
        this.appointmentList = AppointmentList.getInstance(appointmentFilePath);
        // this.outcomeRecordList = AppointmentOutcomeRecordList.getInstance(outcomeFilePath);
    }

    public static synchronized AppointmentManager getInstance(String appointmentFilePath, String outcomeFilePath) {
        if (instance == null) {
            instance = new AppointmentManager(appointmentFilePath, outcomeFilePath);
        }
        return instance;
    }

    // Function to initialize objcets from CSVHandler's readCSVLines
    public List<Appointment> initializeObjects(){
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


    // CRUD
    public void createAppointment(String appointmentID, String patientID, String doctorID, LocalDate date, LocalTime timeSlot) {
        appointmentList.createAppointment(appointmentID, patientID, doctorID, date, timeSlot);
    }

    public void viewAppointment(String appointmentID) {
        appointmentList.readAppointment(appointmentID);
    }

    public void updateAppointment(String appointmentID) {
        appointmentList.updateAppointment(appointmentID);
    }

    public void deleteAppointment(String appointmentID) {
        appointmentList.deleteAppointment(appointmentID);
    }

    public void createAppointmentOutcome(String appointmentID, String hospitalID, LocalDate date, Service service, String medication, PrescriptionStatus prescriptionStatus, String notes) {
        Appointment appointment = appointmentList.getAppointmentById(appointmentID);
        if (appointment != null && appointment.getStatus() == Status.COMPLETED) {
            // outcomeRecordList.createAppointmentOutcomeRecord(appointmentID, hospitalID, date, service, medication, prescriptionStatus, notes);
        } else {
            System.out.println("Appointment not found or not completed. Cannot create outcome record.");
        }
    }

    public void viewAppointmentOutcome(String appointmentID) {
        // outcomeRecordList.readAppointmentOutcomeRecord(appointmentID);
    }

    public void updateAppointmentOutcome(String appointmentID) {
        // outcomeRecordList.updateAppointmentOutcomeRecord(appointmentID);
    }

    public void deleteAppointmentOutcome(String appointmentID) {
        // outcomeRecordList.deleteAppointmentOutcomeRecord(appointmentID);
    }
}
