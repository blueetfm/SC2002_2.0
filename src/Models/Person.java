package Models;

import java.util.*;
import java.time.*;

public class Person extends User {
    protected String name;
    protected LocalDate birthDate;
    protected String gender;
    protected String phoneNum;
    protected String email;

    public Person(int hospitalID, String password, String role, String name, LocalDate birthDate, String gender, String phoneNum, String email){
        super(hospitalID, password, role);
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.email = email;
    }

    
}
