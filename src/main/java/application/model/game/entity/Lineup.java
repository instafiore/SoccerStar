package application.model.game.entity;

import java.nio.charset.Charset;
import java.sql.Blob;
import java.util.StringTokenizer;

import com.sun.javafx.image.impl.ByteIndexed.Getter;

import application.net.common.Protocol;

public class Lineup {
	
	private int id  ;
	private String name = "" ;
	private String price = "" ;

	private byte[] image = null ;
 	private boolean owned = false ;
	private boolean using = false ;
	
	public Lineup() {}

	
	public Lineup(int id, String name, String price, String modulo, byte[] image, boolean owned, boolean using) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
		this.owned = owned;
		this.using = using;
	}




	public void setImage(byte[] image) {
		this.image = image;
	}
	
	
	public byte[] getImage() {
		return image;
	}
	
	public void setUsing(boolean using) {
		this.using = using;
	}
	
	public boolean isUsing() {
		return using;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
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

	public void loadLineup(String string) {
		StringTokenizer stringTokenizer = new StringTokenizer(string, Protocol.DELIMITERINFORMATIONELEMENTSHOP) ;
		
		setId(Integer.parseInt(stringTokenizer.nextToken()));
		setName(stringTokenizer.nextToken());
		setPrice(stringTokenizer.nextToken());
		
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
