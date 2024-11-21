package Models;

import java.util.*;
import java.time.*;
import Views.PatientMenu;
import Models.MedicalRecordManager;
import Models.AppointmentOutcomeRecord;
import Models.Appointment;

public class Patient extends User {
	private String name;
	private LocalDate birthDate;
	private String gender;
	private String phoneNum;
	private String email;
    private String patientID;
    private String bloodType;

	private MedicalRecordManager medicalRecordManager = new MedicalRecordManager();
	private TimeSlotInterface timeSlotManager;
	private AppointmentManager appointmentManager;

    public Patient(String hospitalID, 
	String password, String role, String name, 
	LocalDate birthDate, String gender, String phoneNum, String email,
	String bloodType){
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

    public boolean viewMedicalRecord(){
		MedicalRecordInterface.readMedicalRecordsByPatientID(this.patientID);
		return true;
    }

	public boolean viewAvailableAppointmentSlots() {
		TimeSlotInterface.getTimeSlotsByPatientID(this.patientID);
		return true;
	}

	public boolean scheduleAppointment(String doctorID, String timeslotID) {
		AppointmentManager.scheduleAppointment(this.patientID, doctorID, timeslotID);
		return true;
	}

	public boolean rescheduleAppointment(String oldAppointmentID, String newTimeSlotID) {
		AppointmentManager.rescheduleAppointment(oldAppointmentID, newTimeSlotID);
		return true;
	}

	public boolean cancelAppointment() {
		AppointmentManager.cancelAppointment(this.patientID);
		return true;
	}

	public List<Appointment> viewScheduledAppointments() {
		List<Appointment> appointments = AppointmentManager.getAppointmentsByPatientID(this.patientID);
		return appointments;
	}

	public List<AppointmentOutcomeRecord> viewAppointmentOutcomeRecords() {
		List<AppointmentOutcomeRecord> appointment_outcomes = AppointmentManager.getAppointmentOutcomeRecordsByPatientID(this.patientID);
		return appointment_outcomes;
	}

	public int logout() {
		return 1;
	}

    // Update personal information(only non-medical info)
    public void setPhoneNum(String phoneNum){
        this.phoneNum = phoneNum;
		System.out.println("Phone number changed successfully\n");
    }

    public void setEmail(String email){
        this.email = email;
		System.out.println("Email changed successfully\n");
    }

	// admin setters 
	public void setPatientID(String patientID) {
		this.patientID = patientID;
		System.out.println("Patient ID changed successfully\n");
	}
	
	public void setName(String name) {
		this.name = name;
		System.out.println("Name changed successfully\n");
	}
	
	public void setDateOfBirth(LocalDate birthDate) {
		this.birthDate = birthDate;
		System.out.println("Date of birth changed successfully\n");
	}
	
	public void setGender(String gender) {
		this.gender = gender;
		System.out.println("Gender changed successfully\n");
	}
	
	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
		System.out.println("Blood type changed successfully\n");
	}

	// utils 
	public String getPatientID() {
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

}