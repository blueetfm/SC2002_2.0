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
	
	public DoctorMenu() {
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
		int choice;
		Scanner sc = new Scanner(System.in);
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
			choice = sc.nextInt();
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
				result = currentDoctor.setAppointmentAvailability();
				if (result == 1) {
					System.out.println("Task completed successfully.");
				} else {
					System.out.println("Task failed.");
				}		
			case 5: 
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
		sc.close();
	    return;
	}
}
	

