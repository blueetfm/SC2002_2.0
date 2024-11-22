
package Views;

import Enums.AppointmentStatus;
import Models.Administrator;
import Models.Staff;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
/**
 * The {@code AdministratorMenu} class provides a user interface for administrators
 * to manage various aspects of a hospital management system, including staff,
 * appointments, inventory, and password resets.
 * <p>
 * This menu interacts with the {@code Administrator} model to perform tasks
 * such as viewing staff lists, handling appointments, and managing medication inventory.
 * It implements the {@code Menu} interface.
 * </p>
 *
 * <p>
 * The menu options include:
 * <ul>
 *     <li>View and manage hospital staff</li>
 *     <li>View appointment details</li>
 *     <li>Manage medication inventory</li>
 *     <li>Approve replenishment requests</li>
 *     <li>Reset user passwords</li>
 *     <li>Logout</li>
 * </ul>
 * </p>
 *
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 * @see Models.Administrator
 * @see Enums.AppointmentStatus
 */
public class AdministratorMenu implements Menu {
	/**
     * Scanner object for reading user input.
     */
    private final Scanner scanner;

    /**
     * Current administrator logged into the system.
     */
    private Administrator currentAdmin;

    /**
     * Flag to control the running state of the menu.
     */
    private boolean isRunning;
    /**
     * Constructs an {@code AdministratorMenu} instance.
     * Initializes the menu and sets up the current administrator based on login credentials.
     */
	public AdministratorMenu() {
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
        initializeAdministrator();
    }
    /**
     * Initializes the administrator by retrieving details from the staff data file.
     */
	private void initializeAdministrator() {
        try {
            String loggedInID = UserMenu.getLoggedInHospitalID();
            
            BufferedReader reader = new BufferedReader(new FileReader("data/Staff_List.csv"));
            reader.readLine();
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] staffDetails = line.split(",");
                if (staffDetails[0].equals(loggedInID)) {
                    this.currentAdmin = new Administrator(
                        loggedInID,       //hospital id
                        "",
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
    /**
     * Displays the main menu for the administrator and handles user input for menu options.
     */
	@Override
    public void showMenu() {
        while (isRunning) {
            System.out.println("\nAdministrator Menu:");
            System.out.println("1: View and Manage Hospital Staff");
            System.out.println("2: View Appointments Details");
            System.out.println("3: View and Manage Medication Inventory");
            System.out.println("4: Approve Replenishment Requests");
            System.out.println("5: Reset User Password");
            System.out.println("6: Logout");
            System.out.print("Enter your choice (1-6): ");

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
                        handleViewAppointments();
                        break;
                    case 3:
                        handleInventoryManagement();
                        break;
                    case 4:
                        handleReplenishmentApproval();
                        break;
                    case 5:
                        handleUpdatePassword();
                        break;
                    case 6:
                        if (currentAdmin.logout()) {
                            isRunning = false;
                        }
                        break;
                    default:
                        System.out.println("Invalid choice! Please enter a number between 1 and 6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    /**
     * Display choices for making changes to staff list.
     */
	private void displayStaffMenu() {
        System.out.println("\nStaff Management Options:");
        System.out.println("1: Add new staff");
        System.out.println("2: Remove staff");
        System.out.println("3: Update staff details");
        System.out.println("4: Return to main menu");
        System.out.print("Enter your choice (1-4): ");
    }
    /**
     * Display choices for viewing Inventory Menu.
     */
	private void displayInventoryMenu() {
        System.out.println("\nInventory Management Options:");
        System.out.println("1: Add new medication to inventory");
        System.out.println("2: Remove medication from inventory");
        System.out.println("3: Edit inventory quantities");
        System.out.println("4: Return to main menu");
    }
    /**
     * Displays the menu for managing hospital staff and handles related user input.
     */
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
                        managing = false;
                        break;
                    default:
                        System.out.println("Invalid choice! Please enter a number between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    /**
     * Adds a new staff member to the hospital system.
     */
    private void handleAddStaff() {
        try {
            System.out.println("Enter new staff role (Doctor/Pharmacist/Administrator): ");
            String role = getValidInput("Role");
            
            System.out.println("Enter new staff name: ");
            String name;
        while (true) {
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                String[] words = name.split("\\s+");
                StringBuilder capitalizedNames = new StringBuilder();
                
                for (String word : words) {
                    if (!word.isEmpty()) {
                        capitalizedNames.append(word.substring(0, 1).toUpperCase())
                                         .append(word.substring(1).toLowerCase())
                                         .append(" ");
                    }
                }

                name = capitalizedNames.toString().trim();
                break; 
            } else {
                System.out.println("Name cannot be empty. Please enter a valid name: ");
            }
        }

            
            System.out.println("Enter new staff age: ");
            int age = getValidPositiveNumber("Age");
        
            System.out.println("Select new staff gender: ");
            System.out.println("1: Male");
            System.out.println("2: Female");
            int genderOption = Integer.parseInt(scanner.nextLine().trim());
            String gender;
            switch (genderOption) {
                case 1:
                    gender = "Male";
                    break;
                case 2:
                    gender = "Female";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid gender option");
            }    
            
            boolean success = currentAdmin.addStaff(name, role, gender, age);
            if (success) {
                System.out.println("\nStaff member added successfully!");
                currentAdmin.viewStaffList();
            } else {
                System.out.println("\nFailed to add staff member.");
            }
        } catch (Exception e) {
            System.err.println("Error adding staff: " + e.getMessage());
        }
    }
    /**
     * Removes an existing staff member from the hospital system.
     */
    private void handleRemoveStaff() {
        System.out.println("Enter staff ID to remove: ");
        String staffID = getValidInput("Staff ID");
        currentAdmin.removeStaff(staffID);
    }
    /**
     * Updates the details of an existing staff member.
     */
    private void handleUpdateStaff() {
        // show current staff list
        currentAdmin.viewStaffList();
        
        // Get staffid
        System.out.print("\nEnter Staff ID to update: ");
        String staffId = getValidInput("Staff ID");
        
        // Get current staff details
        Staff currentStaff = currentAdmin.getStaffByID(staffId);
        
        if (currentStaff == null) {
            System.out.println("Staff not found!");
            return;
        }

        // Show update menu
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
        
        if (currentAdmin.updateStaff(staffId, newName, newRole, newGender, newAge)) {
            System.out.println("Staff updated successfully!");
        } else {
            System.out.println("Failed to update staff.");
        }
    }
    /**
     * Resets the password for a user in the hospital system.
     */
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

    /**
     * Displays appointment details and provides options to filter appointments
     * based on criteria such as doctor ID, patient ID, or status.
     */
    private void handleViewAppointments() {
        while (true) {
            System.out.println("\nView Appointments By:");
            System.out.println("1: View All Appointments");
            System.out.println("2: View by Doctor ID");
            System.out.println("3: View by Patient ID");
            System.out.println("4: View by Status");
            System.out.println("5: Return to Main Menu");
            System.out.print("Enter your choice (1-5): ");
    
            try {
                int choice = getValidChoice();
                
                switch (choice) {
                    case 1:
                        currentAdmin.viewAllAppointments();
                        break;
                        
                    case 2:
                        System.out.print("Enter Doctor ID: ");
                        String doctorID = getValidInput("Doctor ID");
                        currentAdmin.viewAppointmentsByDoctorID(doctorID);
                        break;
                        
                    case 3:
                        System.out.print("Enter Patient ID: ");
                        String patientID = getValidInput("Patient ID");
                        currentAdmin.viewAppointmentsByPatientID(patientID);
                        break;
                        
                    case 4:
                        handleViewByStatus();
                        break;
                        
                    case 5:
                        return;
                        
                    default:
                        System.out.println("Invalid choice! Please enter a number between 1 and 5.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    /**
     * Displays appointments filtered by their status.
     */
    private void handleViewByStatus() {
        System.out.println("\nSelect Status:");
        System.out.println("1: Pending");
        System.out.println("2: Confirmed");
        System.out.println("3: Completed");
        System.out.println("4: Cancelled");
        System.out.print("Enter choice (1-4): ");

        try {
            int choice = getValidChoice();
            AppointmentStatus status;
            
            switch (choice) {
                case 1:
                    status = AppointmentStatus.PENDING;
                    break;
                case 2:
                    status = AppointmentStatus.CONFIRMED;
                    break;
                case 3:
                    status = AppointmentStatus.COMPLETED;
                    break;
                case 4:
                    status = AppointmentStatus.CANCELLED;
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }
            
            currentAdmin.viewAppointmentsByStatus(status);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Manages medication inventory by providing options to add, remove, or update inventory items.
     */
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
    /**
     * Adds a new medication to the inventory.
     */
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
    /**
     * Removes a medication from the inventory.
     */
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
    /**
     * Updates the quantity or other details of a medication in the inventory.
     */
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
    /**
     * Handles the approval process for medicine replenishment requests.
     * Displays pending replenishment requests and allows the admin to approve a specific request.
     * If no requests are available, it notifies the admin and exits.
     */
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

    /**
     * Checks if there are any pending replenishment requests.
     * Reads the replenishment requests file and determines if it contains any entries.
     * 
     * @return {@code true} if there are pending requests, {@code false} otherwise.
     */
    private boolean hasAnyReplenishRequests() {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/Medication_Replenish_Requests.csv"))) {
            reader.readLine();
            return reader.readLine() != null;
        } catch (IOException e) {
            System.err.println("Error checking replenishment requests: " + e.getMessage());
            return false;
        }
    }


	/**
     * Prompts the user to enter a valid positive integer.
     * Keeps prompting until the user provides a valid input.
     * 
     * @return A valid positive integer entered by the user.
     */
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
    /**
     * Prompts the user to provide a non-empty input for a specified field.
     * Keeps prompting until a valid input is provided.
     * 
     * @param fieldName The name of the field for which input is being collected.
     * @return A valid non-empty string entered by the user.
     */
	private String getValidInput(String fieldName) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(fieldName + " cannot be empty. Please try again:");
        }
    }
    /**
     * Prompts the user to enter a valid positive number for a specified field.
     * Keeps prompting until the user provides a valid number.
     * 
     * @param fieldName The name of the field for which input is being collected.
     * @return A valid positive number entered by the user.
     */
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