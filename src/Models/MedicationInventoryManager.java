package Models;

import Enums.ReplenishStatus;
import Services.MedicationInventoryInterface;
import Utils.CSVHandler;
import java.util.ArrayList;
import java.util.List;

public class MedicationInventoryManager implements MedicationInventoryInterface {
    private static String csvFilePath;
    private static String requestsFilePath;
    private static final List<Medication> medicationList = new ArrayList<>();
    private static final List<ReplenishRequest> replenishRequests = new ArrayList<>();
    private static boolean isInitialized = false;

    // Inner class to handle replenishment
    private static class ReplenishRequest {
        private final String medicineName;
        private int quantity;
        private ReplenishStatus status;

        public ReplenishRequest(String medicineName, int quantity, ReplenishStatus status) {
            this.medicineName = medicineName;
            this.quantity = quantity;
            this.status = status;
        }

        public String toCSVString() {
            return String.format("%s,%d,%s", medicineName, quantity, status.name());
        }
    }

    public static synchronized void initialize(String medicationCsvPath, String requestsCsvPath) {
        if (!isInitialized) {
            csvFilePath = medicationCsvPath;
            requestsFilePath = requestsCsvPath;
            loadMedications();
            loadReplenishRequests();
            isInitialized = true;
        }
    }

    private static synchronized void loadMedications() {
        medicationList.clear();
        List<List<String>> records = CSVHandler.readCSVLines(csvFilePath);
        
        for (int i = 1; i < records.size(); i++) {
            List<String> record = records.get(i);
            if (record.size() == 3) {
                medicationList.add(new Medication(
                    record.get(0).trim(),
                    Integer.parseInt(record.get(1).trim()),
                    Integer.parseInt(record.get(2).trim())
                ));
            }
        }
    }

    private static synchronized void loadReplenishRequests() {
        replenishRequests.clear();
        List<List<String>> records = CSVHandler.readCSVLines(requestsFilePath);
        
        for (int i = 1; i < records.size(); i++) {
            List<String> record = records.get(i);
            if (record.size() >= 2) {
                ReplenishStatus status = record.size() > 2 ? 
                    ReplenishStatus.valueOf(record.get(2)) : ReplenishStatus.PENDING;
                replenishRequests.add(new ReplenishRequest(
                    record.get(0).trim(),
                    Integer.parseInt(record.get(1).trim()),
                    status
                ));
            }
        }
    }

    private static synchronized void saveMedicationList() {
        String[] headers = {"Medicine Name", "Initial Stock", "Low Stock Level Alert"};
        String[] lines = medicationList.stream()
                                     .map(Medication::toCSVString)
                                     .toArray(String[]::new);
        CSVHandler.writeCSVLines(headers, lines, csvFilePath);
    }

    private static synchronized void saveReplenishRequests() {
        String[] headers = {"Medicine Name", "Requested Quantity", "Status"};
        String[] lines = replenishRequests.stream()
                                        .map(ReplenishRequest::toCSVString)
                                        .toArray(String[]::new);
        CSVHandler.writeCSVLines(headers, lines, requestsFilePath);
    }

    public static synchronized void viewMedicationInventory() {
        System.out.println("\nCurrent Inventory");
        System.out.println("--------------------");
        for (Medication med : medicationList) {
            System.out.println(med);
        }
    }

    public static synchronized void addMedication(String medicineName, int initialStock, int lowStockAlert) {
        if (medicineName == null || medicineName.trim().isEmpty()) {
            System.out.println("Medication name cannot be empty");
            return;
        }

        if (medicationList.stream().anyMatch(med -> med.getName().equalsIgnoreCase(medicineName.trim()))) {
            System.out.println("Medication already exists: " + medicineName);
            return;
        }

        medicationList.add(new Medication(medicineName.trim(), initialStock, lowStockAlert));
        saveMedicationList();
        System.out.println("Successfully added new medication: " + medicineName);
    }

    public static synchronized void removeMedication(String medicineName) {
        boolean removed = medicationList.removeIf(med -> med.getName().equals(medicineName));
        
        if (removed) {
            saveMedicationList();
            System.out.println("Removed medication: " + medicineName);
            System.out.println("\n===Updated Inventory===");
            viewMedicationInventory();
        } else {
            System.out.println("Medication not found: " + medicineName);
        }
    }

    public static synchronized void updateMedication(String medicineName, Integer newStock, Integer newLowStockAlert) {
        boolean updated = false;
        
        for (int i = 0; i < medicationList.size(); i++) {
            Medication med = medicationList.get(i);
            if (med.getName().equals(medicineName)) {
                int currentStock = (newStock != null) ? newStock : med.getCurrentStock();
                int lowStockLevel = (newLowStockAlert != null) ? newLowStockAlert : med.getLowStockThreshold();
                
                medicationList.set(i, new Medication(medicineName, currentStock, lowStockLevel));
                updated = true;
                break;
            }
        }
        
        if (updated) {
            saveMedicationList();
            System.out.println("Updated quantities for medication: " + medicineName);
        } else {
            System.out.println("Medication not found: " + medicineName);
        }
    }

    public static synchronized boolean prescribeMedication(String medicationName, int quantity) {
        boolean prescribed = false;
        
        for (int i = 0; i < medicationList.size(); i++) {
            Medication med = medicationList.get(i);
            if (med.getName().equals(medicationName)) {
                prescribed = med.prescribe(quantity);
                if (prescribed) {
                    medicationList.set(i, med);
                    System.out.println("Prescribed " + quantity + " units of " + medicationName);
                    if (med.isLowStock()) {
                        System.out.println("Warning: " + medicationName + " is now low in stock!");
                    }
                } else {
                    System.out.println("Insufficient stock for prescription");
                }
                break;
            }
        }
        
        if (prescribed) {
            saveMedicationList();
        } else {
            System.out.println("Medication not found: " + medicationName);
        }
        
        return prescribed;
    }

    public static synchronized void submitReplenishRequest(String medicationName, int quantity) {
        if (!medicationList.stream().anyMatch(med -> med.getName().equals(medicationName))) {
            System.out.println("Medication not found: " + medicationName);
            return;
        }

        // Update existing request or add new one
        boolean updated = false;
        for (ReplenishRequest request : replenishRequests) {
            if (request.medicineName.equals(medicationName)) {
                request.quantity = quantity;
                request.status = ReplenishStatus.PENDING;
                updated = true;
                break;
            }
        }

        if (!updated) {
            replenishRequests.add(new ReplenishRequest(medicationName, quantity, ReplenishStatus.PENDING));
        }

        saveReplenishRequests();
        System.out.println("Replenish request submitted to administrator.");
    }

    public static synchronized void displayReplenishRequests() {
        System.out.println("\nReplenishment Requests:");
        System.out.println("----------------------");
        for (ReplenishRequest request : replenishRequests) {
            System.out.printf("%s: %d units (Status: %s)%n", 
                request.medicineName, request.quantity, request.status);
        }
    }

    public static synchronized boolean approveReplenishRequests(String medicineName) {
        ReplenishRequest request = replenishRequests.stream()
            .filter(r -> r.medicineName.equals(medicineName))
            .findFirst()
            .orElse(null);

        if (request == null || request.status == ReplenishStatus.APPROVED) {
            System.out.println("No pending replenish request found for: " + medicineName);
            return false;
        }

        Medication medication = medicationList.stream()
            .filter(med -> med.getName().equals(medicineName))
            .findFirst()
            .orElse(null);

        if (medication == null) {
            System.out.println("Medication no longer exists in inventory");
            return false;
        }

        // Update medication stock
        medication.replenish(request.quantity);
        
        // Update status and save
        request.status = ReplenishStatus.APPROVED;
        saveMedicationList();
        saveReplenishRequests();

        System.out.println("Approved replenishment of " + request.quantity + " units for " + medicineName);
        return true;
    }

    public static void refreshData() {
        if (isInitialized) {
            loadMedications();
            loadReplenishRequests();
        }
    }

    public static boolean hasReplenishRequests() {
        return !replenishRequests.isEmpty();
    }
}