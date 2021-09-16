package application.model.game.handler;

import java.util.ArrayList;
import java.util.StringTokenizer;

import application.model.game.entity.Skin;
import application.net.common.Protocol;

public class SkinHandler {

	private ArrayList<Skin> skins ;
	
	private static SkinHandler instance = null ;
	
	
	public static SkinHandler getInstance() {
		if(instance == null )
			instance = new SkinHandler() ;
		return instance;
	}
	
	private SkinHandler() {
		
		skins = new ArrayList<Skin>();
		
	}
	
	public void add(Skin skin) {
		skins.add(skin);
	}
	
	public void clear() {
		skins.clear();
	}
	
	public void remove(Skin skin) {
		skins.remove(skin);
	}
	
	public ArrayList<Skin> getSkins() {
		return skins;
	}
	
	public ArrayList<Skin> getSkinsOwned() {
		
		ArrayList<Skin> skinsOwned = new ArrayList<Skin>();
		
		for(Skin skin : skins)
			if(skin.isOwned())
				skinsOwned.add(skin);
		
		return skinsOwned ;
	}
	
	public void setUsing(String name) {
		for(Skin skin : skins)
			if(skin.getName().equals(name))
				skin.setUsing(true);
			else
				skin.setUsing(false);
	}
	
	public Skin getSkinFromColor(String color) {
		
		for(Skin skin : skins)
			if(skin.getColor().equals(color))
				return skin ;
		return null ;
	}
	
	public void setOwned(String name) {
		
		for(Skin skin : skins)
			if(skin.getName().equals(name))
			{
				skin.setOwned(true);
				return ;
			}
	}
	
	public void loadOwned(String string) {
		StringTokenizer stringTokenizer = new StringTokenizer(string, Protocol.DELIMITERELEMENTSHOP) ;
		
		while(stringTokenizer.hasMoreTokens()) {
			String name = stringTokenizer.nextToken() ;
			setOwned(name);
		}
	}
	
	public void loadSkins(String string) {
		
		StringTokenizer stringTokenizer = new StringTokenizer(string, Protocol.DELIMITERELEMENTSHOP) ;
		clear();
		while(stringTokenizer.hasMoreTokens()) {
			Skin skin = new Skin() ;
			skin.loadSkin(stringTokenizer.nextToken());
			add(skin);
		}
	}
	
}
