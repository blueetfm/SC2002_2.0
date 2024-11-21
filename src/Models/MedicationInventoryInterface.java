package Models;

public interface MedicationInventoryInterface {
    static void initialize(String medicationCsvPath, String requestsCsvPath) {}
    static void viewMedicationInventory() {}
    static boolean prescribeMedication(String medication, int quantity) { return false; }
    static void submitReplenishRequest(String medication, int quantity) {}
    static void displayReplenishRequests() {}
    static boolean approveReplenishRequests(String medicineName) { return false; }
    static void addMedication(String medicineName, int initialStock, int lowStockAlert) {}
    static void removeMedication(String medicineName) {}
    static void updateMedication(String medicineName, Integer newStock, Integer newLowStockAlert) {}
    static void refreshData() {}
    static boolean hasReplenishRequests() { return false; }
}