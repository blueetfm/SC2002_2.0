package Views;

import Models.*;
import java.util.Scanner;

public class PharmacistMenu implements Menu {
	
	@Override
	public void showMenu() {
		int choice;
		int medToReplenish;
		Scanner sc = new Scanner(System.in);
		//initialise inventory
		MedicationInventory inventory = new MedicationInventory("data(testCopy)/Medicine_List.csv", "data(testCopy)/medication_requests.csv");
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
				System.out.print("Enter Appointment ID to view: ");
				String appointmentID = sc.next();
				AppointmentOutcomeRecordList.getInstance().readAppointmentOutcomeRecord(appointmentID);
				break;
			case 2: 
				break;
			case 3:
				//view medication inventory
				inventory.viewMedicationInventory();
				break;
			case 4: 
				//view inventory and check for low stock alert
				inventory.viewMedicationInventory();
				//replenish medication
				do {
					System.out.println("Which medication would you like to replenish?");
					System.out.println("1: Paracetamol");
					System.out.println("2: Ibuprofen");
					System.out.println("3: Amoxicillin");
					System.out.println("4: Exit to main menu");
					medToReplenish = sc.nextInt();
					if (medToReplenish == 4 ) {
						break;
					}
					System.out.println("Enter quantity to replenish: ");
					int quantity = sc.nextInt();

					switch (medToReplenish) {
						case 1 -> inventory.submitReplenishRequest("Paracetamol", quantity);
						case 2 -> inventory.submitReplenishRequest("Ibuprofen", quantity);
						case 3 -> inventory.submitReplenishRequest("Amoxicillin", quantity);
					}
				} while (medToReplenish < 4);
				break;
			case 5: 
				System.out.println("Logging out ….");
			}
		} while (choice < 5);
		sc.close();
	}
}
