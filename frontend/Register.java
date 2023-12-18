package frontend;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import backend.user.UserService;
import backend.user.User;

public class Register extends JFrame {
	private static final long serialVersionUID = 8943072450658424827L;
	private String usersFile;
	JLabel registerLabel, username, password, confirm, errorLabel, successLabel, goToLogin;
	JTextField usernameField, passwordField, confirmField;
	JButton registerBtn;
	
	public Register(String _usersFile) {
		this.usersFile = _usersFile;
		this.initFrame();
		this.initHandlers();
		this.getRootPane().setDefaultButton(registerBtn);
	}
	
	private void initFrame() {
		this.setFont(new Font("Helvetica", Font.PLAIN, 16));
		this.setLayout(new GridBagLayout());
		this.setTitle("Plafar - Register");
		this.setMinimumSize(new Dimension(400, 700));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		JPanel registerPanel = new JPanel();
		registerPanel.setLayout(new GridBagLayout());
		
		registerLabel = new JLabel("REGISTER");
		username = new JLabel("Username");
		password = new JLabel("Password");
		confirm = new JLabel("Confirm Password");
		errorLabel = new JLabel("");
		successLabel = new JLabel("");
		goToLogin = new JLabel("Already have an account? Click here to log in.");
		usernameField = new JTextField(20);
		passwordField = new JTextField(20);
		confirmField = new JTextField(20);
		registerBtn = new JButton("Register");
		
		registerLabel.setFont(new Font("Helvetica", Font.BOLD, 32));
		usernameField.setHorizontalAlignment(JTextField.CENTER);
		passwordField.setHorizontalAlignment(JTextField.CENTER);
		confirmField.setHorizontalAlignment(JTextField.CENTER);
		errorLabel.setForeground(Color.red.darker());
		successLabel.setForeground(Color.green.darker());
		goToLogin.setForeground(Color.blue.darker());
		goToLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		registerBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		registerBtn.setPreferredSize(new Dimension(205, 25));
		
		Utils.addComp(registerPanel, registerLabel, 0, 0, 0, 0, 100, 0);
		Utils.addComp(registerPanel, username, 0, 1, 0, 0, 3, 0);
		Utils.addComp(registerPanel, usernameField, 0, 2, 0, 0, 7, 0);
		Utils.addComp(registerPanel, password, 0, 3, 0, 0, 3, 0);
		Utils.addComp(registerPanel, passwordField, 0, 4, 0, 0, 7, 0);
		Utils.addComp(registerPanel, confirm, 0, 5, 0, 0, 7, 0);
		Utils.addComp(registerPanel, confirmField, 0, 6, 0, 0, 15, 0);
		Utils.addComp(registerPanel, registerBtn, 0, 7, 0, 0, 15, 0);
		Utils.addComp(registerPanel, errorLabel, 0, 8, 0, 0, 15, 0);
		Utils.addComp(registerPanel, successLabel, 0, 9, 0, 0, 100, 0);
		Utils.addComp(registerPanel, goToLogin, 0, 10, 0, 0, 0, 0);
		
		this.setVisible(true);
		this.add(registerPanel);
	}
	
	private void initHandlers() {
		RegisterHandler registerHandler = new RegisterHandler();
		GoToLoginHandler goToLoginHandler = new GoToLoginHandler();
		
		registerBtn.addActionListener(registerHandler);
		goToLogin.addMouseListener(goToLoginHandler);
	}
	
	private class RegisterHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == registerBtn) {
				String username = usernameField.getText();
				String password = passwordField.getText();
				String confirm = confirmField.getText();
				
				if (username.trim().length() < 3) {
					errorLabel.setText("Username cannot be empty. (at least 3 characters).");
					successLabel.setText("");
				} else if (password.length() < 6) {
					errorLabel.setText("Password should be at least 6 characters.");
					successLabel.setText("");
				} else if (!password.equals(confirm)) {
					errorLabel.setText("Password and confirm password fields should be the same.");
					successLabel.setText("");
				} else {
					UserService us = new UserService(usersFile);
					User newUser = new User(username, password);
					/*
					 * ** Possible response values **
					 * 201 - OK, new user created
					 * 409 - Conflict (User name already exists).
					 * 400 - Bad request (Pass length < 6).
					 */
					int response = us.registerUser(newUser);
					
					switch (response) {
						case 409:
							errorLabel.setText("Username already exists");
							successLabel.setText("");
							break;
						case 400:
							errorLabel.setText("Password should be at least 6 characters.");
							successLabel.setText("");
							break;
						default:
							errorLabel.setText("");
							successLabel.setText("Successfully registered an account, you can now log in");
							setVisible(false);
							JFrame f = new JFrame();
							JOptionPane.showMessageDialog(f, "Account has been successfully created. You may now log in.");
							dispose();
							new Login(usersFile);
							break;
					}
					
				}
			}
		}
		
	}
	
	private class GoToLoginHandler implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == goToLogin) {
				setVisible(false);
				dispose();
				new Login(usersFile);
			}
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
