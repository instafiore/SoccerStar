package application.control;

import application.net.client.Client;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class MatchSucceedController implements EventHandler<WorkerStateEvent>{

	public void handle(WorkerStateEvent event) {
		
		Client.getInstance().matchEnded();
	}
	
}
