package Models;

/**
 * The {@code Record} class represents a medical record containing details about a patient's diagnosis,
 * prescribed medication, and treatment plan.
 * <p>
 * This class provides methods to retrieve and modify the diagnosis, medication, and treatment fields 
 * associated with a specific medical record.
 * </p>
 * 
 * <p>Example usage:</p>
 * <pre>
 *     Record record = new Record("Flu", "Paracetamol", "Rest and hydration");
 *     String diagnosis = record.getDiagnosis();
 *     record.setDiagnosis(record, "Cold");
 * </pre>
 * 
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
public class Record {
    
    /** The diagnosis associated with the medical record. */
    String diagnosis;
    
    /** The prescribed medication associated with the medical record. */
    String medication;
    
    /** The treatment plan associated with the medical record. */
    String treatment;

    /**
     * Constructs a new {@code Record} with the specified diagnosis, medication, and treatment.
     * 
     * @param diagnosis The diagnosis to be associated with the record.
     * @param medication The medication prescribed for the record.
     * @param treatment The treatment plan associated with the record.
     */
    public Record(String diagnosis, String medication, String treatment) {
        this.diagnosis = diagnosis;
        this.medication = medication;
        this.treatment = treatment;
    }
    
    /**
     * Retrieves the diagnosis associated with this medical record.
     * 
     * @return The diagnosis as a {@code String}.
     */
    public String getDiagnosis() {
        return this.diagnosis;
    }
    
    /**
     * Retrieves the medication prescribed for this medical record.
     * 
     * @return The medication as a {@code String}.
     */
    public String getMedication() {
        return this.medication;
    }

    /**
     * Retrieves the treatment plan associated with this medical record.
     * 
     * @return The treatment plan as a {@code String}.
     */
    public String getTreatment() {
        return this.treatment;
    }
    
    /**
     * Sets a new diagnosis for the provided {@code Record}.
     * <p>
     * This method modifies the diagnosis field of the given record and returns the updated diagnosis.
     * </p>
     * 
     * @param record The {@code Record} whose diagnosis is to be updated.
     * @param diagnosis The new diagnosis to set.
     * @return The updated diagnosis as a {@code String}.
     */
    public String setDiagnosis(Record record, String diagnosis) {
        record.diagnosis = diagnosis;
        return record.diagnosis;
    }
    
    /**
     * Sets a new medication for the provided {@code Record}.
     * <p>
     * This method modifies the medication field of the given record and returns the updated medication.
     * </p>
     * 
     * @param record The {@code Record} whose medication is to be updated.
     * @param medication The new medication to set.
     * @return The updated medication as a {@code String}.
     */
    public String setMedication(Record record, String medication) {
        record.medication = medication;
        return record.medication;
    }
    
    /**
     * Sets a new treatment plan for the provided {@code Record}.
     * <p>
     * This method modifies the treatment field of the given record and returns the updated treatment plan.
     * </p>
     * 
     * @param record The {@code Record} whose treatment plan is to be updated.
     * @param treatment The new treatment plan to set.
     * @return The updated treatment plan as a {@code String}.
     */
    public String setTreatment(Record record, String treatment) {
        record.treatment = treatment;
        return record.treatment;
    }
}