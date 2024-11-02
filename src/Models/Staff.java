package Models;

import java.util.*;
import java.time.*;

public class Staff extends Person {
    protected String staffID;

    public Staff(int hospitalID, String password, String role, String name, LocalDate birthDate, String gender, String phoneNum, String email, String staffID){
        super(hospitalID, password, role, name, birthDate, gender, phoneNum, email);
        this.staffID = staffID;
    }
    
}
