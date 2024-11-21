package Services;

import Models.MedicalRecord;
import Models.MedicalRecordManager;

/**
 * Interface for managing medical records.
 * <p>
 * This interface provides methods for creating, reading, updating, and deleting 
 * medical records, as well as updating the corresponding CSV file.
 * </p>
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
public interface MedicalRecordInterface {

    /**
     * Updates the CSV file containing medical records.
     *
     * @return An integer indicating the success or failure of the operation.
     */
    public static int updateCSV() {
        return 0;
    }

    /**
     * Creates a new medical record for a patient with the specified ID.
     *
     * @param patientID The ID of the patient for whom the medical record is being created.
     * @return A {@link MedicalRecord} object representing the created medical record, 
     *         or {@code null} if the creation failed.
     */
    public static MedicalRecord createMedicalRecord(String patientID) {
        return null;
    };

    /**
     * Reads and displays all medical records.
     *
     * @return An integer indicating the number of medical records read successfully.
     */
    public static int readAllMedicalRecords() {
        return 0;
    }

    /**
     * Reads and displays medical records for a specific patient based on their ID.
     *
     * @param patientID The ID of the patient whose medical records are to be retrieved.
     * @return An integer indicating the number of medical records found for the patient.
     */
    public static int readMedicalRecordsByPatientID(String patientID) {
        return MedicalRecordManager.readMedicalRecordsByPatientID(patientID);
    }

    /**
     * Updates an existing medical record with new information.
     *
     * @param patientID The ID of the patient whose medical record is to be updated.
     * @param diagnosis The updated diagnosis for the patient.
     * @param medication The updated medication details for the patient.
     * @param treatment The updated treatment plan for the patient.
     * @return A {@link MedicalRecord} object representing the updated medical record,
     *         or {@code null} if the update failed.
     */
    public static MedicalRecord updateMedicalRecord(
        String patientID,
        String diagnosis,
        String medication,
        String treatment
    ) {
        return null;
    }

    /**
     * Deletes a medical record for a specific patient based on their ID.
     *
     * @param patientID The ID of the patient whose medical record is to be deleted.
     * @return {@code true} if the medical record was successfully deleted; {@code false} otherwise.
     */
    public static boolean deleteMedicalRecord(String patientID) {
        return false;
    }
}
