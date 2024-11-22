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


public class PatientMenu implements Menu {

	private final Scanner sc;
	private Patient currentPatient;
	private boolean isRunning;
	private PatientInterface patientManager;
	// private AppointmentManager appointmentManager;

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
		sc.nextLine(); 
		switch (choice) {
			case 1: 
				String newPhoneNumber = null;
				while (newPhoneNumber == null) {
					System.out.println("Enter your new phone number:");
					String input = sc.nextLine();
					if (input.matches("\\d+")) { 
						newPhoneNumber = input;
					} else {
						System.out.println("Invalid phone number. Please enter digits only.");
					}
				}
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
	
	private void handleLogout() {
		System.out.println("Program terminating...");
		currentPatient.logout();
		PatientInterface.updatePatient(this.currentPatient);
		isRunning = false; // Stop the menu loop
	}

}
