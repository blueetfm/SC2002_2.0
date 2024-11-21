package Models;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import Views.DoctorMenu;

public class Doctor extends User{
	protected String name;
	protected String gender;

	public Doctor(String hospitalID, String password, String role, String name, String gender) {
		super(hospitalID, password, role);
        this.name = name;
        this.gender = gender;
		// TODO Auto-generated constructor stub
	}

//	methods return 0 if no error, else returns 1
	
	public int viewPatientMedicalRecords() {
		List<Patient> patientList = PatientInterface.getAllPatients();
		for (Patient patient : patientList) {
			String ID = patient.getHospitalID();
			PatientInterface.readPatient(ID);
		}
		return 1;
	}
	
	public int updatePatientMedicalRecords() {
		Scanner case2Scanner = new Scanner(System.in);
		String case2Choice;
		case2Choice = case2Scanner.nextLine();
		PatientInterface.getPatient(case2Choice);
		case2Scanner.close();
		return 1;
	}
	
	public int viewPersonalSchedule() {
		Scanner case3Scanner = new Scanner(System.in);
		String case3Choice;
		case3Choice = case3Scanner.nextLine();
		TimeSlotInterface.getTimeSlotsByDoctorID(case3Choice);
		case3Scanner.close();
		return 1;
	}
	
	public int setAppointmentAvailability() {
		return 0;
	}
	
	public int acceptOrDeclineAppointmentRequests() {
		return 0;
	}
	
	public int viewUpcomingAppointments() {
		Scanner case5Scanner = new Scanner(System.in);
		String case5Choice;
		case5Choice = case5Scanner.nextLine();
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
