package Models;

public class Medication {
	private String medicineName;
	private int stock;
	private int lowStockThreshold;

	public Medication() {
		this.medicineName = null;
		this.stock = 0;
		this.lowStockThreshold = 0;
	}
	
	public Medication(String medicineName, int stock, int lowStockAlert) {
		this.medicineName = medicineName;
		this.stock = stock;
		this.lowStockThreshold = lowStockAlert;
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

	@Override
    public String toString() {
        return "Medicine: " + medicineName + 
               " | Current Stock: " + stock + 
               " | Low Stock Alert Level: " + lowStockThreshold +
               (isLowStock() ? " (LOW STOCK!)" : "");
    }
}
