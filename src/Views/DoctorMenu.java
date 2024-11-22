package Views;
import Enums.AppointmentStatus;
import Models.Appointment;
import Models.Doctor;
import Models.TimeSlot;
import Services.AppointmentInterface;
import Services.TimeSlotInterface;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * The DoctorMenu class represents the interface for a doctor to interact with various functionalities of the hospital management system.
 * It provides options for viewing and updating patient records, managing appointments, setting availability, and more.
 * 
 * <p>Dependencies:</p>
 * This class relies on several other classes and interfaces:
 * - Enums.AppointmentStatus
 * - Models.Appointment
 * - Models.Doctor
 * - Models.TimeSlot
 * - Services.AppointmentInterface
 * - Services.TimeSlotInterface
 * - Utils.DateTimeFormatUtils
 * 
 * <p>Features:</p>
 * - Provides a menu-driven interface for the doctor.
 * - Allows viewing, updating, and managing patient medical records and appointments.
 * - Supports managing doctor's personal schedule and availability.
 * 
 * <p>File Reading:</p>
 * Reads doctor information from a CSV file (`Staff_List.csv`) during initialization.
 * 
 * <p>Error Handling:</p>
 * Includes basic error handling for file I/O and user input validation.
 * 
 * <p>Usage:</p>
 * Create an instance of DoctorMenu and invoke the showMenu method to start the interaction.
 * 
 * @see Enums.AppointmentStatus
 * @see Models.Appointment
 * @see Models.Doctor
 * @see Models.TimeSlot
 * @see Services.AppointmentInterface
 * @see Services.TimeSlotInterface
 * @see Utils.DateTimeFormatUtils
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
public class DoctorMenu implements Menu {
	private Scanner scanner;
    private Doctor currentDoctor;
    private boolean isRunning;
	 /**
     * Constructor initializes the scanner and the current doctor based on logged-in user information.
     */
	public DoctorMenu() {
		this.scanner = new Scanner(System.in);
		isRunning = true;
		initializeDoctor();
	}
	 /**
     * Initializes the current doctor based on the logged-in hospital ID.
     * Reads doctor details from the Staff_List.csv file.
     */
	private void initializeDoctor() {
		String loggedInID = UserMenu.getLoggedInHospitalID();
        
        // get administrator details
		try {
			BufferedReader reader = new BufferedReader(new FileReader("data/Staff_List.csv"));
	        reader.readLine(); // Skip header
	        
	        String line;
	        while ((line = reader.readLine()) != null) {
	        	String[] staffDetails = line.split(",");
	            if (staffDetails[0].equals(loggedInID)) {
	            	this.currentDoctor = new Doctor(loggedInID, "", "doctor", staffDetails[1], staffDetails[3]);
	            	break;
	            }
	        }
	        reader.close();
		} catch (IOException e) {
			System.err.println("Error reading staff data: " + e.getMessage());
		}
	}
	 /**
     * Displays the main menu and handles user interactions.
     */
	public void showMenu() {
        scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("Perform the following methods:");
            System.out.println("1: View Patient Medical Records");
            System.out.println("2: Update Patient Medical Records"); 
            System.out.println("3: View Personal Schedule");
            System.out.println("4: Set Availability for Appointments");
            System.out.println("5: Accept or Decline Appointment Requests");
            System.out.println("6: View Upcoming Appointments");
            System.out.println("7: Record Appointment Outcome");
            System.out.println("8: Register Patient");
            System.out.println("9: Discharge Patient");
            System.out.println("10: Logout");
            choice = scanner.nextInt();
            scanner.nextLine();
			int result;
			
			switch (choice) {
			case 1: 
				result = currentDoctor.viewPatientMedicalRecords();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}
				break;
			case 2: 
				result = currentDoctor.updatePatientMedicalRecords();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}
				break;
			case 3: 
				handleViewPersonalSchedule();
				break;
			case 4: 
				handleSetAvailability();
				break;
			case 5: 
				handleAcceptOrDeclineAppointmentRequests();
				break;
			case 6: 
				handleViewUpcomingAppointments();
				break;
			case 7: 
				result = currentDoctor.recordAppointmentOutcome();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}
				break;
			case 8: 
				result = currentDoctor.registerPatient();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}
				break;
			case 9: 
				result = currentDoctor.dischargePatient();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}
				break;	
			case 10: 
				System.out.println("Logging out ….");
				if (currentDoctor.logout()) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}
				break;
			}
		} while (choice < 10);
		scanner.close();
	    return;
	}
	 /**
     * Displays the doctor's personal schedule.
     */
	private void handleViewPersonalSchedule() {
		TimeSlotInterface.initializeObjects();
		List<TimeSlot> slots = currentDoctor.getPersonalSchedule();
		
		if (slots.isEmpty()) {
			System.out.println("\nNo time slots found in your schedule.");
			return;
		}
	
		System.out.println("\nYour Schedule:");
		System.out.println("----------------------------------------");
		for (TimeSlot slot : slots) {
			String status = slot.getStatus().toString();
			String patientInfo = slot.getPatientID() != null ? 
				"Patient: " + slot.getPatientID() : "Available";
				
			System.out.printf("Slot ID: %s\n" +
							 "Date: %s\n" +
							 "Time: %s - %s\n" +
							 "Status: %s\n" +
							 "%s\n",
				slot.getTimeSlotID(),
				slot.getStartTime().toLocalDate(),
				slot.getStartTime().toLocalTime(),
				slot.getEndTime().toLocalTime(),
				status,
				patientInfo);
			System.out.println("----------------------------------------");
		}
	}	
	/**
     * Sets availability for appointments by creating time slots.
     */
	private void handleSetAvailability() {
		System.out.println("\nSet Appointment Availability");
		
		// Get date for the slots
		LocalDate slotDate = getValidDate();
		if (slotDate == null) return;

		// Get number of slots
		System.out.println("Enter number of slots to create: ");
		int numSlots;
		try {
			numSlots = Integer.parseInt(scanner.nextLine().trim());
			if (numSlots <= 0) {
				System.out.println("Please enter a positive number.");
				return;
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a number.");
			return;
		}

		// Get starting hour (9-17 for business hours)
		System.out.println("Enter starting hour (9-17): ");
		int startHour;
		try {
			startHour = Integer.parseInt(scanner.nextLine().trim());
			if (startHour < 9 || startHour > 17) {
				System.out.println("Please enter an hour between 9 and 17.");
				return;
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a number.");
			return;
		}

		LocalDateTime startTime = slotDate.atTime(startHour, 0);
		
		// Check if date is not in the past
		if (startTime.isBefore(LocalDateTime.now())) {
			System.out.println("Cannot create slots for past dates/times.");
			return;
		}

		// Create the slots
		for (int i = 0; i < numSlots; i++) {
			LocalDateTime slotStart = startTime.plusHours(i);
			LocalDateTime slotEnd = slotStart.plusMinutes(30);
			
			// Check if slot would extend beyond business hours
			if (slotEnd.getHour() > 17) {
				System.out.println("Stopping slot creation as it would exceed business hours (17:00).");
				break;
			}
			
			currentDoctor.createTimeSlot(slotStart, slotEnd);
		}
		System.out.println("Time slots created successfully.");
	}

    private void handleAcceptOrDeclineAppointmentRequests() {
        TimeSlotInterface.initializeObjects();
        AppointmentInterface.initializeObjects();
        
        String doctorID = currentDoctor.getHospitalID();
        List<Appointment> pendingAppointments = AppointmentInterface.getAppointmentsByDoctorID(doctorID)
            .stream()
            .filter(apt -> apt.getStatus() == AppointmentStatus.PENDING)
            .collect(Collectors.toList());
            
        if (pendingAppointments.isEmpty()) {
            System.out.println("\nNo pending appointment requests.");
            return;
        }

        System.out.println("\nPending Appointment Requests:");
        System.out.println("----------------------------------------");
        
        for (Appointment apt : pendingAppointments) {
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
                
            System.out.print("Accept appointment? (Y/N): ");
            String response = scanner.nextLine().trim().toUpperCase();
            
            if (response.equals("Y")) {
                AppointmentInterface.updateAppointmentStatus(apt.getAppointmentID(), AppointmentStatus.CONFIRMED);
                System.out.println("Appointment confirmed.");
            } else if (response.equals("N")) {
                AppointmentInterface.updateAppointmentStatus(apt.getAppointmentID(), AppointmentStatus.CANCELLED);
                System.out.println("Appointment rejected.");
            } else {
                System.out.println("Invalid input. Skipping this request.");
            }
            System.out.println("----------------------------------------");
        }
    }
	/**
     * Allows the doctor to see what future appointments have been scheduled by patients.
     */
    private void handleViewUpcomingAppointments() {
		TimeSlotInterface.initializeObjects();
		AppointmentInterface.initializeObjects();
		
		List<Appointment> appointments = currentDoctor.getUpcomingAppointments();
		
		if (appointments.isEmpty()) {
			System.out.println("\nNo upcoming appointments found.");
			return;
		}
		
		System.out.println("\nUpcoming Appointments:");
		System.out.println("----------------------------------------");
		for (Appointment apt : appointments) {
			TimeSlot slot = TimeSlotInterface.getTimeSlotByID(apt.getTimeSlotID());
			if (slot != null) {
				System.out.printf("Appointment ID: %s\n" +
								"Patient ID: %s\n" +
								"Date: %s\n" +
								"Time: %s - %s\n" +
								"Status: %s\n",
					apt.getAppointmentID(),
					apt.getPatientID(),
					apt.getDate(),
					slot.getStartTime().toLocalTime(),
					slot.getEndTime().toLocalTime(),
					apt.getStatus());
				System.out.println("----------------------------------------");
			}
		}
	}
	/**
     * Prompts the user to enter a valid date.
     * 
     * @return A valid LocalDate, or null if the user cancels the operation.
     */
	private LocalDate getValidDate() {
    while (true) {
        System.out.println("Enter date for slots (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine().trim();
        
        try {
            LocalDate date = LocalDate.parse(dateStr);
            if (date.isBefore(LocalDate.now())) {
                System.out.println("Cannot create slots for past dates.");
                continue;
            }
            return date;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            System.out.println("Would you like to try again? (Y/N): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                return null;
            }
        }
    }
}

}
	

