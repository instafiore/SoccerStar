package application.model.game.entity;

import java.util.StringTokenizer;

import application.net.common.Protocol;

public class Skin {

	private String name = "" ;
	private String price = "" ;
	private String color = "" ;
	private boolean owned = false ;
	private boolean using  = false ;
	
	public Skin() {}

	
	public Skin(String name, String price, String color) {
		super();
		this.name = name;
		this.price = price;
		this.color = color;
	}

	public void setUsing(boolean using) {
		this.using = using;
	}
	
	public boolean isUsing() {
		return using;
	}
	
	public void setOwned(boolean owned) {
		this.owned = owned;
	}
	
	public boolean isOwned() {
		return owned;
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
		StringTokenizer stringTokenizer = new StringTokenizer(string, Protocol.DELIMITERINFORMATIONELEMENTSHOP) ;
		
		setName(stringTokenizer.nextToken());
		setPrice(stringTokenizer.nextToken());
		setColor(stringTokenizer.nextToken());
	}

	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false ;
		if(this == obj)
			return true ;
		
		if(!(obj instanceof Skin))
			return false ;
		
		Skin s = (Skin) obj ;
		
		return s.getName().equals(name);
	}
}
