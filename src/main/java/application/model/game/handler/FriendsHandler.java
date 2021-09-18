package application.model.game.handler;

import java.util.ArrayList;
import java.util.StringTokenizer;

import application.model.game.entity.Lineup;
import application.net.common.Protocol;
import javafx.util.Pair;

public class FriendsHandler {
	
	private ArrayList<Pair<String, String>> friendsOnline = null ;
	private ArrayList<Pair<String, String>> friendsOffline = null ;
	
	public static FriendsHandler instance = null ;
	
	
	public static FriendsHandler getInstance() {
		if(instance == null)
			instance = new FriendsHandler();
		return instance;
	}
	
	private FriendsHandler() {
		friendsOnline = new ArrayList<Pair<String, String>>();
		friendsOffline = new ArrayList<Pair<String, String>>();
	}
	
	public ArrayList<Pair<String, String>> getFriendsOffline() {
		return friendsOffline;
	}
	
	public ArrayList<Pair<String, String>> getFriendsOnline() {
		return friendsOnline;
	}
	
	public void addFriendOnline(Pair<String, String> pair) {
		friendsOnline.add(pair);
	}
	
	public void clearFriendsOnline() {
		friendsOnline.clear();
	}
	
	public void removeFriendOnline(Pair<String, String> pair) {
		friendsOnline.remove(pair);
	}
	
	
	public void addFriendOffline(Pair<String, String> pair) {
		friendsOffline.add(pair);
	}
	
	public void clearFriendsOffline() {
		friendsOffline.clear();
	}
	
	public void removeFriendOffline(Pair<String, String> pair) {
		friendsOffline.remove(pair);
	}
	
	
	
	public void loadFriends(String string) {
		
		clearFriendsOffline();
		clearFriendsOnline();
		
		if(string.equals(Protocol.NOFRIENDS))
			return ;
		
		StringTokenizer stringTokenizer = new StringTokenizer(string, Protocol.DELIMITERINFORMATIONFRIENDS);
		
		StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken(), Protocol.DELIMITERFRIEND);
		StringTokenizer stringTokenizer3 = new StringTokenizer(stringTokenizer.nextToken(), Protocol.DELIMITERFRIEND);
	
		
		while(stringTokenizer2.hasMoreTokens())
		{
			String token = stringTokenizer2.nextToken() ;
			if(token.equals(Protocol.NOFRIENDSONLINE))
				break ;
			Pair<String,String> pair = new Pair<String, String>(token.split(Protocol.DELIMITERINFORMATIONFRIEND)[0], token.split(Protocol.DELIMITERINFORMATIONFRIEND)[1]);
			addFriendOnline(pair);
		}
		
		while(stringTokenizer3.hasMoreTokens())
		{
			String token = stringTokenizer3.nextToken() ;
			if(token.equals(Protocol.NOFRIENDSOFFLINE))
				break ;
			Pair<String,String> pair = new Pair<String, String>(token.split(Protocol.DELIMITERINFORMATIONFRIEND)[0], token.split(Protocol.DELIMITERINFORMATIONFRIEND)[1]);
			addFriendOffline(pair);
		}
	}
}
