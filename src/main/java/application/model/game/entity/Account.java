package application.model.game.entity;

import java.util.StringTokenizer;

import application.net.common.Protocol;

public class Account {

	private String username = "" ;
	private String password = "" ;
	private int coins  ;
	private String email = "" ;
	private String current_skin = "" ;
	private int lineup ;
	
	public Account(String username, String password, int coins, String email, String color_balls,
			String color_ball_to_play, int lineup) {
		super();
		this.username = username;
		this.password = password;
		this.coins = coins;
		this.email = email;
		this.current_skin = color_balls;
		this.lineup = lineup;
	}
	
	public Account() {}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCurrentSkin() {
		return current_skin;
	}

	public void setCurrentSkin(String color_balls) {
		this.current_skin = color_balls;
	}


	public int getLineup() {
		return lineup;
	}

	public void setLineup(int lineup) {
		this.lineup = lineup;
	}
	
	public void loadAccount(String string) {
		
		StringTokenizer stringTokenizer = new StringTokenizer(string, Protocol.DELIMITERINFORMATIONACCOUNT);
		
		setUsername(stringTokenizer.nextToken());
		setPassword(stringTokenizer.nextToken());
		setCoins(Integer.parseInt(stringTokenizer.nextToken()));
		setCurrentSkin(stringTokenizer.nextToken());
		setEmail(stringTokenizer.nextToken());
		setLineup(Integer.parseInt(stringTokenizer.nextToken()));
	
	}
	
}
