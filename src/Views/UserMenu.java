package Views;

import Models.UserAuthenticationManager;
import java.util.Scanner;
/**
 * Represents the user-facing menu for the Hospital Management System (HMS).
 * <p>
 * Provides login functionality, user authentication, and routing to role-specific menus
 * (e.g., Patient, Pharmacist, Doctor, or Administrator).
 * Manages the main system loop and handles password changes for users logging in
 * with a default password.
 * </p>
 *
 * <p><strong>Usage:</strong></p>
 * Instantiate the `UserMenu` class and call the `showMenu()` method to start the system.
 *
 * <p><strong>Responsibilities:</strong></p>
 * <ul>
 *     <li>Displays main menu options to users.</li>
 *     <li>Handles user login and password management.</li>
 *     <li>Routes authenticated users to appropriate role-based menus.</li>
 *     <li>Manages system exit.</li>
 * </ul>
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
public class UserMenu implements Menu {
    /**
     * Stores the currently logged-in user's hospital ID.
     * It is shared across menus to ensure proper session management.
     */
    protected static String loggedInHospitalID;

    /**
     * Indicates whether the system is running. Used to control the main menu loop.
     */
    private boolean isSystemRunning;

    /**
     * Scanner object for capturing user input.
     */
    private Scanner sc;

    /**
     * User authentication manager responsible for validating login credentials
     * and managing password changes.
     */
    private UserAuthenticationManager authManager;

     /**
     * Constructs a new UserMenu instance and initializes its fields.
     */
    public UserMenu() {
        this.isSystemRunning = true;
        this.sc = new Scanner(System.in);
        this.authManager = new UserAuthenticationManager();
    }

    /**
     * Displays the main menu and processes user choices in a loop until the system exits.
     */
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
    /**
     * Displays the main menu options to the user.
     */
    private void displayMainMenu() {
        System.out.println("\n===Welcome to HMS!===");
        System.out.println("1: Login");
        System.out.println("2: Exit System");
        System.out.print("Enter your choice: ");
    }
    /**
     * Handles the user's choice from the main menu.
     *
     * @param choice The user's menu choice.
     */
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
    /**
     * Handles the login process, including validation of credentials and
     * password management if the user is logging in with a default password.
     */
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
    /**
     * Handles a successful login by updating the session and routing the user to the appropriate menu.
     *
     * @param hospitalID      The hospital ID of the logged-in user.
     * @param currentPassword The current password of the logged-in user.
     */
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
    /**
     * Prompts the user to change their default password and updates it.
     *
     * @param hospitalID The hospital ID of the user.
     * @return {@code true} if the password was successfully changed; {@code false} otherwise.
     */
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
    /**
     * Prompts the user to enter a new password.
     *
     * @return The new password entered by the user, or {@code null} if invalid.
     */
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
    /**
     * Confirms the new password by prompting the user to re-enter it.
     *
     * @param newPassword The new password entered by the user.
     * @return {@code true} if the passwords match; {@code false} otherwise.
     */
    private boolean confirmPassword(String newPassword) {
        System.out.print("Confirm new password: ");
        String confirmPassword = sc.nextLine();
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return false;
        }
        
        return true;
    }
    /**
     * Routes the user to the appropriate role-based menu based on their hospital ID.
     *
     * @param hospitalID The hospital ID of the logged-in user.
     */
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
    /**
     * Handles the system exit process by stopping the main menu loop.
     */
    private void handleExit() {
        System.out.println("Exiting system...");
        loggedInHospitalID = null;  
        isSystemRunning = false;
        sc.close();
    }
    /**
     * Retrieves the hospital ID of the currently logged-in user.
     *
     * @return The hospital ID of the logged-in user.
     */
    public static String getLoggedInHospitalID() {
        return loggedInHospitalID.toUpperCase();
    }
}
