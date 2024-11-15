package Views;
import java.security.Provider.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import Enums.*;
import Models.AppointmentOutcomeRecord;
import Utils.DateTimeFormatUtils;

public class DoctorMenu implements Menu {
	public void showMenu() {

		// datetime formatter https://stackoverflow.com/questions/8746084/string-to-localdate
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		int choice;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("Perform the following methods:");
			System.out.println("1: View Patient Medical Records");
			System.out.println("2: Update Patient Medical Records");
			System.out.println("3: View Personal Schedule");
			System.out.println("4: Set Availability for Appointments");
			System.out.println("5: Accept or Decline Appointment Requests");
			System.out.println("6: View Upcoming Appointments");
			System.out.println("7: Record Appointment Outcome");
			System.out.println("8: Register Patient");
			System.out.println("9: Discharge Patient");
			System.out.println("10: Logout");
			choice = sc.nextInt();
			
			switch (choice) {
			case 1: 
				break;
			case 2: 
				break;
			case 3: 
				break;
			case 4: 
				break;
			case 5: 
				break;
			case 6: 
				break;
			case 7: 
				System.out.print("Enter Appointment ID to record: ");
				String appointmentID = sc.next();
				System.out.print("Enter Hospital ID of Patient: ");
				String hospitalID = sc.next();
				System.out.print("Enter date of appointment in the form 'YYYY-MM-DD': ");
				LocalDate date = LocalDate.parse(sc.next(), DateTimeFormatUtils.DATE_FORMATTER);
				System.out.print("Enter service provided: ");
				Enums.Service service = Enums.Service.valueOf(sc.next().toUpperCase());
				System.out.print("Enter medication prescribed: ");
				String medication = sc.next();
				System.out.print("Enter notes for appointment: ");
				String notes = sc.next();
				// AppointmentOutcomeRecordList.getInstance().createAppointmentOutcomeRecord(appointmentID, hospitalID, null, null, medication, PrescriptionStatus.PENDING, notes);

				break;
			case 8: 
				break;
			case 9: 
				break;	
			case 10: 
				System.out.println("Logging out ….");
			}
		} while (choice < 10);
	    return;
	}
}
	

