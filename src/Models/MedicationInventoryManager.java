package Models;

import Enums.ReplenishStatus;
import Services.MedicationInventoryInterface;
import Utils.CSVHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the inventory of medications and handles replenishment requests.
 * This class supports the initialization of inventory data from CSV files, 
 * adding/removing/updating medication records, prescribing medication, and submitting/reviewing replenishment requests.
 * 
 * <p>The class uses an internal `ReplenishRequest` class for managing replenishment requests, 
 * and relies on a CSV file handler utility (`CSVHandler`) for data persistence.</p>
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */

public class MedicationInventoryManager implements MedicationInventoryInterface {
    private static String csvFilePath;
    private static String requestsFilePath;
    private static final List<Medication> medicationList = new ArrayList<>();
    private static final List<ReplenishRequest> replenishRequests = new ArrayList<>();
    private static boolean isInitialized = false;

    /**
     * Inner class representing a replenish request for a specific medication.
     * It holds information about the medication's name, requested quantity, 
     * and the status of the request.
     */
    private static class ReplenishRequest {
        private final String medicineName;
        private int quantity;
        private ReplenishStatus status;

         /**
         * Constructs a new replenish request with the specified medication name, 
         * quantity, and request status.
         * 
         * @param medicineName The name of the medication being requested for replenishment.
         * @param quantity The quantity requested for replenishment.
         * @param status The current status of the replenish request (e.g., PENDING, APPROVED).
         */
        public ReplenishRequest(String medicineName, int quantity, ReplenishStatus status) {
            this.medicineName = medicineName;
            this.quantity = quantity;
            this.status = status;
        }

        /**
         * Converts the replenish request into a CSV string format.
         * The format is: "medicineName,quantity,status"
         * 
         * @return A CSV string representation of the replenish request.
         */
        public String toCSVString() {
            return String.format("%s,%d,%s", medicineName, quantity, status.name());
        }
    }
    /**
     * Initializes the medication inventory manager with the specified CSV file paths.
     * This method loads medication data and replenish requests from the given files.
     * 
     * @param medicationCsvPath Path to the CSV file containing medication inventory data.
     * @param requestsCsvPath Path to the CSV file containing replenish requests data.
     */
    public static synchronized void initialize(String medicationCsvPath, String requestsCsvPath) {
        if (!isInitialized) {
            csvFilePath = medicationCsvPath;
            requestsFilePath = requestsCsvPath;
            loadMedications();
            loadReplenishRequests();
            isInitialized = true;
        }
    }

    /**
     * Loads medication data from the CSV file into the medication list.
     */
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

    /**
     * Loads replenish requests from the CSV file into the replenish requests list.
     */
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

    /**
     * Saves the current list of medications to the CSV file.
     */
    private static synchronized void saveMedicationList() {
        String[] headers = {"Medicine Name", "Initial Stock", "Low Stock Level Alert"};
        String[] lines = medicationList.stream()
                                     .map(Medication::toCSVString)
                                     .toArray(String[]::new);
        CSVHandler.writeCSVLines(headers, lines, csvFilePath);
    }

    /**
     * Saves the current list of replenish requests to the CSV file.
     */
    private static synchronized void saveReplenishRequests() {
        String[] headers = {"Medicine Name", "Requested Quantity", "Status"};
        String[] lines = replenishRequests.stream()
                                        .map(ReplenishRequest::toCSVString)
                                        .toArray(String[]::new);
        CSVHandler.writeCSVLines(headers, lines, requestsFilePath);
    }

    /**
     * Displays the current medication inventory to the console.
     */
    public static synchronized void viewMedicationInventory() {
        System.out.println("\nCurrent Inventory");
        System.out.println("--------------------");
        for (Medication med : medicationList) {
            System.out.println(med);
        }
    }

    /**
     * Adds a new medication to the inventory.
     * 
     * @param medicineName The name of the medication.
     * @param initialStock The initial stock level of the medication.
     * @param lowStockAlert The low stock threshold for the medication.
     */
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

    /**
     * Removes a medication from the inventory.
     * 
     * @param medicineName The name of the medication to remove.
     */
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

    /**
     * Updates the stock or low stock alert level for a medication.
     * 
     * @param medicineName The name of the medication to update.
     * @param newStock The new stock quantity. If null, the stock is not updated.
     * @param newLowStockAlert The new low stock alert level. If null, the level is not updated.
     */
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

    /**
     * Prescribes a specified quantity of a medication.
     * 
     * @param medicationName The name of the medication to prescribe.
     * @param quantity The quantity of the medication to prescribe.
     * @return True if the medication was successfully prescribed, false otherwise.
     */
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

    /**
     * Submits a replenishment request for a medication.
     * 
     * @param medicationName The name of the medication to request replenishment for.
     * @param quantity The quantity to be replenished.
     */
    public static synchronized void submitReplenishRequest(String medicationName, int quantity) {
        if (!medicationList.stream().anyMatch(med -> med.getName().equals(medicationName))) {
            System.out.println("Medication not found: " + medicationName);
            return;
        }

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

    /**
     * Displays the list of current replenishment requests.
     * 
     * This method prints out the list of all replenish requests stored in the 
     * {@link replenishRequests} list. For each request, the following information 
     * is displayed:
     * <ul>
     * <li>Medicine name</li>
     * <li>Requested quantity</li>
     * <li>Status of the request (e.g., PENDING, APPROVED)</li>
     * </ul>
     * It is synchronized to ensure thread safety when accessing the shared `replenishRequests` list.
     */
    public static synchronized void displayReplenishRequests() {
        System.out.println("\nReplenishment Requests:");
        System.out.println("----------------------");
        for (ReplenishRequest request : replenishRequests) {
            System.out.printf("%s: %d units (Status: %s)%n", 
                request.medicineName, request.quantity, request.status);
        }
    }

    /**
     * Approves a replenishment request for a specified medication.
     * 
     * This method searches for a replenish request associated with the provided 
     * `medicineName`. If a request is found and is in a pending state (i.e., status 
     * is not already APPROVED), the method will attempt to approve it by updating 
     * the medication's stock and the request's status. The medication's stock is 
     * replenished by the quantity specified in the request.
     * 
     * <p>If the requested medication is not found, or if the request has already 
     * been approved, the method prints an appropriate message and returns false. 
     * Otherwise, it updates the medication's stock, marks the request as approved, 
     * and saves the updated data to the corresponding files.</p>
     * 
     * @param medicineName The name of the medication whose replenish request 
     *                     needs to be approved.
     * @return {@code true} if the replenish request was successfully approved and 
     *         processed, {@code false} otherwise.
     */
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

        //update status
        medication.replenish(request.quantity);
        request.status = ReplenishStatus.APPROVED;
        saveMedicationList();
        saveReplenishRequests();

        System.out.println("Approved replenishment of " + request.quantity + " units for " + medicineName);
        return true;
    }

    /**
     * Refreshes the data by reloading the medications and replenish requests.
     * 
     * This method is used to reload the medication and replenish request data 
     * from their respective data sources (e.g., CSV files). It only performs the 
     * reload if the class has been initialized, as indicated by the 
     * {@link isInitialized} flag. This allows for updating the local data with 
     * any changes made externally or between program runs.
     */
    public static void refreshData() {
        if (isInitialized) {
            loadMedications();
            loadReplenishRequests();
        }
    }

    /**
     * Checks whether there are any pending replenish requests.
     * 
     * This method checks if there are any replenishment requests in the 
     * {@link replenishRequests} list that have not yet been approved. It returns 
     * {@code true} if there are pending requests, and {@code false} if there are 
     * none. This method is useful for determining if any replenish requests need 
     * to be processed.
     * 
     * @return {@code true} if there are pending replenish requests, 
     *         {@code false} otherwise.
     */
    public static boolean hasReplenishRequests() {
        return replenishRequests.stream().anyMatch(r -> r.status == ReplenishStatus.PENDING);
    }
    }