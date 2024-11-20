
package Views;

import Models.Administrator;
import Models.Staff;
import java.io.BufferedReader;
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

    //initialises an administrator object with the info from staff list csv
	private void initializeAdministrator() {
        try {
            String loggedInID = UserMenu.getLoggedInHospitalID();
            
            // get administrator details
            BufferedReader reader = new BufferedReader(new FileReader("data/Staff_List.csv"));
            reader.readLine(); // Skip header
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] staffDetails = line.split(",");
                if (staffDetails[0].equals(loggedInID)) {
                    this.currentAdmin = new Administrator(
                        loggedInID,       //hospital id
                        "",     // password not needed
                        "administrator",
                        staffDetails[1], // name
                        staffDetails[3]  // gender
                    );
                    break;
                }
            }
            reader.close();
            
        } catch (IOException e) {
            System.err.println("Error reading staff data: " + e.getMessage());
        }
    }

	@Override
    public void showMenu() {
        while (isRunning) {
            System.out.println("\nAdministrator Menu:");
            System.out.println("1: View and Manage Hospital Staff"); // Administrator Class  
            System.out.println("2: View Appointments Details");
            // Administrator Class
                // AppointmentManager: Fetch appointment by APT ID/Patient ID/Doctor ID
            System.out.println("3: View and Manage Medication Inventory");
            // Administrator Class
                // MedicationInventoryManager
            System.out.println("4: Approve Replenishment Requests");
                        // Administrator Class
                // MedicationInventoryManager
            System.out.println("5: Logout");
            System.out.print("Enter your choice (1-5): ");

            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    input = scanner.nextLine().trim();
                }
                
                int choice = Integer.parseInt(input);
                
                switch (choice) {
                    case 1:
                        handleStaffManagement();
                        break;
                    case 2:
                        currentAdmin.viewAppointmentDetails();
                        break;
                    case 3:
                        handleInventoryManagement();
                        break;
                    case 4:
                        handleReplenishmentApproval();
                        break;
                    case 5:
                        if (currentAdmin.logout()) {
                            isRunning = false;
                        }
                        break;
                    default:
                        System.out.println("Invalid choice! Please enter a number between 1 and 5.");
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
        System.out.println("4: Reset user password");
        System.out.println("5: Return to main menu");
        System.out.print("Enter your choice (1-5): ");
    }

	private void displayInventoryMenu() {
        System.out.println("\nInventory Management Options:");
        System.out.println("1: Add new medication to inventory");
        System.out.println("2: Remove medication from inventory");
        System.out.println("3: Edit inventory quantities");
        System.out.println("4: Return to main menu");
    }

    private void handleStaffManagement() {
        currentAdmin.viewStaffList();
        boolean managing = true;

        while (managing) {
            displayStaffMenu();
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    input = scanner.nextLine().trim();
                }
                
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1: 
                        handleAddStaff();
                        break;
                    case 2:
                        handleRemoveStaff();
                        break;
                    case 3:
                        handleUpdateStaff();
                        break;
                    case 4:
                        handleUpdatePassword();
                        break;
                    case 5:
                        managing = false;
                        break;
                    default:
                        System.out.println("Invalid choice! Please enter a number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private void handleAddStaff() {
        System.out.println("Enter new staff role (Doctor/Pharmacist/Administrator): ");
        String role = getValidInput("Role");
        
        System.out.println("Enter new staff name: ");
        String name = getValidInput("Name");
        
        System.out.println("Enter new staff age: ");
        int age = getValidPositiveNumber("Age");
    
        System.out.println("Select new staff gender: ");
        System.out.println("1: Male");
        System.out.println("2: Female");
        int genderOption = Integer.parseInt(scanner.nextLine().trim());
        String gender = switch (genderOption) {
            case 1 -> "Male";
            case 2 -> "Female";
            default -> throw new IllegalArgumentException("Invalid gender option");
        };
    
        currentAdmin.addStaff(name, role, gender, age);
    }

    private void handleRemoveStaff() {
        System.out.println("Enter staff ID to remove: ");
        String staffID = getValidInput("Staff ID");
        currentAdmin.removeStaff(staffID);
    }

    private void handleUpdateStaff() {
        // Display current staff list
        currentAdmin.viewStaffList();
        
        // Get staff ID to update
        System.out.print("\nEnter Staff ID to update: ");
        String staffId = getValidInput("Staff ID");
        
        // Get current staff details
        Staff currentStaff = currentAdmin.getStaffById(staffId);
        
        if (currentStaff == null) {
            System.out.println("Staff not found!");
            return;
        }

        // Show update options menu
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Role");
        System.out.println("3. Gender");
        System.out.println("4. Age");
        System.out.println("5. Cancel");
        
        System.out.print("Enter your choice (1-5): ");
        String choice = scanner.nextLine();
        
        String newName = currentStaff.getName();
        String newRole = currentStaff.getRole();
        String newGender = currentStaff.getGender();
        int newAge = currentStaff.getAge();
        
        switch (choice) {
            case "1":
                System.out.print("Enter new name: ");
                newName = getValidInput("Name");
                break;
                
            case "2":
                System.out.print("Enter new role: ");
                newRole = getValidInput("Role");
                break;
                
            case "3":
                System.out.print("Enter new gender: ");
                newGender = getValidInput("Gender");
                break;
                
            case "4":
                System.out.print("Enter new age: ");
                newAge = getValidPositiveNumber("Age");
                break;
                
            case "5":
                System.out.println("Update cancelled.");
                return;
                
            default:
                System.out.println("Invalid choice! Update cancelled.");
                return;
        }
        
        // Call the update method
        if (currentAdmin.updateStaff(staffId, newName, newRole, newGender, newAge)) {
            System.out.println("Staff updated successfully!");
        } else {
            System.out.println("Failed to update staff.");
        }
    }

    private void handleUpdatePassword() {
        System.out.println("\nPassword Reset Menu");
        System.out.println("--------------------");
        System.out.println("Hospital ID Format:");
        System.out.println("Staff: D*** (Doctors), P*** (Pharmacists), A*** (Administrators)");
        System.out.println("Patients: P**** (Regular patients)");
        System.out.println("--------------------");
        
        System.out.print("Enter Hospital ID to reset password: ");
        String hospitalId = getValidInput("Hospital ID");
        
        String currentPassword = currentAdmin.getUserPassword(hospitalId);
        if (currentPassword == null) {
            System.out.println("User not found!");
            return;
        }
        
        System.out.print("Enter new password: ");
        String newPassword = getValidInput("Password");
        
        if (currentAdmin.updateUserPassword(hospitalId, newPassword)) {
            System.out.println("Password reset successfully for user: " + hospitalId);
            System.out.println("New password: " + newPassword);
        } else {
            System.out.println("Failed to reset password.");
        }
    }


    private void handleInventoryManagement() {
        currentAdmin.viewMedicationInventory();
        boolean managing = true;

        while (managing) {
            displayInventoryMenu();
            int choice = getValidChoice();

            switch (choice) {
                case 1:
                    handleAddMedication();
                    break;
                case 2:
                    handleRemoveMedication();
                    break;
                case 3:
                    handleUpdateMedication();
                    break;
                case 4:
                    managing = false;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
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
                case 1:
                    System.out.println("Enter new stock level: ");
                    int newStock = getValidPositiveNumber("Stock level");
                    if (currentAdmin.updateMedicationStock(medName, newStock, null)) {
                        System.out.println("Stock level updated successfully!");
                    }
                    break;
                case 2:
                    System.out.println("Enter new low stock threshold: ");
                    int newThreshold = getValidPositiveNumber("Threshold");
                    if (currentAdmin.updateMedicationStock(medName, null, newThreshold)) {
                        System.out.println("Threshold updated successfully!");
                    }
                    break;
                default:
                    System.out.println("Invalid choice!");
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