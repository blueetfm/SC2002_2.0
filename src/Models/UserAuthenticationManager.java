package Models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserAuthenticationManager {
    private static final String USER_FILE_PATH = "data/User_List.csv";
    private static final String DEFAULT_PASSWORD = "password";

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
                    if (parts[0].trim().equalsIgnoreCase(hospitalID)) {
                        lines.add(hospitalID + "," + newPassword);
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

    public boolean isDefaultPassword(String password) {
        return DEFAULT_PASSWORD.equals(password);
    }
}