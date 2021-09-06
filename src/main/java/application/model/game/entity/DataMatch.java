package application.model.game.entity;

import application.net.common.Protocol;
import application.net.server.MatchServer;

public class DataMatch {
	
	private String date = null ;
	private String time = null ;
	private int goalHome = 0 ;
	private int goalGuest = 0 ;
	private String field = null ;
	private String home = null ;
	private String guest = null ;
	
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
	
	public DataMatch() {}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getResult() {
		String result = goalHome+Protocol.DELIMITERGOALMATCH+goalGuest;
		return result;
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
	
}
