package application;

import application.control.MatchController;
import application.control.MenuMatchPaneController;
import application.net.client.Client;
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
			SceneHandler.getInstance().loadScene("MatchView", false , false);
			String colorHome = matchController.getColorHome() ;
			String colorGuest = matchController.getColorGuest() ;
			MenuMatchPaneController menuMatchPaneController = (MenuMatchPaneController) SceneHandler.getInstance().getLoader("MenuMatchPane").getController() ;
			menuMatchPaneController.setUsernameHome( Client.getInstance().getUsername(), colorHome);
			menuMatchPaneController.setUsernameGuest( MatchController.getInstance().getUsernameGuest(), colorGuest);
			menuMatchPaneController.initGoals();
			firstTime = false ;
		}
		long time = now - previousTime;
		if(time >= Settings.FREQUENCY * 1000000) {
			previousTime = now;
			try {
				
				if(matchController.getParseMatchInformation().isHomeScored()) {
					MenuMatchPaneController menuMatchPaneController = (MenuMatchPaneController) SceneHandler.getInstance().getLoader("MenuMatchPane").getController() ;
					menuMatchPaneController.goalHome();
					matchController.getParseMatchInformation().setHomeScored(false);
				}
				
				if(matchController.getParseMatchInformation().isGuestScored()) {
					MenuMatchPaneController menuMatchPaneController = (MenuMatchPaneController) SceneHandler.getInstance().getLoader("MenuMatchPane").getController() ;
					menuMatchPaneController.goalGuest();
					matchController.getParseMatchInformation().setGuestScored(false);
				}
				
				matchController.update();
			} catch (Exception e) {
				System.out.println("Updater interrupted");
			}	
		}
	}
}