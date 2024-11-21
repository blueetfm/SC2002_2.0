package Models;

import Enums.ReplenishStatus;
import Utils.CSVHandler;
import java.util.ArrayList;
import java.util.List;

public class MedicationInventoryManager implements MedicationInventoryInterface {
    private static MedicationInventoryManager instance;
    private final String csvFilePath;
    private final String requestsFilePath;
    private final List<Medication> medicationList;
    private final List<ReplenishRequest> replenishRequests;

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

    private MedicationInventoryManager(String medicationCsvPath, String requestsCsvPath) {
        this.csvFilePath = medicationCsvPath;
        this.requestsFilePath = requestsCsvPath;
        this.medicationList = new ArrayList<>();
        this.replenishRequests = new ArrayList<>();
        loadMedications();
        loadReplenishRequests();
    }

    public static synchronized MedicationInventoryManager getInstance(String medicationCsvPath, String requestsCsvPath) {
        if (instance == null) {
            instance = new MedicationInventoryManager(medicationCsvPath, requestsCsvPath);
        }
        return instance;
    }

    private synchronized void loadMedications() {
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

    private synchronized void loadReplenishRequests() {
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

    private synchronized void saveMedicationList() {
        String[] headers = {"Medicine Name", "Initial Stock", "Low Stock Level Alert"};
        String[] lines = medicationList.stream()
                                     .map(Medication::toCSVString)
                                     .toArray(String[]::new);
        CSVHandler.writeCSVLines(headers, lines, csvFilePath);
    }

    private synchronized void saveReplenishRequests() {
        String[] headers = {"Medicine Name", "Requested Quantity", "Status"};
        String[] lines = replenishRequests.stream()
                                        .map(ReplenishRequest::toCSVString)
                                        .toArray(String[]::new);
        CSVHandler.writeCSVLines(headers, lines, requestsFilePath);
    }

    @Override
    public synchronized void viewMedicationInventory() {
        System.out.println("\nCurrent Inventory");
        System.out.println("--------------------");
        for (Medication med : medicationList) {
            System.out.println(med);
        }
    }

    @Override
    public synchronized void addMedication(String medicineName, int initialStock, int lowStockAlert) {
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

    @Override
    public synchronized void removeMedication(String medicineName) {
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

    @Override
    public synchronized void updateMedication(String medicineName, Integer newStock, Integer newLowStockAlert) {
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

    @Override
    public synchronized boolean prescribeMedication(String medicationName, int quantity) {
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

    @Override
    public synchronized void submitReplenishRequest(String medicationName, int quantity) {
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

    @Override
    public synchronized void displayReplenishRequests() {
        System.out.println("\nReplenishment Requests:");
        System.out.println("----------------------");
        for (ReplenishRequest request : replenishRequests) {
            System.out.printf("%s: %d units (Status: %s)%n", 
                request.medicineName, request.quantity, request.status);
        }
    }

    @Override
    public synchronized boolean approveReplenishRequests(String medicineName) {
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

    public static void clearInstance() {
        if (instance != null) {
            instance.medicationList.clear();
            instance.replenishRequests.clear();
            instance = null;
        }
    }

    public synchronized void refreshData() {
        loadMedications();
        loadReplenishRequests();
    }

    public boolean hasReplenishRequests() {
        return !replenishRequests.isEmpty();
    }
}