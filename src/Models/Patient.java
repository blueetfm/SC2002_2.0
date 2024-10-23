package Models;

import java.util.*;
import Views.PatientMenu;

// Patients can view their own medical record, which consists of:
// - Patient ID, Name, Date of Birth, Gender
// - Contact Information (e.g., phone number, email address)
// - Blood Type
// - Past Diagnoses and Treatments
// ○ Patients can update non-medical personal information such as email address and
// contact number.
// ○ Patients are not allowed to modify the past diagnoses, prescribed medications,
// treatments or blood type.. 


public class Patient extends Person {
    protected String patientID;
    protected String bloodType;
    protected List<MedicalRecord> medicalHistory;

    public Patient(int hospitalID, String password, String role, String name, Date birthDate, String gender, String phoneNum, String email, String patientID, String bloodType, List<MedicalRecord> medicalHistory){
        super(hospitalID, password, role, name, birthDate, gender, phoneNum, email);
        this.patientID = patientID;
        this.bloodType = bloodType;
        this.medicalHistory = medicalHistory;
    }

    public void viewMedicalRecord(){ 
        for (MedicalRecord record : medicalHistory){
            record.getRecord().forEach((key, value) -> System.out.println(key + " " + value));
        }
        return;
    }

    // setters (only non-medical info)
    public void setPhoneNum(String phoneNum){
        this.phoneNum = phoneNum;
    }

    public void setEmail(String email){
        this.email = email;
    }




    
}
