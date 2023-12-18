package backend.user;

/* Encryption */
import backend.bcrypt.BCrypt;
/* File related imports */
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
/* Exceptions */
import java.io.FileNotFoundException;
import java.io.IOException;
import backend.exceptions.PasswordException;
import backend.exceptions.UserException;

public class UserService {
	private String usersFile;
	
	public UserService(String _usersFile) {
		this.usersFile = _usersFile;
	}
	/*
	 * Checks if a user already exists.
	 */
	public boolean checkUsername(String username) {
		boolean alreadyExists = false;
		
		try {
			File usersRepository = new File(this.usersFile);
			BufferedReader br = new BufferedReader(new FileReader(usersRepository));
			
			String line = null;
			String _username;
			
			while((line = br.readLine()) != null) {
				_username = line.split(" ")[0]; 
				
				if (username.equals(_username)) {
					alreadyExists = true;
					break;
				}
			}
			br.close();
			
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + this.usersFile + "'");
		} catch(IOException ex) {
			System.out.println("Error reading file '" + this.usersFile + "'");
		}
		return alreadyExists;
	}
	
	private static String hashPw(String password) {
		String hashedPw = BCrypt.hashpw(password, BCrypt.gensalt());
		
		return hashedPw;
	}
	
	private String findHashedPw(String username) {
		String hashedPw = "";
		
		try {
			File usersRepository = new File(this.usersFile);
			BufferedReader br = new BufferedReader(new FileReader(usersRepository));
			
			String line = null;
			String storedUsername = "";
			while ((line = br.readLine()) != null) {
				storedUsername = line.split(" ")[0];
				if (username.equals(storedUsername)) {
					hashedPw = line.split(" ")[1];
					break;
				}
			}
			br.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + this.usersFile + "'");
		} catch(IOException ex) {
			System.out.println("Error reading file '" + this.usersFile + "'");
		}
		
		return hashedPw;
	}
	
	public int registerUser(User user) {
		int result = 201;
		try {
			if(this.checkUsername(user.getUsername())) {
				throw new UserException("Username already exists.");
			} else {
				if (user.getPassword().length() < 6) {
					throw new PasswordException("Password should be at least 6 characters.");
				} else {
					user.setPassword(UserService.hashPw(user.getPassword()));
					
					File usersRepository = new File(this.usersFile);
					BufferedWriter bw = new BufferedWriter(new FileWriter(usersRepository, true));
					
					String userData = user.getUsername() + " " + user.getPassword();
					bw.write(userData);
					bw.newLine();
					bw.close();
					System.out.println("Successfuly registered!");
				}
			}
			
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + this.usersFile + "'");
		} catch(IOException ex) {
			System.out.println("Error reading file '" + this.usersFile + "'");
		} catch (UserException ex) {
			System.out.println(ex.getMessage());
			result = 409; // Conflict, user name already exists
		} catch (PasswordException ex) {
			System.out.println(ex.getMessage());
			result = 400; // Bad request, pass length < 6
		}
		
		return result;
	}
	
	public int login(User user) {
		int result = 0;
		try {
			if (!this.checkUsername(user.getUsername()) ) {
				throw new UserException("User not found. Please register.");
				
			} else {
				String hashedPw = this.findHashedPw(user.getUsername());
				if (!BCrypt.checkpw(user.getPassword(), hashedPw)) {
					throw new PasswordException("Wrong password.");
				} else {
					result = 200;
				}
			}
		} catch (UserException ex) {
			System.out.println(ex.getMessage());
			result = 404;
			
		} catch (PasswordException ex) {
			System.out.println(ex.getMessage());
			result = 403;
		}
		
		return result;
	}
	
}

