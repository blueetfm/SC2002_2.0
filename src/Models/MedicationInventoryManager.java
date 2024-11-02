package Models;


public interface MedicationInventoryManager {
    public void viewMedicationInventory();
    public boolean prescribeMedication(String medication, int quantity);
    public void submitReplenishRequest(String medication, int quantity);
    public void displayReplenishRequests();
    public boolean approveReplenishRequests(String medicineName);
}
