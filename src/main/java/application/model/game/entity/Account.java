package application.model.game.entity;

import java.util.StringTokenizer;

import application.net.common.Protocol;

public class Account {

	private String username = "" ;
	private String password = "" ;
	private int coins  ;
	private String email = "" ;
	private String color_my_balls = "" ;
	private String color_ball_to_play = "" ;
	private int lineup ;
	
	public Account(String username, String password, int coins, String email, String color_balls,
			String color_ball_to_play, int lineup) {
		super();
		this.username = username;
		this.password = password;
		this.coins = coins;
		this.email = email;
		this.color_my_balls = color_balls;
		this.color_ball_to_play = color_ball_to_play;
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

	public String getColor_my_balls() {
		return color_my_balls;
	}

	public void setColor_my_balls(String color_balls) {
		this.color_my_balls = color_balls;
	}

	public String getColor_ball_to_play() {
		return color_ball_to_play;
	}

	public void setColor_ball_to_play(String color_ball_to_play) {
		this.color_ball_to_play = color_ball_to_play;
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
		setColor_ball_to_play(stringTokenizer.nextToken());
		setColor_my_balls(stringTokenizer.nextToken());
		setEmail(stringTokenizer.nextToken());
		setLineup(Integer.parseInt(stringTokenizer.nextToken()));
	
	}
	
}
