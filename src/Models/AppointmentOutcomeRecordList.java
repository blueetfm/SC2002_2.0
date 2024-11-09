package Models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

import enums.*;

public class AppointmentOutcomeRecordList implements AppointmentOutcomeRecordManager {
    private static AppointmentOutcomeRecordList instance; 
    private String csvFilePath; 
    private static final String CSV_HEADER = "Appointment ID,Hospital ID,Date,Service,Medication,Prescription Status,Notes";
    
    private AppointmentOutcomeRecordList(String filePath) {
        this.csvFilePath = filePath;
        initializeFile();
    }

    public static synchronized AppointmentOutcomeRecordList getInstance(String filePath) {
        if (instance == null) {
            instance = new AppointmentOutcomeRecordList(filePath);
        }
        return instance;
    }

    private void initializeFile() {
        File file = new File(csvFilePath);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
                writer.write(CSV_HEADER);
                writer.newLine();  // Writing headers
            } catch (IOException e) {
                System.err.println("Error initializing file: " + e.getMessage());
            }
        }
    }

    // copied from MedicationInventory.java, might want to have a helper function file somewhere 
    private String[] readCSVLines(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath)).toArray(new String[0]);
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return new String[0];
        }
    }

    private void writeCSVLines(String[] lines, String filePath) {
        try {
            Files.write(Paths.get(filePath), String.join("\n", lines).getBytes());
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    // CRUD 
    public void createAppointmentOutcomeRecord(String appointmentID, String hospitalID, LocalDate date, Service service, String medication, PrescriptionStatus prescriptionStatus, String notes) {
        String[] lines = readCSVLines(csvFilePath);
        
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length > 0 && parts[0].trim().equals(appointmentID)) {
                System.out.println("Appointment ID already exists: " + appointmentID);
                return;
            }
        }
        
        String newRecord = String.format("%s,%s,%s,%s,%s,%s,%s", 
                                        appointmentID, hospitalID, date.toString(), service.toString(), 
                                        medication, prescriptionStatus.toString(), notes);

        StringBuilder newContent = new StringBuilder();
        for (String line : lines) {
            newContent.append(line).append("\n");
        }
        newContent.append(newRecord);

        writeCSVLines(newContent.toString().split("\n"), csvFilePath);
        System.out.println("Successfully added new appointment outcome record: " + appointmentID);
    }

    public void viewAppointmentOutcomeRecords() {
        System.out.println("\nAppointment Outcome Records:");
        System.out.println("---------------------------");
        String[] lines = readCSVLines(csvFilePath);
        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split(",");
            if (parts.length == 7) {
                AppointmentOutcomeRecord record = new AppointmentOutcomeRecord(
                    parts[0].trim(),
                    parts[1].trim(),
                    LocalDate.parse(parts[2].trim()),
                    Service.valueOf(parts[3].trim()),
                    parts[4].trim(),
                    PrescriptionStatus.valueOf(parts[5].trim()),
                    parts[6].trim()
                );
                System.out.println(record);
            }
        }
    }

    public void updateAppointmentOutcomeRecord(String appointmentID, LocalDate newDate, Service newService, String newMedication, PrescriptionStatus newPrescriptionStatus, String newNotes) {
        String[] lines = readCSVLines(csvFilePath);
        boolean found = false;
        StringBuilder newContent = new StringBuilder(lines[0]); // Keep header
        
        // Update record if found
        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split(",");
            if (parts[0].trim().equals(appointmentID)) {
                found = true;
                
                // Use new values if provided, otherwise keep existing ones
                String updatedRecord = String.format("%s,%s,%s,%s,%s,%s,%s", 
                    appointmentID, parts[1], (newDate != null) ? newDate.toString() : parts[2], 
                    (newService != null) ? newService.toString() : parts[3],
                    (newMedication != null) ? newMedication : parts[4], 
                    (newPrescriptionStatus != null) ? newPrescriptionStatus.toString() : parts[5],
                    (newNotes != null) ? newNotes : parts[6]
                );
                newContent.append("\n").append(updatedRecord);
            } else {
                newContent.append("\n").append(lines[i]);
            }
        }

        if (found) {
            writeCSVLines(newContent.toString().split("\n"), csvFilePath);
            System.out.println("Updated appointment outcome record: " + appointmentID);
        } else {
            System.out.println("Appointment record not found: " + appointmentID);
        }
    }

    public void deleteAppointmentOutcomeRecord(String appointmentID) {
        String[] lines = readCSVLines(csvFilePath);
        boolean found = false;
        StringBuilder newContent = new StringBuilder(lines[0]); // Keep header
        
        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split(",");
            if (!parts[0].trim().equals(appointmentID)) {
                newContent.append("\n").append(lines[i]);
            } else {
                found = true;
            }
        }

        if (found) {
            writeCSVLines(newContent.toString().split("\n"), csvFilePath);
            System.out.println("Deleted appointment outcome record: " + appointmentID);
        } else {
            System.out.println("Appointment record not found: " + appointmentID);
        }
    }
    
    @Override
    public boolean hasAppointmentOutcomeRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            // Skip header
            reader.readLine();
            
            // Check if there are any records
            return reader.readLine() != null;
        } catch (IOException e) {
            System.err.println("Error checking records: " + e.getMessage());
            return false;
        }
    }
}
