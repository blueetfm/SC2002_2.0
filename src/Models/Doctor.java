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
import Services.MedicalRecordInterface;
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
        Scanner scanner = new Scanner(System.in);
        String patientID;
        
        // Get list of patients first
        System.out.println("\nCurrent Patients:");
        System.out.println("----------------------------------------");
        List<Patient> patients = PatientInterface.getAllPatients();
        for (Patient p : patients) {
            System.out.printf("Patient ID: %s, Name: %s\n", 
                p.getPatientID(), 
                p.getName());
        }
        System.out.println("----------------------------------------");

        System.out.print("\nEnter Patient ID (or 'back' to return): ");
        patientID = scanner.nextLine().trim();
        
        if (patientID.equalsIgnoreCase("back")) {
            return 0;
        }

        Patient patient = PatientInterface.getPatient(patientID);
        if (patient == null) {
            System.out.println("Patient not found.");
            return 0;
        }

        // Show current medical record if exists
        System.out.println("\nCurrent Medical Record:");
        MedicalRecordInterface.readMedicalRecordsByPatientID(patientID);
        System.out.println();

        // Get diagnosis
        System.out.println("Enter Diagnosis: ");
        String diagnosis;
        while (true) {
            diagnosis = scanner.nextLine().trim();
            if (!diagnosis.isEmpty()) {
                String[] words = diagnosis.split("\\s+");
                StringBuilder capitalizedDiagnosis = new StringBuilder();
                for (String word : words) {
                    if (!word.isEmpty()) {
                        capitalizedDiagnosis.append(word.substring(0, 1).toUpperCase())
                            .append(word.substring(1).toLowerCase())
                            .append(" ");
                    }
                }
                diagnosis = capitalizedDiagnosis.toString().trim();
                break;
            } else {
                System.out.println("Diagnosis cannot be empty. Please enter a valid diagnosis: ");
            }
        }

        // Get medication
        System.out.println("Enter Medication: ");
        String medication;
        while (true) {
            medication = scanner.nextLine().trim();
            if (!medication.isEmpty()) {
                String[] words = medication.split("\\s+");
                StringBuilder capitalizedMedication = new StringBuilder();
                for (String word : words) {
                    if (!word.isEmpty()) {
                        capitalizedMedication.append(word.substring(0, 1).toUpperCase())
                            .append(word.substring(1).toLowerCase())
                            .append(" ");
                    }
                }
                medication = capitalizedMedication.toString().trim();
                break;
            } else {
                System.out.println("Medication cannot be empty. Please enter a valid medication: ");
            }
        }

        // Get treatment
        System.out.println("Enter Treatment: ");
        String treatment;
        while (true) {
            treatment = scanner.nextLine().trim();
            if (!treatment.isEmpty()) {
                String[] words = treatment.split("\\s+");
                StringBuilder capitalizedTreatment = new StringBuilder();
                for (String word : words) {
                    if (!word.isEmpty()) {
                        capitalizedTreatment.append(word.substring(0, 1).toUpperCase())
                            .append(word.substring(1).toLowerCase())
                            .append(" ");
                    }
                }
                treatment = capitalizedTreatment.toString().trim();
                break;
            } else {
                System.out.println("Treatment cannot be empty. Please enter a valid treatment: ");
            }
        }

        // Update medical record
        MedicalRecord updatedRecord = MedicalRecordManager.updateMedicalRecord(patientID, diagnosis, medication, treatment);
        if (updatedRecord != null) {
            System.out.println("\nUpdated Medical Record:");
            MedicalRecordInterface.readMedicalRecordsByPatientID(patientID);
            return 1;
        } else {
            System.out.println("Failed to update medical record.");
            return 0;
        }
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

    
    /** 
     * @return List<Appointment>
     */
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
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Get Patient ID
            System.out.print("Enter Patient ID (format: P1XXX): ");
            String patientID = scanner.nextLine().trim();
            if (!patientID.matches("P\\d{4}")) {
                System.out.println("Invalid Patient ID format. Must be P followed by 4 digits.");
                return 0;
            }
    
            // Get Name
            String name;
            do {
                System.out.print("Enter Name of Patient: ");
                name = scanner.nextLine().trim();
                if (name.isEmpty()) {
                    System.out.println("Name cannot be empty. Please try again.");
                }
            } while (name.isEmpty());
    
            // Get Date of Birth
            LocalDate date = null;
            while (date == null) {
                System.out.print("Enter date of birth (YYYY-MM-DD): ");
                String dateInput = scanner.nextLine().trim();
                try {
                    date = LocalDate.parse(dateInput, DateTimeFormatUtils.DATE_FORMATTER);
                    if (date.isAfter(LocalDate.now())) {
                        System.out.println("Date of birth cannot be in the future.");
                        date = null;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                }
            }
    
            // Get Gender
            String gender = null;
            while (gender == null) {
                System.out.print("Enter Gender (male/female): ");
                String genderInput = scanner.nextLine().trim().toLowerCase();
                if (genderInput.equals("male") || genderInput.equals("female")) {
                    gender = genderInput;
                } else {
                    System.out.println("Invalid input. Please enter 'male' or 'female'.");
                }
            }
    
            // Get Blood Type
            String[] validBloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
            String bloodType = null;
            while (bloodType == null) {
                System.out.print("Enter blood type (A+/A-/B+/B-/AB+/AB-/O+/O-): ");
                String bloodInput = scanner.nextLine().trim().toUpperCase();
                for (String valid : validBloodTypes) {
                    if (bloodInput.equals(valid)) {
                        bloodType = bloodInput;
                        break;
                    }
                }
                if (bloodType == null) {
                    System.out.println("Invalid blood type. Please enter a valid blood type.");
                }
            }
    
            // Get Contact Number
            String contactNumber;
            do {
                System.out.print("Enter contact number (+65 XXXX XXXX): ");
                contactNumber = scanner.nextLine().trim();
                if (!contactNumber.matches("\\+65 [89]\\d{3} \\d{4}")) {
                    System.out.println("Invalid contact number format. Please use format: +65 XXXX XXXX");
                }
            } while (!contactNumber.matches("\\+65 [89]\\d{3} \\d{4}"));
    
            // Create patient with empty email (can be updated later)
            int result = PatientInterface.createPatient(
                patientID, 
                "password123", // default password
                "patient", 
                name, 
                date, 
                gender, 
                contactNumber,
                "", // empty email
                bloodType
            );
    
            // if (result == 1) {
            //     System.out.println("\nPatient registered successfully!");
            //     System.out.println("Default password set to: password123");
            //     System.out.println("Please inform the patient to change their password upon first login.");
            // }
    
            return result;
    
        } catch (Exception e) {
            System.err.println("Error registering patient: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Discharges a patient from the hospital system by deleting their record.
     *
     * @return an integer indicating success (0) or failure (1)
     */
    public int dischargePatient() {
        try {
            Scanner scanner = new Scanner(System.in);
            
            // Get fresh list of patients from CSV
            List<Patient> patients = PatientInterface.getAllPatients();
            
            if (patients.isEmpty()) {
                System.out.println("\nNo patients found in the system.");
                return 0;
            }
    
            // Show all patients
            System.out.println("\nCurrent Patients:");
            System.out.println("----------------------------------------");
            for (Patient patient : patients) {
                System.out.printf("Patient ID: %s\n" +
                                "Name: %s\n" +
                                "Date of Birth: %s\n" +
                                "Gender: %s\n" +
                                "Blood Type: %s\n" +
                                "Contact: %s\n",
                    patient.getPatientID(),
                    patient.getName(),
                    patient.getDateOfBirth(),
                    patient.getGender(),
                    patient.getBloodType(),
                    patient.getPhoneNum());
                System.out.println("----------------------------------------");
            }
    
            System.out.print("\nEnter Patient ID to discharge (or 'back' to return): ");
            String patientID = scanner.nextLine().trim();
            
            if (patientID.equalsIgnoreCase("back")) {
                return 0;
            }
    
            // Validate patient exists
            Patient patientToDischarge = patients.stream()
                .filter(p -> p.getPatientID().equals(patientID))
                .findFirst()
                .orElse(null);
                
            if (patientToDischarge == null) {
                System.out.println("Patient ID not found.");
                return 0;
            }
    
            // Confirm discharge
            System.out.printf("Are you sure you want to discharge patient:\n" +
                             "Name: %s\n" +
                             "ID: %s\n" +
                             "(Y/N): ", 
                patientToDischarge.getName(), 
                patientID);
                
            String confirm = scanner.nextLine().trim().toUpperCase();
            
            if (!confirm.equals("Y")) {
                System.out.println("Discharge cancelled.");
                return 0;
            }
    
            int result = PatientInterface.deletePatient(patientID);
            if (result == 1) {
                System.out.println("Patient successfully discharged.");
                return 1;
            } else {
                System.out.println("Failed to discharge patient.");
                return 0;
            }
            
        } catch (Exception e) {
            System.err.println("Error during discharge process: " + e.getMessage());
            return 0;
        }
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
