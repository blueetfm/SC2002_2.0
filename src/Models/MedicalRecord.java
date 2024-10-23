package Models;

import java.util.*;

public class MedicalRecord {
    // protected String diagnosis;
    // protected String medication;
    // protected String treatment;
    protected Map<String, String> record;

    public MedicalRecord(String diagnosis, String medication, String treatment){
        record = new HashMap<String, String>();
        record.put("Diagnosis", diagnosis);
        record.put("Medication", medication);
        record.put("Treatment", treatment);
    }

    public Map<String, String> getRecord(){
        return record;
    }
    
    // public MedicalRecord(String diagnosis, String medication, String treatment){
    //     this.diagnosis = diagnosis;
    //     this.medication = medication;
    //     this.treatment = treatment;
    // }
}
