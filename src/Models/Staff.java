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
        this.staffID = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }


    
}
