package Models;

public interface MedicationInventoryManager {
    static MedicationInventoryManager getInstance(String medicationCsvPath, String requestsCsvPath) {
        return MedicationInventory.getInstance(medicationCsvPath, requestsCsvPath);
    }

    void viewMedicationInventory();
    boolean prescribeMedication(String medication, int quantity);
    void submitReplenishRequest(String medication, int quantity);
    void displayReplenishRequests();
    boolean approveReplenishRequests(String medicineName);
    void addMedication(String medicineName, int initialStock, int lowStockAlert);
    void removeMedication(String medicineName);
    void updateMedication(String medicineName, Integer newStock, Integer newLowStockAlert);
}