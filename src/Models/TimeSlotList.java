package Models;

import java.util.ArrayList;
import java.io.*;
import java.time.*;
import enums.*;

public class TimeSlotList {
	private static TimeSlotList instance;
	private static ArrayList<TimeSlot> timeSlotList;
	
	private TimeSlotList() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("../data/TimeSlot_List.csv"));
		String line;
		br.readLine(); 
		while ((line = br.readLine()) != null) {
			String[] row = line.split(",");
			if (row.length >= 5) {  
                String staffID = row[0].trim(); 
                String name = row[1].trim();
                LocalDate date = LocalDate.parse(row[2].trim());
                LocalTime time = LocalTime.parse(row[3].trim());
                ScheduleStatus scheduleStatus = ScheduleStatus.valueOf(row[4].trim().toUpperCase());
                TimeSlot timeSlot = new TimeSlot(staffID, name, date, time, scheduleStatus);
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
}
