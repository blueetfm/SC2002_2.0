package Models;

import java.util.ArrayList;
import java.util.List;

import Enums.ScheduleStatus;

import java.io.*;
import java.time.*;

import Models.TimeSlot;

public class TimeSlotList implements TimeSlotManager{
	private static TimeSlotList instance;
	private static List<TimeSlot> timeSlotList;
	
	private TimeSlotList() throws IOException {
		timeSlotList = new ArrayList<TimeSlot>();

		BufferedReader br = new BufferedReader(new FileReader("../data/TimeSlot_List.csv"));
		String line;
		br.readLine(); 
		while ((line = br.readLine()) != null) {
			String[] row = line.split(",");
			if (row.length >= 5) {  
                String staffID = row[0].trim();
                String patientID = row[1].trim();
                String name = row[2].trim();
                LocalDate date = LocalDate.parse(row[3].trim());
                LocalTime time = LocalTime.parse(row[4].trim());
                ScheduleStatus scheduleStatus = ScheduleStatus.valueOf(row[5].trim().toUpperCase());
                TimeSlot timeSlot = new TimeSlot(staffID, patientID, name, date, time, scheduleStatus);
                timeSlotList.add(timeSlot);
            }	
		}
	}

	public static TimeSlotList getTimeSlotList() throws IOException {
		if (instance == null) {
			instance = new TimeSlotList();
		}	
		return instance;
	}
	
	public static int getTimeSlot(String name) {
		for (TimeSlot timeSlot : timeSlotList) {
			if (timeSlot.getName().equals(name)) { 
				return 1;
			}
		}
		return 0;
	}
	
	public static List<TimeSlot> getTimeSlots() throws IOException {
		TimeSlotList.getTimeSlotList();
		return TimeSlotList.timeSlotList;
	}
	
	public int addTimeSlot(String staffID, String patientID, String name, LocalDate date, LocalTime time, ScheduleStatus scheduleStatus) {
		if (getTimeSlot(name) == 1) {
			System.out.println("Name used, use a different name.");
			return 0;
		}
		TimeSlot newSlot = new TimeSlot(staffID, patientID, name, date, time, scheduleStatus);
		timeSlotList.add(newSlot);
		System.out.println("Time Slot added.");
		return 1;
	}
	
	public int deleteTimeSlot(String name) {
		for (TimeSlot timeSlot : timeSlotList) {
			if (timeSlot.getName().equals(name)) { 
				timeSlotList.remove(timeSlot);
				System.out.println("Time Slot removed.");
				return 1;
			}
		}
		System.out.println("Error: Time Slot not found.");
		return 0;
	}
	
	public int editTimeSlot(String staffID, String patientID, String name, LocalDate date, LocalTime time, ScheduleStatus scheduleStatus) {
		for (TimeSlot timeSlot : timeSlotList) {
			if (timeSlot.getName().equals(name)) { 
				deleteTimeSlot(name);
				addTimeSlot(staffID, patientID, name, date, time, scheduleStatus);
				System.out.println("Time Slot edited.");
				return 1;
			}
		}
		System.out.println("Error: Time Slot not found.");
		return 0;
	}
	
	public List<TimeSlot> getTimeSlotByStaffID(String ID) {
		List<TimeSlot> outputList = new ArrayList<TimeSlot>();
		for (TimeSlot timeSlot : timeSlotList) {
			if (timeSlot.getStaffID().equals(ID)) { 
				outputList.add(timeSlot);
			}
		}
		System.out.println("Doctor's Time Slot List returned.");
		return outputList;
	}
	
	public List<TimeSlot> getTimeSlotByPatientID(String ID) {
		List<TimeSlot> outputList = new ArrayList<TimeSlot>();
		for (TimeSlot timeSlot : timeSlotList) {
			if (timeSlot.getPatientID().equals(ID)) { 
				outputList.add(timeSlot);
			}
		}
		System.out.println("Patient's Time Slot List returned.");
		return outputList;
	}
}
