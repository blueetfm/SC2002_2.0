package Views;

import Models.Administrator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class AdministratorMenu implements Menu {
	private final Scanner scanner;
    private Administrator currentAdmin;
    private boolean isRunning;

	public AdministratorMenu() {
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
        initializeAdministrator();
    }
	private void initializeAdministrator() {
        try {
            String loggedInID = UserMenu.getLoggedInHospitalID();
            if (loggedInID == null) {
                throw new IllegalStateException("No user is logged in");
            }

            // Read from Staff_List.csv to get pharmacist details
            File csvFile = new File("data/Staff_List.csv");
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            String line;

			reader.readLine();

			while ((line = reader.readLine()) != null) {
                String[] staffDetails = line.split(",");
                // Check if staffID matches logged in ID and role is pharmacist
                if (staffDetails.length >= 5 && 
                    staffDetails[0].equals(loggedInID) && 
                    staffDetails[2].equalsIgnoreCase("administrator")) {
                    
                    // Create Pharmacist object with details from Staff_List.csv
                    this.currentAdmin = new Administrator(
                        loggedInID,             // hospitalID
                        "",                     // password (already authenticated)
                        "administrator",           // role
                        staffDetails[1],        // name
                        staffDetails[3]        // gender
                    );
                    break;
                }
            }
            
            if (currentAdmin == null) {
                throw new IllegalStateException("Logged in user is not an administrator");
            }
        } catch (Exception e) {
            System.err.println("Error initializing administrator: " + e.getMessage());
            System.exit(1);
        }
    }

	@Override
    public void showMenu() {
        while (isRunning) {
            System.out.println("\nAdministrator Menu:");
            System.out.println("1: View and Manage Hospital Staff");
            System.out.println("2: View Appointments Details");
            System.out.println("3: View and Manage Medication Inventory");
            System.out.println("4: Approve Replenishment Requests");
            System.out.println("5: Logout");
            System.out.print("Enter your choice (1-5): ");

            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    input = scanner.nextLine().trim();
                }
                
                int choice = Integer.parseInt(input);
                
                switch (choice) {
                    case 1 -> handleStaffManagement();
                    case 2 -> currentAdmin.viewAppointmentDetails();
                    case 3 -> handleInventoryManagement();
                    case 4 -> handleReplenishmentApproval();
                    case 5 -> {
                        if (currentAdmin.logout()) {
                            isRunning = false;
                        }
                    }
                    default -> System.out.println("Invalid choice! Please enter a number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

	//other menu displays

	private void displayStaffMenu() {
        System.out.println("\nStaff Management Options:");
        System.out.println("1: Add new staff");
        System.out.println("2: Remove staff");
        System.out.println("3: Update staff details");
        System.out.println("4: Return to main menu");
        System.out.print("Enter your choice (1-4): ");
    }

	private void displayInventoryMenu() {
        System.out.println("\nInventory Management Options:");
        System.out.println("1: Add new medication to inventory");
        System.out.println("2: Remove medication from inventory");
        System.out.println("3: Edit inventory quantities");
        System.out.println("4: Return to main menu");
    }

    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1 -> handleStaffManagement();
            case 2 -> currentAdmin.viewAppointmentDetails();
            case 3 -> handleInventoryManagement();
            case 4 -> handleReplenishmentApproval();
            case 5 -> {
                if (currentAdmin.logout()) {
                    isRunning = false;
                }
            }
            default -> System.out.println("Invalid choice!");
        }
    }

    private void handleStaffManagement() {
        currentAdmin.viewStaffList();
        boolean managing = true;

        while (managing) {
            displayStaffMenu();
            try {
                // Clear any leftover newline
                String input = scanner.nextLine().trim();
                
                // If input is empty (due to previous nextInt), read again
                if (input.isEmpty()) {
                    input = scanner.nextLine().trim();
                }
                
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1 -> {
                        System.out.println("Enter new staff role: ");
                        String role = scanner.nextLine().trim();
                        
                        System.out.println("Enter new staff ID: ");
                        String staffID = scanner.nextLine().trim();
                        
                        System.out.println("Enter new staff name: ");
                        String name = scanner.nextLine().trim();
                        
                        System.out.println("Enter new staff age: ");
                        int age = Integer.parseInt(scanner.nextLine().trim());

                        System.out.println("Select new staff gender: ");
                        System.out.println("1: Male");
                        System.out.println("2: Female");
                        int genderOption = Integer.parseInt(scanner.nextLine().trim());
                        
                        String gender = switch (genderOption) {
                            case 1 -> "Male";
                            case 2 -> "Female";
                            default -> throw new IllegalArgumentException("Invalid gender option");
                        };

                        currentAdmin.addStaff(staffID, name, role, gender, age);
                    }
                    case 2 -> {
                        System.out.println("Enter staff ID to remove: ");
                        String staffID = scanner.nextLine().trim();
                        currentAdmin.removeStaff(staffID);
                    }
                    case 3 -> currentAdmin.updateStaffDetails();
                    case 4 -> managing = false;
                    default -> System.out.println("Invalid choice! Please enter a number between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }


    private void handleInventoryManagement() {
        currentAdmin.viewMedicationInventory();
        boolean managing = true;

        while (managing) {
            displayInventoryMenu();
            int choice = getValidChoice();

            switch (choice) {
                case 1 -> handleAddMedication();
                case 2 -> handleRemoveMedication();
                case 3 -> handleUpdateMedication();
                case 4 -> managing = false;
                default -> System.out.println("Invalid choice!");
            }
        }
    }

	//specific handlers
	private void handleAddStaff() {
        try {
            // Clear any leftover newline
            scanner.nextLine();

            System.out.println("Enter new staff role: ");
            String role = getValidInput("Role");

            System.out.println("Enter new staff ID: ");
            String staffID = getValidInput("Staff ID");

            System.out.println("Enter new staff name: ");
            String name = getValidInput("Name");

            System.out.println("Enter new staff age: ");
            int age = getValidAge();

            System.out.println("Select new staff gender: ");
            System.out.println("1: Male");
            System.out.println("2: Female");
            int genderOption = getValidChoice();
            String gender = switch (genderOption) {
                case 1 -> "Male";
                case 2 -> "Female";
                default -> throw new IllegalArgumentException("Invalid gender option");
            };

            if (currentAdmin.addStaff(staffID, name, role, gender, age)) {
                System.out.println("Staff added successfully!");
            }
        } catch (Exception e) {
            System.err.println("Error adding staff: " + e.getMessage());
        }
    }

	private void handleRemoveStaff() {
        try {
            currentAdmin.viewStaffList();
            System.out.println("\nEnter staff ID to remove: ");
            String staffID = getValidInput("Staff ID");
            
            if (currentAdmin.removeStaff(staffID)) {
                System.out.println("Staff removed successfully!");
            }
        } catch (Exception e) {
            System.err.println("Error removing staff: " + e.getMessage());
        }
    }

	private void handleAddMedication() {
        try {
            System.out.println("Enter name of new medicine: ");
            String medName = getValidInput("Medicine name");

            System.out.println("Enter initial stock quantity: ");
            int initialStock = getValidPositiveNumber("Initial stock");

            System.out.println("Set low stock threshold: ");
            int threshold = getValidPositiveNumber("Low stock threshold");

            if (currentAdmin.addMedication(medName, initialStock, threshold)) {
                System.out.println("Medication added successfully!");
                currentAdmin.viewMedicationInventory();
            }
        } catch (Exception e) {
            System.err.println("Error adding medication: " + e.getMessage());
        }
    }

	private void handleRemoveMedication() {
        try {
            currentAdmin.viewMedicationInventory();
            System.out.println("\nEnter name of medicine to remove: ");
            String medName = getValidInput("Medicine name");

            if (currentAdmin.removeMedication(medName)) {
                System.out.println("Medication removed successfully!");
            }
        } catch (Exception e) {
            System.err.println("Error removing medication: " + e.getMessage());
        }
    }

	private void handleUpdateMedication() {
        try {
            currentAdmin.viewMedicationInventory();
            System.out.println("\nEnter medication name to update: ");
            String medName = getValidInput("Medicine name");

            System.out.println("Select update option:");
            System.out.println("1: Update current stock level");
            System.out.println("2: Update low level threshold");
            int updateChoice = getValidChoice();

            switch (updateChoice) {
                case 1 -> {
                    System.out.println("Enter new stock level: ");
                    int newStock = getValidPositiveNumber("Stock level");
                    if (currentAdmin.updateMedicationStock(medName, newStock, null)) {
                        System.out.println("Stock level updated successfully!");
                    }
                }
                case 2 -> {
                    System.out.println("Enter new low stock threshold: ");
                    int newThreshold = getValidPositiveNumber("Threshold");
                    if (currentAdmin.updateMedicationStock(medName, null, newThreshold)) {
                        System.out.println("Threshold updated successfully!");
                    }
                }
                default -> System.out.println("Invalid choice!");
            }

            currentAdmin.viewMedicationInventory();
        } catch (Exception e) {
            System.err.println("Error updating medication: " + e.getMessage());
        }
    }

	private void handleReplenishmentApproval() {
        try {
            currentAdmin.viewReplenishRequests();

            if (!hasAnyReplenishRequests()) {
                System.out.println("No pending replenishment requests.");
                return;
            }

            System.out.println("\nEnter medicine name to approve (or 'exit' to return): ");
            String medName = scanner.nextLine().trim();
            
            if (medName.equalsIgnoreCase("exit")) {
                return;
            }
            
            if (medName.isEmpty()) {
                System.out.println("Medicine name cannot be empty!");
                return;
            }
            
            if (currentAdmin.approveReplenishRequest(medName)) {
                System.out.println("Replenishment request approved successfully!");
                currentAdmin.viewMedicationInventory();
            }
        } catch (Exception e) {
            System.err.println("Error approving replenishment: " + e.getMessage());
        }
    }

    // Helper method to check for replenishment requests
    private boolean hasAnyReplenishRequests() {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/Medication_Replenish_Requests.csv"))) {
            reader.readLine();
            return reader.readLine() != null;
        } catch (IOException e) {
            System.err.println("Error checking replenishment requests: " + e.getMessage());
            return false;
        }
    }


	// ensure validity
	private int getValidChoice() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    int choice = Integer.parseInt(input);
                    if (choice > 0) {
                        return choice;
                    }
                }
                System.out.println("Please enter a valid positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

	private String getValidInput(String fieldName) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(fieldName + " cannot be empty. Please try again:");
        }
    }

	private int getValidAge() {
        while (true) {
            try {
                int age = Integer.parseInt(scanner.nextLine().trim());
                if (age >= 18 && age <= 100) {
                    return age;
                }
                System.out.println("Age must be between 18 and 100. Please try again:");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number for age:");
            }
        }
    }

    private int getValidPositiveNumber(String fieldName) {
        while (true) {
            try {
                int number = Integer.parseInt(scanner.nextLine().trim());
                if (number >= 0) {
                    return number;
                }
                System.out.println(fieldName + " must be a positive number. Please try again:");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number for " + fieldName + ":");
            }
        }
    }

}
