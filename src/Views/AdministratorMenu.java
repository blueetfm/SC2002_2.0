package Views;

import java.util.Scanner;

public class AdministratorMenu implements Menu {
	public void showMenu() {
		int choice;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("Perform the following methods:");
			System.out.println("1: View and Manage Hospital Staff");
			System.out.println("2: View Appointments Details");
			System.out.println("3: View and Manage Medication Inventory");
			System.out.println("4: Approve Replenishment Requests");
			System.out.println("5: Add Hospital Staff");
			System.out.println("6: Remove Hospital Staff");
			System.out.println("7: Logout");
			choice = sc.nextInt();
			
			switch (choice) {
			case 1: 
				break;
			case 2: 
				break;
			case 3: 
				break;
			case 4: 
				break;
			case 5: 
				break;
			case 6: 
				break;
			case 7: 
				System.out.println("Logging out ….");
			}
		} while (choice < 7);
	    return;
	}
}
