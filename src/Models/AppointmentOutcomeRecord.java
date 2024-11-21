package Models;
import Enums.*;
import java.time.*;

public class AppointmentOutcomeRecord {
	private String appointmentID;
	private LocalDate date;
	private Enums.Service service;
	private String medicine;
	private PrescriptionStatus status;
	private String notes;
	
	public AppointmentOutcomeRecord(String appointmentID, LocalDate date, Enums.Service service, String medicine, PrescriptionStatus status, String notes) {
		this.appointmentID = appointmentID;
		this.date = date;
		this.service = service;
		this.medicine = medicine;
		this.status = status;
		this.notes = notes;
	}
	
	public String getAppointmentID() {
		return this.appointmentID;
	}
	
	public LocalDate getDate() {
		return this.date;
	}
	
	public Enums.Service getService() {
		return this.service;
	}
	
	public String getMedicine() {
		return this.medicine;
	}
	
	public PrescriptionStatus getStatus() {
		return this.status;
	}
	
	public String getNotes() {
		return this.notes;
	}
	
	public String setAppointmentID(String appointmentID) {
		this.appointmentID = appointmentID;
		return this.appointmentID;
	}
	
	public LocalDate setDate(LocalDate date) {
		this.date = date;
		return this.date;
	}
	
	public Enums.Service setService(Enums.Service service) {
		this.service = service;
		return this.service;
	}
	
	public String setMedicine(String medicine) {
		this.medicine = medicine;
		return this.medicine;
	}
	
	public PrescriptionStatus setStatus(PrescriptionStatus status) {
		this.status = status;
		return this.status;
	}
	
	public String setNotes(String notes) {
		this.notes = notes;
		return this.notes;
	}
}
