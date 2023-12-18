package backend.user;

public class User implements UserInterface{
	private String username;
	private String password;
	
	public User(String _username, String _password) {
		this.username = _username;
		this.password = _password;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setUsername(String _username) {
		this.username = _username;
	}
	
	public void setPassword (String _password) {
		this.password = _password;
	}
}
