/**
 * The Doctor class represents a doctor in a hospital system.
 * It extends the User class and provides functionality for managing 
 * patient records, appointments, and doctor availability.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */

package Models;

import Enums.AppointmentStatus;
import Enums.PrescriptionStatus;
import Services.AppointmentInterface;
import Services.PatientInterface;
import Services.TimeSlotInterface;
import Utils.DateTimeFormatUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Doctor extends User {
    /**
     * The name of the doctor.
     */
    protected String name;

    /**
     * The gender of the doctor.
     */
    protected String gender;

    /**
     * Constructs a new Doctor object with the specified details.
     *
     * @param hospitalID the unique identifier for the hospital system
     * @param password the password for the doctor
     * @param role the role of the user (doctor)
     * @param name the name of the doctor
     * @param gender the gender of the doctor
     */
    public Doctor(String hospitalID, String password, String role, String name, String gender) {
        super(hospitalID, password, role);
        this.name = name;
        this.gender = gender;
    }

    /**
     * Displays all patients' medical records.
     *
     * @return an integer indicating success (0) or failure (1)
     */
    public int viewPatientMedicalRecords() {
        List<Patient> patientList = PatientInterface.getAllPatients();
        System.out.println("\n=====================================");
        for (Patient patient : patientList) {
            String ID = patient.getHospitalID();
            PatientInterface.readPatient(ID);
        }
        System.out.println("=====================================");
        return 1;
    }

    /**
     * Updates a patient's medical records based on input.
     *
     * @return an integer indicating success (0) or failure (1)
     */
    public int updatePatientMedicalRecords() {
        Scanner case2Scanner = new Scanner(System.in);
        String case2Choice;
        case2Choice = case2Scanner.nextLine();
        PatientInterface.getPatient(case2Choice);
        case2Scanner.close();
        return 1;
    }

    /**
     * Displays the doctor's personal schedule.
     *
     * @return an integer indicating success (0) or failure (1)
     */
    public List<TimeSlot> getPersonalSchedule() {
        TimeSlotInterface.initializeObjects();
        return TimeSlotInterface.getTimeSlotsByDoctorID(this.getHospitalID());
    }

    /**
     * Sets the doctor's availability for appointments.
     *
     * @return an integer indicating success (0) or failure (1)
     */
    public void createTimeSlot(LocalDateTime start, LocalDateTime end) {
        TimeSlotInterface.createTimeSlot(this.getHospitalID(), start, end);
    }

    /**
     * Accepts or declines appointment requests for the doctor based on time slot availability.
     *
     * @return an integer indicating success (0) or failure (1)
     */
    public int acceptOrDeclineAppointmentRequests() {
        List<Appointment> pendingAppointments = getPendingAppointments();
        if (pendingAppointments.isEmpty()) {
            System.out.println("No pending appointment requests.");
            return 0;
        }

        Scanner scanner = new Scanner(System.in);
        for (Appointment apt : pendingAppointments) {
            TimeSlot slot = TimeSlotInterface.getTimeSlotByID(apt.getTimeSlotID());
            System.out.printf("Pending Request:\n" +
                            "Appointment ID: %s\n" +
                            "Patient ID: %s\n" +
                            "Date: %s\n" +
                            "Time: %s - %s\n",
                apt.getAppointmentID(),
                apt.getPatientID(),
                apt.getDate(),
                slot.getStartTime().toLocalTime(),
                slot.getEndTime().toLocalTime());
            
            System.out.print("Accept? (Y/N): ");
            String response = scanner.nextLine().trim().toUpperCase();
            
            if (response.equals("Y")) {
                updateAppointmentStatus(apt.getAppointmentID(), AppointmentStatus.CONFIRMED);
                System.out.println("Appointment confirmed.");
            } else if (response.equals("N")) {
                updateAppointmentStatus(apt.getAppointmentID(), AppointmentStatus.CANCELLED);
                System.out.println("Appointment rejected.");
            } else {
                System.out.println("Invalid input. No action taken.");
            }
        }
        return 1;
    }

    public List<Appointment> getPendingAppointments() {
        AppointmentInterface.initializeObjects();
        return AppointmentInterface.getAppointmentsByDoctorID(this.getHospitalID())
            .stream()
            .filter(apt -> apt.getStatus() == AppointmentStatus.PENDING)
            .collect(Collectors.toList());
    }
    public List<Appointment> getUpcomingAppointments() {
        AppointmentInterface.initializeObjects();
        return AppointmentInterface.getAppointmentsByDoctorID(this.getHospitalID())
            .stream()
            .filter(apt -> apt.getStatus() == AppointmentStatus.CONFIRMED)
            .collect(Collectors.toList());
    }

    public void updateAppointmentStatus(String appointmentID, AppointmentStatus status) {
        AppointmentInterface.updateAppointmentStatus(appointmentID, status);
    }

    /**
     * Displays the upcoming appointments for the doctor.
     *
     * @return an integer indicating success (0) or failure (1)
     */
    public int viewUpcomingAppointments() {
        List<Appointment> appointments = getUpcomingAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No upcoming appointments found.");
            return 0;
        }
        
        for (Appointment apt : appointments) {
            TimeSlot slot = TimeSlotInterface.getTimeSlotByID(apt.getTimeSlotID());
            System.out.printf("Appointment ID: %s\n" +
                            "Patient ID: %s\n" +
                            "Date: %s\n" +
                            "Time: %s - %s\n",
                apt.getAppointmentID(),
                apt.getPatientID(),
                apt.getDate(),
                slot.getStartTime().toLocalTime(),
                slot.getEndTime().toLocalTime());
        }
        return 1;
    }


    /**
     * Records the outcome of a specific appointment, including services provided and medications prescribed.
     *
     * @return an integer indicating success (0) or failure (1)
     */
    public int recordAppointmentOutcome() {
        Scanner scanner = new Scanner(System.in);
        
        List<Appointment> confirmedAppointments = getUpcomingAppointments();
        if (confirmedAppointments.isEmpty()) {
            System.out.println("No confirmed appointments available.");
            return 0;
        }
    
        System.out.println("\nConfirmed Appointments:");
        for (Appointment apt : confirmedAppointments) {
            System.out.printf("ID: %s, Patient: %s, Date: %s, Service: %s\n",
                apt.getAppointmentID(),
                apt.getPatientID(),
                apt.getDate(),
                apt.getService());
        }
    
        System.out.print("\nEnter Appointment ID to record outcome: ");
        String appointmentID = scanner.nextLine().trim();
    
        Appointment selectedApt = confirmedAppointments.stream()
            .filter(apt -> apt.getAppointmentID().equals(appointmentID))
            .findFirst()
            .orElse(null);
    
        if (selectedApt == null) {
            System.out.println("Invalid appointment ID.");
            return 0;
        }
    
        System.out.print("Enter medication prescribed (or 'NONE'): ");
        String medication = scanner.nextLine().trim();
        if (medication.isEmpty()) medication = "NONE";
    
        System.out.print("Enter notes for appointment (or 'NIL'): ");
        String notes = scanner.nextLine().trim();
        if (notes.isEmpty()) notes = "NIL";
    
        // Create the AOR
        int result = AppointmentOutcomeRecordManager.createAppointmentOutcomeRecord(
            appointmentID,
            selectedApt.getDate(),
            selectedApt.getService(),
            medication,
            PrescriptionStatus.PENDING,
            notes
        );
    
        if (result == 1) {
            AppointmentInterface.updateAppointmentStatus(appointmentID, AppointmentStatus.COMPLETED);
            System.out.println("Appointment outcome recorded successfully.");
            return 1;
        } else {
            System.out.println("Failed to record appointment outcome.");
            return 0;
        }
    }

    /**
     * Registers a new patient in the system.
     *
     * @return an integer indicating success (0) or failure (1)
     */
    public int registerPatient() {
        Scanner case8Scanner = new Scanner(System.in);
        System.out.print("Enter Patient ID to record: ");
        String patientID = case8Scanner.next();
        System.out.print("Enter Name of Patient: ");
        String name = case8Scanner.next();
         LocalDate date = null;
        while (date == null) {
            System.out.print("Enter date of birth 'YYYY-MM-DD': ");
            String dateInput = case8Scanner.next();
            try {
                date = LocalDate.parse(dateInput, DateTimeFormatUtils.DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in 'YYYY-MM-DD' format.");
            }
        }
        String gender = null;
        while (gender == null) {
            System.out.print("Enter Gender of Patient: ");
            String genderInput = case8Scanner.next().toLowerCase(); // Normalize input to lowercase
            if (genderInput.equals("male") || genderInput.equals("female")) {
                gender = genderInput; // Assign valid gender
            } else {
                System.out.println("Invalid input. Please enter 'male' or 'female'.");
            }
        }
        System.out.print("Enter patient blood type: ");
        String bloodType = case8Scanner.next();
        System.out.print("Enter contact number: ");
        String contactNumber = case8Scanner.next();
        case8Scanner.close();
        return PatientInterface.createPatient(patientID, "", "patient", name, date, gender, contactNumber, "", bloodType);
    }

    /**
     * Discharges a patient from the hospital system by deleting their record.
     *
     * @return an integer indicating success (0) or failure (1)
     */
    public int dischargePatient() {
        Scanner case9Scanner = new Scanner(System.in);
        System.out.print("Enter Patient ID to delete: ");
        String case9Choice = case9Scanner.next();
        case9Scanner.close();
        return PatientInterface.deletePatient(case9Choice);
    }

    /**
     * Logs out the doctor from the system.
     *
     * @return true if logout is successful, false otherwise
     */
    public boolean logout() {
        try {
            System.out.println("Logging out doctor: " + this.name);
            return true;
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            return false;
        }
    }
}
