/**
 * The Doctor class represents a doctor in a hospital system.
 * It extends the User class and provides functionality for managing 
 * patient records, appointments, and doctor availability.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */

package Models;

import Enums.PrescriptionStatus;
import Enums.ScheduleStatus;
import Services.AppointmentInterface;
import Services.PatientInterface;
import Services.TimeSlotInterface;
import Utils.DateTimeFormatUtils;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;


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
    public int viewPersonalSchedule() {
        Scanner case3Scanner = new Scanner(System.in);
        String case3Choice;
        case3Choice = case3Scanner.nextLine();
        List<TimeSlot> timeSlotList = TimeSlotInterface.getTimeSlotsByDoctorID(case3Choice);
        for (TimeSlot slot : timeSlotList) {
            TimeSlotInterface.printTimeSlot(slot);
        }
        case3Scanner.close();
        return 1;
    }

    /**
     * Sets the doctor's availability for appointments.
     *
     * @return an integer indicating success (0) or failure (1)
     */
    public int setAppointmentAvailability() {
        Scanner case4Scanner = new Scanner(System.in);
        return 0;
    }

    /**
     * Accepts or declines appointment requests for the doctor based on time slot availability.
     *
     * @return an integer indicating success (0) or failure (1)
     */
    public int acceptOrDeclineAppointmentRequests() {
        Scanner case5Scanner = new Scanner(System.in);
        String case5Choice;
        case5Choice = case5Scanner.nextLine();
        List<TimeSlot> timeSlotList = TimeSlotInterface.getTimeSlotsByDoctorID(case5Choice);
        for (TimeSlot i : timeSlotList) {
            if (i.getScheduleStatus().equals(ScheduleStatus.PENDING)) {
                System.out.println("Accept? (Y/N): ");
                String acceptChar = case5Scanner.nextLine();
                if (acceptChar.equals("Y")) {
                    i.setScheduleStatus(ScheduleStatus.RESERVED);
                } else if (acceptChar.equals("N")) {
                    i.setScheduleStatus(ScheduleStatus.CANCELLED);
                } else {
                    System.out.println("Invalid Input. No action is done.");
                }
            }
        }
        case5Scanner.close();
        return 0;
    }

    /**
     * Displays the upcoming appointments for the doctor.
     *
     * @return an integer indicating success (0) or failure (1)
     */
    public int viewUpcomingAppointments() {
        Scanner case6Scanner = new Scanner(System.in);
        String case6Choice;
        case6Choice = case6Scanner.nextLine();
        List<Appointment> appointmentList = AppointmentInterface.getAppointmentsByDoctorID(case6Choice);
        for (Appointment i : appointmentList) {
            // Display appointment details (to be implemented)
        }
        case6Scanner.close();
        return 1;
    }

    /**
     * Records the outcome of a specific appointment, including services provided and medications prescribed.
     *
     * @return an integer indicating success (0) or failure (1)
     */
    public int recordAppointmentOutcome() {
        Scanner case7Scanner = new Scanner(System.in);
        System.out.print("Enter Appointment ID to record: ");
        String appointmentID = case7Scanner.next();
        System.out.print("Enter Hospital ID of Patient: ");
        String patientID = case7Scanner.next();
        LocalDate date = null;
        while (date == null) {
            System.out.print("Enter date of appointment in the form 'YYYY-MM-DD': ");
            String dateInput = case7Scanner.next();
            try {
                date = LocalDate.parse(dateInput, DateTimeFormatUtils.DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in 'YYYY-MM-DD' format.");
            }
        }
        System.out.print("Enter service provided: ");
        Enums.Service service = Enums.Service.valueOf(case7Scanner.next().toUpperCase());
        System.out.print("Enter medication prescribed: ");
        String medication = case7Scanner.next();
        System.out.print("Enter notes for appointment: ");
        String notes = case7Scanner.next();
        int recordResult = AppointmentInterface.recordAppointmentOutcomeRecord(appointmentID, patientID, date, service, medication, PrescriptionStatus.valueOf("PENDING"), notes);
        case7Scanner.close();
        return recordResult;
    }

    /**
     * Registers a new patient in the system.
     *
     * @return an integer indicating success (0) or failure (1)
     */
    public int registerPatient() {
        Scanner case8Scanner = new Scanner(System.in);
        System.out.print("Enter Patient ID to record: ");
        String patientID = case8Scanner.next().toUpperCase();
        System.out.print("Enter Name of Patient: ");
        String name = null;
        System.out.print("Enter Name of Patient: ");
        case8Scanner.nextLine(); 
        while (name == null) {
            String nameInput = case8Scanner.nextLine();
            if (nameInput.matches("^[A-Za-z]+( [A-Za-z]+)?$")) {
                name = nameInput;
            } else {
                System.out.println("Invalid name. Please enter a single name or a full name (e.g., 'Alicia' or 'Alicia Smith').");
            }
        }
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
        String bloodType = case8Scanner.next().toUpperCase();
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
        String case9Choice = case9Scanner.next().toUpperCase();
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
