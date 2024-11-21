package Models;

import Services.StaffInterface;
import Utils.CSVHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StaffManager implements StaffInterface {
    private static String csvFilePath;
    private static final String CSV_HEADER = "Staff ID,Name,Role,Gender,Age";
    private static String userListPath;
    private static final String USER_CSV_HEADER = "Hospital ID,Password";
    private static boolean isInitialized = false;
    
    public StaffManager(String csvFilePath) {
        StaffManager.csvFilePath = csvFilePath;
        StaffManager.userListPath = csvFilePath.replace("Staff_List.csv", "User_list.csv");
        File file = new File(csvFilePath);
        if (!file.exists()) {
            CSVHandler.writeCSVLines(CSV_HEADER.split(","), new String[]{}, csvFilePath);
        }
    }

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

    private static synchronized List<Staff> readAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        List<List<String>> records = CSVHandler.readCSVLines(csvFilePath);

        // Skip header row
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

    private static synchronized void writeAllStaff(List<Staff> staffList) {
        String[] headers = CSV_HEADER.split(",");
        String[] lines = new String[staffList.size()];
        for (int i = 0; i < staffList.size(); i++) {
            lines[i] = staffList.get(i).toCSVString();
        }
        
        // Write with headers for new file, without headers for existing file
        File file = new File(csvFilePath);
        if (!file.exists()) {
            CSVHandler.writeCSVLines(headers, lines, csvFilePath);
        } else {
            CSVHandler.writeCSVLines(headers, lines, csvFilePath);
        }
    }

    private static synchronized List<String[]> readUserList() {
        List<String[]> userList = new ArrayList<>();
        List<List<String>> records = CSVHandler.readCSVLines(userListPath);
        
        // Skip header row
        for (int i = 1; i < records.size(); i++) {
            List<String> record = records.get(i);
            if (record.size() == 2) {
                userList.add(new String[]{record.get(0).trim(), record.get(1).trim()});
            }
        }
        return userList;
    }

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

    public static Staff getStaffByID(String staffId) {
        List<Staff> staffList = readAllStaff();
        return staffList.stream()
            .filter(staff -> staff.getStaffID().equals(staffId))
            .findFirst()
            .orElse(null);
    }

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

    public static String getCurrentPassword(String hospitalID) {
        List<String[]> userList = readUserList();
        for (String[] user : userList) {
            if (user[0].equals(hospitalID)) {
                return user[1];
            }
        }
        return null;
    }

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