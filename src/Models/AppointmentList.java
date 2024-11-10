package Models;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import Views.UserMenu;

public class AppointmentList {
    private static AppointmentList instance;
    protected ArrayList<Appointment> appointmentList;
    private String csvFilePath;
    private static final String CSV_HEADER = "Appointment ID,Patient ID,Doctor ID,Date,Time Slot";

    private AppointmentList(String filePath) {
        this.csvFilePath = filePath;
        this.appointmentList = new ArrayList<>();
        initializeFile();
        loadAppointmentsFromCSV();
    }

    public static synchronized AppointmentList getInstance(String filePath) {
        if (instance == null) {
            instance = new AppointmentList(filePath);
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

    private void loadAppointmentsFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    Appointment appointment = new Appointment(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        LocalDate.parse(parts[3].trim()),
                        LocalTime.parse(parts[4].trim())
                    );
                    appointmentList.add(appointment);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading appointments from CSV: " + e.getMessage());
        }
    }

    private void saveAppointmentsToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (Appointment appointment : appointmentList) {
                writer.write(String.format("%s,%s,%s,%s,%s",
                    appointment.getAppointmentID(),
                    appointment.getPatientID(),
                    appointment.getDoctorID(),
                    appointment.getDate().toString(),
                    appointment.getTimeSlot().toString()
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving appointments to CSV: " + e.getMessage());
        }
    }

        
    public Appointment getAppointmentById(String appointmentID) {
        for (Appointment appointment : appointmentList) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                return appointment;
            }
        }
        
        return null;
    }


    // CRUD 
    public void createAppointment(String appointmentID, String patientID, String doctorID, LocalDate date, LocalTime timeSlot) {
        for (Appointment appointment : appointmentList) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                System.out.println("Appointment ID already exists.");
                return;
            }
        }
        Appointment newAppointment = new Appointment(appointmentID, patientID, doctorID, date, timeSlot);
        appointmentList.add(newAppointment);
        saveAppointmentsToCSV();
        System.out.println("Appointment successfully created!");
    }

    public void readAppointment(String appointmentID) {
        String loggedInID = UserMenu.getLoggedInHospitalID();
        boolean isPatient = loggedInID.startsWith("P") && (loggedInID.length() == 5);

        for (Appointment appointment : appointmentList) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                if (!isPatient || appointment.getPatientID().equals(loggedInID)) {
                    appointment.printDetails();
                    return;
                } else {
                    System.out.println("Access Denied. You can only view your own appointment records.");
                    return;
                }
            }
        }
        System.out.println("No matching appointment ID.");
    }

    public void updateAppointment(String appointmentID) {
        
        saveAppointmentsToCSV();
    }

    public void deleteAppointment(String appointmentID) {
        appointmentList.removeIf(appointment -> appointment.getAppointmentID().equals(appointmentID));
        saveAppointmentsToCSV();
        System.out.println("Appointment deleted successfully.");
    }
}
