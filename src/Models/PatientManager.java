package Models;

import java.time.LocalDate;
import java.util.List;

public interface PatientManager {
    public void createPatient(String hospitalID, String password, String role, String name, LocalDate birthDate, String gender, String phoneNum, String email, String patientID, String bloodType, List<MedicalRecord> medicalHistory);
    public void readPatient(String hospitalID);
    public void updatePatient();
    public void deletePatient(String hospitalID);
    public Patient getPatient(String hospitalID);
}

