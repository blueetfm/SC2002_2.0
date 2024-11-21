package Models;

import java.util.ArrayList;

/**
 * The {@code MedicalRecord} class represents a collection of medical records associated with a specific patient.
 * Each {@code MedicalRecord} is identified by a unique patient ID and contains a list of individual {@link Record} objects.
 * <p>
 * This class provides methods to manage, retrieve, update, and delete medical records for a patient.
 * </p>
 * 
 * <p>Example usage:</p>
 * <pre>
 *     MedicalRecord medicalRecord = new MedicalRecord("12345");
 *     medicalRecord.addRecord(medicalRecord, "Flu", "Paracetamol", "Rest and hydration");
 *     Record selectedRecord = medicalRecord.selectRecord(medicalRecord, "Flu");
 *     medicalRecord.updateRecord(medicalRecord, "Flu", "Ibuprofen", "Complete bed rest");
 *     medicalRecord.deleteRecord(medicalRecord, "Flu");
 * </pre>
 * 
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
public class MedicalRecord {
    
    /** The unique patient ID associated with the medical records. */
    String patientID;
    
    /** The list of medical records associated with this patient. */
    ArrayList<Record> recordList;

    /**
     * Constructs a new {@code MedicalRecord} object for a given patient ID.
     * The record list is initialized as an empty list.
     * 
     * @param patientID The unique patient ID for this medical record.
     */
    public MedicalRecord(String patientID) {
        this.patientID = patientID;
        this.recordList = new ArrayList<Record>();
    }
    
    /**
     * Retrieves the patient ID associated with this {@code MedicalRecord}.
     * 
     * @return The patient ID as a {@code String}.
     */
    public String getPatientID() {
        return this.patientID;
    }

    /**
     * Adds a new medical record to this {@code MedicalRecord}.
     * This method creates a new {@code Record} object with the specified diagnosis, medication, and treatment,
     * and adds it to the list of records for this patient.
     * 
     * @param medicalRecord The {@code MedicalRecord} to which the record will be added.
     * @param diagnosis The diagnosis for the new record.
     * @param medication The prescribed medication for the new record.
     * @param treatment The treatment plan for the new record.
     * @return {@code 1} if the record was successfully added, otherwise returns {@code 0}.
     */
    public int addRecord(MedicalRecord medicalRecord, String diagnosis, String medication, String treatment) {
        Record record = new Record(diagnosis, medication, treatment);
        medicalRecord.recordList.add(record);
        return 1;
    }
    
    /**
     * Selects a {@code Record} from the list of medical records for this patient based on the diagnosis.
     * This method searches through the {@code recordList} and returns the first matching record.
     * 
     * @param medicalRecord The {@code MedicalRecord} from which to select the record.
     * @param diagnosis The diagnosis of the record to select.
     * @return The {@code Record} with the specified diagnosis, or {@code null} if no record is found.
     */
    public Record selectRecord(MedicalRecord medicalRecord, String diagnosis) {
        for (Record record: medicalRecord.recordList) {
            if (record.getDiagnosis().equals(diagnosis)) {
                return record;
            }
        }
        System.out.println("Error: record not found.");
        return null;
    }
    
    /**
     * Updates an existing medical record's medication and treatment based on the diagnosis.
     * If a matching record is found, the medication and treatment fields are updated with the new values.
     * 
     * @param medicalRecord The {@code MedicalRecord} in which to update the record.
     * @param diagnosis The diagnosis of the record to update.
     * @param medication The new medication to set for the record.
     * @param treatment The new treatment to set for the record.
     * @return {@code 1} if the record was successfully updated, otherwise returns {@code 0}.
     */
    public int updateRecord(MedicalRecord medicalRecord, String diagnosis, String medication, String treatment) {
        for (Record record: medicalRecord.recordList) {
            if (record.getDiagnosis().equals(diagnosis)) {
                record.medication = medication;
                record.treatment = treatment;
                return 1;
            }
        }
        System.out.println("Error: record not found.");
        return 0;
    }

    /**
     * Deletes a medical record from the list of records based on the diagnosis.
     * If a matching record is found, it is removed from the {@code recordList}.
     * 
     * @param medicalRecord The {@code MedicalRecord} from which to delete the record.
     * @param diagnosis The diagnosis of the record to delete.
     * @return {@code 1} if the record was successfully deleted, otherwise returns {@code 0}.
     */
    public int deleteRecord(MedicalRecord medicalRecord, String diagnosis) {
        for (Record record: medicalRecord.recordList) {
            if (record.getDiagnosis().equals(diagnosis)) {
                recordList.remove(record);
                return 1;
            }
        }
        System.out.println("Error: record not found.");
        return 0;
    }
}
