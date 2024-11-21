package Models;

public interface MedicationInventoryInterface {
    static MedicationInventoryInterface getInstance(String medicationCsvPath, String requestsCsvPath) {
        return MedicationInventoryManager.getInstance(medicationCsvPath, requestsCsvPath);
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