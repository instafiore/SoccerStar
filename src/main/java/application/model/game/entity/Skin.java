package application.model.game.entity;

import java.util.StringTokenizer;

import application.net.common.Protocol;

public class Skin {

	private String name = "" ;
	private String price = "" ;
	private String color = "" ;
	
	public Skin() {}

	
	public Skin(String name, String price, String color) {
		super();
		this.name = name;
		this.price = price;
		this.color = color;
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	
	public void loadSkin(String string) {
		StringTokenizer stringTokenizer = new StringTokenizer(string, Protocol.DELIMITERINFORMATIONSKIN) ;
		
		setName(stringTokenizer.nextToken());
		setPrice(stringTokenizer.nextToken());
		setColor(stringTokenizer.nextToken());
	}
}
