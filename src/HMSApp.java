import Views.*;
import java.util.Scanner;

public class HMSApp{
    public static void main(String[] args) {
		int choice = 0;
		Scanner sc = new Scanner(System.in);
		
		do {
			System.out.println("===Welcome to HMS!===");
			System.out.println("Perform the following methods:");
			System.out.println("1: Log In");
			System.out.println("2: Quit");
			
			if (sc.hasNext()){
				choice = sc.nextInt();
			} else {
                // If not an int, consume the invalid input
                sc.next(); // This will consume the invalid input
                System.out.println("Please enter a valid option (1 or 2).");
                continue; // Skip to the next iteration of the loop
            }		
			
			switch (choice) {
			case 1: 
				UserMenu userMenu = new UserMenu();
				userMenu.showMenu();
			case 2: 
				System.out.println("Program terminating ….");
			}
		} while (choice != 1 || choice != 2);
    }
}