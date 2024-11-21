package Models;

import Enums.PrescriptionStatus;
import Services.AppointmentOutcomeRecordInterface;
import Utils.CSVHandler;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentOutcomeRecordManager implements AppointmentOutcomeRecordInterface {
	private static List<AppointmentOutcomeRecord> AORList;
	private static final String CSV_PATH = "data/AppointmentOutcomeRecord_List.csv";

	public AppointmentOutcomeRecordManager() {
        AORList = new ArrayList<>();
        initializeObjects();
    }

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
	
	
	public static List<AppointmentOutcomeRecord> showAllAppointmentOutcomeRecords() {
		return AORList;
	}
	
	public static AppointmentOutcomeRecord getAppointmentOutcomeRecord(String appointmentID) {
		for (AppointmentOutcomeRecord AORecord : AORList) {
			if (AORecord.getAppointmentID().equals(appointmentID)) {
				return AORecord;
			}
		}
		System.err.println("AppointmentOutcomeRecord not found. ");
		return null;
			
	}

	public static int createAppointmentOutcomeRecord(String appointmentID, LocalDate date, 
            Enums.Service service, String medicine, PrescriptionStatus status, String notes) {
        try {
            // Initialize if not already done
            if (AORList == null) {
                AORList = new ArrayList<>();
            }

            // Check for existing record
            for (AppointmentOutcomeRecord record : AORList) {
                if (record.getAppointmentID().equals(appointmentID)) {
                    System.out.println("Record already exists for this appointment.");
                    return 0;
                }
            }

            // Create new record
            AppointmentOutcomeRecord newAOR = new AppointmentOutcomeRecord(
                appointmentID, date, service, medicine, status, notes);
            AORList.add(newAOR);

            // Update CSV
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