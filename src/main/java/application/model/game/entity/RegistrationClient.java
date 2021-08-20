package application.model.game.entity;

import java.util.StringTokenizer;

import application.net.common.Protocol;

public class RegistrationClient {

	private String username = null ;
	private String password = null ;
	private String email = null ;
	
	public RegistrationClient(String username, String password, String email) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public RegistrationClient() {}
	
	public String getUsername() {
		return username;
	}


	public String getPassword() {
		return password;
	}



	public String getEmail() {
		return email;
	}

	
	public void parseRegistrationClient(String stringa) {
		
		StringTokenizer stringTokenizer = new StringTokenizer(stringa, Protocol.DELIMITERREGISTRATION);
		
		username = stringTokenizer.nextToken();
		password = stringTokenizer.nextToken();
		email    = stringTokenizer.nextToken();
	}
}
