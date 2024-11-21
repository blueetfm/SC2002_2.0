package Services;

import Models.MedicationInventoryManager;

public interface MedicationInventoryInterface {
    static void initialize(String medicationCsvPath, String requestsCsvPath) {
        MedicationInventoryManager.initialize(medicationCsvPath, requestsCsvPath);
    }
 
    static void viewMedicationInventory() {
        MedicationInventoryManager.viewMedicationInventory();
    }
 
    static boolean prescribeMedication(String medication, int quantity) {
        return MedicationInventoryManager.prescribeMedication(medication, quantity);
    }
 
    static void submitReplenishRequest(String medication, int quantity) {
        MedicationInventoryManager.submitReplenishRequest(medication, quantity);
    }
 
    static void displayReplenishRequests() {
        MedicationInventoryManager.displayReplenishRequests();
    }
 
    static boolean approveReplenishRequests(String medicineName) {
        return MedicationInventoryManager.approveReplenishRequests(medicineName);
    }
 
    static void addMedication(String medicineName, int initialStock, int lowStockAlert) {
        MedicationInventoryManager.addMedication(medicineName, initialStock, lowStockAlert);
    }
 
    static void removeMedication(String medicineName) {
        MedicationInventoryManager.removeMedication(medicineName);
    }
 
    static void updateMedication(String medicineName, Integer newStock, Integer newLowStockAlert) {
        MedicationInventoryManager.updateMedication(medicineName, newStock, newLowStockAlert);
    }
 
    static void refreshData() {
        MedicationInventoryManager.refreshData();
    }
 
    static boolean hasReplenishRequests() {
        return MedicationInventoryManager.hasReplenishRequests();
    }
 }