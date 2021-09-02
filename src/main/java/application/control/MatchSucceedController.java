package application.control;

import application.SceneHandler;
import application.Updater;
import application.net.client.Client;
import application.net.common.Protocol;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class MatchSucceedController implements EventHandler<WorkerStateEvent>{

	public void handle(WorkerStateEvent event) {
		
		if((Boolean) event.getSource().getValue()){
			System.out.println("[MATCHSUCCEEDCONTROLLER] "+Protocol.MATCHSUCCEED);
			Client.getInstance().matchEnded();
			SceneHandler.getInstance().loadScene("MainPage",true);
			Updater.getInstance().stopUpdater();
			Updater.getInstance().setFirstTime(true);
		}else {
			//TODO
			System.out.println("[MATCHSUCCEEDCONTROLLER] "+Protocol.MATCHFAILED);
			System.out.println("[MATCHSUCCEEDCONTROLLER] "+Protocol.GENERALERROR);
			System.exit(0);
		}
		
		
		
	}
	
}
