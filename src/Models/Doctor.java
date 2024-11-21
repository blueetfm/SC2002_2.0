package Models;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import Enums.PrescriptionStatus;
import Enums.Service;
import Utils.DateTimeFormatUtils;
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
		List<TimeSlot> timeSlotList = TimeSlotInterface.getTimeSlotsByDoctorID(case3Choice);
		for (TimeSlot slot : timeSlotList) {
			TimeSlotInterface.printTimeSlot(slot);
		}
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
		Scanner case6Scanner = new Scanner(System.in);
		String case6Choice;
		case6Choice = case6Scanner.nextLine();
		List<TimeSlot> timeSlotList = TimeSlotInterface.getTimeSlotsByDoctorID(case6Choice);
		for (TimeSlot slot : timeSlotList) {
			if (slot.doctorID.equals(case6Choice)) {
				TimeSlotInterface.printTimeSlot(slot);				
			}
		}
		case6Scanner.close();
		return 1;
	}
	
	public int recordAppointmentOutcome() {
		Scanner case7Scanner = new Scanner(System.in);
		System.out.print("Enter Appointment ID to record: ");
		String appointmentID = case7Scanner.next();
		System.out.print("Enter Hospital ID of Patient: ");
		String patientID = case7Scanner.next();
		System.out.print("Enter date of appointment in the form 'YYYY-MM-DD': ");
		LocalDate date = LocalDate.parse(case7Scanner.next(), DateTimeFormatUtils.DATE_FORMATTER);
		System.out.print("Enter service provided: ");
		Enums.Service service = Enums.Service.valueOf(case7Scanner.next().toUpperCase());
		System.out.print("Enter medication prescribed: ");
		String medication = case7Scanner.next();
		System.out.print("Enter notes for appointment: ");
		String notes = case7Scanner.next();
		int recordResult = AppointmentInterface.recordAppointmentOutcomeRecord(appointmentID, patientID, date, service, medication, PrescriptionStatus.valueOf("PENDING"), notes);
		return recordResult;
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
