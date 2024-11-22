// PharmacistMenu.java
package Views;

import Models.*;
import Services.AppointmentOutcomeRecordInterface;
import java.io.*;
import java.util.Scanner;

public class PharmacistMenu implements Menu {
    private final Scanner scanner;
    private Pharmacist currentPharmacist;
    private boolean isRunning;

    public PharmacistMenu() {
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
        initializePharmacist();
        new AppointmentOutcomeRecordManager();
    }

    // Assumption that UserMenu.java already checks the person is a Pharmacist
    private void initializePharmacist() {
        try {
            String loggedInID = UserMenu.getLoggedInHospitalID();
            if (loggedInID == null) {
                throw new IllegalStateException("No user is logged in");
            }

            // Read from Staff_List.csv to get pharmacist details
            File csvFile = new File("data/Staff_List.csv");
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            String line;
            
            // Skip header if exists
            reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] staffDetails = line.split(",");
                // Check if staffID matches logged in ID and role is pharmacist
                if (staffDetails.length >= 5 && 
                    staffDetails[0].equals(loggedInID) && 
                    staffDetails[2].equalsIgnoreCase("pharmacist")) {
                    
                    // Create Pharmacist object with details from Staff_List.csv
                    this.currentPharmacist = new Pharmacist(
                        loggedInID,             // hospitalID
                        "",                     // password (already authenticated)
                        "pharmacist",           // role
                        staffDetails[1],        // name
                        staffDetails[3],        // gender
                        "data/Medicine_List.csv",
                        "data/Medication_Replenish_Requests.csv"
                    );
                    break;
                }
            }
            reader.close();

            if (currentPharmacist == null) {
                throw new IllegalStateException("Logged in user is not a pharmacist");
            }

        } catch (IOException e) {
            System.err.println("Error reading staff data: " + e.getMessage());
            System.exit(1);
        } catch (IllegalStateException e) {
            System.err.println("Authentication error: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void showMenu() {
        while (isRunning) {
            displayOptions();
            int choice = getValidChoice();
            handleMenuChoice(choice);
        }
    }

    public void displayOptions() {
        System.out.println("\nPharmacist Menu:");
        System.out.println("1: View Appointment Outcome Record");
        System.out.println("2: Update Prescription Status");
        System.out.println("3: View Medication Inventory");
        System.out.println("4: Submit Replenishment Request");
        System.out.println("5: Logout");
    }

    public boolean handleMenuChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    handleAppointmentView();
                    break;
                case 2:
                    handlePrescriptionStatus();
                    break;
                case 3:
                    currentPharmacist.viewMedicationInventory();
                    break;
                case 4:
                    handleReplenishmentRequest();
                    break;
                case 5:
                    if (currentPharmacist.logout()) {
                        isRunning = false;
                        return false;
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error processing choice: " + e.getMessage());
        }
        return true;
    }

    public boolean validateInput(String input) {
        return input != null && !input.trim().isEmpty();
    }

    private int getValidChoice() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (validateInput(input)) {
                    return Integer.parseInt(input);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }


        if (validateInput(appointmentID)) {
            boolean success = currentPharmacist.updatePrescriptionStatus(appointmentID);
            if (success) {
                int result = AppointmentOutcomeRecordInterface.updateCSV();
                if (result == 1) {
                    System.out.println("Successfully dispensed medication and updated records.");
                } else {
                    System.err.println("Error updating records.");
                }
            }
        } else {
            System.out.println("Invalid appointment ID");
        }
    }

    private void handleAppointmentView() {
        System.out.println("\nPending Prescriptions:");
        if (!currentPharmacist.viewAppointmentOutcomes()) {
            System.out.println("No prescriptions to display.");
        }
    }

    private void handleReplenishmentRequest() {
        while (true) {
            currentPharmacist.viewMedicationInventory();
            System.out.println("\nEnter medicine name to replenish (or 'exit' to return to main menu): ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            
            if (input.isEmpty()) {
                System.out.println("Medicine name cannot be empty!");
                continue;
            }

            System.out.println("Enter quantity to replenish: ");
            try {
                int quantity = Integer.parseInt(scanner.nextLine().trim());
                if (quantity <= 0) {
                    System.out.println("Quantity must be positive");
                    continue;
                }
                currentPharmacist.requestMedicationReplenishment(input, quantity);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
    }
}