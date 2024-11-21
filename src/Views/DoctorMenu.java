package Views;
import Enums.*;
import Models.Appointment;
import Models.Doctor;
import Models.TimeSlot;
import Services.AppointmentInterface;
import Services.TimeSlotInterface;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class DoctorMenu implements Menu {
	private Scanner scanner;
    private Doctor currentDoctor;
    private boolean isRunning;
	
	public DoctorMenu() {
		this.scanner = new Scanner(System.in);
		isRunning = true;
		initializeDoctor();
	}
	
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
			case 2: 
				result = currentDoctor.updatePatientMedicalRecords();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}
			case 3: 
				result = currentDoctor.viewPersonalSchedule();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}
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
			case 9: 
				result = currentDoctor.dischargePatient();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}	
			case 10: 
				System.out.println("Logging out ….");
				if (currentDoctor.logout()) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}
			}
		} while (choice < 10);
		scanner.close();
	    return;
	}

	private void handleSetAvailability() {
        System.out.println("\nSet Appointment Availability");
        Scanner input = new Scanner(System.in);
        
        System.out.println("Enter number of slots to create: ");
        int numSlots = input.nextInt();
        
        LocalDateTime startTime = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0);
        
        for (int i = 0; i < numSlots; i++) {
            LocalDateTime slotStart = startTime.plusHours(i);
            LocalDateTime slotEnd = slotStart.plusMinutes(30);
            TimeSlotInterface.createTimeSlot(currentDoctor.getHospitalID(), slotStart, slotEnd);
        }
        System.out.println("Created " + numSlots + " time slots");
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

    private void handleViewUpcomingAppointments() {
        List<Appointment> appointments = AppointmentInterface.getAppointmentsByDoctorID(currentDoctor.getHospitalID());
        System.out.println("\nUpcoming Appointments:");
        for (Appointment apt : appointments) {
            if (apt.getStatus() == AppointmentStatus.CONFIRMED) {
                System.out.printf("ID: %s, Patient: %s, Date: %s\n",
                    apt.getAppointmentID(), apt.getPatientID(), apt.getDate());
            }
        }
    }

}
	

