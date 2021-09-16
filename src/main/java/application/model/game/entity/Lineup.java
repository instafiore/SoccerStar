package application.model.game.entity;

import java.util.StringTokenizer;

import com.sun.javafx.image.impl.ByteIndexed.Getter;

import application.net.common.Protocol;

public class Lineup {
	
	private String id = "" ;
	private String name = "" ;
	private String price = "" ;
	private String image = "" ;
	private boolean owned = false ;
	
	public Lineup() {}

	
	public Lineup(String name, String price, String image) {
		super();
		this.name = name;
		this.price = price;
		this.image = image;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	
	public void loadLineup(String string) {
		StringTokenizer stringTokenizer = new StringTokenizer(string, Protocol.DELIMITERINFORMATIONELEMENTSHOP) ;
		
		setId(stringTokenizer.nextToken());
		setName(stringTokenizer.nextToken());
		setPrice(stringTokenizer.nextToken());
		setImage(stringTokenizer.nextToken());
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
