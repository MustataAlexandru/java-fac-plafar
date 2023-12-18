package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import backend.user.UserService;

public class Dashboard extends JFrame {
	// Data
	private static final long serialVersionUID = 3643040403140703126L;
	private String usersFile;
	private String currentUser;
	private String plantsFile;
	private String soldPlantsFile;
	private UserService us;
	private JButton logOutBtn;
	
	public Dashboard(String _usersFile, String user) {
		this.usersFile = _usersFile;
		this.currentUser = user;
		us = new UserService(this.usersFile);
		
		if (us.checkUsername(this.currentUser)) {
			this.plantsFile = "./src/database/plants/plants";
			this.soldPlantsFile = "./src/database/plants/planteVandute";
			
			this.initFrame(); 
			this.initHandlers();
		} else {
			new Login(this.usersFile);
		}
		
	}
	
	private void initFrame() {
		this.setFont(new Font("Helvetica", Font.PLAIN, 16));
		this.setTitle("Plafar");
		this.setSize(new Dimension(800, 600));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		JPanel logOutPanel = new JPanel();
		logOutPanel.setLayout(new BorderLayout());
		logOutPanel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
		this.logOutBtn = new JButton("Log Out");
		logOutBtn.setForeground(Color.red.darker());
		logOutPanel.add(logOutBtn, BorderLayout.EAST);
		
		Container mainContainer = this.getContentPane();
		mainContainer.setLayout(new BorderLayout(5, 0));
		
		mainContainer.add(new DashboardHeader(this.currentUser), BorderLayout.NORTH);
		mainContainer.add(new DashboardBody(this.plantsFile, this.soldPlantsFile, this.currentUser), BorderLayout.CENTER);
		mainContainer.add(logOutPanel, BorderLayout.PAGE_END);
		
		this.setVisible(true);
	}
	
	private void initHandlers() {
		LogOutHandler logOutHandler = new LogOutHandler();
		logOutBtn.addActionListener(logOutHandler);
	}
	
	private class LogOutHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == logOutBtn) {
				int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?");
				if (input == 0) {
					dispose();
					new Login(usersFile);
				}				
			}
			
		} 
		
	}
}
