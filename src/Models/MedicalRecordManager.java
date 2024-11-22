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
        try {
            // First verify the medicalRecordList is not null
            if (medicalRecordList == null) {
                System.err.println("Medical record list is not initialized");
                return 0;
            }
    
            String[] headers = {"ID", "Diagnosis", "Medication", "Treatment"};
            List<String> records = new ArrayList<>();
    
            // Create formatted records
            for (MedicalRecord indivRecord : medicalRecordList) {
                if (indivRecord != null && indivRecord.recordList != null) {
                    String ID = indivRecord.patientID;
                    for (Record soloRecord : indivRecord.recordList) {
                        if (soloRecord != null) {
                            // Ensure no null values and proper formatting
                            String diagnosis = soloRecord.diagnosis != null ? soloRecord.diagnosis.trim() : "";
                            String medication = soloRecord.medication != null ? soloRecord.medication.trim() : "";
                            String treatment = soloRecord.treatment != null ? soloRecord.treatment.trim() : "";
                            
                            String record = String.format("%s,%s,%s,%s",
                                ID,
                                diagnosis,
                                medication,
                                treatment
                            );
                            records.add(record);
                        }
                    }
                }
            }
    
            // Convert list to array for CSVHandler
            String[] lines = records.toArray(new String[0]);
    
            // Write to CSV using absolute path
            CSVHandler.writeCSVLines(
                headers,
                lines,
                "data/MedicalRecord_List.csv"
            );
    
            // Verify the file was updated
            List<List<String>> verification = CSVHandler.readCSVLines("data/MedicalRecord_List.csv");
            if (verification == null || verification.size() <= 1) { // Only headers or empty
                System.err.println("Failed to verify CSV update");
                return 0;
            }
    
            System.out.println("Medical records CSV updated successfully");
            return 1;
        } catch (Exception e) {
            System.err.println("Error in updateCSV: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    

    /**
     * Creates a new medical record for a patient with the given patient ID.
     * If a record already exists for the patient, the method does nothing.
     *
     * @param patientID The ID of the patient for whom the medical record is to be created.
     * @return The newly created {@link MedicalRecord} object for the patient.
     */
    public static MedicalRecord createMedicalRecord(String patientID) {
        if (medicalRecordList == null) {
            medicalRecordList = new ArrayList<>();
        }
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
        if (patient == null){
            return -1;
        }

        int found = 0;
        for (MedicalRecord record : medicalRecordList) {
            if (patientID.equals(record.getPatientID())) {
                found++;
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
        if (medicalRecordList == null) {
            medicalRecordList = new ArrayList<>();
        }

        MedicalRecord recordToUpdate = null;
        
        // Find existing record or create new one
        for (MedicalRecord record : medicalRecordList) {
            if (record.patientID.equals(patientID)) {
                recordToUpdate = record;
                break;
            }
        }

        if (recordToUpdate == null) {
            recordToUpdate = new MedicalRecord(patientID);
            medicalRecordList.add(recordToUpdate);
        }

        // Add new record entry
        recordToUpdate.addRecord(recordToUpdate, diagnosis, medication, treatment);
        
        // Force save to CSV
        int saveResult = updateCSV();
        if (saveResult == 0) {
            System.err.println("Warning: Changes may not have been saved to CSV");
        }

        return recordToUpdate;
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
