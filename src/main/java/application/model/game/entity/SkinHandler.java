package application.model.game.entity;

import java.util.ArrayList;
import java.util.StringTokenizer;

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
	
	public void remove(Skin skin) {
		skins.remove(skin);
	}
	
	public ArrayList<Skin> getSkins() {
		return skins;
	}
	
	
	public void loadSkins(String string) {
		
		StringTokenizer stringTokenizer = new StringTokenizer(string, Protocol.DELIMITERSKIN) ;
		
		while(stringTokenizer.hasMoreTokens()) {
			Skin skin = new Skin() ;
			skin.loadSkin(stringTokenizer.nextToken());
			add(skin);
		}
	}
	
}
