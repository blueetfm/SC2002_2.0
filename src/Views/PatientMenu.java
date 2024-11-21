package Views;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Models.Patient;
import Models.PatientManager;
import Services.PatientInterface;
import Utils.DateTimeFormatUtils;
import Models.AppointmentOutcomeRecord;
import Models.Appointment;
import Models.AppointmentManager;

public class PatientMenu implements Menu {

	private final Scanner sc;
	private Patient currentPatient;
	private boolean isRunning;
	private PatientInterface patientManager;
	private AppointmentManager appointmentManager;

	public PatientMenu() {
		this.sc = new Scanner(System.in);
		this.patientManager = PatientManager.getInstance();
		this.isRunning = true;
		initializePatient();
	}

	// Initializes a patient object with the info from the patient list CSV
	private void initializePatient() {
		try {
			String loggedInID = UserMenu.getLoggedInHospitalID();
			System.out.println("Logged In Hospital ID: " + loggedInID);  
	
			BufferedReader reader = new BufferedReader(new FileReader("data/Patient_List.csv"));
			System.out.println("Patient list CSV file opened successfully.");  
			reader.readLine(); // Skip header
			System.out.println("Header skipped.");  
	
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println("Reading line: " + line);  
				String[] patientDetails = line.split(",");
				System.out.println(patientDetails[0].toUpperCase()); 
				// Check if patient ID matches
				if (patientDetails[0].equals(loggedInID.toUpperCase())) {
					System.out.println("Patient found: " + patientDetails[1]);  
	
					this.currentPatient = new Patient(
						loggedInID,                           // Hospital ID
						"",                                   // Password not needed
						"administrator",                      // Role
						patientDetails[1],                    // Name
						LocalDate.parse(patientDetails[2]),   // Date of Birth
						patientDetails[3],                    // Gender
						patientDetails[6],                    // Phone Number
						patientDetails[5],                    // Email
						patientDetails[4]                    // Blood Type
					);
					this.currentPatient.getBloodType();
					System.out.println("Patient object created successfully.");  
					break;
				}
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Error reading patient data: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error initializing patient: " + e.getMessage());
		}
	}
	public void showMenu() {
		while (isRunning) {
			System.out.println("Patient Menu:");
			System.out.println("1: View Medical Record");
			System.out.println("2: Update Personal Information");
			System.out.println("3: View Available Appointment Slots");
			System.out.println("4: Schedule an Appointment");
			System.out.println("5: Reschedule an Appointment");
			System.out.println("6: Cancel an Appointment");
			System.out.println("7: View Scheduled Appointments");
			System.out.println("8: View Past Appointment Outcome Records");
			System.out.println("9: Logout");

			int choice = sc.nextInt();
			sc.nextLine(); // Consume newline character

			switch (choice) {
				case 1: handleViewMedicalRecord(); break;
				case 2: handleUpdatePersonalInfo(); break;
				case 3: handleViewAvailableAppointmentSlots(); break;
				case 4: handleScheduleAppointment(); break;
				case 5: handleRescheduleAppointment(); break;
				case 6: handleCancelAppointment(); break;
				case 7: handleViewScheduledAppointments(); break;
				case 8: handleViewPastAppointmentOutcomeRecords(); break;
				case 9: handleLogout(); break;
				default: System.out.println("Invalid choice. Please try again."); break;
			}
		}
		sc.close();
	}

	private void handleViewMedicalRecord() {
		boolean succeed = currentPatient.viewMedicalRecord();
		if (!succeed) {
			System.out.println("Failed to retrieve medical records.");
		}
	}

	private void handleUpdatePersonalInfo() {
		System.out.println("\nEnter field to update:");
		System.out.println("1: Phone number");
		System.out.println("2: Email");
		int choice = sc.nextInt();
		sc.nextLine(); // Consume newline character
		switch (choice) {
			case 1: 
				System.out.println("Enter your new phone number:");
				String newPhoneNumber = sc.nextLine();
				currentPatient.setPhoneNum(newPhoneNumber);
				break;
			case 2: 
				System.out.println("Enter your new email address:");
				String newEmail = sc.nextLine();
				currentPatient.setEmail(newEmail);
				break;
			default:
				System.out.println("Invalid choice.");
		}
	}

	private void handleScheduleAppointment() {
		System.out.println("Enter doctor ID:");
		String doctorID = sc.nextLine();
		
		System.out.println("Enter timeslot ID:");
		String timeslotID = sc.nextLine();
		
		boolean succeed = currentPatient.scheduleAppointment(doctorID, timeslotID);
		if (succeed) {
			System.out.println("Appointment scheduled successfully!");
		} else {
			System.out.println("Failed to schedule the appointment. Please try again.");
		}
	}

	private void handleRescheduleAppointment() {
		System.out.println("Enter your old appointment ID:");
		String oldAppointmentID = sc.nextLine();
		
		System.out.println("Enter new timeslot ID:");
		String newTimeSlotID = sc.nextLine();

		boolean succeed = currentPatient.rescheduleAppointment(oldAppointmentID, newTimeSlotID);
		if (succeed) {
			System.out.println("Appointment rescheduled successfully!");
		} else {
			System.out.println("Failed to reschedule the appointment. Please try again.");
		}
	}


	private void handleCancelAppointment() {
		boolean succeed = currentPatient.cancelAppointment();
		if (succeed) {
			System.out.println("Appointment canceled successfully!");
		} else {
			System.out.println("Failed to cancel the appointment. Please try again.");
		}
	}

	
	
	private void handleViewAvailableAppointmentSlots() {
		boolean succeed = currentPatient.viewAvailableAppointmentSlots();
		if (!succeed) {
	
		}
	}

	private void handleViewScheduledAppointments() {
		List<Appointment> appointments = currentPatient.viewScheduledAppointments();
		// Check if the list is null or empty
		if (appointments == null || appointments.isEmpty()) {
			System.out.println("No past appointment outcome records found.");
		} else {
			for (Appointment record : appointments) {
				AppointmentManager.printAppointment(record);
			}
		}
		return;
	}

	// Handles viewing past appointment outcome records
	private void handleViewPastAppointmentOutcomeRecords() {
		// Get the list of appointment outcome records for the current patient
		List<AppointmentOutcomeRecord> appointment_outcomes = currentPatient.viewAppointmentOutcomeRecords();
		
		// Check if the list is null or empty
		if (appointment_outcomes == null || appointment_outcomes.isEmpty()) {
			// If no records found, display a message
			System.out.println("No past appointment outcome records found.");
		} else {
			// If records exist, loop through the list and print each record's details
			for (AppointmentOutcomeRecord record : appointment_outcomes) {
				AppointmentManager.printAppointmentOutcomeRecord(record);
			}
		}
	}
	

	// Handles logout
	private void handleLogout() {
		System.out.println("Program terminating...");
		currentPatient.logout();
		patientManager.updatePatient();
		isRunning = false; // Stop the menu loop
	}

	
}
