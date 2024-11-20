package Models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Models.Record;
import Utils.CSVHandler;

public class MedicalRecordList {
	ArrayList<MedicalRecord> medicalRecordList;

//	public MedicalRecordList() throws IOException {
//		try (BufferedReader br = new BufferedReader(new FileReader("../data/MedicalRecord_List.csv"))) {
//			String line;
//			br.readLine(); 
//			while ((line = br.readLine()) != null) {
//				MedicalRecord medicalRecord = null;
//				String[] row = line.split(",");
//				if (row.length >= 2) { 
//					String patientID = row[0].trim();
//					medicalRecord = new MedicalRecord(patientID);
//					String[] recordList = row[1].replaceAll("{}", "").split(");(");
//					for (String recordString : recordList) {
//						String[] recordParts = recordString.replaceAll("()", "").split(";");
//						this.medicalRecordList = new ArrayList<MedicalRecord>();
//						medicalRecord.recordList.add(new Record(recordParts[0], recordParts[1], recordParts[2]));
//					}
//				}
//				if (medicalRecord != null) {
//					medicalRecordList.add(medicalRecord);
//				}
//			}
//		}
//	}
//	
	public MedicalRecordList() {
		List<List<String>> record = CSVHandler.readCSVLines("data/MedicalRecord_List.csv");
		for (List<String> medicalRecord : record) {
			for (String variable : medicalRecord) {
				System.out.printf("%s ", variable);
			}
			System.out.printf("\n");
		}
	}
}
