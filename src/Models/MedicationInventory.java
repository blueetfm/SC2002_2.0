package Models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MedicationInventory implements MedicationInventoryManager {
    public Map<String, Medication> medications;
    public Map<String, Integer> requests;

    public MedicationInventory() {
        this.medications = new HashMap<>();
        this.requests = new HashMap<>();
    }

    public void loadMedicineCSV(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            // Skip header
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                
                try {
                    if (parts.length == 3) {
                        String name = parts[0].trim();
                        int initialStock = Integer.parseInt(parts[1].trim());
                        int lowStockLevel = Integer.parseInt(parts[2].trim());
                        
                        medications.put(name, new Medication(name, initialStock, lowStockLevel));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing number in line: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found: " + e.getMessage());
        }
    }
    
    @Override
    public void viewMedicationInventory() {
        System.out.println("\nCurrent Inventory");
        System.out.println("--------------------");
        for (Medication med : medications.values()) {
            System.out.println(med);
        }        
    }

    @Override
    public boolean prescribeMedication(String medicationName, int quantity) {
        Medication medication = medications.get(medicationName);
        if (medication == null) {
            System.out.println("Medication not found: " + medicationName);
            return false;
        }
        else {
            boolean prescribed = medication.prescribe(quantity);
            if (prescribed) {
                System.out.println("Prescribed " + quantity + " units of " + medicationName);
                if (medication.isLowStock()) {
                    System.out.println("Warning: " + medicationName + " is now low in stock!");
                }
            } else {
                System.out.println("Insufficient stock for prescription");
            }
            return prescribed;
        }
    }

    @Override
    public void submitReplenishRequest(String medicationName, int quantity) {
        Medication medication = medications.get(medicationName);
        if (medication == null) {
            System.out.println("Medication not found: " + medicationName);
            return;
        }

        requests.put(medicationName, quantity);
        System.out.println("Replenish request submitted to administrator. ");
    }

    @Override
    public void displayReplenishRequests() {
        System.out.println("\nReplenishment Requests:");
        System.out.println("----------------------");
        for (Map.Entry<String, Integer> request : requests.entrySet()) {
            System.out.print(request.getKey() + ": ");
            System.out.println(request.getValue());
        }
    }

    @Override
    public boolean approveReplenishRequests(String medicineName) {
        if (!requests.containsKey(medicineName)) {
            System.out.println("No replenish request found for: " + medicineName);
            return false;
        }

        Medication medication = medications.get(medicineName);
        int requestedQuantity = requests.get(medicineName);

        if (medication == null) {
            System.out.println("Medication no longer exists in inventory");
            return false;
        }

        medication.replenish(requestedQuantity);
        requests.remove(medicineName);

        System.out.println("Approved replenishment of " + requestedQuantity + " units for " + medicineName);
        System.out.println("New stock level: " + medication.getStock());

        return true;    
    }
}