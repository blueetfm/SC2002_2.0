package Models;

import java.time.LocalDate;
import java.util.List;

public interface PatientInterface {
    public static int createPatient(String hospitalID, String password, String role, String name, LocalDate birthDate, String gender, String phoneNum, String email, String bloodType) {
		return PatientManager.createPatient(hospitalID, password, role, name, birthDate, gender, phoneNum, email, bloodType);
	}
    public static void readPatient(String hospitalID) {
    	PatientManager.readPatient(hospitalID);
	};
    public static int updatePatient() {;
    	return PatientManager.updatePatient();
    }
    public static int deletePatient(String hospitalID) {
		return PatientManager.deletePatient(hospitalID);
	}
    public static Patient getPatient(String hospitalID) {
		return PatientManager.getPatient(hospitalID);
	}
	public static List<Patient> getAllPatients() {
		return PatientManager.getAllPatients();
	}
}

