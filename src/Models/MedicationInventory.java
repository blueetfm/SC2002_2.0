package Models;

import Utils.CSVHandler;
import java.util.ArrayList;
import java.util.List;

public class MedicationInventory implements MedicationInventoryManager {
    private static MedicationInventory instance;
    private final String csvFilePath;
    private final String requestsFilePath;

    private MedicationInventory(String medicationCsvPath, String requestsCsvPath) {
        this.csvFilePath = medicationCsvPath;
        this.requestsFilePath = requestsCsvPath;
    }

    public static synchronized MedicationInventory getInstance(String medicationCsvPath, String requestsCsvPath) {
        if (instance == null) {
            instance = new MedicationInventory(medicationCsvPath, requestsCsvPath);
        }
        return instance;
    }
    
    @Override
    public void viewMedicationInventory() {
        System.out.println("\nCurrent Inventory");
        System.out.println("--------------------");
        List<List<String>> records = CSVHandler.readCSVLines(csvFilePath);
        
        for (int i = 1; i < records.size(); i++) {
            List<String> record = records.get(i);
            if (record.size() == 3) {
                Medication med = new Medication(
                    record.get(0).trim(),
                    Integer.parseInt(record.get(1).trim()),
                    Integer.parseInt(record.get(2).trim())
                );
                System.out.println(med);
            }
        }
    }
    
    @Override
    public void addMedication(String medicineName, int initialStock, int lowStockAlert) {
        if (medicineName == null || medicineName.trim().isEmpty()) {
            System.out.println("Medication name cannot be empty");
            return;
        }
        
        List<List<String>> records = CSVHandler.readCSVLines(csvFilePath);
        
        for (List<String> record : records) {
            if (record.get(0).trim().equalsIgnoreCase(medicineName.trim())) {
                System.out.println("Medication already exists: " + medicineName);
                return;
            }
        }
        
        String[] lines = {String.format("%s,%d,%d", medicineName.trim(), initialStock, lowStockAlert)};
        CSVHandler.writeCSVLines(null, lines, csvFilePath);
        System.out.println("Successfully added new medication: " + medicineName);
    }

    @Override
    public void removeMedication(String medicineName) {
        List<List<String>> records = CSVHandler.readCSVLines(csvFilePath);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;
        
        updatedLines.add(String.join(",", records.get(0)));
        
        for (int i = 1; i < records.size(); i++) {
            List<String> record = records.get(i);
            if (!record.get(0).trim().equals(medicineName)) {
                updatedLines.add(String.join(",", record));
            } else {
                found = true;
            }
        }
        
        if (found) {
            CSVHandler.writeCSVLines(
                null,
                updatedLines.toArray(new String[0]),
                csvFilePath
            );
            System.out.println("Removed medication: " + medicineName);
            System.out.println("\n===Updated Inventory===");
            viewMedicationInventory();
        } else {
            System.out.println("Medication not found: " + medicineName);
        }
    }

    @Override
    public void updateMedication(String medicineName, Integer newStock, Integer newLowStockAlert) {
        List<List<String>> records = CSVHandler.readCSVLines(csvFilePath);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;
        
        updatedLines.add(String.join(",", records.get(0)));
        
        for (int i = 1; i < records.size(); i++) {
            List<String> record = records.get(i);
            if (record.get(0).trim().equals(medicineName)) {
                int currentStock = Integer.parseInt(record.get(1));
                int currentLowStock = Integer.parseInt(record.get(2));
                
                int updatedStock = (newStock != null) ? newStock : currentStock;
                int updatedLowStock = (newLowStockAlert != null) ? newLowStockAlert : currentLowStock;
                
                Medication updatedMed = new Medication(medicineName, updatedStock, updatedLowStock);
                updatedLines.add(updatedMed.toCSVString());
                found = true;
            } else {
                updatedLines.add(String.join(",", record));
            }
        }
        
        if (found) {
            CSVHandler.writeCSVLines(
                null,
                updatedLines.toArray(new String[0]),
                csvFilePath
            );
            System.out.println("Updated quantities for medication: " + medicineName);
        } else {
            System.out.println("Medication not found: " + medicineName);
        }
    }

    @Override
    public boolean prescribeMedication(String medicationName, int quantity) {
        List<List<String>> records = CSVHandler.readCSVLines(csvFilePath);
        List<String> updatedLines = new ArrayList<>();
        boolean prescribed = false;
        
        updatedLines.add(String.join(",", records.get(0)));
        
        for (int i = 1; i < records.size(); i++) {
            List<String> record = records.get(i);
            if (record.get(0).trim().equals(medicationName)) {
                Medication med = new Medication(
                    record.get(0).trim(),
                    Integer.parseInt(record.get(1).trim()),
                    Integer.parseInt(record.get(2).trim())
                );
                
                prescribed = med.prescribe(quantity);
                if (prescribed) {
                    updatedLines.add(med.toCSVString());
                    System.out.println("Prescribed " + quantity + " units of " + medicationName);
                    if (med.isLowStock()) {
                        System.out.println("Warning: " + medicationName + " is now low in stock!");
                    }
                } else {
                    updatedLines.add(String.join(",", record));
                    System.out.println("Insufficient stock for prescription");
                }
            } else {
                updatedLines.add(String.join(",", record));
            }
        }
        
        if (prescribed) {
            CSVHandler.writeCSVLines(
                null,
                updatedLines.toArray(new String[0]),
                csvFilePath
            );
        } else {
            System.out.println("Medication not found: " + medicationName);
        }
        
        return prescribed;
    }

    @Override
    public void submitReplenishRequest(String medicationName, int quantity) {
        List<List<String>> inventoryRecords = CSVHandler.readCSVLines(csvFilePath);
        boolean medicationExists = false;
        
        for (int i = 1; i < inventoryRecords.size(); i++) {
            if (inventoryRecords.get(i).get(0).equals(medicationName)) {
                medicationExists = true;
                break;
            }
        }
        
        if (!medicationExists) {
            System.out.println("Medication not found: " + medicationName);
            return;
        }

        List<List<String>> requestRecords = CSVHandler.readCSVLines(requestsFilePath);
        List<String> updatedLines = new ArrayList<>();
        boolean updated = false;
        
        updatedLines.add(String.join(",", requestRecords.get(0)));
        
        for (int i = 1; i < requestRecords.size(); i++) {
            List<String> record = requestRecords.get(i);
            if (record.get(0).equals(medicationName)) {
                updatedLines.add(String.format("%s,%d", medicationName, quantity));
                updated = true;
            } else {
                updatedLines.add(String.join(",", record));
            }
        }

        if (!updated) {
            updatedLines.add(String.format("%s,%d", medicationName, quantity));
        }

        CSVHandler.writeCSVLines(
            null,
            updatedLines.toArray(new String[0]),
            requestsFilePath
        );
        System.out.println("Replenish request submitted to administrator.");
    }

    @Override
    public void displayReplenishRequests() {
        System.out.println("\nReplenishment Requests:");
        System.out.println("----------------------");
        List<List<String>> records = CSVHandler.readCSVLines(requestsFilePath);
        
        for (int i = 1; i < records.size(); i++) {
            List<String> record = records.get(i);
            if (record.size() == 2) {
                System.out.println(record.get(0) + ": " + record.get(1));
            }
        }
    }

    @Override
    public boolean approveReplenishRequests(String medicineName) {
        List<List<String>> requestRecords = CSVHandler.readCSVLines(requestsFilePath);
        List<List<String>> inventoryRecords = CSVHandler.readCSVLines(csvFilePath);
        List<String> updatedRequests = new ArrayList<>();
        boolean requestFound = false;
        int requestedQuantity = 0;

        updatedRequests.add(String.join(",", requestRecords.get(0)));
        for (int i = 1; i < requestRecords.size(); i++) {
            List<String> record = requestRecords.get(i);
            if (record.get(0).equals(medicineName)) {
                requestFound = true;
                requestedQuantity = Integer.parseInt(record.get(1));
            } else {
                updatedRequests.add(String.join(",", record));
            }
        }

        if (!requestFound) {
            System.out.println("No replenish request found for: " + medicineName);
            return false;
        }

        List<String> updatedInventory = new ArrayList<>();
        boolean updated = false;
        
        updatedInventory.add(String.join(",", inventoryRecords.get(0)));
        for (int i = 1; i < inventoryRecords.size(); i++) {
            List<String> record = inventoryRecords.get(i);
            if (record.get(0).equals(medicineName)) {
                int currentStock = Integer.parseInt(record.get(1));
                int lowStockLevel = Integer.parseInt(record.get(2));
                Medication med = new Medication(medicineName, currentStock, lowStockLevel);
                med.replenish(requestedQuantity);
                updatedInventory.add(med.toCSVString());
                updated = true;
            } else {
                updatedInventory.add(String.join(",", record));
            }
        }

        if (!updated) {
            System.out.println("Medication no longer exists in inventory");
            return false;
        }

        CSVHandler.writeCSVLines(null, updatedRequests.toArray(new String[0]), requestsFilePath);
        CSVHandler.writeCSVLines(null, updatedInventory.toArray(new String[0]), csvFilePath);

        System.out.println("Approved replenishment of " + requestedQuantity + " units for " + medicineName);
        return true;
    }

    public boolean hasReplenishRequests() {
        List<List<String>> records = CSVHandler.readCSVLines(requestsFilePath);
        return records.size() > 1;
    }
}