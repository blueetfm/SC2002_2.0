package Models;

public class AppointmentOutcomeRecordList {
    protected AppointmentOutcomeRecord[] appointmentOutcomeRecordList;

    public void createAppointmentOutcomeRecord(){
        
    }

    public void readAppointmentOutcomeRecord(String appointmentID){
        for (AppointmentOutcomeRecord AOR : appointmentOutcomeRecordList) {
            if (AOR.getAppointmentID().equals(appointmentID)){

            }
        }
        
    }

    public void updateAppointmentOutcomeRecord(String appointmentID){
        for (AppointmentOutcomeRecord AOR : appointmentOutcomeRecordList) {
            if (AOR.getAppointmentID().equals(appointmentID)){

            }
        }
        
    }

    public void deleteAppointmentOutcomeRecord(){
        
    }
}
