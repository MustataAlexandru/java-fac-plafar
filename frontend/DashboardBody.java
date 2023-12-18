package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import backend.exceptions.PriceException;
import backend.exceptions.QuantityException;
import backend.plant.Plant;
import backend.plant.PlantService;

public class DashboardBody extends JPanel {
	private static final long serialVersionUID = 4430460849005114063L;
	/* Data */
	private String currentUser;
	private PlantService ps;
	private ArrayList<Plant> plants = new ArrayList<Plant>();
	private String plantsFile;
	private String soldPlantsFile;
	private String selectedPlant;
	private String totalPrice;
	/* List related */
	private JList<String> plantsList;
	private DefaultListModel<String> listModel;
	/* List interface related */
	private JPanel listPanel, btnPanel;
	private JScrollPane listScroll;
	private JButton buyPlantBtn, deletePlantBtn;
	private JSpinner qtySpinner;
	private JPanel qtyPanel;
	private JLabel totalValue;
	/* Add / update plant related */
	private JPanel addPlantPanel;
	private JLabel plantName, plantQty, plantPrice;
	private JTextField nameField, qtyField, priceField;
	private JButton addPlantBtn;
	
	public DashboardBody(String _plantsFile, String _soldPlantsFile, String _crtUser) {
		this.plantsFile = _plantsFile;
		this.soldPlantsFile = _soldPlantsFile;
		this.currentUser = _crtUser;
		this.selectedPlant = "";
		this.ps = new PlantService(this.plantsFile, this.soldPlantsFile);
		this.plants = ps.getPlants();
		
		this.setLayout(new BorderLayout(0, 10));
		this.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
		this.initBuyPlant();
		this.initAddPlantPanel();
		this.initHandlers();
	}
	
	private void initBuyPlant() {
		this.listPanel = new JPanel();
		this.btnPanel = new JPanel();
		
		listPanel.setLayout(new BorderLayout(0, 10));
		btnPanel.setLayout(new BorderLayout(0, 5));
		
		this.initList();
		
		buyPlantBtn = new JButton("Buy plant");
		deletePlantBtn = new JButton("Delete plant");
		
		buyPlantBtn.setPreferredSize(new Dimension(270, 25));
		deletePlantBtn.setPreferredSize(new Dimension(270, 25));
		deletePlantBtn.setForeground(Color.red.darker());
		
		btnPanel.add(buyPlantBtn, BorderLayout.PAGE_START);
		btnPanel.add(deletePlantBtn, BorderLayout.PAGE_END);
		
		this.listPanel.add(btnPanel, BorderLayout.PAGE_END);
		this.add(listPanel, BorderLayout.WEST);
	}
	
	private void initAddPlantPanel() {
		this.addPlantPanel = new JPanel();
		this.addPlantPanel.setLayout(new BorderLayout(5, 5));
		
		JPanel addPlantContainer = new JPanel();
		addPlantContainer.setLayout(new GridBagLayout());
		
		TitledBorder borderTitle = new TitledBorder("Add / Update a plant");
		addPlantContainer.setBorder(borderTitle);
		
		this.plantName = new JLabel("Plant Name");
		this.plantQty = new JLabel("Plant Qty");
		this.plantPrice = new JLabel("Plant Price");
		this.nameField = new JTextField(15);
		this.qtyField = new JTextField(5);
		this.priceField = new JTextField(5);
		this.addPlantBtn = new JButton("Add plant");

		nameField.setHorizontalAlignment(JTextField.CENTER);
		qtyField.setHorizontalAlignment(JTextField.CENTER);
		priceField.setHorizontalAlignment(JTextField.CENTER);
		
		Utils.addComp(addPlantContainer, plantName, 0, 0, 10, 20, 0, 20);
		Utils.addComp(addPlantContainer, plantQty, 1, 0, 10, 20, 0, 20);
		Utils.addComp(addPlantContainer, plantPrice, 2, 0, 10, 20, 0, 20);
		Utils.addComp(addPlantContainer, nameField, 0, 1, 10, 20, 10, 20);
		Utils.addComp(addPlantContainer, qtyField, 1, 1, 10, 20, 10, 20);
		Utils.addComp(addPlantContainer, priceField, 2, 1, 10, 20, 10, 20);
		
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 2;
		gc.gridwidth = 3;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 20, 10, 0);
		
		addPlantBtn.setPreferredSize(new Dimension(345, 25));
		addPlantContainer.add(addPlantBtn, gc);
		
		this.addPlantPanel.add(addPlantContainer, BorderLayout.PAGE_START);
		this.add(addPlantPanel, BorderLayout.EAST);
	}
	
	private void initHandlers() {
		PlantHandler plantHandler = new PlantHandler();
		
		addPlantBtn.addActionListener(plantHandler);
		deletePlantBtn.addActionListener(plantHandler);
		buyPlantBtn.addActionListener(plantHandler);
	}
	
	public void initList() {
		listModel = new DefaultListModel<String>();
		
		for (Plant plant : this.plants) {
			listModel.addElement(plant.toString());
		
		}
		plantsList = new JList<String>(listModel);
		plantsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		plantsList.setFont(new Font("monospaced", Font.BOLD, 14));
		listScroll = new JScrollPane(plantsList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listScroll.setPreferredSize(new Dimension(280, 0));
		this.listPanel.add(listScroll, BorderLayout.WEST);
	}
	
	private void qtyDialog(double price, int maxQty) {
		this.qtyPanel = new JPanel();
		qtyPanel.setLayout(new GridBagLayout());
		
		SpinnerNumberModel qtySpinnerModel;
		JLabel qtyLabel = new JLabel("Insert quantity: ");
		JLabel total = new JLabel("Total: ");
		qtyLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
		total.setFont(new Font("Helvetica", Font.BOLD, 16));
		
		qtySpinnerModel = new SpinnerNumberModel(0, 0, maxQty, 1);
	
		this.qtySpinner = new JSpinner(qtySpinnerModel);
		this.qtySpinner.setPreferredSize(new Dimension(50, 30));
		this.qtySpinner.setFont(new Font("Helvetica", Font.BOLD, 14));
		
		totalValue = new JLabel("0.00");
		totalValue.setFont(new Font("Helvetica", Font.BOLD, 14));
		
		Utils.addComp(qtyPanel, qtyLabel, 0, 0, 0, 0, 0, 5);
		Utils.addComp(qtyPanel, qtySpinner , 1, 0, 0, 0, 0, 0);
		Utils.addComp(qtyPanel, total, 0, 1, 10, 0, 10, 75);
		Utils.addComp(qtyPanel, totalValue, 1, 1, 0, 0, 0, 0);
		
		SpinnerHandler spinnerHandler = new SpinnerHandler();
		qtySpinner.addChangeListener(spinnerHandler);
		
		int input  = JOptionPane.showConfirmDialog(null, qtyPanel, this.selectedPlant, JOptionPane.PLAIN_MESSAGE);
		if (input == 0) {
			int spinnerValue = (int) qtySpinner.getValue();
	
			try {
				if (spinnerValue > maxQty) throw new QuantityException("Desired quantity is bigger than actual quantity!");
				else ps.buyPlant(this.selectedPlant.trim(), spinnerValue, this.currentUser);
			} catch (QuantityException ex) {
				JFrame f = new JFrame();
				JOptionPane.showMessageDialog(f, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			
			this.refreshPanel();
		}
	}
	
	public void refreshPanel() {
		this.listPanel.remove(plantsList);
		this.listPanel.remove(listScroll);
	
		this.initList();
		
		this.listPanel.revalidate();
		this.listPanel.repaint();
	}
	
	private class PlantHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buyPlantBtn) {
				selectedPlant = plantsList.getSelectedValue();
				if (selectedPlant == null) {
					JFrame f = new JFrame();
					JOptionPane.showMessageDialog(f, "Please make sure you have selected a plant.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					String[] plantData = selectedPlant.split("\\s+");
					
					double price = Double.parseDouble(plantData[plantData.length - 1]);
					int qty = Integer.parseInt(plantData[plantData.length - 2]);
					selectedPlant = "";
					for (int i = 0; i < plantData.length - 2; i++) {
						selectedPlant += plantData[i] + " ";
					}
					
					qtyDialog(price, qty);
				}
			}
			
			if (e.getSource() == deletePlantBtn) {
				selectedPlant = plantsList.getSelectedValue();
				if (selectedPlant == null) {
					JFrame f = new JFrame();
					JOptionPane.showMessageDialog(f, "Please make sure you have selected a plant.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					String[] plantData = selectedPlant.split("\\s+");
					double price = Double.parseDouble(plantData[plantData.length - 1]);
					int qty = Integer.parseInt(plantData[plantData.length - 2]);
					selectedPlant = "";
					for (int i = 0; i < plantData.length - 2; i++) {
						selectedPlant += plantData[i] + " ";
					}
					
					int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + selectedPlant + " ?");
					if (input == 0) {	
						ps.deletePlant(new Plant(selectedPlant.trim(), qty, price));
						refreshPanel();
					}	
				}
			}
			
			if (e.getSource() == addPlantBtn) {
				if (nameField.getText().length() == 0 || qtyField.getText().length() == 0 || priceField.getText().length() == 0) {
					JFrame f = new JFrame();
					JOptionPane.showMessageDialog(f, "Please make sure all fields are completed.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					String plantName = nameField.getText();
					int plantQty = Integer.parseInt(qtyField.getText());
					double plantPrice = Double.parseDouble(priceField.getText());
					plantName = plantName.toLowerCase().substring(0, 1).toUpperCase() + plantName.substring(1);
					
					Plant p = new Plant(plantName, plantQty, plantPrice);
					try {
						if (plantPrice <= 0) throw new PriceException("Price should be greater than 0.");
						else if (plantQty <= 0) throw new QuantityException("Quantity sould be greater than 0.");
						else ps.addPlant(p);
					} catch (PriceException ex) {
						JFrame f = new JFrame();
						JOptionPane.showMessageDialog(f, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (QuantityException ex) {
						JFrame f = new JFrame();
						JOptionPane.showMessageDialog(f, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
					
					refreshPanel();
					nameField.setText("");
					qtyField.setText("");
					priceField.setText("");
					nameField.requestFocus();
				}
			}
			
		}
		
	}
	
	private class SpinnerHandler implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			if (e.getSource() == qtySpinner) {
				totalPrice = String.valueOf(ps.calculatePrice(selectedPlant.trim(), (int) qtySpinner.getValue()));
				totalValue.setText(totalPrice);
			}
		}
		
	}
	
}
