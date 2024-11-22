/**
 * Manages a list of appointment outcome records, providing functionality for
 * creating, retrieving, displaying, and persisting records to a CSV file.
 * This class interacts with {@code AppointmentOutcomeRecord}, 
 * {@code PrescriptionStatus}, and {@code CSVHandler}.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
package Models;

import Enums.PrescriptionStatus;
import Services.AppointmentOutcomeRecordInterface;
import Utils.CSVHandler;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentOutcomeRecordManager implements AppointmentOutcomeRecordInterface {
    /** A static list that holds all appointment outcome records. */
    private static List<AppointmentOutcomeRecord> AORList;

    /** The file path to the CSV file used for persisting appointment outcome records. */
    private static final String CSV_PATH = "data/AppointmentOutcomeRecord_List.csv";

    /**
     * Constructs a new {@code AppointmentOutcomeRecordManager} and initializes
     * the list of appointment outcome records by reading data from the CSV file.
     */
	public AppointmentOutcomeRecordManager() {
        AORList = new ArrayList<>();
        initializeObjects();
    }
    /**
     * Reads the appointment outcome records from the CSV file and populates {@code AORList}.
     * If the file is empty, it initializes the file with headers.
     */
	private void initializeObjects() {
        List<List<String>> AORecords = CSVHandler.readCSVLines(CSV_PATH);
        if (AORecords == null || AORecords.isEmpty()) {
            String[] headers = {"Appointment ID", "Date", "Service", "Medicine", "Prescription Status", "Notes"};
            CSVHandler.writeCSVLines(headers, new String[]{}, CSV_PATH);
            return;
        }

        for (int i = 1; i < AORecords.size(); i++) {
            List<String> AORecord = AORecords.get(i);
            if (AORecord.size() >= 6) {
                try {
                    String appointmentID = AORecord.get(0).trim();
                    LocalDate date = LocalDate.parse(AORecord.get(1).trim());
                    Enums.Service service = Enums.Service.valueOf(AORecord.get(2).trim());
                    String medicine = AORecord.get(3).trim();
                    PrescriptionStatus status = PrescriptionStatus.valueOf(AORecord.get(4).trim());
                    String notes = AORecord.get(5).trim();
                    
                    AppointmentOutcomeRecord newRecord = new AppointmentOutcomeRecord(
                        appointmentID, date, service, medicine, status, notes);
                    AORList.add(newRecord);
                } catch (Exception e) {
                    System.err.println("Error parsing record: " + e.getMessage());
                }
            }
        }
    }
	/**
     * Prints the details of a specific appointment outcome record.
     *
     * @param appointmentOutcomeRecord the record to print
     * @return 1 if the operation succeeds
     */
	public static int printAppointmentOutcomeRecord(AppointmentOutcomeRecord appointmentOutcomeRecord) {
    	System.out.println("\n=====================================");
    	System.out.printf("Appointment ID: %s\n", appointmentOutcomeRecord.getAppointmentID());
    	System.out.printf("          Date: %s\n", appointmentOutcomeRecord.getDate().toString());
    	System.out.printf("       Service: %s\n", appointmentOutcomeRecord.getService().toString());
    	System.out.printf("      Medicine: %s\n", appointmentOutcomeRecord.getMedicine());
    	System.out.printf("        Status: %s\n", appointmentOutcomeRecord.getStatus().toString());
    	System.out.printf("         Notes: %s\n", appointmentOutcomeRecord.getNotes());
    	System.out.println("=====================================");
    	return 1;
	}
	
	/**
     * Retrieves all stored appointment outcome records.
     *
     * @return a list of {@code AppointmentOutcomeRecord} objects
     */
	public static List<AppointmentOutcomeRecord> showAllAppointmentOutcomeRecords() {
		return AORList;
	}
	/**
     * Retrieves a specific appointment outcome record by its ID.
     *
     * @param appointmentID the ID of the desired record
     * @return the matching {@code AppointmentOutcomeRecord}, or {@code null} if not found
     */
	public static AppointmentOutcomeRecord getAppointmentOutcomeRecord(String appointmentID) {
		for (AppointmentOutcomeRecord AORecord : AORList) {
			if (AORecord.getAppointmentID().equals(appointmentID)) {
				return AORecord;
			}
		}
		System.err.println("AppointmentOutcomeRecord not found. ");
		return null;
			
	}
    /**
     * Creates a new appointment outcome record and updates the CSV file.
     *
     * @param appointmentID the unique ID of the appointment
     * @param date the date of the appointment
     * @param service the service provided
     * @param medicine the prescribed medicine
     * @param status the prescription status
     * @param notes additional notes about the appointment
     * @return 1 if the operation succeeds, or 0 if it fails
     */
	public static int createAppointmentOutcomeRecord(String appointmentID, LocalDate date, 
            Enums.Service service, String medicine, PrescriptionStatus status, String notes) {
        try {
            // init if not done
            if (AORList == null) {
                AORList = new ArrayList<>();
            }

            // check cur record
            for (AppointmentOutcomeRecord record : AORList) {
                if (record.getAppointmentID().equals(appointmentID)) {
                    System.out.println("Record already exists for this appointment.");
                    return 0;
                }
            }
            // add new record
            AppointmentOutcomeRecord newAOR = new AppointmentOutcomeRecord(
                appointmentID, date, service, medicine, status, notes);
            AORList.add(newAOR);

            // update csv
            int result = updateCSV();
            if (result == 0) {
                System.err.println("Failed to update CSV file.");
                return 0;
            }

            System.out.println("Appointment outcome record created successfully.");
            return 1;

        } catch (Exception e) {
            System.err.println("Error creating appointment outcome record: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
	/**
     * Updates the CSV file with the current list of appointment outcome records.
     *
     * @return 1 if the operation succeeds, or 0 if it fails
     */
	public static int updateCSV() {
        try {
            if (AORList == null) {
                System.err.println("AORList is null");
                return 0;
            }

            List<String> lines = new ArrayList<>();
            for (AppointmentOutcomeRecord AOR : AORList) {
                String input = String.format("%s,%s,%s,%s,%s,%s",
                    AOR.getAppointmentID(),
                    AOR.getDate().toString(),
                    AOR.getService().toString(),
                    AOR.getMedicine(),
                    AOR.getStatus().toString(),
                    AOR.getNotes());
                lines.add(input);
            }

            String[] headers = {"Appointment ID", "Date", "Service", "Medicine", "Prescription Status", "Notes"};
            CSVHandler.writeCSVLines(headers, lines.toArray(new String[0]), CSV_PATH);
            return 1;

        } catch (Exception e) {
            System.err.println("Failed to update CSV: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}