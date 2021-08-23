package application.model.game.entity;

import java.util.StringTokenizer;

import application.net.common.Protocol;

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
	
	
	public LoginClient() {
		// TODO Auto-generated constructor stub
	}
	
	public void parseLoginClient(String stringa) {
		
		StringTokenizer stringTokenizer = new StringTokenizer(stringa, Protocol.DELIMITERLOGIN);
		
		username = stringTokenizer.nextToken();
		password = stringTokenizer.nextToken();
	}
}
