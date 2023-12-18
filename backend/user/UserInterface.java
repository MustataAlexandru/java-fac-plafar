package backend.user;

public interface UserInterface {
	String username = "";
	String password = "";
	
	public String getUsername();
	public String getPassword();
	public void setUsername(String _username);
	public void setPassword(String _password);
}
