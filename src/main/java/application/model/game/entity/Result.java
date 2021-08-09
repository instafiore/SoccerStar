package application.model.game.entity;

import application.Settings;

public class Result {
	
	private int home ;
	private int guest ;
	
	public Result() {
		home = 0 ;
		guest = 0 ;
	}
	
	public int getHome() {
		return home;
	}
	
	public void setHome(int home) {
		this.home = home;
	}
	
	public int getGuest() {
		return guest;
	}
	
	public void setGuest(int guest) {
		this.guest = guest;
	}
	
	public boolean goalHome() {
		if(home < Settings.MAX_GOAL)
		{
			++home ;
			if(home == Settings.MAX_GOAL)
				return true ;
			
			return false;
		}
		
		return true;
	}
	
	public boolean goalGuest() {
		if(guest < Settings.MAX_GOAL)
		{
			++guest ;
			if(guest == Settings.MAX_GOAL)
				return true ;
			
			return false;
		}
		
		return true;
	}
	
}
