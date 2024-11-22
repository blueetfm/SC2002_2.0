package Views;
import Enums.AppointmentStatus;
import Enums.Service;
import Models.Appointment;
import Models.Patient;
import Models.PatientManager;
import Models.TimeSlot;
import Models.TimeSlotManager;
import Services.AppointmentInterface;
import Services.PatientInterface;
import Services.TimeSlotInterface;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * The PatientMenu class provides an interactive console-based menu
 * for managing patient-related operations in a hospital system.
 * This menu allows patients to view and update personal information,
 * manage appointments, and access medical records.
 * <p>
 * It utilizes the Patient, Appointment, and TimeSlot models along with
 * relevant interfaces for data management. It integrates with external
 * CSV files for initial patient data retrieval.
 * </p>
 * 
 * @see Models.Patient
 * @see Models.Appointment
 * @see Models.TimeSlot
 * @see Services.PatientInterface
 * @see Services.TimeSlotInterface
 * @see Services.AppointmentInterface
 * @see Enums.Service
 * @see Enums.AppointmentStatus
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
public class PatientMenu implements Menu {
 /**
     * Scanner object for user input.
     */
    private final Scanner sc;

    /**
     * Current patient object for the logged-in session.
     */
    private Patient currentPatient;

    /**
     * Flag to control the menu's running state.
     */
    private boolean isRunning;

    /**
     * Interface for managing patient-related operations.
     */
    private PatientInterface patientManager;

    /**
     * Constructor for PatientMenu.
     * Initializes necessary components and retrieves patient data
     * based on the logged-in hospital ID.
     */
	public PatientMenu() {
		this.sc = new Scanner(System.in);
		this.patientManager = PatientManager.getInstance();
		this.isRunning = true;
		initializePatient();
	}

	/**
     * Reads the patient list CSV file to initialize the {@code currentPatient} object
     * using the logged-in hospital ID.
     * <p>
     * If no matching patient is found, the {@code currentPatient} remains null.
     * </p>
     */
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
	/**
     * Displays the patient menu and processes user input
     * to execute the selected operation.
     */
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
	/**
     * Handles the operation to view the current patient's medical record.
     * Displays an error message if the operation fails.
     */
	private void handleViewMedicalRecord() {
		boolean succeed = currentPatient.viewMedicalRecord();
		if (!succeed) {
			System.out.println("Failed to retrieve medical records.");
		}
	}
	/**
     * Handles updating the current patient's personal information.
     * The user can update their phone number or email address.
     */
	private void handleUpdatePersonalInfo() {
		System.out.println("\nEnter field to update:");
		System.out.println("1: Phone number");
		System.out.println("2: Email");
		int choice = sc.nextInt();
		sc.nextLine(); 
		switch (choice) {
			case 1: 
				String newPhoneNumber = null;
				while (newPhoneNumber == null) {
					System.out.println("Enter your new phone number (format: 9xxx xxxx or 8xxx xxxx):");
					String input = sc.nextLine();
					if (input.matches("^[98]\\d{3} \\d{4}$")) {
						newPhoneNumber = "+65 " + input.substring(0, 4) + input.substring(4);
					} else {
						System.out.println("Invalid phone number. Please enter digits only.");
					}
				}
				currentPatient.setPhoneNum(newPhoneNumber);
				PatientManager.updatePatient(currentPatient);
				break;
			case 2: 
				System.out.println("Enter your new email address:");
				String newEmail = sc.nextLine();
				currentPatient.setEmail(newEmail);
				PatientManager.updatePatient(currentPatient);
				break;
			default:
				System.out.println("Invalid choice.");
		}
	}
	/**
     * Handles scheduling a new appointment for the current patient.
     * Displays available slots and allows the user to select a slot and service.
     */
	private void handleScheduleAppointment() {
		handleViewAvailableAppointmentSlots();
		
		System.out.println("\nEnter slot ID to book (or 'back' to return): ");
		String slotID = sc.nextLine();
		
		if (slotID.equalsIgnoreCase("back")) {
			return;
		}
		
		TimeSlot slot = TimeSlotManager.getTimeSlotByID(slotID);
		if (slot == null) {
			System.out.println("Invalid slot ID.");
			return;
		}

		// Show available services
		System.out.println("\nAvailable services:");
		System.out.println("1: CONSULTATION");
		System.out.println("2: XRAY");
		System.out.println("3: BLOODTEST");
		System.out.print("Choose service (1-3): ");
		
		int serviceChoice;
		Service selectedService;
		try {
			serviceChoice = Integer.parseInt(sc.nextLine());
			switch (serviceChoice) {
				case 1:
					selectedService = Service.CONSULTATION;
					break;
				case 2:
					selectedService = Service.XRAY;
					break;
				case 3:
					selectedService = Service.BLOODTEST;
					break;
				default:
					System.out.println("Invalid choice. Defaulting to CONSULTATION.");
					selectedService = Service.CONSULTATION;
			}
			
			boolean success = currentPatient.scheduleAppointment(
				slot.getDoctorID(), 
				slotID,
				selectedService
			);
			
			if (success) {
				System.out.println("Appointment request sent successfully!");
			} else {
				System.out.println("Failed to schedule appointment. Please try again.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a number.");
		}
	}
	/**
     * Handles rescheduling an existing appointment.
     * Allows the user to select an appointment to modify and choose a new time slot.
     */
	private void handleRescheduleAppointment() {
        TimeSlotInterface.initializeObjects();
        AppointmentInterface.initializeObjects();
        
        // Show current appointments
        List<Appointment> appointments = currentPatient.getScheduledAppointments();
        if (appointments.isEmpty()) {
            System.out.println("You have no appointments to reschedule.");
            return;
        }

        System.out.println("\nYour Current Appointments:");
        System.out.println("----------------------------------------");
        boolean hasValidAppointments = false;
        for (Appointment apt : appointments) {
            if (apt.getStatus() == AppointmentStatus.CONFIRMED || 
                apt.getStatus() == AppointmentStatus.PENDING) {
                hasValidAppointments = true;
                TimeSlot slot = TimeSlotInterface.getTimeSlotByID(apt.getTimeSlotID());
                System.out.printf("Appointment ID: %s\n" +
                                "Doctor ID: %s\n" +
                                "Date: %s\n" +
                                "Time: %s - %s\n" +
                                "Service: %s\n" +
                                "Status: %s\n",
                    apt.getAppointmentID(),
                    apt.getDoctorID(),
                    apt.getDate(),
                    slot.getStartTime().toLocalTime(),
                    slot.getEndTime().toLocalTime(),
                    apt.getService(),
                    apt.getStatus());
                System.out.println("----------------------------------------");
            }
        }

        if (!hasValidAppointments) {
            System.out.println("No appointments available for rescheduling.");
            return;
        }

        System.out.print("\nEnter Appointment ID to reschedule (or 'back' to return): ");
        String oldAppointmentID = sc.nextLine().trim();

        if (oldAppointmentID.equalsIgnoreCase("back")) {
            return;
        }

        // Find the old appointment to get its service
        Appointment oldAppointment = appointments.stream()
            .filter(apt -> apt.getAppointmentID().equals(oldAppointmentID))
            .findFirst()
            .orElse(null);

        if (oldAppointment == null) {
            System.out.println("Invalid appointment ID.");
            return;
        }

        if (oldAppointment.getStatus() != AppointmentStatus.CONFIRMED && 
            oldAppointment.getStatus() != AppointmentStatus.PENDING) {
            System.out.println("This appointment cannot be rescheduled.");
            return;
        }

        // Show available slots
        handleViewAvailableAppointmentSlots();

        System.out.print("\nEnter new slot ID (or 'back' to return): ");
        String newSlotID = sc.nextLine().trim();

        if (newSlotID.equalsIgnoreCase("back")) {
            return;
        }

        TimeSlot newSlot = TimeSlotInterface.getTimeSlotByID(newSlotID);
        if (newSlot == null) {
            System.out.println("Invalid slot ID.");
            return;
        }

        // Cancel old appointment
        AppointmentInterface.updateAppointmentStatus(oldAppointmentID, AppointmentStatus.CANCELLED);

        // Schedule new appointment with same service
        try {
            AppointmentInterface.scheduleAppointment(
                currentPatient.getPatientID(),
                newSlot.getDoctorID(),
                newSlotID,
                oldAppointment.getService()  // Preserve the original service
            );
            System.out.println("Appointment rescheduled successfully!");
        } catch (Exception e) {
            System.out.println("Failed to reschedule appointment: " + e.getMessage());
        }
    }
	/**
     * Handles canceling a confirmed or pending appointment.
     * Displays the list of current appointments for selection.
     */
	private void handleCancelAppointment() {
        TimeSlotInterface.initializeObjects();
        AppointmentInterface.initializeObjects();
        
        List<Appointment> appointments = AppointmentInterface.getAppointmentsByPatientID(currentPatient.getPatientID());
        
        if (appointments.isEmpty()) {
            System.out.println("You have no scheduled appointments.");
            return;
        }
        
        System.out.println("\nYour appointments:");
        boolean hasConfirmedAppointments = false;
        for (Appointment apt : appointments) {
            if (apt.getStatus() == AppointmentStatus.CONFIRMED || 
                apt.getStatus() == AppointmentStatus.PENDING) {
                hasConfirmedAppointments = true;
                System.out.printf("ID: %s, Doctor: %s, Date: %s, Status: %s\n",
                    apt.getAppointmentID(), apt.getDoctorID(), apt.getDate(), apt.getStatus());
            }
        }
        
        if (!hasConfirmedAppointments) {
            System.out.println("You have no active appointments to cancel.");
            return;
        }
        
        System.out.println("\nEnter appointment ID to cancel (or 'back' to return): ");
        String aptID = sc.nextLine();
        
        if (aptID.equalsIgnoreCase("back")) {
            return;
        }
        
        boolean found = false;
        for (Appointment apt : appointments) {
            if (apt.getAppointmentID().equals(aptID)) {
                found = true;
                AppointmentInterface.updateAppointmentStatus(aptID, AppointmentStatus.CANCELLED);
                System.out.println("Appointment cancelled successfully.");
                break;
            }
        }
        
        if (!found) {
            System.out.println("Appointment ID not found.");
        }
    }
	/**
     * Displays available appointment slots for booking or rescheduling.
     */
	private void handleViewAvailableAppointmentSlots() {
        TimeSlotInterface.initializeObjects(); // Initialize before getting slots
        List<TimeSlot> availableSlots = TimeSlotInterface.getAvailableTimeSlots();
        if (availableSlots.isEmpty()) {
            System.out.println("No available slots found.");
            return;
        }
        System.out.println("\nAvailable Appointment Slots:");
        for (TimeSlot slot : availableSlots) {
            System.out.printf("Slot ID: %s, Doctor: %s, Date: %s, Time: %s-%s\n",
                slot.getTimeSlotID(), 
                slot.getDoctorID(),
                slot.getStartTime().toLocalDate(),
                slot.getStartTime().toLocalTime(),
                slot.getEndTime().toLocalTime());
        }
    }
	/**
     * Displays a list of scheduled appointments for the current patient.
     */
	private void handleViewScheduledAppointments() {
		TimeSlotInterface.initializeObjects();
		AppointmentInterface.initializeObjects();
		
		List<Appointment> appointments = currentPatient.getScheduledAppointments();
		
		if (appointments == null || appointments.isEmpty()) {
			System.out.println("\nNo appointments found.");
			return;
		}
		
		System.out.println("\nYour Appointments:");
		System.out.println("----------------------------------------");
		for (Appointment apt : appointments) {
			TimeSlot slot = TimeSlotInterface.getTimeSlotByID(apt.getTimeSlotID());
			if (slot != null) {
				System.out.printf("Appointment ID: %s\n" +
								"Doctor ID: %s\n" +
								"Date: %s\n" +
								"Time: %s - %s\n" +
								"Status: %s\n" +
								"Service: %s\n",
					apt.getAppointmentID(),
					apt.getDoctorID(),
					apt.getDate(),
					slot.getStartTime().toLocalTime(),
					slot.getEndTime().toLocalTime(),
					apt.getStatus(),
					apt.getService());
				System.out.println("----------------------------------------");
			}
		}
	}
	/**
     * Handles viewing past appointment outcome records.
     */
	private void handleViewPastAppointmentOutcomeRecords() {
		// // Get the list of appointment outcome records for the current patient
		// List<AppointmentOutcomeRecord> appointment_outcomes = currentPatient.viewAppointmentOutcomeRecords();
		
		// // Check if the list is null or empty
		// if (appointment_outcomes == null || appointment_outcomes.isEmpty()) {
		// 	// If no records found, display a message
		// 	System.out.println("No past appointment outcome records found.");
		// } else {
		// 	// If records exist, loop through the list and print each record's details
		// 	for (AppointmentOutcomeRecord record : appointment_outcomes) {
		// 		// AppointmentManager.printAppointmentOutcomeRecord(record);
		// 	}
		// }
	}
	/**
     * Logs out the current patient and terminates the menu session.
     */
	private void handleLogout() {
		System.out.println("Program terminating...");
		if (currentPatient.logout()) {
			isRunning = false;
			PatientInterface.updatePatient(this.currentPatient);
		}
	}

}
