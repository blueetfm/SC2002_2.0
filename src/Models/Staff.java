/**
 * The `Staff` class represents an employee or staff member in a system.
 * It holds information about the staff member such as their ID, name, role,
 * gender, and age. The class provides validation methods for the staff ID,
 * methods to convert staff data into a CSV format, and getters and setters
 * for each attribute.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
package Models;

public class Staff {
    
    /** 
     * The unique identifier for the staff member.
     * The ID follows a specific format with a prefix ('D', 'P', or 'A') followed by three digits.
     */
    protected String staffID;
    
    /** 
     * The name of the staff member.
     * It is a string that stores the full name of the staff member.
     */
    protected String name;
    
    /** 
     * The role of the staff member.
     * It represents the job title or function of the staff member (e.g., "Manager", "Technician").
     */
    protected String role;
    
    /** 
     * The gender of the staff member.
     * It can be any string representing the gender identity of the staff member.
     */
    protected String gender;
    
    /** 
     * The age of the staff member.
     * It is an integer representing the age of the staff member.
     */
    protected int age;

    /**
     * Constructs a new `Staff` object with the specified details.
     * 
     * @param staffID The unique staff ID of the staff member. The ID must be validated via `isValidStaffID`.
     * @param name The name of the staff member.
     * @param role The role of the staff member.
     * @param gender The gender of the staff member.
     * @param age The age of the staff member. Must be between 18 and 100.
     */
    public Staff(String staffID, String name, String role, String gender, int age){
        this.staffID = staffID;
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.age = age;
    }

    /**
     * Validates the format of the provided staff ID.
     * The staff ID must be 4 characters long, with the first character being either 'D', 'P', or 'A',
     * and the next three characters being digits.
     * 
     * @param staffID The staff ID to validate.
     * @return `true` if the staff ID is valid, `false` otherwise.
     */
    public boolean isValidStaffID(String staffID) {
        if (staffID == null || staffID.length() != 4) return false;
        char prefix = staffID.charAt(0);
        String numbers = staffID.substring(1);
        
        boolean validPrefix = prefix == 'D' || prefix == 'P' || prefix == 'A';
        boolean validNumbers = numbers.matches("\\d{3}");
        
        return validPrefix && validNumbers;
    }

    /**
     * Converts the staff information into a CSV (Comma Separated Values) string.
     * The resulting string will contain the staff ID, name, role, gender, and age,
     * separated by commas.
     * 
     * @return A CSV string representation of the staff information.
     */
    public String toCSVString() {
        return String.format("%s,%s,%s,%s,%d", staffID, name, role, gender, age);
    }

    // Getters and Setters

    /**
     * Gets the staff ID of the staff member.
     * 
     * @return The staff ID of the staff member.
     */
    public String getStaffID() {
        return staffID;
    }

    /**
     * Gets the name of the staff member.
     * 
     * @return The name of the staff member.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the role of the staff member.
     * 
     * @return The role of the staff member.
     */
    public String getRole() {
        return role;
    }

    /**
     * Gets the gender of the staff member.
     * 
     * @return The gender of the staff member.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Gets the age of the staff member.
     * 
     * @return The age of the staff member.
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the staff ID of the staff member.
     * The staff ID must not be empty and must follow the valid format as defined in `isValidStaffID`.
     * 
     * @param id The new staff ID.
     * @throws IllegalArgumentException If the staff ID is null, empty, or invalid.
     */
    public void setStaffID(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff ID cannot be empty");
        }
        if (!isValidStaffID(id)) {
            throw new IllegalArgumentException("Invalid Staff ID format");
        }
        this.staffID = id.trim();
    }

    /**
     * Sets the name of the staff member.
     * The name must not be empty or null.
     * 
     * @param name The new name of the staff member.
     * @throws IllegalArgumentException If the name is null or empty.
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name.trim();
    }

    /**
     * Sets the role of the staff member.
     * The role is a simple string representing the staff member's job title or function.
     * 
     * @param role The new role of the staff member.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Sets the gender of the staff member.
     * 
     * @param gender The new gender of the staff member.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Sets the age of the staff member.
     * The age must be between 18 and 100.
     * 
     * @param age The new age of the staff member.
     * @throws IllegalArgumentException If the age is less than 18 or greater than 100.
     */
    public void setAge(int age) {
        if (age < 18 || age > 100) {
            throw new IllegalArgumentException("Age must be between 18 and 100");
        }
        this.age = age;
    }
}
