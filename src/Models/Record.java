package Models;

public class Record {
	String diagnosis;
	String medication;
	String treatment;

	public Record(String diagnosis, String medication, String treatment) {
		this.diagnosis = diagnosis;
		this.medication = medication;
		this.treatment = treatment;
	}
	
	public String getDiagnosis() {
		return this.diagnosis;
	}
	
	public String getMediation() {
		return this.medication;
	}

	public String getTreatment() {
		return this.treatment;
	}
	
	public String setDiagnosis(Record record, String diagnosis) {
		record.diagnosis = diagnosis;
		return record.diagnosis;
	}
	
	public String setMedication(Record record, String medication) {
		record.medication = medication;
		return record.medication;
	}
	
	public String setTreatment(Record record, String treatment) {
		record.treatment = treatment;
		return record.treatment;
	}
}
