package Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Views.UserMenu;
import Utils.CSVHandler;

public class PatientList implements PatientManager {
    private static PatientList instance;
    protected ArrayList<Patient> patientList;

    public PatientList(){
        this.patientList = new ArrayList<>();
    }

    public static PatientList getInstance(){
        if (instance == null){
            instance = new PatientList();
        }
        return instance;
    }
    
    @ Override
    public void createPatient(String hospitalID, String password, String role, String name, LocalDate birthDate, String gender, String phoneNum, String email, String patientID, String bloodType, List<MedicalRecord> medicalHistory) {
        String[] headers = {"Patient ID", "Name", "Date of Birth", "Gender", "Blood Type", "Contact Information"};
        List<String> allLines = new ArrayList<>();
        for (Patient patient: patientList) {
            if (patient.getPatientId().equals(hospitalID)) {
                System.out.println("Patient profile already exists.");
                return;
            }
            String line = String.format("%s,%s,%s,%s,%s,%s",
                patient.getPatientId(),
                patient.getName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getBloodType(),
                patient.getContactInformation()
            );
            allLines.add(line);
        }
        Patient newPatient = new Patient(hospitalID, password, role, name, birthDate, gender, phoneNum, email, bloodType, medicalHistory);
        patientList.add(newPatient);
        String newLine = String.format("%s,%s,%s,%s,%s,%s",
           newPatient.getPatientId(),
           newPatient.getName(),
           newPatient.getDateOfBirth(),
           newPatient.getGender(),
           newPatient.getBloodType(),
           newPatient.getContactInformation()
        );
        allLines.add(newLine);
        String[] lines = allLines.toArray(new String[0]);
        CSVHandler.writeCSVLines(headers, lines, "../../data/Patient_List.csv"); 
        System.out.println("Patient successfully created!"); 
    }

    

    public Patient getPatient(String hospitalID) {
        String loggedInID = UserMenu.getLoggedInHospitalID(); 
        boolean isPatient = loggedInID.startsWith("P") && (loggedInID.length() == 5);

        if (isPatient && !loggedInID.equals(hospitalID)) {
            System.out.println("Access Denied. You may not view that profile.");
            return null;
        }

        for (Patient patient: patientList) {
            if (patient.getPatientId().equals(hospitalID)){
                System.out.println("Succesfully retrieved patient profile.");
                return patient;
            }
        }

        System.out.println("No matching patient profile ID.");

        return null;
    }

    public void deletePatient(String hospitalID) {
        String[] headers = {"Patient ID", "Name", "Date of Birth", "Gender", "Blood Type", "Contact Information"};

        List<String> remainingLines = new ArrayList<>();
        boolean found = false;
        
        for (Patient patient : patientList) {
            if (!patient.getPatientId().equals(hospitalID)) {
                String line = String.format("%s,%s,%s,%s,%s,%s",
                    patient.getPatientId(),
                    patient.getName(),
                    patient.getDateOfBirth(),
                    patient.getGender(),
                    patient.getBloodType(),
                    patient.getContactInformation()
                );
                remainingLines.add(line);
            } else {
                found = true;
                patientList.remove(patient);
            }
        }
    
        if (found) {
            String[] lines = remainingLines.toArray(new String[0]);
            CSVHandler.writeCSVLines(headers, lines, "../../data/Patient_List.csv"); 
            System.out.println("Successfully deleted patient profile.");
            return;}
        System.out.println("No matching patient profile ID.");
    }
}
