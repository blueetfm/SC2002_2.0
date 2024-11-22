package Models;

import Services.MedicalRecordInterface;
import Services.PatientInterface;
import Utils.CSVHandler;
import Views.UserMenu;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public static PatientManager getInstance() {
        if (instance == null) {
            instance = new PatientManager();
            if (patientList == null) {
                patientList = initializeObjects();
            }
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
        try {
            // Always read from CSV to get current list
            List<List<String>> allLines = CSVHandler.readCSVLines("data/Patient_List.csv");
            patientList = new ArrayList<>();
            
            if (allLines == null || allLines.size() <= 1) {
                System.out.println("No patients found in system.");
                return patientList;
            }
        
            // Skip header (index 0) and process records
            for (int i = 1; i < allLines.size(); i++) {
                List<String> row = allLines.get(i);
                try {
                    if (row.size() >= 6) {  // Ensure row has required fields
                        String patientID = row.get(0).trim();
                        String name = row.get(1).trim();
                        LocalDate dateOfBirth = LocalDate.parse(row.get(2).trim());
                        String gender = row.get(3).trim();
                        String bloodType = row.get(4).trim();
                        String phoneNum = row.get(5).trim();
                        
                        Patient patient = new Patient(
                            patientID,      // hospitalID
                            name,           // name
                            dateOfBirth,    // birthDate
                            gender,         // gender
                            bloodType,      // bloodType
                            "",            // email (not in CSV)
                            phoneNum        // phoneNum
                        );
                        patientList.add(patient);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing patient record at line " + i + ": " + e.getMessage());
                }
            }
            
            return patientList;
        } catch (Exception e) {
            System.err.println("Error reading patient data: " + e.getMessage());
            return new ArrayList<>();
        }
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

    public static int createPatient(String hospitalID, String password, String role, String name, 
                                LocalDate birthDate, String gender, String phoneNum, String email, String bloodType) {
        if (patientList == null) {
            patientList = initializeObjects();
        }

        try {
            // Check for existing patient
            for (Patient patient : patientList) {
                if (patient.getPatientID().equals(hospitalID)) {
                    System.out.println("Patient profile already exists.");
                    return 0;
                }
            }

            // Get current patients from CSV
            List<List<String>> existingPatients = CSVHandler.readCSVLines("data/Patient_List.csv");
            List<String> patientLines = new ArrayList<>();
            
            // Add existing patients (skip header)
            for (int i = 1; i < existingPatients.size(); i++) {
                List<String> row = existingPatients.get(i);
                // Keep existing lines as they are to maintain format
                patientLines.add(String.join(",", row));
            }

            // Format new patient line to match existing format
            // Format: Patient ID, Name, Date of Birth, Gender, Blood Type, Contact Information
            String newPatientLine = String.format("%s,%s,%s,%s,%s,%s",
                hospitalID,
                name,
                birthDate.toString(),
                gender,
                bloodType,
                phoneNum  // Contact information at the end
            );
            patientLines.add(newPatientLine);

            // Write back to Patient_List.csv
            String[] patientHeaders = {"Patient ID", "Name", "Date of Birth", "Gender", "Blood Type", "Contact Information"};
            CSVHandler.writeCSVLines(patientHeaders, patientLines.toArray(new String[0]), "data/Patient_List.csv");

            // Update User_List.csv
            List<List<String>> existingUsers = CSVHandler.readCSVLines("data/User_List.csv");
            List<String> userLines = new ArrayList<>();
            
            // Add existing users (skip header)
            for (int i = 1; i < existingUsers.size(); i++) {
                List<String> row = existingUsers.get(i);
                userLines.add(String.join(",", row));
            }
            
            // Add new user credentials
            userLines.add(String.format("%s,%s", hospitalID, "password"));
            
            // Write back to User_List.csv
            String[] userHeaders = {"Hospital ID", "Password"};
            CSVHandler.writeCSVLines(userHeaders, userLines.toArray(new String[0]), "data/User_List.csv");

            // Add to patientList in memory
            Patient newPatient = new Patient(hospitalID, password, role, name, birthDate, gender, phoneNum, email, bloodType);
            patientList.add(newPatient);

            System.out.println("\nPatient successfully registered!");
            System.out.println("Default password set to: 'password'");
            System.out.println("Please inform the patient to change their password upon first login.");
            
            return 1;
        } catch (Exception e) {
            System.err.println("Error creating patient: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
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
        
        try {
            List<List<String>> allLines = CSVHandler.readCSVLines("data/Patient_List.csv");
            
            if (allLines == null || allLines.isEmpty()) {
                // Create new file with headers if it doesn't exist
                String[] headers = {"Patient ID", "Name", "Date of Birth", "Gender", "Blood Type", "Email", "Contact Information"};
                CSVHandler.writeCSVLines(headers, new String[]{}, "data/Patient_List.csv");
                return patients;
            }
        
            // Skip header and process records
            for (int i = 1; i < allLines.size(); i++) {
                List<String> row = allLines.get(i);
        
                try {
                    if (row.size() >= 7) {
                        String patientID = row.get(0).trim().toUpperCase();
                        String name = row.get(1).trim();
                        LocalDate dateOfBirth = LocalDate.parse(row.get(2).trim());
                        String gender = row.get(3).trim();
                        String bloodType = row.get(4).trim();
                        String email = row.get(5).trim();
                        String phoneNum = row.get(6).trim();
        
                        Patient patient = new Patient(patientID, name, dateOfBirth, gender, bloodType, email, phoneNum);
                        patients.add(patient);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing patient record: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading patient data: " + e.getMessage());
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
        try {
            // First validate if patient exists
            boolean found = false;
            for (Patient patient : patientList) {
                if (patient.getPatientID().equals(hospitalID)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("No matching patient profile ID.");
                return 0;
            }

            // Update Patient_List.csv
            List<List<String>> existingPatients = CSVHandler.readCSVLines("data/Patient_List.csv");
            List<String> remainingLines = new ArrayList<>();
            
            // Add header
            if (!existingPatients.isEmpty()) {
                remainingLines.add(String.join(",", existingPatients.get(0)));
            }

            // Add all patients except the one to be deleted
            for (int i = 1; i < existingPatients.size(); i++) {
                List<String> row = existingPatients.get(i);
                if (!row.get(0).equals(hospitalID)) {
                    remainingLines.add(String.join(",", row));
                }
            }

            // Write back to Patient_List.csv
            String[] patientLines = remainingLines.toArray(new String[0]);
            CSVHandler.writeCSVLines(new String[0], patientLines, "data/Patient_List.csv");

            // Update User_List.csv
            List<List<String>> existingUsers = CSVHandler.readCSVLines("data/User_List.csv");
            List<String> updatedUserLines = new ArrayList<>();
            
            // Add header
            if (!existingUsers.isEmpty()) {
                updatedUserLines.add(String.join(",", existingUsers.get(0)));
            }
            
            // Add all users except the deleted patient
            for (int i = 1; i < existingUsers.size(); i++) {
                List<String> row = existingUsers.get(i);
                if (!row.get(0).equals(hospitalID)) {
                    updatedUserLines.add(String.join(",", row));
                }
            }

            // Write back to User_List.csv
            String[] userLines = updatedUserLines.toArray(new String[0]);
            CSVHandler.writeCSVLines(new String[0], userLines, "data/User_List.csv");

            // Update in-memory list without using iterator
            patientList = patientList.stream()
                .filter(p -> !p.getPatientID().equals(hospitalID))
                .collect(Collectors.toList());

            System.out.println("Successfully deleted patient profile.");
            return 1;
        } catch (Exception e) {
            System.err.println("Error deleting patient: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}
