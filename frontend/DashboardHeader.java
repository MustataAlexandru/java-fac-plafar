package frontend;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DashboardHeader extends JPanel {
	private static final long serialVersionUID = -130478114896962071L;
	JLabel dashboardLabel, crtUser, nameLabel, qtyLabel, priceLabel;
	String user;
	
	public DashboardHeader(String _crtUser) {
		this.user = _crtUser;
		this.initPanel();
	}
	
	private void initPanel() {
		this.setLayout(new BorderLayout(5, 0));
		this.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 15));
		JPanel labels = new JPanel();
		labels.setLayout(new GridBagLayout());
		
		dashboardLabel = new JLabel("Plafar", SwingConstants.CENTER);
		nameLabel = new JLabel("Name");
		qtyLabel = new JLabel("Qty");
		priceLabel = new JLabel("Price");
		crtUser = new JLabel("Logged user: " + this.user);
		
		dashboardLabel.setFont(new Font("Helvetica", Font.BOLD, 24));
		nameLabel.setFont(new Font("monospaced", Font.BOLD, 14));
		qtyLabel.setFont(new Font("monospaced", Font.BOLD, 14));
		priceLabel.setFont(new Font("monospaced", Font.BOLD, 14));
		
		this.add(dashboardLabel, BorderLayout.PAGE_START);
		this.add(crtUser, BorderLayout.LINE_END);
		this.add(labels, BorderLayout.LINE_START);
		
		Utils.addComp(labels, nameLabel, 0, 0, 0, 0, 0, 110);
		Utils.addComp(labels, qtyLabel, 1, 0, 0, 0, 0, 40);
		Utils.addComp(labels, priceLabel, 2, 0, 0, 0, 0, 0);
	}
}