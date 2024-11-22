package Models;

import Utils.CSVHandler;
import Services.MedicalRecordInterface;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code MedicalRecordManager} class is responsible for managing the medical records
 * of patients. It provides methods to create, update, read, and delete medical records,
 * as well as manage persistence by reading and writing records to a CSV file.
 * It implements the {@link MedicalRecordInterface} interface.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
public class MedicalRecordManager implements MedicalRecordInterface {

    private static MedicalRecordManager instance;
    public static List<MedicalRecord> medicalRecordList;

    /**
     * Constructs a new {@code MedicalRecordManager} instance.
     * This constructor reads the medical records from the CSV file and initializes
     * the {@code medicalRecordList} by parsing the data from the file. It ensures that
     * existing records are merged and new records are added properly.
     */
    public MedicalRecordManager() {
        MedicalRecordManager.medicalRecordList = new ArrayList<MedicalRecord>();

        List<List<String>> record = CSVHandler.readCSVLines(
            "data/MedicalRecord_List.csv"
        );

        for (List<String> medicalRecord : record) {
            if (medicalRecord.size() == 4) {
                String patientID = medicalRecord.get(0);
                String diagnosis = medicalRecord.get(1);
                String medication = medicalRecord.get(2);
                String treatment = medicalRecord.get(3);

                Boolean flag = false;
                for (MedicalRecord indivRecord : medicalRecordList) {
                    if (indivRecord.patientID.equals(patientID)) {
                        indivRecord.addRecord(
                            indivRecord,
                            diagnosis,
                            medication,
                            treatment
                        );
                        flag = true;
                    }
                }
                if (flag == false) {
                    MedicalRecord indivRecord = new MedicalRecord(patientID);
                    indivRecord.addRecord(
                        indivRecord,
                        diagnosis,
                        medication,
                        treatment
                    );
                    medicalRecordList.add(indivRecord);
                }
            }
        }
    }

    /**
     * Gets the singleton instance of the {@code MedicalRecordManager}. If the instance
     * does not exist, it creates a new one. This method ensures that only one instance
     * of the manager exists throughout the application's lifecycle.
     *
     * @return The singleton instance of the {@code MedicalRecordManager}.
     */
    public static synchronized MedicalRecordManager getMedicalRecordList() {
        if (instance == null) {
            instance = new MedicalRecordManager();
        }
        return instance;
    }

    /**
     * Updates the medical records CSV file with the current data in {@code medicalRecordList}.
     * The method writes all the records to the CSV file in a format with headers "ID", "Diagnosis",
     * "Medication", and "Treatment".
     *
     * @return 0 to indicate the successful update of the CSV file.
     */
    public static int updateCSV() {
        List<String> record = new ArrayList<>();
        for (MedicalRecord indivRecord : medicalRecordList) {
            String ID = indivRecord.patientID;
            for (Record soloRecord : indivRecord.recordList) {
                String input =
                    ID +
                    "," +
                    soloRecord.diagnosis +
                    "," +
                    soloRecord.medication +
                    "," +
                    soloRecord.treatment;
                record.add(input);
            }
        }

        String[] headers = { "ID", "Diagnosis", "Medication", "Treatment" };
        CSVHandler.writeCSVLines(
            headers,
            record.toArray(new String[0]),
            "data/MedicalRecord_List.csv"
        );
        return 0;
    }

    /**
     * Creates a new medical record for a patient with the given patient ID.
     * If a record already exists for the patient, the method does nothing.
     *
     * @param patientID The ID of the patient for whom the medical record is to be created.
     * @return The newly created {@link MedicalRecord} object for the patient.
     */
    public static MedicalRecord createMedicalRecord(String patientID) {
        MedicalRecord newRecord = new MedicalRecord(patientID);
        medicalRecordList.add(newRecord);
        updateCSV(); 
        return newRecord;
    }

    /**
     * Reads and prints all the medical records stored in {@code medicalRecordList}.
     * The method prints the patient ID, diagnosis, medication, and treatment for each record.
     *
     * @return 1 to indicate successful reading and printing of all medical records.
     */
    public static int readAllMedicalRecords() {
        for (MedicalRecord record : medicalRecordList) {
            String ID = record.patientID;
            for (Record obj : record.recordList) {
                System.out.printf(
                    "ID: %s, Diagnosis: %s, Medication: %s, Treatment: %s \n",
                    ID,
                    obj.diagnosis,
                    obj.medication,
                    obj.treatment
                );
            }
        }
        return 1;
    }

    /**
     * Reads and prints the medical records for a specific patient identified by the patient ID.
     * If no records are found for the given patient ID, the method prints an error message.
     *
     * @param patientID The ID of the patient whose medical records are to be retrieved.
     * @return The number of records found and printed for the given patient. Returns -1 for invalid IDs,
     *         and 0 if no records are found for the patient.
     */
    public static int readMedicalRecordsByPatientID(String patientID) {
        Patient patient = PatientManager.getPatient(patientID);
        if (patient == null) {
            return -1; 
        }
    
        int found = 0; // Counter to track the number of records found
    
        for (MedicalRecord record : medicalRecordList) {
            if (patientID.equals(record.getPatientID())) {
                    System.out.println("\n====== Patient Profile ======");
                    System.out.println("Patient ID: " + patient.getPatientID());
                    System.out.println("Name: " + patient.getName());
                    System.out.println("Date of Birth: " + patient.getDateOfBirth());
                    System.out.println("Gender: " + patient.getGender());
                    System.out.println("Blood Type: " + patient.getBloodType());
                    System.out.println("Contact Information: " + patient.getPhoneNum());
                
                        for (Record obj : record.recordList) {
                            System.out.println("  Diagnosis: %s" + obj.getDiagnosis());
                            System.out.println("  Medication: %s" + obj.getMedication());
                            System.out.println("  Treatment: %s" + obj.getTreatment());
                        }
                    }                     
                    System.out.println("===========================\n");
    
                    found++;
        }
    
        if (found == 0) {
            System.out.println("No records found for patient ID: " + patientID);
            return 0;
        }
        return found;
    }

    /**
     * Updates the medical record for a given patient identified by the patient ID.
     * If no record exists for the patient, a new record is created. The method adds a new diagnosis,
     * medication, and treatment to the patient's existing record.
     *
     * @param patientID The ID of the patient whose medical record is to be updated.
     * @param diagnosis The diagnosis to be added to the medical record.
     * @param medication The medication to be added to the medical record.
     * @param treatment The treatment to be added to the medical record.
     * @return The updated or newly created {@link MedicalRecord} object.
     */
    public static MedicalRecord updateMedicalRecord(
        String patientID,
        String diagnosis,
        String medication,
        String treatment
    ) {
        for (MedicalRecord record : medicalRecordList) {
            if (record.patientID.equals(patientID)) {
                record.addRecord(record, diagnosis, medication, treatment);
                updateCSV();
                return record;
            }
        }
        MedicalRecord newRecord = createMedicalRecord(patientID);
        newRecord.addRecord(newRecord, diagnosis, medication, treatment);
        updateCSV();
        return newRecord;
    }

    /**
     * Deletes the medical record for a patient identified by the patient ID.
     * If no record exists for the patient, the method returns false.
     *
     * @param patientID The ID of the patient whose medical record is to be deleted.
     * @return True if the medical record was successfully deleted, false otherwise.
     */
    public static boolean deleteMedicalRecord(String patientID) {
        if (patientID == null || patientID.trim().isEmpty()) {
            return false;
        }

        return medicalRecordList.removeIf(record -> 
            record.patientID.equals(patientID)
        );
    }
}
