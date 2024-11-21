package Models;

/**
 * The {@code Medication} class represents a medicine item in a pharmacy or healthcare setting. 
 * It contains details about the medicine's name, stock quantity, and low stock alert threshold. 
 * This class provides functionality for managing the stock, checking stock levels, prescribing, 
 * replenishing stock, and converting the information to a CSV string for export.
 * 
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
public class Medication {
    
    /** The name of the medicine. */
    private final String medicineName;
    
    /** The current stock level of the medicine. */
    private int stock;
    
    /** The threshold level of stock at which a low stock alert is triggered. */
    private int lowStockThreshold;

    /**
     * Constructs a new {@code Medication} object with the specified name, initial stock level, 
     * and low stock alert threshold.
     * 
     * @param medicineName The name of the medicine.
     * @param stock The initial stock level of the medicine.
     * @param lowStockAlert The stock threshold that triggers a low stock alert.
     */
    public Medication(String medicineName, int stock, int lowStockAlert) {
        this.medicineName = medicineName;
        this.stock = stock;
        this.lowStockThreshold = lowStockAlert;
    }

    /**
     * Checks if the current stock of the medicine is below or equal to the low stock threshold.
     * 
     * @return {@code true} if the stock is low or equal to the low stock threshold, otherwise {@code false}.
     */
    public boolean isLowStock() {
        return stock <= lowStockThreshold;
    }

    /**
     * Prescribes a specified quantity of the medicine. If the quantity is available in stock, 
     * it is deducted from the current stock.
     * 
     * @param quantity The quantity of the medicine to prescribe.
     * @return {@code true} if the prescription was successful (i.e., enough stock is available), 
     *         {@code false} otherwise (if the requested quantity exceeds available stock).
     */
    public boolean prescribe(int quantity) {
        if (quantity <= stock) {
            stock -= quantity;
            return true;
        }
        return false;
    }

    /**
     * Replenishes the stock of the medicine by a specified quantity.
     * 
     * @param quantity The quantity to add to the stock.
     */
    public void replenish(int quantity) {
        stock += quantity;
    }

    /**
     * Converts the details of this {@code Medication} to a CSV-formatted string.
     * The format will be: "medicineName, stock, lowStockThreshold".
     * 
     * @return A CSV-formatted string representing the medication details.
     */
    public String toCSVString() {
        return String.format("%s,%d,%d", medicineName, stock, lowStockThreshold);
    }

    /**
     * Returns a string representation of this {@code Medication} in a human-readable format. 
     * If the stock is below or equal to the low stock threshold, the string includes a "LOW STOCK!" message.
     * 
     * @return A formatted string representing the medication details.
     */
    @Override
    public String toString() {
        return "Medicine: " + medicineName + 
               " | Current Stock: " + stock + 
               " | Low Stock Alert Level: " + lowStockThreshold +
               (isLowStock() ? " (LOW STOCK!)" : "");
    }

    // Getters and Setters

    /**
     * Gets the name of the medicine.
     * 
     * @return The name of the medicine.
     */
    public String getName() {
        return medicineName;
    }

    /**
     * Gets the current stock level of the medicine.
     * 
     * @return The current stock of the medicine.
     */
    public int getCurrentStock() {
        return stock;
    }

    /**
     * Gets the low stock threshold for this medicine.
     * 
     * @return The low stock threshold.
     */
    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    /**
     * Sets the current stock level of the medicine.
     * 
     * @param stock The new stock level.
     */
    public void setCurrentStock(int stock) {
        this.stock = stock;
    }

    /**
     * Sets the low stock threshold for this medicine.
     * 
     * @param threshold The new low stock threshold.
     */
    public void setLowStockThreshold(int threshold) {
        this.lowStockThreshold = threshold;
    }
}
