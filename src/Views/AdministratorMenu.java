package Views;

import Models.MedicationInventory;
import Models.StaffList;
import java.util.Scanner;

public class AdministratorMenu implements Menu {
	
	@Override
	public void showMenu() {
		int choice;
		int manageStaffOption;
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
