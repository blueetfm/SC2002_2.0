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

    public Patient(String hospitalID, String password, String role, String name, LocalDate birthDate, String gender, String phoneNum, String email, String bloodType, List<MedicalRecord> medicalHistory){
        super(hospitalID, password, role);
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.email = email;
        this.patientID = hospitalID;
        this.bloodType = bloodType;
    }

//	methods return 0 if no error, else returns 1

    public void viewMedicalRecord(){
		
    }

	public int viewAvailableAppointmentSlots() {
		return 0;
	}

	public int scheduleAppointment() {
		return 0;
	}

	public int rescheduleAppointment() {
		return 0;
	}

	public int cancelAppointment() {
		return 0;
	}

	public int viewScheduledAppointments() {
		return 0;
	}

	public int viewAppointmentOutcomeRecords() {
		return 0;
	}



	public int logout() {
		return 0;
	}



    // Update personal information(only non-medical info)
    public void setPhoneNum(String phoneNum){
        this.phoneNum = phoneNum;
		System.out.print("Phone number changed successfully");
    }

    public void setEmail(String email){
        this.email = email;
		System.out.print("Email changed successfully");
    }

	// utils 
	public String getPatientId() {
		return this.patientID;
	}

	public String getPhoneNum(){
        return this.phoneNum;
    }

    public String getEmail(){
        return this.email;
    }
	public String getName() {
		return this.name;
	}
	public LocalDate getDateOfBirth(){
		return this.birthDate;
	}
	public String getGender() {
		return this.gender;
	}
    public String getBloodType() {
		return this.bloodType;
	}
	public String getContactInformation() {
		return this.phoneNum;
	}

}