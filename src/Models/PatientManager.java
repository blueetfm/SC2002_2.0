package Models;

import Services.MedicalRecordInterface;
import Services.PatientInterface;
import Utils.CSVHandler;
import Views.UserMenu;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the patient profiles in the system.
 * This class handles CRUD operations on the patient database, 
 * such as retrieving, creating, updating, and deleting patient profiles.
 * It also manages the loading and saving of patient data to a CSV file.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
public class PatientManager implements PatientInterface {
    private static PatientManager instance;
    protected static List<Patient> patientList;

    /**
     * Constructs a new PatientManager instance and initializes the patient list.
     */
    public PatientManager(){
        this.patientList = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of the PatientManager.
     * If the instance does not exist, it creates a new one.
     *
     * @return the singleton instance of PatientManager
     */
    public static PatientManager getInstance(){
        if (instance == null){
            instance = new PatientManager();
        }
        return instance;
    }
    
    /**
     * Retrieves all patients from the database and initializes the patient list.
     * This method fetches data from the CSV file and returns the list of patients.
     *
     * @return a list of all patients
     */
    public static List<Patient> getAllPatients() {
        PatientManager.patientList = PatientManager.initializeObjects();
        System.out.println(patientList);
        return PatientManager.patientList;
    }

    /**
     * Retrieves a patient profile based on the hospital ID.
     * Access is restricted if the logged-in user is a patient and attempts to view another patient's profile.
     *
     * @param hospitalID the ID of the patient to retrieve
     * @return the patient object if found, or null if no match is found or access is denied
     */
    public static Patient getPatient(String hospitalID) {
        List<Patient> patientList = getAllPatients();
        String loggedInID = UserMenu.getLoggedInHospitalID(); 
        boolean isPatient = loggedInID.startsWith("P") && loggedInID.length() == 5;
        boolean isPharmacist = loggedInID.startsWith("P") && loggedInID.length() == 4;
        boolean isDoctor = loggedInID.startsWith("D");
        boolean isAdministrator = loggedInID.startsWith("A");
    
        if (isPatient && !loggedInID.equals(hospitalID)) {
            System.out.println("Access Denied. You may not view that profile.");
            return null;
        } else if (isPharmacist || isDoctor || isAdministrator || isPatient && loggedInID.equals(hospitalID)){
            for (Patient patient : patientList) {
                if (patient.getPatientID().equals(hospitalID)) {
                    System.out.println("Patient profile exists.");
                    return patient;
                }
            }  
        }
        System.out.println("No matching patient profile ID.");
        return null;
    
        
    }

    /**
     * Creates a new patient profile and stores it in the system.
     * If a patient with the same ID already exists, it prevents creating a new profile.
     * The patient data is saved to the patient list and written to the CSV file.
     *
     * @param hospitalID the ID of the new patient
     * @param password the password for the new patient
     * @param role the role of the new patient
     * @param name the name of the new patient
     * @param birthDate the birth date of the new patient
     * @param gender the gender of the new patient
     * @param phoneNum the phone number of the new patient
     * @param email the email of the new patient
     * @param bloodType the blood type of the new patient
     * @return 1 if the patient was created successfully, 0 if the patient already exists
     */
    public static int createPatient(String hospitalID, String password, String role, String name, LocalDate birthDate, String gender, String phoneNum, String email, String bloodType) {
        List<Patient> patientList = getAllPatients();
        String[] headers = {"Patient ID", "Name", "Date of Birth", "Gender", "Blood Type", "Contact Information"};
        List<String> allLines = new ArrayList<>();
        for (Patient patient : patientList) {
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

    /**
     * Reads a patient profile and displays their details.
     * Access is restricted if the logged-in user is a patient and attempts to view another patient's profile.
     *
     * @param hospitalID the ID of the patient to read
     */
    public static void readPatient(String hospitalID) {
        String loggedInID = UserMenu.getLoggedInHospitalID(); 
        boolean isPatient = loggedInID.startsWith("P") && (loggedInID.length() == 5);
    
        if (isPatient && !loggedInID.equals(hospitalID)) {
            System.out.println("Access Denied. You may not view that profile.");
            return;
        }
    
        for (Patient patient : patientList) {
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

    /**
     * Initializes the patient list by reading data from the CSV file.
     * This method populates the patient list with Patient objects created from the CSV file content.
     *
     * @return a list of Patient objects initialized from the CSV file
     */
    public static List<Patient> initializeObjects() {
        List<Patient> patients = new ArrayList<>();
        
        List<List<String>> allLines = CSVHandler.readCSVLines("data/Patient_List.csv");
    
        if (allLines.isEmpty()) {
            System.out.println("No records found in Patient_List.csv.");
            return patients;
        }
    
        for (int i = 1; i < allLines.size(); i++) {
            List<String> row = allLines.get(i);
    
            try {
                if (row.size() < 7) {
                    System.err.println("Skipping incomplete record at line " + (i + 1) + ": " + String.join(",", row));
                    continue;
                }
    
                String patientID = row.get(0).trim().toUpperCase();
                String name = row.get(1).trim();
                LocalDate dateOfBirth = LocalDate.parse(row.get(2).trim());
                String gender = row.get(3).trim();
                String bloodType = row.get(4).trim();
                String email = row.get(5).trim();
                String phoneNum = row.get(6).trim();
    
                Patient patient = new Patient(patientID, name, dateOfBirth, gender, bloodType, email, phoneNum);
                patients.add(patient);
    
            } catch (Exception e) {
                System.err.println("Error parsing record at line " + (i + 1) + ": " + e.getMessage());
                System.err.println("Record content: " + String.join(",", row));
            }
        }
    
        return patients;
    }

    /**
     * Updates the details of an existing patient in the database.
     * The updated patient data is written back to the CSV file.
     *
     * @param updatedPatient the patient object with updated details
     * @return 1 if the patient was successfully updated, 0 if the patient was not found
     */
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
        System.out.println("Patient database successfully updated!");
        return 1;
    }
    
    
    /**
    * Deletes a patient profile from the patient list and the corresponding CSV file.
    * <p>
    * The method searches for a patient with the given hospital ID. If the patient is found,
    * the profile is removed from the internal list of patients, and the updated list is saved
    * back to the CSV file. If the patient is not found, a message is displayed indicating
    * that no matching profile was found.
    * </p>
    *
    * @param hospitalID The hospital ID of the patient to be deleted.
    * @return 1 if the patient profile was successfully deleted and the CSV file was updated,
    *         0 if no matching patient profile was found.
    */
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
