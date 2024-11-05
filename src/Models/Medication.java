package Models;

public class Medication {
	private final String medicineName;
	private int stock;
	private int lowStockThreshold;
	
	public Medication(String medicineName, int stock, int lowStockAlert) {
		this.medicineName = medicineName;
		this.stock = stock;
		this.lowStockThreshold = lowStockAlert;
	}

	//other methods
	public boolean isLowStock() {
		return stock <= lowStockThreshold;
	}

	public boolean prescribe(int quantity) {
		if (quantity <= stock) {
			stock -= quantity;
			return true;
		}
		return false;
	}

	public void replenish(int quantity) {
		stock += quantity;
	}

	//convert to csv format
	public String toCSVString() {
        return String.format("%s,%d,%d", medicineName, stock, lowStockThreshold);
    }

	//display formatted string to users
	@Override
    public String toString() {
        return "Medicine: " + medicineName + 
               " | Current Stock: " + stock + 
               " | Low Stock Alert Level: " + lowStockThreshold +
               (isLowStock() ? " (LOW STOCK!)" : "");
    }
	
	// getters and setters
	public String getMedicineName() {
		return medicineName;
	}

	public int getStock() {
		return stock;
	}

	public int getLowStockThreshold() {
		return lowStockThreshold;
	}


	public void setStock(int stock) {
		this.stock = stock;
	}

	public void setLowStockThreshold(int threshold) {
		this.lowStockThreshold = threshold;
	}

}
