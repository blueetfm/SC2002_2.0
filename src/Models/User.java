/**
 * Represents a user in the system.
 *
 * This class provides basic user information and functionalities, including login, password reset, and role update.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
package Models;

public class User {
    /**
     * The unique identifier of the user within the hospital.
     */
    protected String hospitalID;

    /**
     * The user's password.
     */
    protected String password;

    /**
     * The user's role in the system (e.g., doctor, nurse, admin).
     */
    protected String role;

    /**
     * Constructs a new User object with the specified hospital ID, password, and role.
     *
     * @param hospitalID The unique identifier of the user.
     * @param password   The user's password.
     * @param role       The user's role.
     */
    public User(String hospitalID, String password, String role) {
        this.hospitalID = hospitalID;
        this.password = password;
        this.role = role;
    }

    /**
     * Attempts to log in the user with the provided credentials.
     *
     * @param hospitalID The user's hospital ID.
     * @param password   The user's password.
     * @return `true` if the login is successful, `false` otherwise.
     */
    public boolean login(String hospitalID, String password) {
        return this.hospitalID.equals(hospitalID) && this.password.equals(password);
    }

    public boolean logout() {
        System.out.println("  _______ _                 _                           __                       _               _    _ __  __  _____ _ \n" + //
                " |__   __| |               | |                         / _|                     (_)             | |  | |  \\/  |/ ____| |\n" + //
                "    | |  | |__   __ _ _ __ | | __  _   _  ___  _   _  | |_ ___  _ __   _   _ ___ _ _ __   __ _  | |__| | \\  / | (___ | |\n" + //
                "    | |  | '_ \\ / _` | '_ \\| |/ / | | | |/ _ \\| | | | |  _/ _ \\| '__| | | | / __| | '_ \\ / _` | |  __  | |\\/| |\\___ \\| |\n" + //
                "    | |  | | | | (_| | | | |   <  | |_| | (_) | |_| | | || (_) | |    | |_| \\__ \\ | | | | (_| | | |  | | |  | |____) |_|\n" + //
                "    |_|  |_| |_|\\__,_|_| |_|_|\\_\\  \\__, |\\___/ \\__,_| |_| \\___/|_|     \\__,_|___/_|_| |_|\\__, | |_|  |_|_|  |_|_____/(_)\n" + //
                "                                    __/ |                                                 __/ |                         \n" + //
                "                                   |___/                                                 |___/                          ");
                return true;
    }

    /**
     * Resets the user's password to the specified new password.
     *
     * @param newPassword The new password for the user.
     */
    public void resetPassword(String newPassword) {
        password = newPassword;
        System.out.println("Password has been successfully reset!");
    }

    /**
     * Updates the user's role to the specified new role.
     *
     * @param newRole The new role for the user.
     */
    public void updateRole(String newRole) {
        role = newRole;
        System.out.println("Role has been successfully updated!");
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return The user's hospital ID.
     */
    public String getHospitalID() {
        return hospitalID;
    }

    /**
     * Gets the user's password.
     *
     * @return The user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the user's role.
     *
     * @return The user's role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id The new hospital ID for the user.
     */
    public void setHospitalID(String id) {
        this.hospitalID = id;
    }

    /**
     * Sets the user's password.
     *
     * @param password The new password for the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the user's role.
     *
     * @param role The new role for the user.
     */
    public void setRole(String role) {
        this.role = role;
    }
}