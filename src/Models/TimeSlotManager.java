package Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import Enums.ScheduleStatus;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;

import Models.TimeSlot;
import Utils.CSVHandler;
import Utils.DateTimeFormatUtils;

public class TimeSlotManager implements TimeSlotInterface{
	private static TimeSlotManager instance;
	private static List<TimeSlot> timeSlots;
	
	private static final AtomicInteger counter = new AtomicInteger(0);
	private static final String[] headers = {"TimeSlot ID","Doctor ID","Patient ID","Date","Time","Schedule Status"};

	private TimeSlotManager(){
		if (timeSlots == null || timeSlots.isEmpty()){
			timeSlots = initializeObjects();
		}
	}

	public static synchronized TimeSlotManager getInstance() {
		if (instance == null) {
			instance = new TimeSlotManager();

			if (timeSlots == null){
				timeSlots = initializeObjects();
			}
		}	
		return instance;
	}

	public static List<TimeSlot> initializeObjects(){
		timeSlots = new ArrayList<TimeSlot>();

		List<List<String>> records = CSVHandler.readCSVLines("data/TimeSlot_List.csv");

		if (!records.isEmpty()) {
			for (int i = 1; i < records.size(); i++) {
				List<String> record = records.get(i);

				// parsing timeslots
				String timeSlotID = record.get(0);
				String doctorID = record.get(1);
				String patientID = record.get(2);				
				LocalDate date = LocalDate.parse(record.get(3), DateTimeFormatUtils.DATE_FORMATTER);
                LocalTime time = LocalTime.parse(record.get(4), DateTimeFormatUtils.TIME_FORMATTER);
				Enums.ScheduleStatus status = Enums.ScheduleStatus.valueOf(record.get(5).toUpperCase());

				TimeSlot timeSlot = new TimeSlot(timeSlotID, doctorID, patientID, date, time, status);

				timeSlots.add(timeSlot);
				
			}

		}
		return timeSlots;
	}

	public static int updateCSV(List<TimeSlot> timeSlots){
		List<String> records = new ArrayList<>();

		for (TimeSlot timeSlot : timeSlots) {
			if (timeSlot != null) {
				String line = timeSlot.getTimeSlotID() + "," +
							  timeSlot.getDoctorID() + "," +
							  timeSlot.getPatientID() + "," +
							  timeSlot.getDate().format(DateTimeFormatUtils.DATE_FORMATTER) + "," +
							  timeSlot.getTime().format(DateTimeFormatUtils.TIME_FORMATTER) + "," +
							  timeSlot.getScheduleStatus().name();
				records.add(line);
			}
		}
	
		CSVHandler.writeCSVLines(headers, records.toArray(new String[0]), "data/TimeSlot_List.csv");
		return 0;
	}

	
	public String generateTimeSlotID(){
        int uniqueNumber = counter.incrementAndGet();
        String uniquePart = String.format("%03d", uniqueNumber);

        return "TS-" + "-" + uniquePart;
    }

    public static List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

	/* TimeSlot Queries */
	public static TimeSlot getTimeSlotByID(String timeSlotID){
		String upperCaseTimeSlotID = timeSlotID.toUpperCase();
		for (TimeSlot timeSlot : timeSlots){
			if (timeSlot.getTimeSlotID().equals(upperCaseTimeSlotID)){
				return timeSlot;
			}
		}
		return null;
	}

	public static List<TimeSlot> getTimeSlotsByDoctorID(String doctorID) {
	    String upperCaseDoctorID = doctorID.toUpperCase();  
	    return timeSlots.stream()
	            .filter(timeSlot -> timeSlot.getDoctorID().equals(upperCaseDoctorID))  
	            .collect(Collectors.toList());
	}

	public static List<TimeSlot> getTimeSlotsByPatientID(String patientID) {
	    String upperCasePatientID = patientID.toUpperCase();  
	    return timeSlots.stream()
	            .filter(timeSlot -> timeSlot.getPatientID().equals(upperCasePatientID))  
	            .collect(Collectors.toList());
	}

	public int addTimeSlot(String doctorID, String patientID, LocalDate date, LocalTime time, ScheduleStatus scheduleStatus) {
		// validate inputs
		if (doctorID == null || doctorID.trim().isEmpty()) {
			System.out.println("Doctor ID cannot be null or empty.");
			return 0;
		}
	
		if (patientID == null || patientID.trim().isEmpty()) {
			System.out.println("Patient ID cannot be null or empty.");
			return 0;
		}
	
		if (date == null) {
			System.out.println("Date cannot be null.");
			return 0;
		}
	
		if (time == null) {
			System.out.println("Time cannot be null.");
			return 0;
		}
	
		if (scheduleStatus == null) {
			System.out.println("Schedule Status cannot be null.");
			return 0;
		}
	
		String upperCaseDoctorID = doctorID.toUpperCase();
		if (timeSlots.stream().anyMatch(slot -> slot.getDoctorID().equals(upperCaseDoctorID) &&
												 slot.getDate().equals(date) &&
												 slot.getTime().equals(time))) {
			System.out.println("A time slot with the same staff ID and time already exists.");
			return 0;
		}
	
		String timeSlotID = generateTimeSlotID();
	
		TimeSlot newSlot = new TimeSlot(timeSlotID, upperCaseDoctorID, patientID, date, time, scheduleStatus);
		timeSlots.add(newSlot);
	
		System.out.println("Time Slot added.");
		return 1;
	}

	/* Editing Time Slot */	
	public boolean editTimeSlotPatientID(String timeSlotID, String patientID) {
		TimeSlot timeSlot = getTimeSlotByID(timeSlotID);
		if (timeSlot != null && !timeSlot.getPatientID().equals("")){
			timeSlot.setPatientID(patientID);

			updateCSV(timeSlots);
			return true;
		}
		return false;
	}

	public boolean editTimeSlotScheduleStatus(String timeSlotID, ScheduleStatus scheduleStatus) {
		TimeSlot timeSlot = getTimeSlotByID(timeSlotID);
		if (timeSlot != null){
			timeSlot.setScheduleStatus(scheduleStatus);

			updateCSV(timeSlots);
			return true;
		}
		return false;
	}
	
	public static void printTimeSlot(TimeSlot slot) {
		System.out.println("\n=====================================");
		System.out.printf("Time Slot ID: %s\n", slot.getTimeSlotID());
		System.out.printf("Doctor ID: %s\n", slot.getDoctorID());
		System.out.printf("Patient ID: %s\n", slot.getPatientID());
		System.out.printf("Date: %s\n", slot.getDate());
		System.out.printf("Time: %s\n", slot.getTime());
		System.out.printf("Schedule Status: %s\n", slot.getScheduleStatus());
		System.out.println("=====================================");
	}
	
}
