package Models;

import java.util.ArrayList;

public class MedicalRecord {
	String patientID;
	ArrayList<Record> recordList;

	public MedicalRecord(String patientID) {
		this.patientID = patientID;
		this.recordList = new ArrayList<Record>();
	}
	
	public int addRecord(MedicalRecord medicalRecord, String diagnosis, String medication, String treatment) {
		Record record = new Record(diagnosis, medication, treatment);
		medicalRecord.recordList.add(record);
		return 1;
	}
	
	public Record selectRecord(MedicalRecord medicalRecord, String diagnosis) {
		for (Record record: medicalRecord.recordList) {
			if (record.getDiagnosis().equals(diagnosis)) {
				return record;
			}
		}
		System.out.println("Error: record not found.");
		return null;
	}
	
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
