package Views;
import java.util.Scanner;
import Models.Patient;
import Models.PatientList;
import Models.PatientManager;

public class PatientMenu implements Menu {
	private String hospitalID;
	private PatientManager patientmanager = PatientList.getInstance();
	private Patient patient = patientmanager.getPatient(hospitalID);
	public PatientMenu(String hospitalID) {
		this.hospitalID=hospitalID;
	}
	public void showMenu() {
		int choice;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("Perform the following methods:");
			System.out.println("1: View Medical Record");
			System.out.println("2: Update Personal Information");
			System.out.println("3: View Available Appointment Slots");
			System.out.println("4: Schedule an Appointment");
			System.out.println("5: Reschedule an Appointment");
			System.out.println("6: Cancel an Appointment");
			System.out.println("7: View Scheduled Appointments");
			System.out.println("8: View Past Appointment Outcome Records");
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
