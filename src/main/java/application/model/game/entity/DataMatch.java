package application.model.game.entity;

import java.util.ArrayList;
import java.util.StringTokenizer;

import application.net.common.Protocol;
import application.net.server.MatchServer;

public class DataMatch implements Comparable<DataMatch>{
	
	/*
	 * Devo inviare i dati del color della home della guest e del campo (SERVER)
	 * 
	 */
	
	private String date = null ;
	private String time = null ;
	private int goalHome = 0 ;
	private int goalGuest = 0 ;
	private String field = null ;
	private String home = null ;
	private String guest = null ;
	private String resultMatch = "" ;
	private String colorHome = "";
	private String colorGuest = "" ;
	private String colorField = "" ;
	
	public static final int NOONE = 3;
	public static final int HOME = Ball.BLUE;
	public static final int GUEST = Ball.RED ;
	
	public DataMatch(String date,String field, String home, String guest) {
		super();
		this.date = date;
		this.field = field;
		this.home = home;
		this.guest = guest;
	}
	
	public void setResultMatch(String resultMatch) {
		this.resultMatch = resultMatch;
		String[] goals = resultMatch.split(Protocol.DELIMITERGOALMATCH);
		goalHome = Integer.parseInt(goals[0]);
		goalGuest = Integer.parseInt(goals[1]);
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getTime() {
		return time;
	}
	
	public int getGoalGuest() {
		return goalGuest;
	}
	
	public int getGoalHome() {
		return goalHome;
	}
	
	public void forfeitOnTheBooks(int who) {
		
		switch (who) {
		case HOME:
			goalHome = 0 ;
			goalGuest = Protocol.GOALSTOWIN ;
			break;
		case GUEST:
			goalHome =  Protocol.GOALSTOWIN;
			goalGuest = 0 ;
			break;
		
		}
		
	}
	
	
	public void loadMatch(String string) {
		
		StringTokenizer stringTokenizer1 = new StringTokenizer(string,Protocol.DELIMITERINFORMATIONDATAMATCH);
		
		setHome(stringTokenizer1.nextToken());
		setGuest(stringTokenizer1.nextToken());
		setField(stringTokenizer1.nextToken());
		setResultMatch(stringTokenizer1.nextToken());
		setDate(stringTokenizer1.nextToken());
		setTime(stringTokenizer1.nextToken());
		setColorField(stringTokenizer1.nextToken());
		setColorHome(stringTokenizer1.nextToken());
		setColorGuest(stringTokenizer1.nextToken());
		
	}
	
	public static ArrayList<DataMatch> getMatches(String string){
		
		ArrayList<DataMatch> dataMatches = new ArrayList<DataMatch>();
		StringTokenizer stringTokenizer = new StringTokenizer(string,Protocol.DELIMITERDATAMATCH);
		
		while(stringTokenizer.hasMoreTokens()) {
			DataMatch dataMatch = new DataMatch() ;
			dataMatch.loadMatch(stringTokenizer.nextToken());
			dataMatches.add(dataMatch);
		}
		
		return dataMatches ;
	}
	
	public DataMatch() {}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getResult() {
		return goalHome+Protocol.DELIMITERGOALMATCH+goalGuest;
	}
	
	public String getResultReversed() {
		StringTokenizer stringTokenizer = new StringTokenizer(getResult(), Protocol.DELIMITERGOALMATCH) ;
		
		String guest = stringTokenizer.nextToken() ;
		String home = stringTokenizer.nextToken() ;
		
		return home+Protocol.DELIMITERGOALMATCH+guest ;
	}

	public void incHome() {
		if(goalHome >= 3)
			return ;
		++goalHome;
	}

	public void incGuest() {
		if(goalGuest >= 3)
			return ;
		++goalGuest;
	}
	
	public String getField() {
		return field;
	}
	
	public void decHome() {
		if(goalHome <= 0)
			return ;
		--goalHome;
	}
	
	public void decGuest() {
		if(goalGuest <= 0)
			return ;
		--goalGuest;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getGuest() {
		return guest;
	}

	public void setGuest(String guest) {
		this.guest = guest;
	}

	
	public boolean isConcluded() {
		return goalHome >= Protocol.GOALSTOWIN || goalGuest >= Protocol.GOALSTOWIN ;
	}
	
	public int whoWon() {
		
		if(!isConcluded())
			return NOONE ;
		
		if(goalHome >= Protocol.GOALSTOWIN)
			return HOME ;
		return GUEST ;
	}

	@Override
	public int compareTo(DataMatch match) {
		return this.date.compareTo(match.date) * -1;
	}
	
	
	public void setColorHome(String colorHome) {
		this.colorHome = colorHome;
	}
	
	public void setColorField(String colorField) {
		this.colorField = colorField;
	}
	
	public void setColorGuest(String colorGuest) {
		this.colorGuest = colorGuest;
	}
	
	public String getColorField() {
		return colorField;
	}
	
	public String getColorGuest() {
		return colorGuest;
	}
	
	public String getColorHome() {
		return colorHome;
	}
}
