package Models;

import Utils.CSVHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class StaffList implements StaffManager {
    //staff list csv 
    private String csvFilePath;
    private static final String CSV_HEADER = "Staff ID,Name,Role,Gender,Age";

    //user list csv
    private String userListPath;
    private static final String USER_CSV_HEADER = "Hospital ID,Password";
    
    public StaffList(String csvFilePath) {
        this.csvFilePath = csvFilePath;
        this.userListPath = csvFilePath.replace("Staff_List.csv", "User_list.csv");
        File file = new File(csvFilePath);
        if (!file.exists()) {
            CSVHandler.writeCSVLines(CSV_HEADER.split(","), new String[]{}, csvFilePath);
        }
    }

    //handles staff list csv operations
    private synchronized List<Staff> readAllStaff() {
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

    private synchronized void writeAllStaff(List<Staff> staffList) {
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

    //handles user list csv operations
    private synchronized List<String[]> readUserList() {
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

    private synchronized void writeUserList(List<String[]> userList) {
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

    public Staff getStaffById(String staffId) {
        List<Staff> staffList = readAllStaff();
        return staffList.stream()
            .filter(staff -> staff.getStaffID().equals(staffId))
            .findFirst()
            .orElse(null);
    }



    @Override
    public synchronized void addStaff(String StaffID, String password, String name, String role, String gender, int age) {
        List<Staff> staffList = readAllStaff();
        boolean exists = staffList.stream()
            .anyMatch(s -> s.getStaffID().equals(StaffID));
        
        if (exists) {
            System.out.println("Staff ID already exists: " + StaffID);
            return;
        }

        staffList.add(new Staff(StaffID, name, role, gender, age));
        writeAllStaff(staffList);

        List<String[]> userList = readUserList();
        userList.add(new String[]{StaffID, password});
        writeUserList(userList);

        System.out.println("Staff " + StaffID + " has been added to the system with default password: " + password);
    }

    @Override
    public synchronized void removeStaff(String StaffID) {
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

    @Override
    public synchronized boolean updateStaff(String staffID, String name, String role, String gender, int age) {
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

    @Override
    public void viewStaffList() {
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

    public synchronized boolean updatePassword(String hospitalID, String newPassword) {
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

    public String getCurrentPassword(String hospitalID) {
        List<String[]> userList = readUserList();
        for (String[] user : userList) {
            if (user[0].equals(hospitalID)) {
                return user[1];
            }
        }
        return null;
    }
}