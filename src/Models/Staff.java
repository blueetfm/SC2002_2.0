package Models;


public class Staff {
    protected String staffID;
    protected String name;
    protected String role;
    protected String gender;
    protected int age;

    public Staff(String staffID, String name, String role, String gender, int age){
        this.staffID = staffID;
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.age = age;
    }

    //validation methods:
    public boolean isValidStaffID(String staffID) {
        if (staffID == null || staffID.length() != 4) return false;
        char prefix = staffID.charAt(0);
        String numbers = staffID.substring(1);
        
        boolean validPrefix = prefix == 'D' || prefix == 'P' || prefix == 'A';
        boolean validNumbers = numbers.matches("\\d{3}");
        
        return validPrefix && validNumbers;
    }

    public String toCSVString() {
        return String.format("%s,%s,%s,%s,%d", staffID, name, role, gender, age);
    }


    //getters and setters
    public String getStaffID() {
        return staffID;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public void setStaffID(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff ID cannot be empty");
        }
        if (!isValidStaffID(id)) {
            throw new IllegalArgumentException("Invalid Staff ID format");
        }
        this.staffID = id.trim();
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name.trim();
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        if (age < 18 || age > 100) {
            throw new IllegalArgumentException("Age must be between 18 and 100");
        }
        this.age = age;
    }


    
}
