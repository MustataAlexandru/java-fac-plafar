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
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import backend.user.UserService;
import backend.user.User;

public class Login extends JFrame {
	private static final long serialVersionUID = -3487631947484103828L;
	private String usersFile;
	JLabel username, password, loginLabel, showPassword, goToRegister, errorLabel;
	JTextField usernameField;
	JPasswordField passwordField;
	JButton loginBtn;
	JCheckBox showPass;
	
	public Login(String _usersFile) {
		this.usersFile = _usersFile;
		this.initFrame();
		this.initHandlers();
		this.getRootPane().setDefaultButton(loginBtn);
	}
	
	private void initHandlers() {
		LoginHandler loginHandler = new LoginHandler();
		GoToRegisterHandler goToRegisterHandler = new GoToRegisterHandler();
		
		loginBtn.addActionListener(loginHandler);
		showPass.addActionListener(loginHandler);
		goToRegister.addMouseListener(goToRegisterHandler);
	}
	
	private void initFrame() {
		this.setFont(new Font("Helvetica", Font.PLAIN, 16));
		this.setLayout(new GridBagLayout());
		this.setTitle("Plafar - Login");
		this.setMinimumSize(new Dimension(400, 700));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new GridBagLayout());
		
		loginLabel = new JLabel("LOGIN");
		username = new JLabel("Username");
		password = new JLabel("Password");
		errorLabel = new JLabel("");
		goToRegister = new JLabel("Don't have an account? Click here to register.");
		usernameField = new JTextField(20);
		passwordField = new JPasswordField(20);
		loginBtn = new JButton("Log In");
		showPass = new JCheckBox("Show password", false);
		
		loginLabel.setFont(new Font("Helvetica", Font.BOLD, 32));
		usernameField.setHorizontalAlignment(JTextField.CENTER);
		passwordField.setHorizontalAlignment(JTextField.CENTER);
		passwordField.setEchoChar('*');
		errorLabel.setForeground(Color.red.darker());
		goToRegister.setForeground(Color.blue.darker());
		goToRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loginBtn.setPreferredSize(new Dimension(205, 25));
		loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		showPass.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		Utils.addComp(loginPanel, loginLabel, 0, 0, 0, 0, 100, 0);
		Utils.addComp(loginPanel, username, 0, 1, 0, 0, 3, 0);
		Utils.addComp(loginPanel, usernameField, 0, 2, 0, 0, 7, 0);
		Utils.addComp(loginPanel, password, 0, 3, 0, 0, 3, 0);
		Utils.addComp(loginPanel, passwordField, 0, 4, 0, 0, 7, 0);
		Utils.addComp(loginPanel, showPass, 0, 5, 0, 0, 15, 0);
		Utils.addComp(loginPanel, loginBtn, 0, 6, 0, 0, 15, 0);
		Utils.addComp(loginPanel, errorLabel, 0, 7, 0, 0, 100, 0);
		Utils.addComp(loginPanel, goToRegister, 0, 8, 0, 0, 0, 0);
		
		this.setVisible(true);
		this.add(loginPanel);
	}
	
	// Handlers
	private class LoginHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loginBtn) {
				String user = usernameField.getText();
				char[] _pass = passwordField.getPassword();
				String pass = String.valueOf(_pass);
				User u = new User(user, pass);
				UserService us = new UserService(usersFile);
				/*
				 * ** Possible response values **
				 * 404 - User not found.
				 * 403 - Forbidden (Wrong password).
				 * 200 - OK (Successfully logged in).
				 */
				int response = us.login(u); 
				if (response == 200) {
					errorLabel.setText("");
					dispose();
					new Dashboard(usersFile, u.getUsername());
				} else { 
					errorLabel.setText("Wrong username or password.");
				}
			}
			
			if (e.getSource() == showPass) {
				if (showPass.isSelected())
					passwordField.setEchoChar((char)0);
				else
					passwordField.setEchoChar('*');
			}
		}
	}
	
	private class GoToRegisterHandler implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == goToRegister) {
				setVisible(false);
				dispose();
				new Register(usersFile);
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
