package Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Views.UserMenu;
import Utils.CSVHandler;

public class PatientManager implements PatientInterface {
    private static PatientManager instance;
    protected static ArrayList<Patient> patientList;

    public PatientManager(){
        this.patientList = new ArrayList<>();
    }

    public static PatientManager getInstance(){
        if (instance == null){
            instance = new PatientManager();
        }
        return instance;
    }
    
    public static List<Patient> getAllPatients() {
    	return PatientManager.patientList;
    }

    public static Patient getPatient(String hospitalID) {
        String loggedInID = UserMenu.getLoggedInHospitalID(); 
        boolean isPatient = loggedInID.startsWith("P") && (loggedInID.length() == 5);
    
        if (isPatient && !loggedInID.equals(hospitalID)) {
            System.out.println("Access Denied. You may not view that profile.");
            return null;
        }
    
        for (Patient patient: patientList) {
            if (patient.getPatientID().equals(hospitalID)) {
                System.out.println("Patient profile already exists.");
                return patient;
            }
        }
        System.out.println("No matching patient profile ID.");
        return null;
    }

    public static int createPatient(String hospitalID, String password, String role, String name, LocalDate birthDate, String gender, String phoneNum, String email, String bloodType) {
        String[] headers = {"Patient ID", "Name", "Date of Birth", "Gender", "Blood Type", "Contact Information"};
        List<String> allLines = new ArrayList<>();
        for (Patient patient: patientList) {
            if (patient.getPatientID().equals(hospitalID)) {
                System.out.println("Patient profile already exists.");
                return 0;
            }
            String line = String.format("%s,%s,%s,%s,%s,%s",
                patient.getPatientID(),
                patient.getName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getBloodType(),
                patient.getPhoneNum()
            );
            allLines.add(line);
        }
        Patient newPatient = new Patient(hospitalID, password, role, name, birthDate, gender, phoneNum, email, bloodType);
        patientList.add(newPatient);
        String newLine = String.format("%s,%s,%s,%s,%s,%s",
           newPatient.getPatientID(),
           newPatient.getName(),
           newPatient.getDateOfBirth(),
           newPatient.getGender(),
           newPatient.getBloodType(),
           newPatient.getPhoneNum()
        );
        allLines.add(newLine);
        String[] lines = allLines.toArray(new String[0]);
        CSVHandler.writeCSVLines(headers, lines, "../../data/Patient_List.csv"); 
        System.out.println("Patient successfully created!");
        return 1;
    }

    public static void readPatient(String hospitalID) {
        String loggedInID = UserMenu.getLoggedInHospitalID(); 
        boolean isPatient = loggedInID.startsWith("P") && (loggedInID.length() == 5);
    
        if (isPatient && !loggedInID.equals(hospitalID)) {
            System.out.println("Access Denied. You may not view that profile.");
            return;
        }
    
        for (Patient patient: patientList) {
        if (patient.getPatientID().equals(hospitalID)) {
            System.out.println("\n====== Patient Profile ======");
            System.out.println("Patient ID: " + patient.getPatientID());
            System.out.println("Name: " + patient.getName());
            System.out.println("Date of Birth: " + patient.getDateOfBirth());
            System.out.println("Gender: " + patient.getGender());
            System.out.println("Blood Type: " + patient.getBloodType());
            System.out.println("Contact Information: " + patient.getPhoneNum());
            System.out.println("===========================\n");
            
            System.out.println("View More:");
            System.out.println("[1] Medical History");
            System.out.println("[2] Appointments");
            System.out.println("[3] Appointment Outcomes");
            System.out.println("[0] Back to Previous Menu");
            System.out.print("Enter your choice: ");
            
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            
            switch(choice) {
                case 1:
                    System.out.println("\nViewing Medical History...");
                    // Add method call to view medical history
                    break;
                case 2:
                    System.out.println("\nViewing Appointments...");
                    // Add method call to view appointments
                    break;
                case 3:
                    System.out.println("\nViewing Appointment Outcomes...");
                    // Add method call to view appointment outcomes
                    break;
                case 0:
                	scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice!");
                }
            }
        }

        System.out.println("No matching patient profile ID.");
    }

    public void updatePatient() {
        String[] headers = {"Patient ID", "Name", "Date of Birth", "Gender", "Blood Type", "Contact Information"};
        List<String> allLines = new ArrayList<>();
        for (Patient patient: patientList) {
            String line = String.format("%s,%s,%s,%s,%s,%s",
                patient.getPatientID(),
                patient.getName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getBloodType(),
                patient.getPhoneNum()
            );
            allLines.add(line);
        }
   
        String[] lines = allLines.toArray(new String[0]);
        CSVHandler.writeCSVLines(headers, lines, "../../data/Patient_List.csv"); 
        System.out.println("Patient database successfully updated!"); 
    }

    public void deletePatient(String hospitalID) {
        String[] headers = {"Patient ID", "Name", "Date of Birth", "Gender", "Blood Type", "Contact Information"};

        List<String> remainingLines = new ArrayList<>();
        boolean found = false;
        
        for (Patient patient : patientList) {
            if (!patient.getPatientID().equals(hospitalID)) {
                String line = String.format("%s,%s,%s,%s,%s,%s",
                    patient.getPatientID(),
                    patient.getName(),
                    patient.getDateOfBirth(),
                    patient.getGender(),
                    patient.getBloodType(),
                    patient.getPhoneNum()
                );
                remainingLines.add(line);
            } else {
                found = true;
                patientList.remove(patient);
            }
        }
    
        if (found) {
            String[] lines = remainingLines.toArray(new String[0]);
            CSVHandler.writeCSVLines(headers, lines, "data/Patient_List.csv"); 
            System.out.println("Successfully deleted patient profile.");
            return;}
        System.out.println("No matching patient profile ID.");
    }
}
