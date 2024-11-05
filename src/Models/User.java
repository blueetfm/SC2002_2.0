package Models;


public class User {
    protected String hospitalID;
    protected String password;
    protected String role;

    public User(String hospitalID, String password, String role){
        this.hospitalID = hospitalID;
        this.password = password;
        this.role = role;
    }

    public void login(String hospitalID, String password) {
        //implementation to be added
    }
    
    public void resetPassword(String newPassword){
        password = newPassword;
        System.out.println("Password has been successfully reset!");
    }

    public void updateRole(String newRole){
        role = newRole;
        System.out.println("Role has been successfully updated!");
    }

    //setters and getters
    public String getHospitalID() {
        return hospitalID;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setHospitalID(String id) {
        this.hospitalID = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
