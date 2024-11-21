package Views;

import Models.UserAuthenticationManager;
import java.util.Scanner;

public class UserMenu implements Menu {
    protected static String loggedInHospitalID;
    private boolean isSystemRunning;
    private Scanner sc;
    private UserAuthenticationManager authManager;

    public UserMenu() {
        this.isSystemRunning = true;
        this.sc = new Scanner(System.in);
        this.authManager = new UserAuthenticationManager();
    }

    @Override
    public void showMenu() {
        while (isSystemRunning) {
            displayMainMenu();
            try {
                int choice = sc.nextInt();
                sc.nextLine();
                handleMainMenuChoice(choice);
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
                sc.nextLine();
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n===Welcome to HMS!===");
        System.out.println("1: Login");
        System.out.println("2: Exit System");
        System.out.print("Enter your choice: ");
    }

    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleExit();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private void handleLogin() {
        try {
            System.out.print("Enter your Hospital ID: ");
            String hospitalID = sc.next();
            System.out.print("Enter your password: ");
            String password = sc.next();
            sc.nextLine();

            String currentPassword = authManager.getUserPassword(hospitalID);
            
            if (currentPassword != null && password.equals(currentPassword)) {
                handleSuccessfulLogin(hospitalID, currentPassword);
            } else {
                System.out.println("Invalid Hospital ID or Password. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred during login: " + e.getMessage());
        }
    }

    private void handleSuccessfulLogin(String hospitalID, String currentPassword) {
        loggedInHospitalID = hospitalID;
        
        if (authManager.isDefaultPassword(currentPassword)) {
            if (!handlePasswordChange(hospitalID)) {
                System.out.println("Failed to change default password. Please try again later.");
                return;
            }
        }
        
        System.out.println("===Login successful!===\nWelcome, " + hospitalID);
        routeToAppropriateMenu(hospitalID);
        loggedInHospitalID = null;
    }

    private boolean handlePasswordChange(String hospitalID) {
        System.out.println("\nYou must change your default password.");
        
        while (true) {
            String newPassword = promptForNewPassword();
            if (newPassword == null) continue;
            
            if (confirmPassword(newPassword)) {
                return authManager.updatePassword(hospitalID, newPassword);
            }
        }
    }

    private String promptForNewPassword() {
        System.out.print("Enter new password: ");
        String newPassword = sc.nextLine();
        
        if (authManager.isDefaultPassword(newPassword)) {
            System.out.println("New password cannot be the default password.");
            return null;
        }
        
        if (newPassword.trim().isEmpty()) {
            System.out.println("Password cannot be empty.");
            return null;
        }
        
        return newPassword;
    }

    private boolean confirmPassword(String newPassword) {
        System.out.print("Confirm new password: ");
        String confirmPassword = sc.nextLine();
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return false;
        }
        
        return true;
    }

    private void routeToAppropriateMenu(String hospitalID) {
        String hospitalIDUpper = hospitalID.toUpperCase();
        Menu nextMenu = null;

        if (hospitalIDUpper.startsWith("P") && (hospitalID.length() == 5)) {
            nextMenu = new PatientMenu();
        } else if (hospitalIDUpper.startsWith("P") && (hospitalID.length() == 4)) {
            nextMenu = new PharmacistMenu();
        } else if (hospitalIDUpper.startsWith("D")) {
            nextMenu = new DoctorMenu();
        } else if (hospitalIDUpper.startsWith("A")) {
            nextMenu = new AdministratorMenu();
        }

        if (nextMenu != null) {
            nextMenu.showMenu();
        }
    }

    private void handleExit() {
        System.out.println("Exiting system...");
        isSystemRunning = false;
        sc.close();
    }

    public static String getLoggedInHospitalID() {
        return loggedInHospitalID;
    }
}