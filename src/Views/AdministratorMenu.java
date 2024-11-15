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

            File csvFile = new File("data/Staff_List.csv");
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] staffDetails = line.split(",");
                if (staffDetails.length >= 5 &&
                    staffDetails[0].equals(loggedInID) &&
                    staffDetails[2].equalsIgnoreCase("administrator")) {

                    this.currentAdmin = new Administrator(
                        loggedInID,
                        "",
                        "administrator",
                        staffDetails[1],
                        staffDetails[3]
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
                    case 1:
                        handleStaffManagement();
                        break;
                    case 2:
                        currentAdmin.viewAppointmentDetails();
                        break;
                    case 3:
                        // handleInventoryManagement();
                        break;
                    case 4:
                        // handleReplenishmentApproval();
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

    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                handleStaffManagement();
                break;
            case 2:
                currentAdmin.viewAppointmentDetails();
                break;
            case 3:
                // handleInventoryManagement();
                break;
            case 4:
                // handleReplenishmentApproval();
                break;
            case 5:
                if (currentAdmin.logout()) {
                    isRunning = false;
                }
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private void handleStaffManagement() {
        currentAdmin.viewStaffList();
        boolean managing = true;

        while (managing) {
            // displayStaffMenu();
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    input = scanner.nextLine().trim();
                }

                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
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

                        currentAdmin.addStaff(staffID, name, role, gender, age);
                        break;
                    case 2:
                        System.out.println("Enter staff ID to remove: ");
                        String removeStaffID = scanner.nextLine().trim();
                        currentAdmin.removeStaff(removeStaffID);
                        break;
                    case 3:
                        currentAdmin.updateStaffDetails();
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
}
