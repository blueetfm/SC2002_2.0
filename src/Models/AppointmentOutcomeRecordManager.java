package Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Enums.PrescriptionStatus;
import Services.AppointmentOutcomeRecordInterface;
import Utils.CSVHandler;
import Utils.DateTimeFormatUtils;

public class AppointmentOutcomeRecordManager implements AppointmentOutcomeRecordInterface {
	private static List<AppointmentOutcomeRecord> AORList;

	public AppointmentOutcomeRecordManager() {
		AORList = new ArrayList<AppointmentOutcomeRecord>();
		List<List<String>> AORecords = CSVHandler.readCSVLines("data/AppointmentOutcomeRecord_List.csv");
		for (List<String> AORecord : AORecords) {
			if (AORecord.get(0).equals("Appointment ID")) {
				continue;
			} else { 
				String appointmentID = AORecord.get(0).trim();
				LocalDate date = LocalDate.parse(AORecord.get(1).trim(), DateTimeFormatUtils.DATE_FORMATTER);
				Enums.Service service = Enums.Service.valueOf(AORecord.get(2).trim());
				String medicine = AORecord.get(3).trim();
				PrescriptionStatus status = PrescriptionStatus.valueOf(AORecord.get(4).trim());
				String notes = AORecord.get(5).trim();
				AppointmentOutcomeRecord newRecord = new AppointmentOutcomeRecord(appointmentID, date, service, medicine, status, notes);
				AORList.add(newRecord);
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

	public static int createAppointmentOutcomeRecord(String appointmentID, LocalDate date, Enums.Service service, String medicine, PrescriptionStatus status, String notes) {
		try {
			AppointmentOutcomeRecord newAOR = new AppointmentOutcomeRecord(appointmentID, date, service, medicine, status, notes);
			AORList.add(newAOR);
			return 1;			
		} catch (Exception e) {
			System.err.println(" Creating AppointmentOutcomeRecord failed. ");
			return 0;
		}
	}
	
//	public static int updateCSV()
}