package Models;


public class User {
    protected int hospitalID;
    protected String password;
    protected String role;

    public User(int hospitalID, String password, String role){
        this.hospitalID = hospitalID;
        this.password = password;
        this.role = role;
    }

    public void resetPassword(String newPassword){
        password = newPassword;
        System.out.println("Password has been successfully reset!");
        return;
    }

    public void updateRole(String newRole){
        role = newRole;
        System.out.println("Role has been successfully updated!");
        return;
    }

}
