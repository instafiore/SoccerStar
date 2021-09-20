package application.model.game.handler;

import application.model.game.entity.Sound;

public class SoundHandler {

	private Sound background = new Sound("background.wav");
	private Sound goal_home = new Sound("goal_home.wav");
	private Sound goal_guest = new Sound("goal_guest.wav");
	private Sound background_match = new Sound("background_match.wav");
	private Sound hit = new Sound("hit.wav");
	
	private SoundHandler() {
		background.reduceVolume();
		background_match.reduceVolume();
	}
	private static SoundHandler instance = null ;
	
	public static SoundHandler getInstance() {
		if(instance == null)
			instance = new SoundHandler() ;
		return instance;
	}
	
	public void startBackground() {
		background.loop();
		
	}
	
	public void startBackgroundMatch() {
		background_match.loop();
		
	}
	

	public void stopBackground() {
		background.stop();
	}
	
	
	public void stopBackgroundMatch() {
		background_match.stop();
	}
	
	public void startGoalHome() {
		goal_home.start();
	}
	
	public void startGolGuest() {
		goal_guest.start();
	}
	
	public void startHit() {
		hit.start();
	}
	
}
