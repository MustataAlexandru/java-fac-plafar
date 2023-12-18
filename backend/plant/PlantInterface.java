package backend.plant;

public interface PlantInterface {
	String name = "";
	int qty = 0;
	double price = 0;
	
	public String getName();
	public int getQty();
	public double getPrice();
	public void setName(String _name);
	public void setQty(int _qty);
	public void setPrice(double _price);
}
