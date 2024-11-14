package Models;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import Enums.*;
import Views.UserMenu;

public class AppointmentList {
    private static AppointmentList instance;
    protected ArrayList<Appointment> appointmentList;

    public AppointmentList(){
        this.appointmentList = new ArrayList<>();
    }

    public static AppointmentList getInstance(){
        if (instance == null){
            instance = new AppointmentList();
        }
        return instance;
    }

    // CRUD
    public void createAppointment(String appointmentID, String patientID, String doctorID, LocalDate date, LocalTime timeSlot){
        for (Appointment appointment : appointmentList){
            if (appointment.getAppointmentID().equals(appointmentID)) {
                System.out.println("Appointment ID already exists.");
                return;
            }
        }
        Appointment newAppointment = new Appointment(appointmentID, patientID, doctorID, date, timeSlot);
        appointmentList.add(newAppointment);
        System.out.println("Appointment successfully created!");
        return;
    }

    public void readAppointment(String appointmentID){
        String loggedInID = UserMenu.getLoggedInHospitalID(); // Get the logged-in user's ID
        boolean isPatient = loggedInID.startsWith("P") && (loggedInID.length() == 5);

        for (Appointment appointment: appointmentList) {
            if (appointment.getAppointmentID().equals(appointmentID)){
                if (!isPatient || appointment.getPatientID().equals(loggedInID)){
                    appointment.printDetails();
                    return;
                } else {
                    System.out.println("Access Denied. You can only view your own appointment records.");
                    return;
                }
            }
        }

        System.out.println("No matching appointment ID.");
        
        return;
    }

    public void updateAppointment(String appointmentID){
        String loggedInID = UserMenu.getLoggedInHospitalID();
        boolean isPatient = loggedInID.startsWith("P") && (loggedInID.length() == 5);
        for (Appointment appointment : appointmentList) {
            if (appointment.getAppointmentID().equals(appointmentID)){
                if (!isPatient || appointment.getPatientID().equals(loggedInID)){
                    // insert
                } else {
                    System.out.println("Access Denied. You can only view your own appointment records.");
                    return;
                }

            }
        }

        return;
    }

    public void deleteAppointment(String appointmentID){
        for (Appointment appointment : appointmentList) {
            if (appointment.getAppointmentID().equals(appointmentID)){

            }
        }
    }
    
}
