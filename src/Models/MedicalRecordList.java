package Models;

import java.util.ArrayList;
import java.util.List;

import Utils.CSVHandler;

public class MedicalRecordList {
	private static List<MedicalRecord> medicalRecordList;

	public MedicalRecordList() {
		List<MedicalRecord> medicalRecordList = new ArrayList<MedicalRecord>();
		
		List<List<String>> record = CSVHandler.readCSVLines("data/MedicalRecord_List.csv");
		
		for (List<String> medicalRecord : record) {
			if (medicalRecord.size() == 4) {
				String patientID = medicalRecord.get(0);
				String diagnosis = medicalRecord.get(1);
				String medication = medicalRecord.get(2);	
				String treatment = medicalRecord.get(3);
				
				Boolean flag = false;
				for (MedicalRecord indivRecord : medicalRecordList) {
					if (indivRecord.patientID.equals(patientID)) {
						indivRecord.addRecord(indivRecord, diagnosis, medication, treatment);
						flag = true;
					}
				}
				if (flag == false) {
					MedicalRecord indivRecord = new MedicalRecord(patientID);
					indivRecord.addRecord(indivRecord, diagnosis, medication, treatment);
					medicalRecordList.add(indivRecord);
				}
			}
		}
		MedicalRecordList.medicalRecordList = medicalRecordList;
	}
	
	public int updateCSV() {
		String input = null;
		List<String> record = new ArrayList<>();
		for (MedicalRecord indivRecord : medicalRecordList) {
			String ID = indivRecord.patientID;
			for (Record soloRecord : indivRecord.recordList) {
				input.concat(ID).concat(",").concat(soloRecord.diagnosis).concat(",").concat(soloRecord.medication).concat(",").concat(soloRecord.treatment);
				record.add(input);
			}
		}
		
		String[] headers = {"ID","Diagnosis","Medication","Treatment"};
		CSVHandler.writeCSVLines(headers, record.toArray(new String[0]), "data/MedicalRecord_List.csv");
		return 0;
	}
	
	public MedicalRecord createMedicalRecord(String patientID) {
		MedicalRecord newRecord = new MedicalRecord(patientID);
		medicalRecordList.add(newRecord);
		return newRecord;
	}
	
	public int readAllMedicalRecords() {
		for (MedicalRecord record : medicalRecordList) {
			String ID = record.patientID;
			for (Record obj : record.recordList) {
				System.out.printf("ID: %s, Diagnosis: %s, Medication: %s, Treatment: %s \n", ID, obj.diagnosis, obj.medication, obj.treatment);
			}
		}
		return 1;
	}
	
	public int readMedicalRecordsByPatientID(String patientID) {
		for (MedicalRecord record : medicalRecordList) {
			if (record.patientID.equals(patientID)) {
				String ID = record.patientID;
				for (Record obj : record.recordList) {
					System.out.printf("ID: %s, Diagnosis: %s, Medication: %s, Treatment: %s \n", ID, obj.diagnosis, obj.medication, obj.treatment);
				}
			}
		}
		return 1;
	}
	
	public MedicalRecord updateMedicalRecord(String patientID, String diagnosis, String medication, String treatment) {
		return null;
	}
}
