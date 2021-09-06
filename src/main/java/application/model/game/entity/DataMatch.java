package application.model.game.entity;

import application.net.common.Protocol;

public class DataMatch {
	
	private String date = null ;
	private String time = null ;
	private int goalHome = 0 ;
	private int goalGuest = 0 ;
	private String field = null ;
	private String home = null ;
	private String guest = null ;
	
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

	
}
