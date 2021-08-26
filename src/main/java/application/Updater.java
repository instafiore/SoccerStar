package application;

import application.control.MatchController;
import javafx.animation.AnimationTimer;

public class Updater extends AnimationTimer{

	private long previousTime = 0 ;
	private MatchController matchController;
	private static Updater instance = null;
	
	private boolean firstTime = true;
	
	private Updater(){
		super();	
	}
	
	public static Updater getInstance() {
		
		if(instance == null )
			instance = new Updater();
		return instance;
	}
	
	public void addMatchController(MatchController matchController ) {
		this.matchController = matchController;
	}
	
	public void startUpdater() {
		this.start();
	}
	
	public void stopUpdater() {
		this.stop();
	}
	
	public boolean isFirstTime() {
		return firstTime;
	}
	
	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}
	
	@Override
	public void handle(long now) {
		
		if(firstTime)
		{
			SceneHandler.getInstance().loadScene("MatchView", false );
			firstTime = false ;
		}
		long time = now - previousTime;
		if(time >= Settings.FREQUENCY * 1000000) {
			previousTime = now;
			try {
				matchController.update();
			} catch (Exception e) {
				System.out.println("Updater interrupted");
			}	
		}
	}
}