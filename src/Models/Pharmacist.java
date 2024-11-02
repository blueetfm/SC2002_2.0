package Models;

import Views.PharmacistMenu;
import java.util.*;
import java.time.*;

public class Pharmacist extends Staff {

	public Pharmacist(int hospitalID, String password, String role, String name, LocalDate birthDate, String gender, String phoneNum, String email, String staffID){
        super(hospitalID, password, role, name, birthDate, gender, phoneNum, email, staffID);
    }

}
