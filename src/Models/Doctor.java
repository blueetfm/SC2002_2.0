package Models;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import Enums.PrescriptionStatus;
import Enums.ScheduleStatus;
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
		Scanner case5Scanner = new Scanner(System.in);
		String case5Choice;
		case5Choice = case5Scanner.nextLine();
		List<TimeSlot> timeSlotList = TimeSlotInterface.getTimeSlotsByDoctorID(case5Choice);
		for (TimeSlot i : timeSlotList) {
			if (i.getScheduleStatus().equals(ScheduleStatus.PENDING)) {
				System.out.println("Accept? (Y/N): ");
				String acceptChar = case5Scanner.nextLine();
				if (acceptChar.equals("Y")) {
					i.setScheduleStatus(ScheduleStatus.RESERVED);
				} else if (acceptChar.equals("N")) {
					i.setScheduleStatus(ScheduleStatus.CANCELLED);
				} else {
					System.out.println("Invalid Input. No action is done.");
				}
			}
		}
		case5Scanner.close();
		return 0;
	}
	
	public int viewUpcomingAppointments() {
		Scanner case6Scanner = new Scanner(System.in);
		String case6Choice;
		case6Choice = case6Scanner.nextLine();
		List<Appointment> appointmentList = AppointmentInterface.getAppointmentsByDoctorID(case6Choice);
		for (Appointment i : appointmentList) {
			
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
		case7Scanner.close();
		return recordResult;
	}
	
	public int registerPatient() {
		Scanner case8Scanner = new Scanner(System.in);
		System.out.print("Enter Patient ID to record: ");
		String patientID = case8Scanner.next();
		System.out.print("Enter Name of Patient: ");
		String name = case8Scanner.next();
		System.out.print("Enter date of birth 'YYYY-MM-DD': ");
		LocalDate date = LocalDate.parse(case8Scanner.next(), DateTimeFormatUtils.DATE_FORMATTER);
		System.out.print("Enter Gender of Patient: ");
		String gender = case8Scanner.next();
		System.out.print("Enter patient blood type: ");
		String bloodType = case8Scanner.next();
		System.out.print("Enter contact number: ");
		String contactNumber = case8Scanner.next();
		PatientManager.createPatient(patientID, "", "patient", name, date, gender, contactNumber, "", bloodType);
		return 0;
	}
	
	public int dischargePatient() {
		Scanner case9Scanner = new Scanner(System.in);
		System.out.print("Enter Patient ID to delete: ");
		String case9Choice = case9Scanner.next();
		return PatientManager.deletePatient(case9Choice);
	}
	
	public int logout() {
		return 0;
	}
}
