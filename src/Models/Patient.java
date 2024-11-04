package Models;

import java.util.*;
import java.time.*;
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


public class Patient extends User {
	protected String name;
	protected LocalDate birthDate;
	protected String gender;
	protected String phoneNum;
	protected String email;
    protected String patientID;
    protected String bloodType;

    public Patient(String hospitalID, String password, String role, String name, LocalDate birthDate, String gender, String phoneNum, String email, String patientID, String bloodType, List<MedicalRecord> medicalHistory){
        super(hospitalID, password, role);
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.email = email;
        this.patientID = patientID;
        this.bloodType = bloodType;
    }

    public void viewMedicalRecord(){ 
        
    }

    // setters (only non-medical info)
    public void setPhoneNum(String phoneNum){
        this.phoneNum = phoneNum;
    }

    public void setEmail(String email){
        this.email = email;
    }




    
}
