package Services;

import java.time.LocalDate;
import java.util.List;

import Enums.PrescriptionStatus;
import Models.AppointmentOutcomeRecord;
import Models.AppointmentOutcomeRecordManager;

public interface AppointmentOutcomeRecordInterface {

	public static int printAppointmentOutcomeRecord(AppointmentOutcomeRecord appointmentOutcomeRecord) {
		return AppointmentOutcomeRecordManager.printAppointmentOutcomeRecord(appointmentOutcomeRecord);
	}
	
	public static List<AppointmentOutcomeRecord> showAllAppointmentOutcomeRecords() {
		return AppointmentOutcomeRecordManager.showAllAppointmentOutcomeRecords();
	}
	
	public static AppointmentOutcomeRecord getAppointmentOutcomeRecord(String appointmentID) {
		return AppointmentOutcomeRecordManager.getAppointmentOutcomeRecord(appointmentID);
	}
	
	public static int createAppointmentOutcomeRecord(String appointmentID, LocalDate date, Enums.Service service, String medicine, PrescriptionStatus status, String notes) {
		return AppointmentOutcomeRecordManager.createAppointmentOutcomeRecord(appointmentID, date, service, medicine, status, notes);
	}
}
