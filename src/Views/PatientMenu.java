package Views;
import java.util.Scanner;
import Models.Patient;
import Models.PatientManager;
import Models.PatientInterface;

public class PatientMenu implements Menu {
	private String hospitalID;
	private PatientInterface patientManager = PatientManager.getInstance();
	private Patient patient = patientManager.getPatient(hospitalID);
	public PatientMenu(String hospitalID) {
		this.hospitalID = hospitalID;
	}
	public void showMenu() {
		int choice;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("Perform the following methods:");
			System.out.println("1: View Medical Record");
			// patient class
				// MedicalRecord: Returns list of medical record
			System.out.println("2: Update Personal Information");
			// patient class - just setters
			System.out.println("3: View Available Appointment Slots"); 
			// patient class
				// TimeSlotManager: Returns list of all available appointment slots

			System.out.println("4: Schedule an Appointment"); 
			// patient class
				// TimeSlotManager: Check availability 
				// AppointmentManager: Create new Appointment

			System.out.println("5: Reschedule an Appointment"); 
			// patient class
				// AppointmentManager: Cancel old appointment + Create new appointment
				// TimeSlotManager: Change availability of old and new timeslots

			System.out.println("6: Cancel an Appointment"); 
			// patient class
				// AppointmentManager: Cancel old appointment
				// TimeSlotManager: Change availability of old timeslot

			System.out.println("7: View Scheduled Appointments");  
			// patient class
				// AppointmentManager: Fetch all appointments and sort by 'Availability Status'

			System.out.println("8: View Past Appointment Outcome Records"); 
			// patient class
				// AppointmentManager: Fetch all appointments by Patient ID and print out AOR
			System.out.println("9: Logout");
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
				break;
			case 8: 
			
				break;
			case 9: 
				System.out.println("Program terminating ….");
			default:
				break;
			}
		} while (choice < 9);
		sc.close();
        return;
	}
}
