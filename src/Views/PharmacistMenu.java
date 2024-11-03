package Views;

import java.util.Scanner;
import Models.*;

public class PharmacistMenu implements Menu {
	public void showMenu() {
		int choice;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("Perform the following methods:");
			System.out.println("1: View Appointment Outcome Record");
			System.out.println("2: Update Prescription Status");
			System.out.println("3: View Medication Inventory");
			System.out.println("4: Submit Replenishment Request");
			System.out.println("5: Logout");
			choice = sc.nextInt();
			
			switch (choice) {
			case 1: 
				System.out.print("Enter Appointment ID: ");
				String appointmentID = sc.next();
				AppointmentOutcomeRecordList.getInstance().readAppointmentOutcomeRecord(appointmentID);
				break;
			case 2: 
				break;
			case 3: 
				break;
			case 4: 
				break;
			case 5: 
				System.out.println("Logging out ….");
			}
		} while (choice < 5);
		sc.close();
	    return;
	}
}
