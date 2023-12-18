package backend.plant;
/* Arrays */ 
import java.util.ArrayList;
import java.util.Collections;
/* File related */
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
/* Exceptions */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import backend.exceptions.PriceException;
import backend.exceptions.QuantityException;
 
public class PlantService { 
	private String plantsFile = "";
	private String soldPlantsFile = "";
	private ArrayList<Plant> plants = new ArrayList<Plant>();
	
	public PlantService(String _plantsFile, String _soldPlantsFile) { 
		this.plantsFile = _plantsFile;
		this.soldPlantsFile = _soldPlantsFile;
		this.plants = this.fetchPlants();
	}
	
	/*
	 * Fetches plants information from the file line by line.
	 * Used for initiating plant list in the constructor.
	 * */
	private ArrayList<Plant> fetchPlants() {
		try {
			File plantsRepository = new File(this.plantsFile);
			BufferedReader br = new BufferedReader(new FileReader(plantsRepository));
			String line = null;
			String name = "";
			int qty = 0;
			double price = 0;
			 
			while((line = br.readLine()) != null) {
				String[] splittedLine = line.split(" ");
				
				if (splittedLine.length > 0) {
					price = Double.parseDouble(splittedLine[splittedLine.length - 1]);
					qty = Integer.parseInt(splittedLine[splittedLine.length - 2]);
					
					for (int i = 0; i < splittedLine.length - 2; i++) {
						name += splittedLine[i] + " ";
					}
					
					Plant p = new Plant(name.trim(), qty, price);
					this.plants.add(p);
					name = "";
				}
			}

			br.close();
			 
		 } catch (FileNotFoundException ex) {
			 System.out.println("Unable to open file '" + this.plantsFile + "'");
		 } catch(IOException ex) {
			 System.out.println("Error reading file '" + this.plantsFile + "'");
		 }
		
		this.sortRepository();
		return plants; 
	 }
	
	public ArrayList<Plant> getPlants() {
		return this.plants;
	}
	 
	/* 
	 * Adds a plant to the file and also to this.plants.
	 * If the plant doesn't exist it appends it to the end of the file, also in the plants ArrayList.
	 * Otherwise, plants quantity gets updated in the array also in the file.
	 * */
	public void addPlant(Plant p) throws PriceException, QuantityException {
		try {
			p.setPrice(Double.parseDouble(new DecimalFormat("##.##").format(p.getPrice())));
			File plantsRepository = new File(this.plantsFile);
			// Check if plant already exists.
			boolean alreadyExists = false;
			for (int i = 0; i < plants.size(); i++) {
				if (plants.get(i).getName().equals(p.getName())) {
					alreadyExists = true;
				}
			}
			 
			if (!alreadyExists) {
				BufferedWriter bw = new BufferedWriter(new FileWriter(plantsRepository, true));
				// Add the plant to the array.
				this.plants.add(p);				 
				// Build and append plant data to the plants file.
				String content = p.getName() + " " + p.getQty() + " " + p.getPrice();
				bw.write(content + "\n");
				bw.close();
			} else {
				this.updateQty(p.getName(), p.getQty());
				this.updatePrice(p.getName(), p.getPrice());
			}
			 
			 this.sortRepository();
		 } catch (FileNotFoundException ex) {
			 System.out.println("Unable to open file '" + this.plantsFile + "'");
		 } catch(IOException ex) {
			 System.out.println("Error reading file '" + this.plantsFile + "'");
		 } 
	 }
	
	public void deletePlant(Plant p) {
		try {
			String newContent = "";
			String name = "";
			double price;
			int qty;
			
			for (int i = 0; i < this.plants.size(); i++) {
				if (p.getName().equals(this.plants.get(i).getName())) this.plants.remove(i);
			}
			
			for (int i = 0; i < this.plants.size(); i++) {
				name = this.plants.get(i).getName();
				qty = this.plants.get(i).getQty();
				price = this.plants.get(i).getPrice();
				newContent += name + " " + qty + " " + price + "\n";
			}

			File plantsRepository = new File(this.plantsFile);
			BufferedWriter bw = new BufferedWriter(new FileWriter(plantsRepository));
			bw.write(newContent);
			bw.close();
			
		 } catch (FileNotFoundException ex) {
			 System.out.println("Unable to open file '" + this.plantsFile + "'");
		 } catch(IOException ex) {
			 System.out.println("Error reading file '" + this.plantsFile + "'");
		 } 
	}
	 
	public void updatePrice(String plantName, double newPrice) throws PriceException {
		try {
			newPrice = Double.parseDouble(new DecimalFormat("##.##").format(newPrice));
			File plantsRepository = new File(this.plantsFile);
			BufferedReader br = new BufferedReader(new FileReader(plantsRepository));
			String line = null;
			String newContent = "";
			String qty;
			double oldPrice;
			String name = "";
			
			while ((line = br.readLine()) != null ) {
				String[] splittedLine = line.split(" ");
				oldPrice = Double.parseDouble(splittedLine[splittedLine.length - 1]);
				qty = splittedLine[splittedLine.length - 2];
				// Build plant name. (it can be made up from 1+ words therefore looping is needed)
				for (int i = 0; i < splittedLine.length - 2; i++) {
					name += splittedLine[i] + " ";
				}
				
				if (plantName.equals(name.trim())) {
					for (int i = 0; i < plants.size(); i++) {
						if (plants.get(i).getName().equals(plantName) && oldPrice != newPrice) {
							plants.get(i).setPrice(newPrice);
							break;
						}
					}
					
					if (oldPrice != newPrice) {
						newContent += name.trim() + " " + qty + " " + newPrice + "\n";
					} else {
						newContent += name.trim() + " " + qty + " " + oldPrice + "\n";
					}
				}
				name = "";
			}
			
			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter(plantsRepository));
			bw.write(newContent);
			bw.close();
			
		}  catch (FileNotFoundException ex) {
			 System.out.println("Unable to open file '" + this.plantsFile + "'");
		 } catch(IOException ex) {
			 System.out.println("Error reading file '" + this.plantsFile + "'");
		 }
	}
	
	/* Updates quantity of a plant in the file and also in this.plants
	 * Used in add a new plant and buy plant.
	 * */
	public int updateQty(String plantName, int desiredQty) throws QuantityException {
		int response = 1;
		try {
			File plantsRepository = new File(this.plantsFile);
			BufferedReader br = new BufferedReader(new FileReader(plantsRepository));
			String line = null;
			String newContent = "";
			String name = "";
			int actualQty = 0;
			double price = 0;
			 
			while ((line = br.readLine()) != null ) {
				/* Split every line into words
				 * to access the name, quantity and price
				 */
				String[] splittedLine = line.split(" ");
				price = Double.parseDouble(splittedLine[splittedLine.length - 1]);
				actualQty = Integer.parseInt(splittedLine[splittedLine.length - 2]);
				// Build plant name. (it can be made up from 1+ words therefore looping is needed)
				for (int i = 0; i < splittedLine.length - 2; i++) {
					name += splittedLine[i] + " ";
				}
				 
				// If plant already exists, update quantity, but don't add a new record of it.
				if (plantName.equals(name.trim())) {
					for (int i = 0; i < plants.size(); i++) {
						if (plants.get(i).getName().equals(plantName) && desiredQty + actualQty >= 0) {
							plants.get(i).setQty(desiredQty + actualQty); 
						}
					}
					 
					if (desiredQty + actualQty < 0) {
						newContent += name.trim() + " " + actualQty + " " + price + "\n";
					} else {
						newContent += name.trim() + " " + (desiredQty + actualQty) + " " + price + "\n";
					}
					 
				} else {
					newContent += name.trim() + " " + actualQty + " " + price + "\n";
				}
				name = "";
			}
			
			br.close();
			// Update plant file.
			BufferedWriter bw = new BufferedWriter(new FileWriter(plantsRepository));
			bw.write(newContent);
			bw.close();
			
		 } catch (FileNotFoundException ex) {
			 response = -1;
			 System.out.println("Unable to open file '" + this.plantsFile + "'");
		 } catch(IOException ex) {
			 response = -1;
			 System.out.println("Error reading file '" + this.plantsFile + "'");
		 }
		
		return response;
	 }
	 
	public double calculatePrice(String name, int qty) {
		double toPay = 0;
		for (int i = 0; i < this.plants.size(); i++) {
			Plant p = plants.get(i);
			if (p.getName().equals(name) ) {
					toPay = p.getPrice() * qty;
			}
		}

		return Double.parseDouble(new DecimalFormat("##.##").format(toPay));
	}
	 
	public void buyPlant(String name, int desiredQty, String username) throws QuantityException {
		try {
			int response = this.updateQty(name, -desiredQty);
			File soldPlantsRepository = new File(this.soldPlantsFile);
			BufferedWriter bw = new BufferedWriter(new FileWriter(soldPlantsRepository, true));
			
			if (response == 1 && desiredQty > 0) {
				double price = this.calculatePrice(name, desiredQty);
				String content = name + " " + desiredQty + " " + price + " by " + username;
				bw.write(content + "\n");
			}
	
			bw.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + this.plantsFile + "'");
		} catch(IOException ex) {
			System.out.println("Error reading file '" + this.plantsFile + "'");
		} catch(QuantityException ex) {
			 System.out.println(ex.getMessage());
		}
	}
	 
	/*
	 * Utility: Sorts the plants file alphabetically.
	 */
	private void sortRepository() {
		Collections.sort(this.plants);
	
		try {			 			 
			String content = "";
			String name = "";
			int qty = 0;
			double price = 0;
	
			for (int i = 0; i < this.plants.size(); i++) {
				name = this.plants.get(i).getName();
				qty = this.plants.get(i).getQty();
				price = this.plants.get(i).getPrice();
				content += name + " " + qty + " " + price + "\n";
			}
			 
			File plantsRepository = new File(this.plantsFile);
			BufferedWriter bw = new BufferedWriter(new FileWriter(plantsRepository));
			bw.write(content);
			bw.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + this.plantsFile + "'");
		} catch(IOException ex) {
			System.out.println("Error reading file '" + this.plantsFile + "'");
		}
	}
	 
	/*
	 * Utility: Logs the plants array.
	 */
	public void logPlants() {
		String name = "";
		int qty = 0;
		double price = 0;
		for (int i = 0; i < plants.size(); i++) {
			name = this.plants.get(i).getName();
			qty = this.plants.get(i).getQty();
			price = this.plants.get(i).getPrice();
			System.out.println(name + " " + qty + " " + price);
		}
	}
 }
 