package Models;

import Utils.CSVHandler;
import Utils.CSVHandler;
import java.util.ArrayList;
import java.util.List;

import Services.MedicalRecordInterface;

public class MedicalRecordManager implements MedicalRecordInterface {
	private static MedicalRecordManager instance;
    private static List<MedicalRecord> medicalRecordList;

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
    
    public static synchronized MedicalRecordManager getMedicalRecordList() {
    	if (instance.equals(null)) {
    		instance = new MedicalRecordManager();
    	}
    	return instance;
    }

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

    public static MedicalRecord createMedicalRecord(String patientID) {
        MedicalRecord newRecord = new MedicalRecord(patientID);
        medicalRecordList.add(newRecord);
        return newRecord;
    }
 
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

    public static int readMedicalRecordsByPatientID(String patientID) {
        if (patientID == null || patientID.trim().isEmpty()) {
            System.out.println("Invalid patient ID");
            return -1;
        }

        boolean found = false;
        for (MedicalRecord record : medicalRecordList) {
            if (record.patientID.equals(patientID)) {
                found = true;
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

        if (!found) {
            System.out.println("No records found for patient ID: " + patientID);
            return 0;
        }
        return 1;
    }

    public static MedicalRecord updateMedicalRecord(
        String patientID,
        String diagnosis,
        String medication,
        String treatment
    ) {
        for (MedicalRecord record : medicalRecordList) {
            if (record.patientID.equals(patientID)) {
                record.addRecord(record, diagnosis, medication, treatment);
                return record;
            }
        }
        MedicalRecord newRecord = createMedicalRecord(patientID);
        newRecord.addRecord(newRecord, diagnosis, medication, treatment);
        return newRecord;
    }

    public static boolean deleteMedicalRecord(String patientID) {
        if (patientID == null || patientID.trim().isEmpty()) {
            return false;
        }

        return medicalRecordList.removeIf(record ->
            record.patientID.equals(patientID)
        );
    }
}
