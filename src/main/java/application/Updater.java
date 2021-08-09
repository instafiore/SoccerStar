package application;

import application.control.MatchController;
import javafx.animation.AnimationTimer;

public class Updater extends AnimationTimer{

	private long previousTime = 0 ;
	private MatchController ballController;
	
	public Updater(MatchController ballController) {
		super();
		this.ballController = ballController;
		this.start();
	}
	
	@Override
	public void handle(long now) {
		long time = now - previousTime;
		if(time >= Settings.FREQUENCY * 1000000) {
			previousTime = now;
			try {
				ballController.update();
			} catch (Exception e) {
				System.out.println("Updater interrupted");
			}	
		}
	}
}