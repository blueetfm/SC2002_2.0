package Models;

import java.time.LocalDate;

import Views.AdministratorMenu;

public class Administrator extends User{
	protected String name;
	protected LocalDate birthDate;
	protected String gender;
	protected String phoneNum;
	protected String email;
    protected String staffID;

	public Administrator(String hospitalID, String password, String role, String name, LocalDate birthDate, String gender, String phoneNum, String email, String staffID) {
		super(hospitalID, password, role);
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.email = email;
        this.staffID = staffID;
		// TODO Auto-generated constructor stub
	}

	public int manageHospitalStaff() {
		return 0;
	}
	
	public int viewAppointmentDetails() {
		return 0;
	}
	
	public int manageMedicationInventory() {
		return 0;
	}
	
	public int approveReplenishRequest() {
		return 0;
	}
	
	public int addHospitalStaff() {
		return 0;
	}
	
	public int removeHospitalStaff() {
		return 0;
	}
	
	public int logout() {
		return 0;
	}
}
