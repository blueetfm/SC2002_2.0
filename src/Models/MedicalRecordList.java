package Models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Models.Record;
import Utils.CSVHandler;

public class MedicalRecordList {
	List<MedicalRecord> medicalRecordList;

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
		this.medicalRecordList = medicalRecordList;
	}
}
