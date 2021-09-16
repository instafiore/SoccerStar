package application.model.game.handler;

import java.util.ArrayList;
import java.util.StringTokenizer;

import application.model.game.entity.Lineup;
import application.model.game.entity.Skin;
import application.net.common.Protocol;

public class LineupHandler {
	
	private ArrayList<Lineup> lineups ;
	
	private static LineupHandler instance = null ;
	
	public static LineupHandler getInstance() {
		if(instance == null)
			instance = new LineupHandler() ;
		return instance;
	}
		
	private LineupHandler() {
		lineups = new ArrayList<Lineup>();
	}
	
	public void add(Lineup lineup) {
		lineups.add(lineup);
	}
	
	public void clear() {
		lineups.clear();
	}
	
	public void remove(Lineup lineup) {
		lineups.remove(lineup);
	}
	
	
	public ArrayList<Lineup> getLineups() {
		return lineups;
	}
	
	public void setOwned(String id) {
		for(Lineup lineup : lineups) {
			if(lineup.getId().equals(id)) {
				lineup.setOwned(true);
				return ;
			}
		}
	}
	
	public void loadOwned(String string) {
		
		StringTokenizer stringTokenizer = new StringTokenizer(string, Protocol.DELIMITERELEMENTSHOP) ;
		
		while(stringTokenizer.hasMoreTokens()) {
			String id = stringTokenizer.nextToken() ;
			setOwned(id);
		}
		
	}
	
	public void loadLineups(String string) {
		
		StringTokenizer stringTokenizer = new StringTokenizer(string, Protocol.DELIMITERELEMENTSHOP) ;
		clear();
		while(stringTokenizer.hasMoreTokens()) {
			Lineup lineup = new Lineup() ;
			lineup.loadLineup(stringTokenizer.nextToken());
			add(lineup);
		}
		
	}
	
}
