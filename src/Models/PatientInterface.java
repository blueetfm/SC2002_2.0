package Models;

import java.time.LocalDate;
import java.util.List;

public interface PatientInterface {
    public void createPatient(String hospitalID, String password, String role, String name, LocalDate birthDate, String gender, String phoneNum, String email, String patientID, String bloodType, List<MedicalRecord> medicalHistory);
    public static void readPatient(String hospitalID) {
	};
    public void updatePatient();
    public void deletePatient(String hospitalID);
    public static Patient getPatient(String hospitalID) {
		return null;
	}
	public static List<Patient> getAllPatients() {
		return null;
	}
}

