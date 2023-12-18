package backend.plant;

import backend.plant.PlantInterface;
import frontend.Utils;

public class Plant implements PlantInterface, Comparable<Plant> {
	private String name;
	private int qty;
	private double price;
	
	public Plant(String _name, int _qty, double _price) {
		this.name = _name;
		this.qty = _qty;
		this.price = _price;
	}
	
	// Getters.
	public String getName() {
		return this.name;
	}
	
	public int getQty() {
		return this.qty;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	// Setters.
	public void setName(String _name) {
		this.name = _name;
	}
	
	public void setQty(int _qty) {
		this.qty = _qty;
	}
	
	public void setPrice(double _price) {
		this.price = _price;
	}

	@Override
	public int compareTo(Plant o) {
		return this.name.compareTo(o.getName());
	}
	
	@Override
	public String toString() {
		String plantName = this.name;
		String plantQty = String.valueOf(this.qty);
		String plantPrice = String.valueOf(this.price);
		
		plantName = Utils.formatString(plantName, 15);
		plantQty = Utils.formatString(plantQty, 7);
		
		return plantName + " " + plantQty + " " + plantPrice;
	}
}
