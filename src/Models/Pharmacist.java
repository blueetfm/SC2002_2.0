package Models;

import Views.PharmacistMenu;
import java.util.*;

public class Pharmacist extends Staff {

	public Pharmacist(int hospitalID, String password, String role, String name, Date birthDate, String gender, String phoneNum, String email, String staffID){
        super(hospitalID, password, role, name, birthDate, gender, phoneNum, email, staffID);
    }

}
