package application.control;

import java.util.StringTokenizer;

import application.SceneHandler;
import application.model.game.entity.Account;
import application.model.game.entity.DataMatch;
import application.model.game.entity.Message;
import application.model.game.entity.SkinHandler;
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
			loginController.showText(message.getProtocol(),15,Dialog.ERROR_WINDOW,4);
		}else if(message.getProtocol().equals(Protocol.INITIALINFORMATION)) {
			
			MainPageController mainPageController =  SceneHandler.getInstance().getLoader("MainPage").getController() ;
			StringTokenizer stringTokenizer = new StringTokenizer(message.getMessage(), Protocol.DELIMITERINITIALINFORMATION);
			
			mainPageController.setCoins_main_page_label(stringTokenizer.nextToken());
			SkinHandler.getInstance().loadSkins(stringTokenizer.nextToken());
			
		}else if(message.getProtocol().equals(Protocol.INFORMATIONACCOUNT)) {
			
			AccountController accountController =  SceneHandler.getInstance().getLoader("AccountPage").getController() ;
			Account account = new Account();
			account.loadAccount(message.getMessage());
			accountController.setCard_field_account(Protocol.NOTINSERTED);
			accountController.setUsername_field_account(account.getUsername());
			accountController.setEmail_field_account(account.getEmail());
			accountController.changeColorBall(account.getColor_my_balls());
			accountController.setCoins_label_account(""+account.getCoins());
			
		}else if(message.getProtocol().equals(Protocol.INFORMATIONHISTORY)) {
			
			HistoryController historyController =  SceneHandler.getInstance().getLoader("HistoryPage").getController() ;
			historyController.init(DataMatch.getMatches(message.getMessage()));
			
		}else if(message.getProtocol().equals(Protocol.USERNAMEDOESNTEXIST)) {
			
			Step1PSW step1psw = SceneHandler.getInstance().getLoader("Step1PSW").getController();
			step1psw.showError(Protocol.USERNAMEDOESNTEXIST, 18);
			
		}else if(message.getProtocol().equals(Protocol.EMAILSENT)) {
			
			Client.getInstance().setCurrentState(Client.STEP2PSW);
			SceneHandler.getInstance().loadScene("Step2PSW", false, true);
			Step2PSW step2psw = SceneHandler.getInstance().getLoader("Step2PSW").getController() ;
			step2psw.setText_area_step2psw(message.getMessage());
			
		}else if(message.getProtocol().equals(Protocol.CODENOTVALID)) {
			
			
			Step2PSW step2psw = SceneHandler.getInstance().getLoader("Step2PSW").getController() ;
			step2psw.showError(Protocol.CODENOTVALID, 17);
			
		}else if(message.getProtocol().equals(Protocol.PASSWORDCHANGED)) {
			
			Client.getInstance().setCurrentState(Client.STEP_LOGIN);
			SceneHandler.getInstance().loadScene("LoginPage", false, true);
			LoginController loginController = SceneHandler.getInstance().getLoader("LoginPage").getController() ;
			loginController.showText(message.getProtocol(),15,Dialog.INFORMATION_WINDOW,4);
			
		}else if(message.getProtocol().equals(Protocol.GENERALERROR)) {
			
			Dialog.getInstance().showInformationDialog(Dialog.ERROR_WINDOW,message.getProtocol());
			System.exit(0);
		}else if(message.getProtocol().equals(Protocol.GENERALERROR)){
			
			// RELOADING APP
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
