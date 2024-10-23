package Models;

public class Medicine {
	String medicineName;
	int stock;
	int lowStockAlert;

	public Medicine() {
		this.medicineName = null;
		this.stock = 0;
		this.lowStockAlert = 0;
	}
	
	public Medicine(String medicineName, int stock, int lowStockAlert) {
		this.medicineName = medicineName;
		this.stock = stock;
		this.lowStockAlert = lowStockAlert;
	}

}
