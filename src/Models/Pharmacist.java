package Models;

import Views.PharmacistMenu;
import java.util.*;
import java.time.*;

public class Pharmacist extends User {
	protected String name;
	protected LocalDate birthDate;
	protected String gender;
	protected String phoneNum;
	protected String email;
    protected String staffID;

	public Pharmacist(String hospitalID, String password, String role, String name, LocalDate birthDate, String gender, String phoneNum, String email, String staffID){
        super(hospitalID, password, role);
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.email = email;
        this.staffID = staffID;
    }
	
//	methods return 0 if no error, else returns 1
	
	public int viewMedicationInventory() {
		return 0;
	}
	
	public int updatePrescriptionStatus() {
		return 0;
	}
	
	public int viewAppointmentOutcomeRecord() {
		return 0;
	}
	
	public int submitReplenishRequest() {
		return 0;
	}

	public int logout() {
		return 0;
	}
}
