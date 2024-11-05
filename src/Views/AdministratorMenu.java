package Views;

import Models.MedicationInventory;
import Models.StaffList;
import java.util.Scanner;

public class AdministratorMenu implements Menu {
	
	@Override
	public void showMenu() {
		int choice;
		int manageStaffOption;
		int inventoryOption;
		//initialise staff list
        StaffList staffList = new StaffList("data(testCopy)/Staff_List.csv");
		//initialise medication inventory
		MedicationInventory inventory = new MedicationInventory("data(testCopy)/Medicine_List.csv", "data(testCopy)/medication_requests.csv");
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("Perform the following methods:");
			System.out.println("1: View and Manage Hospital Staff");
			System.out.println("2: View Appointments Details");
			System.out.println("3: View and Manage Medication Inventory");
			System.out.println("4: Approve Replenishment Requests");
			System.out.println("5: Logout");
			choice = sc.nextInt();
			
			switch (choice) {
			case 1: 
				//view hospital staff list
				staffList.viewStaffList();
				do { 
					System.out.println("Perform the following actions: ");
					System.out.println("1: Add new staff");
					System.out.println("2: Remove staff");
					System.out.println("3: Update staff details(ID, name, role, gender, age)");
					System.out.println("4: Return to main menu");
					manageStaffOption = sc.nextInt();

					switch(manageStaffOption) {
						case 1:
							// Clear the scanner buffer after nextInt()
							sc.nextLine();
							
							// add new staff
							System.out.println("Enter new staff role: ");
							String role = sc.nextLine();
							
							System.out.println("Enter new staff ID: ");
							String staffID = sc.nextLine();
							
							System.out.println("Enter new staff name: ");
							String name = sc.nextLine();
							
							System.out.println("Enter new staff age: ");
							int age = sc.nextInt();
							sc.nextLine(); 

							System.out.println("Select new staff gender: ");
							System.out.println("1: Male");
							System.out.println("2: Female");
							int genderOption = sc.nextInt();
							sc.nextLine();
							
							switch(genderOption) {
								case 1 -> staffList.addStaff(staffID, "password", name, role, "Male", age);
								case 2 -> staffList.addStaff(staffID, "password", name, role, "Female", age);
							}
							break;
						case 2:
							// remove existing staff
							staffList.viewStaffList();
							System.out.println("Enter staff ID to remove staff: ");
							sc.nextLine();
							String removeID = sc.nextLine();
							staffList.removeStaff(removeID);
							System.out.println("===Updated Staff List===");
							staffList.viewStaffList();
							break;
						case 3:
							staffList.updateStaffDetails();
							break;
						case 4:
							break;
					}
				} while (manageStaffOption < 4);
				break;
			case 2: 
				break;
			case 3: 
				//view and manage medication inventory
				inventory.viewMedicationInventory();
				do { 
					System.out.println("Perform the following actions: ");
					System.out.println("1: Add new medication to inventory");
					System.out.println("2: Remove medication from inventory");
					System.out.println("3: Edit inventory quantities");
					System.out.println("4: Exit to main menu");
					System.out.println("Enter your choice (1-4): ");
					inventoryOption = sc.nextInt();

					switch(inventoryOption) {
						case 1:
							//add med
							System.out.println("Enter name of new medicine: ");
							String newMedName = sc.nextLine();
							sc.nextLine();

							System.out.println("Enter stock: ");
							int newMedStock = sc.nextInt();
							sc.nextLine();

							System.out.println("Set low stock threshold: ");
							int newMedThreshold = sc.nextInt();

							inventory.addMedication(newMedName, newMedStock, newMedThreshold);
							break;
						case 2:
							//remove med
							inventory.viewMedicationInventory();
							System.out.println("Enter name of medicine to remove: ");
							String removeMedName = sc.nextLine();
							inventory.removeMedication(removeMedName);

							System.out.println("===Updated Medication Inventory===");
							inventory.viewMedicationInventory();
							break;
						case 3:
							//edit quantities
							int medUpdateOption;
							inventory.viewMedicationInventory();
							System.out.println("Enter medication name to update: ");
							String medToUpdate = sc.nextLine();
							do { 
								System.out.println("Perform the following: ");
								System.out.println("1: Update current stock level");
								System.out.println("2: Update low level threshold");
								medUpdateOption = sc.nextInt();

								switch(medUpdateOption) {
									case 1:
										System.out.println("Enter new stock level: ");
										int newStock = sc.nextInt();
										inventory.updateMedication(medToUpdate, newStock, null);
										break;
									case 2:
										System.out.println("Enter new low stock threshold: ");
										int newThreshold = sc.nextInt();
										inventory.updateMedication(medToUpdate, null, newThreshold);
										break;
								}
							} while (medUpdateOption < 3);
							break;
						case 4:
						System.out.println("Exiting to main menu...");
						break;
					}
				} while (inventoryOption < 4);
				break;
			case 4: 
				//Approve Replenishment Requests
				inventory.displayReplenishRequests();
				System.out.println("Enter name of medicine to approve: ");
				sc.nextLine();
				String medName = sc.nextLine();
				inventory.approveReplenishRequests(medName);
				break;
			case 5: 
				System.out.println("Logging out ….");
			}
		} while (choice < 5);
	}
}
