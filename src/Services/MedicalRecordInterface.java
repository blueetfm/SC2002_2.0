package Services;

import Models.MedicalRecord;
import Models.MedicalRecordManager;

public interface MedicalRecordInterface {

	public static int updateCSV() {
		return 0;
	}
	
	public static MedicalRecord createMedicalRecord(String patientID) {
		return null;
	};
	
	public static int readAllMedicalRecords() {
		return 0;
	}
	
	public static int readMedicalRecordsByPatientID(String patientID) {
		return MedicalRecordManager.readMedicalRecordsByPatientID(patientID);
	}
	
	public static MedicalRecord updateMedicalRecord(
	        String patientID,
	        String diagnosis,
	        String medication,
	        String treatment
	    ) {
		return null;
	}
	
	public static boolean deleteMedicalRecord(String patientID) {
		return false;
	}
}
