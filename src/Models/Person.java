package Models;

import java.util.*;

public class Person extends User {
    protected String name;
    protected Date birthDate;
    protected String gender;
    protected String phoneNum;
    protected String email;

    public Person(int hospitalID, String password, String role, String name, Date birthDate, String gender, String phoneNum, String email){
        super(hospitalID, password, role);
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.email = email;
    }

    
}
