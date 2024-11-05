package Models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;

public class MedicationInventory implements MedicationInventoryManager {

    private String csvFilePath;
    private String requestsFilePath;
    private static final String CSV_HEADER = "Medicine Name,Initial Stock,Low Stock Level Alert";
    private static final String REQUESTS_HEADER = "Medicine Name,Requested Quantity";

    public MedicationInventory(String medicationCsvPath, String requestsCsvPath) {
        this.csvFilePath = medicationCsvPath;
        this.requestsFilePath = requestsCsvPath;
        initializeFiles();
    }

    private void initializeFiles() {
        // Initialize inventory file
        File inventoryFile = new File(csvFilePath);
        if (!inventoryFile.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
                writer.write(CSV_HEADER);
                writer.newLine();
                writer.write("Paracetamol,100,20");
                writer.newLine();
                writer.write("Ibuprofen,50,10");
                writer.newLine();
                writer.write("Amoxicillin,75,15");
            } catch (IOException e) {
                System.err.println("Error initializing inventory file: " + e.getMessage());
            }
        }

        // Initialize requests file
        File requestsFile = new File(requestsFilePath);
        if (!requestsFile.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(requestsFilePath))) {
                writer.write(REQUESTS_HEADER);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Error initializing requests file: " + e.getMessage());
            }
        }
    }

    private String[] readCSVLines(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath)).toArray(new String[0]);
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return new String[0];
        }
    }

    private void writeCSVLines(String[] lines, String filePath) {
        try {
            Files.write(Paths.get(filePath), String.join("\n", lines).getBytes());
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
    
    @Override
    public void viewMedicationInventory() {
        System.out.println("\nCurrent Inventory");
        System.out.println("--------------------");
        String[] lines = readCSVLines(csvFilePath);
        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split(",");
            if (parts.length == 3) {
                Medication med = new Medication(
                    parts[0].trim(),
                    Integer.parseInt(parts[1].trim()),
                    Integer.parseInt(parts[2].trim())
                );
                System.out.println(med);
            }
        }
    }
    
    public void addMedication(String medicineName, int initialStock, int lowStockAlert) {
        String[] lines = readCSVLines(csvFilePath);
        
        // Check if medication already exists
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length > 0 && parts[0].trim().equals(medicineName)) {
                System.out.println("Medication already exists: " + medicineName);
                return;
            }
        }
        
        // Add new medication
        StringBuilder newContent = new StringBuilder();
        for (String line : lines) {
            newContent.append(line).append("\n");
        }
        newContent.append(String.format("%s,%d,%d", medicineName, initialStock, lowStockAlert));
        
        writeCSVLines(newContent.toString().split("\n"), csvFilePath);
        System.out.println("Added new medication: " + medicineName);
    }

    public void removeMedication(String medicineName) {
        String[] lines = readCSVLines(csvFilePath);
        boolean found = false;
        StringBuilder newContent = new StringBuilder(lines[0]); // Keep header
        
        // Rebuild file content without the medication to remove
        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split(",");
            if (!parts[0].trim().equals(medicineName)) {
                newContent.append("\n").append(lines[i]);
            } else {
                found = true;
            }
        }
        
        if (found) {
            writeCSVLines(newContent.toString().split("\n"), csvFilePath);
            System.out.println("Removed medication: " + medicineName);
            
            // Also remove any pending replenishment requests for this medication
            String[] requestLines = readCSVLines(requestsFilePath);
            StringBuilder newRequests = new StringBuilder(requestLines[0]); // Keep header
            for (int i = 1; i < requestLines.length; i++) {
                if (!requestLines[i].startsWith(medicineName + ",")) {
                    newRequests.append("\n").append(requestLines[i]);
                }
            }
            writeCSVLines(newRequests.toString().split("\n"), requestsFilePath);
        } else {
            System.out.println("Medication not found: " + medicineName);
        }
    }

    public void updateMedication(String medicineName, Integer newStock, Integer newLowStockAlert) {
        String[] lines = readCSVLines(csvFilePath);
        boolean found = false;
        StringBuilder newContent = new StringBuilder(lines[0]); // Keep header
        
        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split(",");
            if (parts[0].trim().equals(medicineName)) {
                // Get current values
                int currentStock = Integer.parseInt(parts[1]);
                int currentLowStock = Integer.parseInt(parts[2]);
                
                // Update with new values if provided, otherwise keep current values
                int updatedStock = (newStock != null) ? newStock : currentStock;
                int updatedLowStock = (newLowStockAlert != null) ? newLowStockAlert : currentLowStock;
                
                Medication updatedMed = new Medication(medicineName, updatedStock, updatedLowStock);
                newContent.append("\n").append(updatedMed.toCSVString());
                found = true;
            } else {
                newContent.append("\n").append(lines[i]);
            }
        }
        
        if (found) {
            writeCSVLines(newContent.toString().split("\n"), csvFilePath);
            System.out.println("Updated quantities for medication: " + medicineName);
        } else {
            System.out.println("Medication not found: " + medicineName);
        }
    }

    @Override
    public boolean prescribeMedication(String medicationName, int quantity) {
        String[] lines = readCSVLines(csvFilePath);
        boolean prescribed = false;
        
        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split(",");
            if (parts[0].trim().equals(medicationName)) {
                Medication med = new Medication(
                    parts[0].trim(),
                    Integer.parseInt(parts[1].trim()),
                    Integer.parseInt(parts[2].trim())
                );
                
                prescribed = med.prescribe(quantity);
                if (prescribed) {
                    lines[i] = med.toCSVString();
                    writeCSVLines(lines, csvFilePath);
                    System.out.println("Prescribed " + quantity + " units of " + medicationName);
                    if (med.isLowStock()) {
                        System.out.println("Warning: " + medicationName + " is now low in stock!");
                    }
                } else {
                    System.out.println("Insufficient stock for prescription");
                }
                return prescribed;
            }
        }
        
        System.out.println("Medication not found: " + medicationName);
        return false;
    }

    @Override
    public void submitReplenishRequest(String medicationName, int quantity) {
        String[] inventoryLines = readCSVLines(csvFilePath);
        boolean medicationExists = false;
        
        for (int i = 1; i < inventoryLines.length; i++) {
            if (inventoryLines[i].startsWith(medicationName + ",")) {
                medicationExists = true;
                break;
            }
        }
        
        if (!medicationExists) {
            System.out.println("Medication not found: " + medicationName);
            return;
        }

        String[] requestLines = readCSVLines(requestsFilePath);
        boolean updated = false;
        StringBuilder newContent = new StringBuilder(requestLines[0]);

        for (int i = 1; i < requestLines.length; i++) {
            String[] parts = requestLines[i].split(",");
            if (parts[0].equals(medicationName)) {
                newContent.append("\n").append(medicationName).append(",").append(quantity);
                updated = true;
            } else {
                newContent.append("\n").append(requestLines[i]);
            }
        }

        if (!updated) {
            newContent.append("\n").append(medicationName).append(",").append(quantity);
        }

        writeCSVLines(newContent.toString().split("\n"), requestsFilePath);
        System.out.println("Replenish request submitted to administrator.");
    }

    @Override
    public void displayReplenishRequests() {
        System.out.println("\nReplenishment Requests:");
        System.out.println("----------------------");
        String[] lines = readCSVLines(requestsFilePath);
        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split(",");
            if (parts.length == 2) {
                System.out.println(parts[0] + ": " + parts[1]);
            }
        }
    }

    @Override
    public boolean approveReplenishRequests(String medicineName) {
        String[] requestLines = readCSVLines(requestsFilePath);
        String[] inventoryLines = readCSVLines(csvFilePath);
        boolean requestFound = false;
        int requestedQuantity = 0;

        // Find and remove request
        StringBuilder newRequests = new StringBuilder(requestLines[0]);
        for (int i = 1; i < requestLines.length; i++) {
            String[] parts = requestLines[i].split(",");
            if (parts[0].equals(medicineName)) {
                requestFound = true;
                requestedQuantity = Integer.parseInt(parts[1]);
            } else {
                newRequests.append("\n").append(requestLines[i]);
            }
        }

        if (!requestFound) {
            System.out.println("No replenish request found for: " + medicineName);
            return false;
        }

        // Update inventory
        boolean updated = false;
        for (int i = 1; i < inventoryLines.length; i++) {
            String[] parts = inventoryLines[i].split(",");
            if (parts[0].equals(medicineName)) {
                int currentStock = Integer.parseInt(parts[1]);
                int lowStockLevel = Integer.parseInt(parts[2]);
                Medication med = new Medication(medicineName, currentStock, lowStockLevel);
                med.replenish(requestedQuantity);
                inventoryLines[i] = med.toCSVString();
                updated = true;
                break;
            }
        }

        if (!updated) {
            System.out.println("Medication no longer exists in inventory");
            return false;
        }

        // save changes to files
        writeCSVLines(newRequests.toString().split("\n"), requestsFilePath);
        writeCSVLines(inventoryLines, csvFilePath);

        System.out.println("Approved replenishment of " + requestedQuantity + " units for " + medicineName);
        return true;
    }
}