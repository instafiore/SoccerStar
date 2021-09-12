package application.control;

import application.SceneHandler;
import application.model.game.entity.Message;
import application.net.client.Client;
import application.net.client.MatchClient;
import application.net.common.Protocol;
import application.view.Dialog;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class ClientSucceedController implements EventHandler<WorkerStateEvent>{

	public void handle(WorkerStateEvent event) {
		
		Message message = (Message) event.getSource().getValue();
		
		System.out.println("[CLIENTSUCCEED] "+message);
		
		if(message.getProtocol().equals(Protocol.REGISTRATIONCOMPLETED)) {
			
			SceneHandler.getInstance().loadScene("MainPage", true , true);
			
		}else if(message.getProtocol().equals(Protocol.REGISTRATIONFAILED)){
			Dialog.getInstance().showInformationDialog(Dialog.ERROR_WINDOW,message.getProtocol()) ;
			System.exit(0);
		}
		else if(message.getProtocol().equals(Protocol.ALREADYEXISTS)){
			
			RegistrationController registrationController =  SceneHandler.getInstance().getLoader("RegistrationPage").getController();
			registrationController.showError(message.getProtocol(), 15);
		}else if(message.getProtocol().equals(Protocol.LOGINCOMPLETED)) {
			
			SceneHandler.getInstance().loadScene("MainPage", true , true);
			
		}else if(message.getProtocol().equals(Protocol.LOGINFAILED) || message.getProtocol().equals(Protocol.ALREADYONLINE)){
			
			LoginController loginController = SceneHandler.getInstance().getLoader("LoginPage").getController() ;
			loginController.showError(message.getProtocol(), 15);
		}
		else if(message.getProtocol().equals(Protocol.GENERALERROR)) {
			
			Dialog.getInstance().showInformationDialog(Dialog.ERROR_WINDOW,message.getProtocol());
			System.exit(0);
		}else if(message.getProtocol().equals(Protocol.GENERALERROR)){
			
			// RELOADING APP
			Dialog.getInstance().showInformationDialog(Dialog.ERROR_WINDOW,message.getProtocol());
			System.exit(0);
		}else if(message.getProtocol().equals(Protocol.INPUT_STREAM_NULL)) {
			
			Dialog.getInstance().showInformationDialog(Dialog.ERROR_WINDOW,message.getProtocol());
			System.exit(0);
		}else if(message.getProtocol().equals(Protocol.CONNECTION_LOST)) {
			
			Dialog.getInstance().showInformationDialog(Dialog.ERROR_WINDOW,message.getProtocol());
			System.exit(0);
		}else if(message.getProtocol().equals(Protocol.PREPARINGMATCH)) {
			MatchClient match = new MatchClient(Client.getInstance());
			Client.getInstance().setCurrentMatch(match);
			match.setOnSucceeded(new MatchSucceedController());
		}
		
		
		Client.getInstance().restart();
	}

}
