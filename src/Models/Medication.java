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

	public void setLowStockAlert(int threshold) {
		this.lowStockThreshold = threshold;
	}

	public boolean isLowStock(Medication medication) {
		return medication.getStock() <= medication.getLowStockThreshold();
	}
}
