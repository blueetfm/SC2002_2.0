package Models;

import java.util.*;

// Date of Appointment
// ● Type of service provided (e.g., consultation, X-ray, blood test etc).
// ● Any prescribed medications:
// - medication name
// - status (default is pending)
// ● Consultation notes 

public class AppointmentOutcome {
    protected Date date;
    protected String service;
    protected Map<String, String> medication;
    protected String notes;

    // don't construct with the medication
    public AppointmentOutcome(Date date, String service, String notes){
        this.date = date;
        this.service = service;
        this.medication = new HashMap<String, String>();
        this.notes = notes;
    }

    public void addMedication(String medicine){
        medication.put(medicine, "pending");
    }
}
