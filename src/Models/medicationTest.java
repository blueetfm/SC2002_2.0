package Models;

public class medicationTest {
    public static void main(String[] args) {
        // Initialize with paths to both CSV files
        MedicationInventory inventory = new MedicationInventory("data(testCopy)/Medicine_List.csv", "data(testCopy)/medication_requests.csv");

        System.out.println("=== Initial Inventory ===");
        inventory.viewMedicationInventory();

        System.out.println("\n=== Testing Prescribe Medication ===");
        inventory.prescribeMedication("Paracetamol", 30);
        inventory.prescribeMedication("Ibuprofen", 20);
        System.out.println("\nInventory after prescriptions:");
        inventory.viewMedicationInventory();

        System.out.println("\n=== Testing Replenish Requests ===");
        inventory.submitReplenishRequest("Paracetamol", 50);
        inventory.submitReplenishRequest("Ibuprofen", 30);
        System.out.println("\nCurrent replenish requests:");
        inventory.displayReplenishRequests();

        System.out.println("\n=== Testing Approve Replenish Requests ===");
        inventory.approveReplenishRequests("Paracetamol");
        System.out.println("\nInventory after approval:");
        inventory.viewMedicationInventory();
        System.out.println("\nRemaining requests:");
        inventory.displayReplenishRequests();

        System.out.println("\n=== Testing Error Cases ===");
        System.out.println("\nTrying to prescribe non-existent medication:");
        inventory.prescribeMedication("NonExistent", 10);

        System.out.println("\nTrying to prescribe more than available stock:");
        inventory.prescribeMedication("Amoxicillin", 100);

        System.out.println("\nTrying to submit request for non-existent medication:");
        inventory.submitReplenishRequest("NonExistent", 50);

        System.out.println("\nTrying to approve non-existent request:");
        inventory.approveReplenishRequests("NonExistent");
    }
}