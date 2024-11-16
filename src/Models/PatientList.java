package Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Views.UserMenu;
import Utils.CSVHandler;

public class PatientList implements PatientManager {
    private static PatientList instance;
    private static CSVHandler csvhandler;
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
        for (Patient patient: patientList) {
            if (patient.getPatientId().equals(hospitalID)) {
                System.out.println("Patient profile already exists.");
                return;
            }
        Patient newPatient = new Patient(hospitalID, password, role, name, birthDate, gender, phoneNum, email,  patientID,  bloodType, medicalHistory);
        patientList.add(newPatient);
        System.out.println("Patient successfully created!"); }

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

    public void deletePatient(String hospitalID)  {
        for (Patient patient: patientList) {
            if (patient.getPatientId().equals(hospitalID)){
                System.out.println("Succesfully deleted patient profile.");
                patientList.remove(patient);
                return;
            }
        }
        System.out.println("No matching patient profile ID.");
        return;
    }
}
