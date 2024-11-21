// package Models;

// import java.time.LocalDate;
// import java.time.LocalTime;
// import java.time.format.DateTimeFormatter;
// import java.util.ArrayList;
// import java.util.Iterator;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Set;
// import java.util.concurrent.atomic.AtomicInteger;
// import java.util.stream.Collectors;

// import javax.swing.text.View;

// import Enums.*;
// import Utils.CSVHandler;
// import Utils.DateTimeFormatUtils;

// public class AppointmentManager {
//     private static AppointmentManager instance;
//     private static List<Appointment> appointments;
//     private static List<AppointmentOutcomeRecord> appointmentOutcomeRecords;

//     private static final AtomicInteger counter = new AtomicInteger(0);
//     private static final String[] headers = {"Appointment ID","Patient ID","Doctor ID","Date","Time Slot","Status","Outcome Date","Service","Medication","Prescription Status","Notes"};

//     private AppointmentManager() {
//         if (appointments == null || appointments.isEmpty()) {
//             initializeObjects();
//         }
//     }

//     public static synchronized AppointmentManager getInstance() {
//         if (instance == null) {
//             instance = new AppointmentManager();
//             if (appointments == null) {
//                 appointments = initializeObjects();
//             }
//         }
//         return instance;
//     }

//     // Function to initialize objects from CSVHandler's readCSVLines
//     public static List<Appointment> initializeObjects() {
//         appointments = new ArrayList<Appointment>();
//         appointmentOutcomeRecords = new ArrayList<AppointmentOutcomeRecord>();
    
//         List<List<String>> records = CSVHandler.readCSVLines("data/Appointment_List.csv");
    
//         if (!records.isEmpty()) {
//             // skip headers
//             for (int i = 1; i < records.size(); i++) {
//                 List<String> record = records.get(i);
                
//                 try {
//                     // parsing appointment
//                     String appointmentID = record.get(0).trim();
//                     String patientID = record.get(1).trim();
//                     String doctorID = record.get(2).trim();
//                     LocalDate date = LocalDate.parse(record.get(3).trim(), DateTimeFormatUtils.DATE_FORMATTER);
//                     String timeSlotID = record.get(4).trim();
//                     AppointmentStatus status = AppointmentStatus.valueOf(record.get(5).trim().toUpperCase());
        
//                     // parsing appointment outcome record
//                     LocalDate outcomeDate = null;
//                     Service service = null;
//                     String medication = "";
//                     PrescriptionStatus prescriptionStatus = PrescriptionStatus.PENDING;
//                     String notes = "";
    
//                     if (record.size() > 6 && !record.get(6).trim().isEmpty()) {
//                         outcomeDate = LocalDate.parse(record.get(6).trim(), DateTimeFormatUtils.DATE_FORMATTER);
//                         service = Service.valueOf(record.get(7).trim().toUpperCase());
//                         medication = record.get(8).trim();
//                         prescriptionStatus = PrescriptionStatus.valueOf(record.get(9).trim().toUpperCase());
//                         notes = record.get(10).trim();
//                     }
    
//                     AppointmentOutcomeRecord outcomeRecord = null;
//                     if (outcomeDate != null) {
//                         outcomeRecord = new AppointmentOutcomeRecord(
//                             appointmentID, patientID, outcomeDate, service, medication, prescriptionStatus, notes
//                         );
//                     }
    
//                     Appointment appointment = new Appointment(
//                         appointmentID, patientID, doctorID, date, timeSlotID, status, outcomeRecord
//                     );
    
//                     appointments.add(appointment);
//                     if (outcomeRecord != null) {
//                         appointmentOutcomeRecords.add(outcomeRecord);
//                     }
//                 } catch (Exception e) {
//                     System.err.println("Error parsing appointment record at line " + (i+1) + ": " + e.getMessage());
//                     System.err.println("Record content: " + String.join(",", record));
//                 }
//             }
//         }
//         return appointments;
//     }

//     // Function to transform objects back into CSV lines
//     public static int updateCSV(List<Appointment> appointments){
//         List<String> records = new ArrayList<>();

//         for (Appointment appointment : appointments) {
//             if (appointment != null) {
//                 String line = appointment.getAppointmentID() + "," +
//                               appointment.getPatientID() + "," +
//                               appointment.getDoctorID() + "," +
//                               appointment.getDate().format(DateTimeFormatUtils.DATE_FORMATTER) + "," +
//                               appointment.getTimeSlotID() + "," +
//                               appointment.getStatus().name() + "," +
//                               appointment.getOutcomeRecord().getDate().format(DateTimeFormatUtils.DATE_FORMATTER) + "," +
//                               appointment.getOutcomeRecord().getService().name() + "," +
//                               appointment.getOutcomeRecord().getMedication() + "," +
//                               appointment.getOutcomeRecord().getPrescriptionStatus().name() + "," +
//                               appointment.getOutcomeRecord().getNotes();
//                 records.add(line);
//             }
//         }

//         CSVHandler.writeCSVLines(headers, records.toArray(new String[0]), "data/Appointment_List.csv");
//         return 0;
//     }

//     public static String generateAppointmentID(){
//         int uniqueNumber = counter.incrementAndGet();
//         String uniquePart = String.format("%03d", uniqueNumber);

//         return "APT-" + "-" + uniquePart;
//     }

//     public static List<Appointment> getAppointments() {
//         return appointments;
//     }

//     public static List<AppointmentOutcomeRecord> getAppointmentOutcomeRecords(){
//         return appointmentOutcomeRecords;
//     }

//     /* Appointment Queries */
//     public static Appointment getAppointmentByID(String appointmentID) {
//         String upperCaseAppointmentID = appointmentID.toUpperCase();
//         for (Appointment appointment : appointments) {
//             if (appointment.getAppointmentID().equals(upperCaseAppointmentID)) {
//                 return appointment;
//             }
//         }
//         return null;
//     }

//     public static List<Appointment> getAppointmentsByPatientID(String patientID) {
//         String upperCasePatientID = patientID.toUpperCase();
//         return appointments.stream()
//                 .filter(appointment -> appointment.getPatientID().equals(upperCasePatientID))
//                 .collect(Collectors.toList());
//     }

//     public static List<Appointment> getAppointmentsByDoctorID(String doctorID) {
//         String upperCaseDoctorID = doctorID.toUpperCase();
//         return appointments.stream()
//                 .filter(appointment -> appointment.getDoctorID().equals(upperCaseDoctorID))
//                 .collect(Collectors.toList());
//     }

//     /* Appointment Outcome Records */
//     public static AppointmentOutcomeRecord getAppointmentOutcomeRecordByID(String appointmentID){
//         String upperCaseAppointmentID = appointmentID.toUpperCase();
//         Appointment appointment = getAppointmentByID(upperCaseAppointmentID);

//         return appointment.outcomeRecord;
//     }

//     public static List<AppointmentOutcomeRecord> getAppointmentOutcomeRecordsByPatientID(String patientID){
//         String upperCasePatientID = patientID.toUpperCase();
//         return appointments.stream()
//                 .filter(appointment -> appointment.getPatientID().equals(upperCasePatientID)) 
//                 .map(Appointment::getOutcomeRecord) 
//                 .collect(Collectors.toList()); 
//     }


//     /*Patient Menu Stuff*/
//     public static boolean scheduleAppointment(String patientID, String doctorID, String timeSlotID){
//         if (doctorID == null || doctorID.trim().isEmpty()) {
// 			System.out.println("Doctor ID cannot be null or empty.");
// 			return false;
// 		}

//         if (timeSlotID == null || timeSlotID.trim().isEmpty()){
//             System.out.println("TimeSlot ID cannot be null or empty.");
//             return false;
//         }

//         TimeSlot timeSlot = TimeSlotManager.getTimeSlotByID(timeSlotID);

//         Appointment appointment = new Appointment(
//             generateAppointmentID(), 
//             doctorID,                
//             timeSlot.getPatientID(), 
//             timeSlot.getDate(),      
//             timeSlot.getTimeSlotID(),      
//             AppointmentStatus.SCHEDULED,
//             null                      
//         );

//         appointments.add(appointment);
//         updateCSV(appointments);
//         return true;
//     }

//     public static boolean rescheduleAppointment(String oldAppointmentID, String newTimeSlotID){
//         if (oldAppointmentID == null || oldAppointmentID.trim().isEmpty()) {
// 			System.out.println("Appointment ID cannot be null or empty.");
// 			return false;
// 		}

//         if (newTimeSlotID == null || newTimeSlotID.trim().isEmpty()){
//             System.out.println("TimeSlot ID cannot be null or empty.");
//             return false;
//         }
//         Appointment appointment = getAppointmentByID(oldAppointmentID);
//         String oldTimeSlotID = appointment.getTimeSlotID();
//         TimeSlot oldTimeSlot = TimeSlotManager.getTimeSlotByID(oldTimeSlotID);
//         TimeSlot newTimeSlot = TimeSlotManager.getTimeSlotByID(newTimeSlotID);

//         if (newTimeSlot == null){
//             System.out.println("TimeSlot to be switched to does not exist.");
//             return false;
//         } else if (newTimeSlot.getScheduleStatus() != ScheduleStatus.AVAILABLE){
//             System.out.println("The time slot is not available.");
//             return false;
//         }
//         oldTimeSlot.setScheduleStatus(ScheduleStatus.AVAILABLE);
//         newTimeSlot.setScheduleStatus(ScheduleStatus.RESERVED);
        
//         updateCSV(appointments);
//         return true;
//     }

//     // Cancel appointment
//     public static int cancelAppointment(String appointmentID){
//         Appointment appointment = getAppointmentByID(appointmentID);
//         if (appointment == null){
//             System.out.println("Appointment cannot be found.");
//             return 0;
//         }
//         String oldTimeSlotID = appointment.getTimeSlotID();
//         TimeSlot oldTimeSlot = TimeSlotManager.getTimeSlotByID(oldTimeSlotID);
        
//         oldTimeSlot.setScheduleStatus(ScheduleStatus.AVAILABLE);
//         appointment.setStatus(AppointmentStatus.CANCELLED);
//         return 1; 
//     }
    

//     public static int recordAppointmentOutcomeRecord(String appointmentID, String patientID, LocalDate outcomeDate, Service service, String medication, PrescriptionStatus prescriptionStatus, String notes){
//         Appointment appointment = getAppointmentByID(appointmentID);

//         if (appointment.outcomeRecord == null){
//             AppointmentOutcomeRecord outcomeRecord = new AppointmentOutcomeRecord(
//                     appointmentID, patientID, outcomeDate, service, medication, prescriptionStatus, notes
//                 );
//             appointment.outcomeRecord = outcomeRecord;

//             updateCSV(appointments);
//             return 1;

//         } else {
//             return 0;
//         }
//     }   
    
//     public static void printAppointment(Appointment appointment) {
//     	System.out.println("\n=====================================");
//     	System.out.printf("Appointment ID: %s\n", appointment.getAppointmentID());
//     	System.out.printf("Patient ID: %s\n", appointment.getPatientID());
//     	System.out.printf("Doctor ID: %s\n", appointment.getDoctorID());
//     	System.out.print("Date: "); System.out.println(appointment.getDate());
//     	System.out.printf("Time Slot ID: %s\n", appointment.getTimeSlotID());
//     	System.out.printf("Status: %s\n", appointment.getStatus().toString());
//     	System.out.println("=====================================");
//     }

//     public static void printAppointmentOutcomeRecord(AppointmentOutcomeRecord appointmentOutcomeRecord){
//         System.out.println("--------------------");
//         System.out.println("Date of Appointment: " + appointmentOutcomeRecord.getDate().format(DateTimeFormatUtils.DATE_FORMATTER));
//         System.out.println("Service provided: " + appointmentOutcomeRecord.getService().name());
//         System.out.println("Prescribed Medication: " + appointmentOutcomeRecord.getMedication());
//         System.out.println("--------------------");
//         return;
//     }
// }
