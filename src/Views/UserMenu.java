package Views;
import java.util.Scanner;
import java.io.*;

public class UserMenu implements Menu {
    protected static String loggedInHospitalID;

    public void showMenu(){
        File csvFile = new File("data/User_List.csv");
        String line;
        boolean isAuthenticated = false;        

        try (BufferedReader fileReader = new BufferedReader(new FileReader(csvFile))) {
            Scanner sc = new Scanner(System.in);

            System.out.print("Enter your Hospital ID: ");
            String hospitalID = sc.next();

            System.out.print("Enter your password: ");
            String password = sc.next();

            while ((line = fileReader.readLine()) != null) {
                String[] userDetails = line.split(",");  

                if (userDetails.length >= 2) {
                    String storedHospitalID = userDetails[0].trim();
                    String storedPassword = userDetails[1].trim();

                    if (hospitalID.equalsIgnoreCase(storedHospitalID) && password.equalsIgnoreCase(storedPassword)) {
                        isAuthenticated = true;
                        loggedInHospitalID = hospitalID;
                        break;
                    }
                }
            }

            if (isAuthenticated) {
                System.out.println("Login successful! Welcome, " + hospitalID);

                if (hospitalID.startsWith("P") && (hospitalID.length() == 5)){
                    System.out.println("Showing Patient Menu");
                    PatientMenu patientMenu = new PatientMenu();
				    patientMenu.showMenu();
                } else if (hospitalID.startsWith("P") && (hospitalID.length() == 4)){
                    PharmacistMenu pharmacistMenu = new PharmacistMenu();
                    pharmacistMenu.showMenu();
                } else if (hospitalID.startsWith("D")){
                    DoctorMenu doctorMenu = new DoctorMenu();
                    doctorMenu.showMenu();
                } else if (hospitalID.startsWith("A")) {
                    AdministratorMenu administratorMenu = new AdministratorMenu();
                    administratorMenu.showMenu();
                }


            } else {
                System.out.println("Invalid Hospital ID or Password. Please try again.");
            }

            sc.close();  

        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        return;

    }

    // Method to get logged-in user's ID
    public static String getLoggedInHospitalID() {
        return loggedInHospitalID;
    }
    
}
