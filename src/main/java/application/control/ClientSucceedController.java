package application.control;

import application.SceneHandler;
import application.model.game.entity.Message;
import application.net.client.Client;
import application.net.common.Protocol;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class ClientSucceedController implements EventHandler<WorkerStateEvent>{

	public void handle(WorkerStateEvent event) {
		
		Message message = (Message) event.getSource().getValue();
		
		System.out.println("[CLIENTSUCCEED] "+message);
		
		if(message.getProtocol().equals(Protocol.REGISTRATIONCOMPLETED)) {
			
			SceneHandler.getInstance().loadScene("MainPage", true);
		
		}else if(message.getProtocol().equals(Protocol.LOGINCOMPLETED)) {
		
			SceneHandler.getInstance().loadScene("MainPage", true);
			
		}else if(message.getProtocol().equals(Protocol.GENERALERROR)) {
			
			// SHOW ERROR
			System.exit(0);
			
		}else if(message.getProtocol().equals(Protocol.RELOADING_APP)){
			
			// RELOADING APP
			System.exit(0);
		
		}else if(message.getProtocol().equals(Protocol.INPUT_STREAM_NULL)) {
			
			// RELOADING APP
			System.exit(0);
		}else if(message.getProtocol().equals(Protocol.CONNECTION_LOST)) {
			
			// RELOADING APP
			System.exit(0);
		}else if(message.getProtocol().equals(Protocol.PREPARINGMATCH)) {
			
			// JUST TO CONCLUDE THE READ OF THE CLIENT
			
		}
		
		
		Client.getInstance().restart();
	}

}
