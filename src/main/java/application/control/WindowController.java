package application.control;

import application.net.client.Client;
import application.net.common.Protocol;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public class WindowController implements EventHandler<WindowEvent>{

	public void handle(WindowEvent event) {
		
		switch (Client.getInstance().getCurrentState()) {
		case Client.IN_APP:

			break;
		case Client.IN_GAME:
			Client.getInstance().sendMessage(Protocol.CONNECTION_LOST);
			System.out.println("[WINDOWMATCHCONTROLLER] "+Protocol.CONNECTION_LOST);
			break;
		case Client.STEP_LOGIN:

			break;
		case Client.STEP_REGISTRATION:

			break;
		default:
			break;
		}
		
		
	}

}
