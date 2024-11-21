package Views;

import java.io.*;
import java.util.Scanner;

import Models.AppointmentManager;

public class UserMenu implements Menu {
    protected static String loggedInHospitalID;
    private boolean isSystemRunning;
    private Scanner sc;

    public UserMenu() {
        this.isSystemRunning = true;
        this.sc = new Scanner(System.in);
    }

    @Override
    public void showMenu() {
        while (isSystemRunning) {
            System.out.println("\n===Welcome to HMS!===");
            System.out.println("1: Login");
            System.out.println("2: Exit System");
            System.out.print("Enter your choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        handleLogin();
                        break;
                    case 2:
                        System.out.println("Exiting system...");
                        isSystemRunning = false;
                        sc.close();
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
                sc.nextLine();
            }
        }
    }

    private void handleLogin() {
        File csvFile = new File("data/User_List.csv");
        String line;
        boolean isAuthenticated = false;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(csvFile))) {
            System.out.print("Enter your Hospital ID: ");
            String hospitalID = sc.next();
            System.out.print("Enter your password: ");
            String password = sc.next();
            sc.nextLine();

            while ((line = fileReader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length >= 2) {
                    String storedHospitalID = userDetails[0].trim();
                    String storedPassword = userDetails[1].trim();
                    if (hospitalID.equalsIgnoreCase(storedHospitalID) && 
                        password.equalsIgnoreCase(storedPassword)) {
                        isAuthenticated = true;
                        loggedInHospitalID = hospitalID;
                        break;
                    }
                }
            }

            if (isAuthenticated) {
                System.out.println("===Login successful!===\nWelcome, " + hospitalID);
                routeToAppropriateMenu(hospitalID);
                loggedInHospitalID = null;
            } else {
                System.out.println("Invalid Hospital ID or Password. Please try again.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    private void routeToAppropriateMenu(String hospitalID) {
        String hospitalIDUpper = hospitalID.toUpperCase();
        
        if (hospitalIDUpper.startsWith("P") && (hospitalID.length() == 5)) {
            System.out.println("Showing Patient Menu");
            new PatientMenu().showMenu();
        } else if (hospitalIDUpper.startsWith("P") && (hospitalID.length() == 4)) {
            new PharmacistMenu().showMenu();
        } else if (hospitalIDUpper.startsWith("D")) {
            new DoctorMenu().showMenu();
        } else if (hospitalIDUpper.startsWith("A")) {
            new AdministratorMenu().showMenu();
        }
    }

    public static String getLoggedInHospitalID() {
        return loggedInHospitalID;
    }
}