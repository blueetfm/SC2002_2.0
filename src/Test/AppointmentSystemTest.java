package Test;

import Models.*;
import Services.*;
import Enums.*;
import java.time.*;
import java.util.*;

public class AppointmentSystemTest {
    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        // Initialize test data
        TimeSlotInterface.initializeObjects();
        AppointmentInterface.initializeObjects();
        
        // Test case constants
        final String DOCTOR_ID = "D001";
        final String PATIENT_ID = "P1001";
        
        testDoctorTimeSlotCreation(DOCTOR_ID);
        testViewAvailableSlots();
        testAppointmentBooking(PATIENT_ID, DOCTOR_ID);
        testAppointmentApproval();
        testAppointmentRescheduling(PATIENT_ID);
        testAppointmentCancellation(PATIENT_ID);
    }

    private static void testDoctorTimeSlotCreation(String doctorID) {
        System.out.println("\n=== Testing Doctor TimeSlot Creation ===");
        
        // Create slots for next week
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusDays(7).withHour(9).withMinute(0);
        
        // Create 3 time slots
        for (int i = 0; i < 3; i++) {
            LocalDateTime slotStart = start.plusHours(i);
            LocalDateTime slotEnd = slotStart.plusMinutes(30);
            TimeSlotInterface.createTimeSlot(doctorID, slotStart, slotEnd);
        }
        
        // Verify slots were created
        List<TimeSlot> doctorSlots = TimeSlotInterface.getTimeSlotsByDoctorID(doctorID);
        System.out.println("Created " + doctorSlots.size() + " time slots for doctor " + doctorID);
        for (TimeSlot slot : doctorSlots) {
            System.out.printf("Slot %s: %s - %s%n", 
                slot.getTimeSlotID(),
                slot.getStartTime(),
                slot.getEndTime()
            );
        }
    }

    private static void testViewAvailableSlots() {
        System.out.println("\n=== Testing View Available Slots ===");
        List<TimeSlot> availableSlots = TimeSlotInterface.getAvailableTimeSlots();
        System.out.println("Found " + availableSlots.size() + " available slots");
        for (TimeSlot slot : availableSlots) {
            System.out.printf("Available Slot: %s with Doctor %s at %s%n",
                slot.getTimeSlotID(),
                slot.getDoctorID(),
                slot.getStartTime()
            );
        }
    }

    private static void testAppointmentBooking(String patientID, String doctorID) {
        System.out.println("\n=== Testing Appointment Booking ===");
        List<TimeSlot> availableSlots = TimeSlotInterface.getAvailableTimeSlots();
        if (!availableSlots.isEmpty()) {
            TimeSlot slot = availableSlots.get(0);
            AppointmentInterface.scheduleAppointment(patientID, doctorID, slot.getTimeSlotID(), Service.CONSULTATION);
            System.out.println("Booked appointment for patient " + patientID + " in slot " + slot.getTimeSlotID());
            
            // Verify booking
            List<Appointment> patientAppointments = AppointmentInterface.getAppointmentsByPatientID(patientID);
            System.out.println("Patient has " + patientAppointments.size() + " appointments");
            for (Appointment apt : patientAppointments) {
                System.out.printf("Appointment %s Status: %s%n", apt.getAppointmentID(), apt.getStatus());
            }
        }
    }

    private static void testAppointmentApproval() {
        System.out.println("\n=== Testing Appointment Approval ===");
        List<Appointment> pendingAppointments = new ArrayList<>();
        for (Appointment apt : AppointmentInterface.getAppointmentsByDoctorID("D001")) {
            if (apt.getStatus() == AppointmentStatus.PENDING) {
                pendingAppointments.add(apt);
            }
        }
        
        if (!pendingAppointments.isEmpty()) {
            Appointment apt = pendingAppointments.get(0);
            AppointmentInterface.updateAppointmentStatus(apt.getAppointmentID(), AppointmentStatus.CONFIRMED);
            System.out.println("Approved appointment: " + apt.getAppointmentID());
            
            // Verify status update
            TimeSlot slot = TimeSlotInterface.getTimeSlotByID(apt.getTimeSlotID());
            System.out.println("TimeSlot status: " + slot.getStatus());
        }
    }

    private static void testAppointmentRescheduling(String patientID) {
        System.out.println("\n=== Testing Appointment Rescheduling ===");
        List<Appointment> appointments = AppointmentInterface.getAppointmentsByPatientID(patientID);
        List<TimeSlot> availableSlots = TimeSlotInterface.getAvailableTimeSlots();
        
        if (!appointments.isEmpty() && !availableSlots.isEmpty()) {
            Appointment oldApt = appointments.get(0);
            TimeSlot newSlot = availableSlots.get(0);
            
            // Cancel old appointment
            AppointmentInterface.updateAppointmentStatus(oldApt.getAppointmentID(), AppointmentStatus.CANCELLED);
            
            // Create new appointment
            AppointmentInterface.scheduleAppointment(patientID, oldApt.getDoctorID(), newSlot.getTimeSlotID(), Service.CONSULTATION);
            System.out.println("Rescheduled appointment to slot: " + newSlot.getTimeSlotID());
        }
    }

    private static void testAppointmentCancellation(String patientID) {
        System.out.println("\n=== Testing Appointment Cancellation ===");
        List<Appointment> appointments = AppointmentInterface.getAppointmentsByPatientID(patientID);
        
        if (!appointments.isEmpty()) {
            Appointment apt = appointments.get(0);
            AppointmentInterface.updateAppointmentStatus(apt.getAppointmentID(), AppointmentStatus.CANCELLED);
            System.out.println("Cancelled appointment: " + apt.getAppointmentID());
            
            // Verify cancellation
            TimeSlot slot = TimeSlotInterface.getTimeSlotByID(apt.getTimeSlotID());
            System.out.println("TimeSlot status after cancellation: " + slot.getStatus());
        }
    }
}