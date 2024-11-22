/**
 * This class is responsible for managing user authentication, including retrieving and updating user passwords.
 * The user data is stored in a CSV file, and the class interacts with this file to authenticate users and modify passwords.
 * <p>
 * It provides methods for:
 * <ul>
 *     <li>Retrieving the password associated with a specific user based on their hospital ID.</li>
 *     <li>Updating the password for a given user.</li>
 *     <li>Checking if a given password is the default password.</li>
 * </ul>
 * </p>
 * 
 * <p>Example usage:</p>
 * <pre>
 *     UserAuthenticationManager authManager = new UserAuthenticationManager();
 *     String password = authManager.getUserPassword("H12345");
 *     boolean updated = authManager.updatePassword("H12345", "newPassword");
 * </pre>
 * 
 * <p>Note: The user data file must be in CSV format and located at "data/User_List.csv".</p>
 * 
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
package Models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class UserAuthenticationManager {
    
    /** The path to the user data file (CSV format). */
    private static final String USER_FILE_PATH = "data/User_List.csv";
    
    /** The default password for users. */
    private static final String DEFAULT_PASSWORD = "password";

    /**
     * Retrieves the password associated with a specific user by their hospital ID.
     * <p>
     * This method reads the user data from the CSV file, searches for the user based on their hospital ID,
     * and returns the corresponding password. If the user is not found or an error occurs, {@code null} is returned.
     * </p>
     *
     * @param hospitalID The unique hospital ID of the user whose password is to be retrieved.
     * @return The password of the user, or {@code null} if the user is not found or an error occurs.
     */
    public String getUserPassword(String hospitalID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE_PATH))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].trim().equalsIgnoreCase(hospitalID)) {
                    return parts[1].trim();
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading user data: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates the password of a user identified by their hospital ID.
     * <p>
     * This method reads the user data from the CSV file, searches for the user based on their hospital ID, 
     * and updates their password. The changes are then written back to the file. 
     * If the user is not found, the password is not updated and {@code false} is returned.
     * </p>
     *
     * @param hospitalID The unique hospital ID of the user whose password is to be updated.
     * @param newPassword The new password to set for the user.
     * @return {@code true} if the password was updated successfully, {@code false} if the user was not found.
     */
    public boolean updatePassword(String hospitalID, String newPassword) {
        try {
            List<String> lines = new ArrayList<>();
            boolean foundUser = false;
            
            try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE_PATH))) {
                String header = reader.readLine();
                lines.add(header);
                
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].trim().equalsIgnoreCase(hospitalID.toUpperCase())) {
                        lines.add(hospitalID.toUpperCase() + "," + newPassword);
                        foundUser = true;
                    } else {
                        lines.add(line);
                    }
                }
            }
            
            if (!foundUser) return false;
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE_PATH))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            
            return true;
            
        } catch (IOException e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if the given password is the default password.
     * <p>
     * This method compares the provided password to the default password ("password").
     * </p>
     *
     * @param password The password to check.
     * @return {@code true} if the password is the default password, {@code false} otherwise.
     */
    public boolean isDefaultPassword(String password) {
        return DEFAULT_PASSWORD.equals(password);
    }
}
