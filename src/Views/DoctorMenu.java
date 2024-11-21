package Views;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import Enums.*;
import Models.AppointmentManager;
import Models.AppointmentOutcomeRecord;
import Models.Doctor;


public class DoctorMenu implements Menu {
	private final Scanner scanner = null;
	private Doctor currentDoctor;
	private boolean isRunning; 
	
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
		int choice;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("Perform the following methods:");
			System.out.println("1: View Patient Medical Records");
			System.out.println("2: Update Patient Medical Records"); 
			System.out.println("3: View Personal Schedule");
			System.out.println("4: Set Availability for Appointments");
			// Doctor Class
				// TimeSlotManager: Change availability of new TimeSlot
			System.out.println("5: Accept or Decline Appointment Requests");
			/*  Doctor Class
				IF ACCEPT:
				TimeSlotManager: Change availability of new TimeSlot
				*/
			System.out.println("6: View Upcoming Appointments");
			// Doctor Class
				// AppointmentManager: Fetch appointments by Doctor ID and sort by Schedule Status
			System.out.println("7: Record Appointment Outcome");
			// Doctor Class
				// AppointmentManager: Fetch appointment by APT ID and record
			System.out.println("8: Register Patient");
			// Doctor Class
			System.out.println("9: Discharge Patient");
			// Doctor Class
			System.out.println("10: Logout");
			choice = sc.nextInt();
			int result;
			
			switch (choice) {
			case 1: 
//				View Patient Medical Records
				result = currentDoctor.viewPatientMedicalRecords();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}
			case 2: 
//				Update Patient Medical Records
				result = currentDoctor.updatePatientMedicalRecords();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}
			case 3: 
//				View Personal Schedule
				result = currentDoctor.viewPersonalSchedule();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}
			case 4: 
//				Set Availability for Appointments
				result = currentDoctor.setAppointmentAvailability();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}		
			case 5: 
//				Accept or Decline Appointment Requests
				result = currentDoctor.acceptOrDeclineAppointmentRequests();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}	
			case 6: 
				result = currentDoctor.viewUpcomingAppointments();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}
			case 7: 
				result = currentDoctor.recordAppointmentOutcome();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}

				// if (AppointmentManager.recordAppointmentOutcomeRecord(appointmentID, hospitalID, date, 
				// 	service, medication, PrescriptionStatus.PENDING, notes)){
				// 		System.out.println("Appointment Outcome Record created successfully!");
				// } else {
				// 	System.out.println("Appointment Outcome Record already exists for this appointment.");
					
				// };

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
				result = currentDoctor.logout();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}
			}
		} while (choice < 10);
		sc.close();
	    return;
	}
}
	

