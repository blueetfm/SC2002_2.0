package Models;

import Services.MedicalRecordInterface;
import Services.PatientInterface;
import Utils.CSVHandler;
import Views.UserMenu;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PatientManager implements PatientInterface {
    private static PatientManager instance;
    protected static List<Patient> patientList;

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
        PatientManager.patientList=PatientManager.initializeObjects();
        System.out.println(patientList);

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
            System.out.println("Medical Records: " + MedicalRecordInterface.readMedicalRecordsByPatientID(patient.getPatientID()));
            System.out.println("===========================\n");
        }
        }
    }

    public static List<Patient> initializeObjects() {
        // Initialize the list to store patient objects
        List<Patient> patients = new ArrayList<>();
        
        // Read the CSV file into a List of List of Strings (rows and columns)
        List<List<String>> allLines = CSVHandler.readCSVLines("data/Patient_List.csv");
    
        if (allLines.isEmpty()) {
            System.out.println("No records found in Patient_List.csv.");
            return patients;
        }
    
        // Skip the header row (assuming the first row contains headers)
        for (int i = 1; i < allLines.size(); i++) {
            List<String> row = allLines.get(i);
    
            try {
                // Ensure the row has the expected number of columns
                if (row.size() < 7) {
                    System.err.println("Skipping incomplete record at line " + (i + 1) + ": " + String.join(",", row));
                    continue;
                }
    
                // Parse each column into its corresponding field
                String patientID = row.get(0).trim().toUpperCase();
                String name = row.get(1).trim();
                LocalDate dateOfBirth = LocalDate.parse(row.get(2).trim()); // Ensure date format matches
                String gender = row.get(3).trim();
                String bloodType = row.get(4).trim();
                String email = row.get(5).trim();
                String phoneNum = row.get(6).trim();
    
                // Create a Patient object and add it to the list
                Patient patient = new Patient(patientID, name, dateOfBirth, gender, bloodType, email, phoneNum);
                patients.add(patient);
    
            } catch (Exception e) {
                // Handle any errors during parsing
                System.err.println("Error parsing record at line " + (i + 1) + ": " + e.getMessage());
                System.err.println("Record content: " + String.join(",", row));
            }
        }
    
        return patients;
    }
    

    public static int updatePatient(Patient updatedPatient) {
        String[] headers = {"Patient ID", "Name", "Date of Birth", "Gender", "Blood Type", "Email", "Contact Information"};
        
        // Read the CSV file into a List of List of Strings (representing rows and columns)
        List<List<String>> allLines = CSVHandler.readCSVLines("data/Patient_List.csv");
        boolean patientUpdated = false;
    
        // Loop through all lines
        for (int i = 0; i < allLines.size(); i++) {
            List<String> row = allLines.get(i);
    
            // Ensure we're checking for 7 columns and the Patient ID is the first column
            if (row.get(0).equals(updatedPatient.getPatientID().toUpperCase())) {
                // Update the row with the new patient details
                row.set(0, updatedPatient.getPatientID().toUpperCase());
                row.set(1, updatedPatient.getName());
                row.set(2, updatedPatient.getDateOfBirth().toString());
                row.set(3, updatedPatient.getGender());
                row.set(4, updatedPatient.getBloodType());
                row.set(5, updatedPatient.getEmail()); // Update email
                row.set(6, updatedPatient.getPhoneNum()); // Update contact info
                patientUpdated = true;
                break;
            }
        }
    
        if (!patientUpdated) {
            System.out.println("Patient with ID " + updatedPatient.getPatientID() + " not found.");
            return 0;
        }
    
        // Convert List<List<String>> to String[] array for writeCSVLines method
        List<String> flattenedLines = new ArrayList<>();
        for (List<String> row : allLines) {
            flattenedLines.add(String.join(",", row));  // Convert each row (List<String>) to a single comma-separated String
        }
    
        // Convert the list to a String[] array
        String[] allLinesArray = flattenedLines.toArray(new String[0]);
        
        String[] pls_lah={};
        // Write the updated data back to the CSV (without headers being overwritten)
        CSVHandler.writeCSVLines(pls_lah, allLinesArray, "data/Patient_List.csv");
        System.out.println("Patient database successfully updated!\n");
        return 1;
    }
    
    

    public static int deletePatient(String hospitalID) {
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
            return 1;}
        System.out.println("No matching patient profile ID.");
        return 0;
    }
}
