package Models;

import Services.StaffInterface;
import Utils.CSVHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The StaffManager class manages a collection of staff records. It handles operations such as adding, removing,
 * updating, and viewing staff information, as well as managing staff credentials (user IDs and passwords). The staff data
 * is stored in CSV files, and operations are synchronized to ensure thread safety.
 * <p>
 * The staff data is stored in a CSV file with the header: "Staff ID, Name, Role, Gender, Age". Additionally, user credentials
 * are stored in a separate CSV file with the header: "Hospital ID, Password".
 * <p>
 * Staff records are represented by the {@link Staff} class, and staff credentials are handled as pairs of hospital ID
 * and password.
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
public class StaffManager implements StaffInterface {

    /**
     * Path to the CSV file containing staff records.
     */
    private static String csvFilePath;

    /**
     * Header for the staff CSV file.
     */
    private static final String CSV_HEADER = "Staff ID,Name,Role,Gender,Age";

    /**
     * Path to the CSV file containing user credentials.
     */
    private static String userListPath;

    /**
     * Header for the user credentials CSV file.
     */
    private static final String USER_CSV_HEADER = "Hospital ID,Password";

    /**
     * Flag indicating whether the StaffManager has been initialized.
     */
    private static boolean isInitialized = false;

    /**
     * Constructs a StaffManager instance and initializes file paths for the staff and user CSV files.
     * If the staff CSV file does not exist, it creates the file with the appropriate header.
     *
     * @param csvFilePath The path to the staff CSV file.
     */
    public StaffManager(String csvFilePath) {
        StaffManager.csvFilePath = csvFilePath;
        StaffManager.userListPath = csvFilePath.replace("Staff_List.csv", "User_list.csv");
        File file = new File(csvFilePath);
        if (!file.exists()) {
            CSVHandler.writeCSVLines(CSV_HEADER.split(","), new String[]{}, csvFilePath);
        }
    }

    /**
     * Initializes the StaffManager, setting the paths for the staff and user CSV files and creating them if they do not exist.
     * This method is synchronized to ensure thread safety during initialization.
     *
     * @param path The path to the staff CSV file.
     */
    public static synchronized void initialize(String path) {
        if (!isInitialized) {
            csvFilePath = path;
            userListPath = path.replace("Staff_List.csv", "User_list.csv");
            File file = new File(csvFilePath);
            if (!file.exists()) {
                CSVHandler.writeCSVLines(CSV_HEADER.split(","), new String[]{}, csvFilePath);
            }
            isInitialized = true;
        }
    }

    /**
     * Reads all staff records from the CSV file and returns them as a list of {@link Staff} objects.
     * This method is synchronized to ensure thread safety.
     *
     * @return A list of all staff records.
     */
    private static synchronized List<Staff> readAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        List<List<String>> records = CSVHandler.readCSVLines(csvFilePath);

        for (int i = 1; i < records.size(); i++) {
            List<String> record = records.get(i);
            if (record.size() == 5) {
                staffList.add(new Staff(
                    record.get(0).trim(),
                    record.get(1).trim(),
                    record.get(2).trim(),
                    record.get(3).trim(),
                    Integer.parseInt(record.get(4).trim())
                ));
            }
        }
        return staffList;
    }

    /**
     * Writes the provided list of staff records to the staff CSV file.
     * This method is synchronized to ensure thread safety.
     *
     * @param staffList The list of staff records to be written.
     */
    private static synchronized void writeAllStaff(List<Staff> staffList) {
        String[] headers = CSV_HEADER.split(",");
        String[] lines = new String[staffList.size()];
        for (int i = 0; i < staffList.size(); i++) {
            lines[i] = staffList.get(i).toCSVString();
        }

        File file = new File(csvFilePath);
        if (!file.exists()) {
            CSVHandler.writeCSVLines(headers, lines, csvFilePath);
        } else {
            CSVHandler.writeCSVLines(headers, lines, csvFilePath);
        }
    }

    /**
     * Reads the user list (hospital ID and password pairs) from the user credentials CSV file.
     * This method is synchronized to ensure thread safety.
     *
     * @return A list of user credentials.
     */
    private static synchronized List<String[]> readUserList() {
        List<String[]> userList = new ArrayList<>();
        List<List<String>> records = CSVHandler.readCSVLines(userListPath);

        for (int i = 1; i < records.size(); i++) {
            List<String> record = records.get(i);
            if (record.size() == 2) {
                userList.add(new String[]{record.get(0).trim(), record.get(1).trim()});
            }
        }
        return userList;
    }

    /**
     * Writes the provided list of user credentials to the user credentials CSV file.
     * This method is synchronized to ensure thread safety.
     *
     * @param userList The list of user credentials to be written.
     */
    private static synchronized void writeUserList(List<String[]> userList) {
        String[] headers = USER_CSV_HEADER.split(",");
        String[] lines = new String[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
            lines[i] = String.format("%s,%s", userList.get(i)[0], userList.get(i)[1]);
        }

        File file = new File(userListPath);
        if (!file.exists()) {
            CSVHandler.writeCSVLines(headers, lines, userListPath);
        } else {
            CSVHandler.writeCSVLines(headers, lines, userListPath);
        }
    }

    /**
     * Retrieves a staff member by their unique staff ID.
     *
     * @param staffId The unique staff ID.
     * @return The {@link Staff} object corresponding to the staff ID, or null if no such staff member is found.
     */
    public static Staff getStaffByID(String staffId) {
        List<Staff> staffList = readAllStaff();
        return staffList.stream()
            .filter(staff -> staff.getStaffID().equals(staffId))
            .findFirst()
            .orElse(null);
    }

    /**
     * Adds a new staff member with the specified details and assigns them a hospital ID and default password.
     * This method also updates the user list with the new staff member's credentials.
     *
     * @param name   The name of the staff member.
     * @param role   The role of the staff member (e.g., doctor, pharmacist).
     * @param gender The gender of the staff member.
     * @param age    The age of the staff member.
     * @return True if the staff member was successfully added, false otherwise.
     */
    public static synchronized boolean addStaff(String name, String role, String gender, int age) {
        try {
            String staffID = generateHospitalID(role);
            String defaultPassword = "password";

            List<Staff> staffList = readAllStaff();
            staffList.add(new Staff(staffID, name, role, gender, age));
            writeAllStaff(staffList);

            List<String[]> userList = readUserList();
            userList.add(new String[]{staffID, defaultPassword});
            writeUserList(userList);

            System.out.println("Staff " + name + "'s account has been created with the following account details: ");
            System.out.println("Staff ID:" + staffID);
            System.out.println("Default password: " + defaultPassword);
            return true;
        } catch (Exception e) {
            System.err.println("Error adding staff: " + e.getMessage());
            return false;
        }
    }

    /**
     * Removes a staff member from both the staff records and user credentials list.
     *
     * @param StaffID The unique ID of the staff member to be removed.
     */
    public static synchronized void removeStaff(String StaffID) {
        List<Staff> staffList = readAllStaff();
        boolean removed = staffList.removeIf(staff -> staff.getStaffID().equals(StaffID));

        if (removed) {
            writeAllStaff(staffList);

            List<String[]> userList = readUserList();
            boolean userRemoved = userList.removeIf(user -> user[0].equals(StaffID));
            if (userRemoved) {
                writeUserList(userList);
            }

            System.out.println("Staff " + StaffID + " has been removed from both Staff and User lists.");
        } else {
            System.out.println("Staff not found: " + StaffID);
        }
    }

    /**
     * Updates the details of a staff member in the hospital database. This includes updating the staff's name, role, gender, and age.
     * If the staff member is not found, the update is not performed.
     *
     * @param staffID The unique ID of the staff member to be updated.
     * @param name The new name of the staff member.
     * @param role The new role of the staff member.
     * @param gender The new gender of the staff member.
     * @param age The new age of the staff member.
     * @return True if the staff member's details were successfully updated, false otherwise.
     */
    public static synchronized boolean updateStaff(String staffID, String name, String role, String gender, int age) {
        List<Staff> staffList = readAllStaff();
        boolean updated = false;
        
        for (int i = 0; i < staffList.size(); i++) {
            if (staffList.get(i).getStaffID().equals(staffID)) {
                staffList.set(i, new Staff(staffID, name, role, gender, age));
                updated = true;
                break;
            }
        }
        
        if (updated) {
            writeAllStaff(staffList);
            return true;
        }
        return false;
    }

    /**
     * Displays the list of all staff members in the hospital, including their ID, name, role, gender, and age. 
     * It ensures that no staff member is printed more than once.
     */
    public static void viewStaffList() {
        System.out.println("Staff List");
        System.out.println("--------------------");
        List<Staff> staffList = readAllStaff();
        Set<String> printed = new HashSet<>();
        for (Staff staff : staffList) {
            if (!printed.contains(staff.getStaffID())) {
                System.out.printf("ID: %s, Name: %s, Role: %s, Gender: %s, Age: %d%n",
                    staff.getStaffID(),
                    staff.getName(),
                    staff.getRole(),
                    staff.getGender(),
                    staff.getAge());
                printed.add(staff.getStaffID());
            }
        }
    }

    /**
     * Updates the password of a hospital user. The password is updated if the hospital ID exists in the user list.
     *
     * @param hospitalID The hospital ID of the user whose password needs to be updated.
     * @param newPassword The new password for the user.
     * @return True if the password was successfully updated, false otherwise.
     */
    public static synchronized boolean updatePassword(String hospitalID, String newPassword) {
        List<String[]> userList = readUserList();
        boolean updated = false;
        
        for (String[] user : userList) {
            if (user[0].equals(hospitalID)) {
                user[1] = newPassword;
                updated = true;
                break;
            }
        }
        
        if (updated) {
            writeUserList(userList);
            return true;
        }
        return false;
    }

    /**
     * Retrieves the current password for a hospital user.
     *
     * @param hospitalID The hospital ID of the user whose password is to be fetched.
     * @return The current password of the user, or null if the hospital ID is not found.
     */
    public static String getCurrentPassword(String hospitalID) {
        List<String[]> userList = readUserList();
        for (String[] user : userList) {
            if (user[0].equals(hospitalID)) {
                return user[1];
            }
        }
        return null;
    }

    /**
     * Generates a unique hospital ID based on the role of the staff member. The ID is formed by a prefix (first letter of the role)
     * followed by a three-digit number. The number is incremented from the highest number already assigned to that role.
     *
     * @param role The role of the staff member (e.g., "doctor", "pharmacist", "administrator").
     * @return A unique hospital ID for the given role.
     * @throws IllegalArgumentException If the role is invalid.
     */
    private static String generateHospitalID(String role) {
        List<Staff> staffList = readAllStaff();
        String prefix;
        switch (role.toLowerCase()) {
            case "doctor":
                prefix = "D";
                break;
            case "pharmacist":
                prefix = "P";
                break;
            case "administrator":
                prefix = "A";
                break;
            default:
                throw new IllegalArgumentException("Invalid role");
        }
        
        // Find highest existing ID number for this role
        int maxNum = 0;
        for (Staff staff : staffList) {
            String id = staff.getStaffID();
            if (id.startsWith(prefix)) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    maxNum = Math.max(maxNum, num);
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
        
        // Generate new ID
        return String.format("%s%03d", prefix, maxNum + 1);
    }
}