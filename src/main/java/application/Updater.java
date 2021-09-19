package application;

import application.control.ChooseFieldController;
import application.control.MatchController;
import application.control.MenuMatchPaneController;
import application.net.client.Client;
import javafx.animation.AnimationTimer;

public class Updater extends AnimationTimer{

	private long previousTime = 0 ;
	private MatchController matchController;
	private static Updater instance = null;
	
	private boolean firstTime = true;
	private int field;
	private Updater(){
		super();	
	}
	
	private boolean friendly_battle = false ;
	
	public static Updater getInstance() {
		
		if(instance == null )
			instance = new Updater();
		return instance;
	}
	
	public void addMatchController(MatchController matchController ) {
		this.matchController = matchController;
	}
	
	public void startUpdater(boolean friendly_battle,int field) {
		this.start();
		this.friendly_battle = friendly_battle ;
		this.field = field ;
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
			
			if(!friendly_battle)
				menuMatchPaneController.setField();
			else
				menuMatchPaneController.setFieldFriendlyBattle(field);
			firstTime = false ;
		}
		long time = now - previousTime;
		if(time >= Settings.FREQUENCY * 1000000) {
			previousTime = now;
			try {
				if(!matchController.getInstance().getTextToShow().equals("")) {
					matchController.getInstance().showText();
				}
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