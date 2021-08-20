package application.model.game.entity;

public class LoginClient {

	private String username ;
	private String password ;
	
	public LoginClient(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
}
