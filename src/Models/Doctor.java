package Models;

import java.time.LocalDate;

import Views.DoctorMenu;

public class Doctor extends User{
	protected String name;
	protected LocalDate birthDate;
	protected String gender;
	protected String phoneNum;
	protected String email;
    protected String staffID;

	public Doctor(String hospitalID, String password, String role, String name, LocalDate birthDate, String gender, String phoneNum, String email, String staffID) {
		super(hospitalID, password, role);
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.email = email;
        this.staffID = staffID;
		// TODO Auto-generated constructor stub
	}

//	methods return 0 if no error, else returns 1
	
	public int viewPatientMedicalRecords() {
		return 0;
	}
	
	public int updatePatientMedicalRecords() {
		return 0;
	}
	
	public int viewPersonalSchedule() {
		return 0;
	}
	
	public int setAppointmentAvailability() {
		return 0;
	}
	
	public int viewUpcomingAppointments() {
		return 0;
	}
	
	public int recordAppointmentOutcome() {
		return 0;
	}
	
	public int registerPatient() {
		return 0;
	}
	
	public int dischargePatient() {
		return 0;
	}
	
	public int logout() {
		return 0;
	}
}
