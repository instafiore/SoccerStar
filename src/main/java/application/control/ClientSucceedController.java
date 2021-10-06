package application.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.sun.scenario.effect.Effect.AccelType;

import application.SceneHandler;
import application.Settings;
import application.model.game.entity.Account;
import application.model.game.entity.DataMatch;
import application.model.game.entity.Lineup;
import application.model.game.entity.Message;
import application.model.game.handler.FriendsHandler;
import application.model.game.handler.LineupHandler;
import application.model.game.handler.SkinHandler;
import application.net.client.Client;
import application.net.client.MatchClient;
import application.net.common.Protocol;
import application.view.Dialog;
import application.view.MatchView;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;

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
			registrationController.setPressed(false);
		}else if(message.getProtocol().equals(Protocol.LOGINCOMPLETED)) {
			
			SceneHandler.getInstance().loadScene("MainPage", true , true);
			
		}else if(message.getProtocol().equals(Protocol.LOGINFAILED) || message.getProtocol().equals(Protocol.ALREADYONLINE)){
			
			LoginController loginController = SceneHandler.getInstance().getLoader("LoginPage").getController() ;
			loginController.showText(message.getProtocol(),15,Dialog.ERROR_WINDOW,4);
			loginController.setPressed(false);
		}else if(message.getProtocol().equals(Protocol.INITIALINFORMATION)) {
			
			MainPageController mainPageController =  SceneHandler.getInstance().getLoader("MainPage").getController() ;
			
			mainPageController.setCoins_main_page_label((String)(String) message.getMessage());
			mainPageController.setReady(true);
			
		}else if(message.getProtocol().equals(Protocol.INFORMATIONFRIENDS)) {
			
			FriendsController friendsController = (FriendsController) SceneHandler.getInstance().getLoader("FriendsPage").getController() ;
			
			FriendsHandler.getInstance().loadFriends((String)(String) message.getMessage());
			
			friendsController.init();
			friendsController.setReady(true);
		
		}else if(message.getProtocol().equals(Protocol.FRIENDADDED) || message.getProtocol().equals(Protocol.ALREADYFRIENDS)) {
			
			FriendsController friendsController = (FriendsController) SceneHandler.getInstance().getLoader("FriendsPage").getController() ;
			
			friendsController.showText(message.getProtocol(), 30, Dialog.INFORMATION_WINDOW, 5);
			
			Client.getInstance().sendMessage(Protocol.INFORMATIONFRIENDS);
		
			friendsController.setReady(true);
		}else if(message.getProtocol().equals(Protocol.ISNOTINAGAME)) {
			
			Dialog.getInstance().showSelectField((String)(String) message.getMessage());
			
		}else if(message.getProtocol().equals(Protocol.ISINAGAME) || message.getProtocol().equals(Protocol.NOLONGERONLINE)) {
			
			FriendsController friendsController = (FriendsController) SceneHandler.getInstance().getLoader("FriendsPage").getController() ;
			
			friendsController.showText(message.getProtocol(), 25, Dialog.ERROR_WINDOW, 5);
			
		}else if(message.getProtocol().equals(Protocol.REQUESTACCEPTED) || message.getProtocol().equals(Protocol.REQUESTEDECLINED)) {
			
			FriendsController friendsController = (FriendsController) SceneHandler.getInstance().getLoader("FriendsPage").getController() ;
			
			friendsController.showText(message.getProtocol(), 25, Dialog.INFORMATION_WINDOW, 5);
			
			Dialog.getInstance().closeDialog();
		
				
		}else if(message.getProtocol().equals(Protocol.FRIENDLYREQUESTFIELD1) || message.getProtocol().equals(Protocol.FRIENDLYREQUESTFIELD2) || message.getProtocol().equals(Protocol.FRIENDLYREQUESTFIELD3)){
			 
			String whoWantToChallengeYou = (String) (String) message.getMessage() ;
			
			String text = whoWantToChallengeYou + " would like to challenge \n you to a friendly match on the pitch " ;
			
			String field = "" ;
			if(message.getProtocol().equals(Protocol.FRIENDLYREQUESTFIELD1)) 
				field = Settings.FIELD1 ;
			else if(message.getProtocol().equals(Protocol.FRIENDLYREQUESTFIELD2)) 
				field = Settings.FIELD2 ;
			else 
				field = Settings.FIELD3 ;
	
			text += field ;
			
			int res = Dialog.getInstance().showRequestFriendlyBattle(text)  ;
			
			switch (res) {
			case Dialog.YES:
				Client.getInstance().sendMessage(Protocol.IACCEPTEDTHEFRIENDLYBATTLE);
				Client.getInstance().sendMessage(whoWantToChallengeYou + Protocol.DELIMITERFRIEND + field );
				break;
			case Dialog.NO:
				Client.getInstance().sendMessage(Protocol.IDECLINEDFRIENDLYBATTLE);
				Client.getInstance().sendMessage(whoWantToChallengeYou);
				break;
			default:
				Client.getInstance().sendMessage(Protocol.IDECLINEDFRIENDLYBATTLE);
				Client.getInstance().sendMessage(whoWantToChallengeYou);
				break;
			}
			
		}else if(message.getProtocol().equals(Protocol.PREPARINGFRIENDLYMATCH)) {
			
			
			Client.getInstance().setCurrentState(Client.IN_GAME);
			String field = (String) message.getMessage() ;
			
			int nField = MatchView.FIELD1;
			
			if(field.equals(Settings.FIELD1)) 
				nField = MatchView.FIELD1 ;
			else if(field.equals(Settings.FIELD2)) 
				nField = MatchView.FIELD2 ;
			else 
				nField = MatchView.FIELD3 ;
			
			MatchController.getInstance().getMatchView().setField(nField);
			MatchClient match = new MatchClient(Client.getInstance(),true,nField);
			Client.getInstance().setCurrentMatch(match);
			match.setOnSucceeded(new MatchSucceedController());
			
		}else if(message.getProtocol().equals(Protocol.USERNAMEFRIENDDOESNTEXIST)) {
			
			FriendsController friendsController = (FriendsController) SceneHandler.getInstance().getLoader("FriendsPage").getController() ;
			
			friendsController.showText(message.getProtocol(), 30, Dialog.ERROR_WINDOW, 5);
			
			friendsController.setReady(true);
			
		}else if(message.getProtocol().equals(Protocol.INFORMATIONACCOUNT)) {
			
			AccountController accountController =  SceneHandler.getInstance().getLoader("AccountPage").getController() ;
			Account account = new Account();
			account.loadAccount((String) message.getMessage());
			accountController.setCard_field_account(Protocol.NOTINSERTED);
			accountController.setUsername_field_account(account.getUsername());
			accountController.setEmail_field_account(account.getEmail());
			accountController.changeColorBall(account.getCurrentSkin());
			accountController.setCoins_label_account(""+account.getCoins());
			accountController.setReady(true);
			
		}else if(message.getProtocol().equals(Protocol.INFORMATIONSHOP)) {
			
			ShopController shopController = (ShopController) SceneHandler.getInstance().getLoader("ShopPage").getController() ;
			
			StringTokenizer stringTokenizer = new StringTokenizer((String) message.getMessage(), Protocol.DELIMITERINFORMATIONSHOP);
			SkinHandler.getInstance().loadSkins(stringTokenizer.nextToken());
			SkinHandler.getInstance().loadOwned(stringTokenizer.nextToken());
			LineupHandler.getInstance().loadLineups(stringTokenizer.nextToken());
			LineupHandler.getInstance().loadOwned(stringTokenizer.nextToken());
			String coins = stringTokenizer.nextToken() ;
			shopController.setCoins(coins);
			
			
			
			
		}else if(message.getProtocol().equals(Protocol.IMAGESLINEUPSHOP)) {
			
			FXMLLoader loadShop = SceneHandler.getInstance().getLoader("ShopPage") ;
			
			if(loadShop != null) {
				ShopController shopController = loadShop.getController() ;
				
				ArrayList<byte[]> imagesLineups = (ArrayList<byte[]>) message.getMessage() ;
				
				int i = 0 ;
				for(Lineup lineup : LineupHandler.getInstance().getLineups())
					lineup.setImage(imagesLineups.get(i++));
				
				shopController.init();
				shopController.setReady(true);
			}
			
		}else if(message.getProtocol().equals(Protocol.IMAGESLINEUPINVENTORY)) {
				
			FXMLLoader loadInventory = SceneHandler.getInstance().getLoader("InventoryPage") ;
			
			if(loadInventory != null) {
				InventoryController inventoryController = loadInventory.getController() ;
				
				ArrayList<byte[]> imagesLineups = (ArrayList<byte[]>) message.getMessage() ;
				
				int i = 0 ;
				for(Lineup lineup : LineupHandler.getInstance().getLineups())
					lineup.setImage(imagesLineups.get(i++));
				
				inventoryController.init();
				inventoryController.setReady(true);
			}
			
		}else if(message.getProtocol().equals(Protocol.INFORMATIONINVENTORY)) {
			
			InventoryController inventoryController = (InventoryController) SceneHandler.getInstance().getLoader("InventoryPage").getController() ;
			
			StringTokenizer stringTokenizer = new StringTokenizer((String) message.getMessage(), Protocol.DELIMITERINFORMATIONSHOP);
			SkinHandler.getInstance().loadSkins(stringTokenizer.nextToken());
			SkinHandler.getInstance().loadOwned(stringTokenizer.nextToken());
			LineupHandler.getInstance().loadLineups(stringTokenizer.nextToken());
			LineupHandler.getInstance().loadOwned(stringTokenizer.nextToken());
		

		}else if(message.getProtocol().equals(Protocol.SKININUSE)) {
			
			InventoryController inventoryController = (InventoryController) SceneHandler.getInstance().getLoader("InventoryPage").getController() ;
						
			String colorSkinInUse = (String) message.getMessage() ;
			SkinHandler.getInstance().setUsing(SkinHandler.getInstance().getSkinFromColor(colorSkinInUse).getName());
			
			
			
		}else if(message.getProtocol().equals(Protocol.LINEUPINUSE)) {
			
			InventoryController inventoryController = (InventoryController) SceneHandler.getInstance().getLoader("InventoryPage").getController() ;
						
			int lineupInUse = Integer.parseInt((String) message.getMessage());
			LineupHandler.getInstance().setUsing(lineupInUse);
			
			
		}else if(message.getProtocol().equals(Protocol.ELEMENTSHOPBOUGHT) || message.getProtocol().equals(Protocol.ELEMENTSHOPNOTBOUGHT)) {
		
			ShopController shopController = SceneHandler.getInstance().getLoader("ShopPage").getController() ;
			
			if(message.getProtocol().equals(Protocol.ELEMENTSHOPBOUGHT))
			{
				shopController.showText(message.getProtocol(), 30, Dialog.INFORMATION_WINDOW, 6);
				Client.getInstance().sendMessage(Protocol.INFORMATIONSHOP);
				Client.getInstance().sendMessage(Protocol.IMAGESLINEUPSHOP);
			}
			else
				shopController.showText(message.getProtocol(), 21, Dialog.ERROR_WINDOW, 6);
			
		}else if(message.getProtocol().equals(Protocol.INFORMATIONHISTORY)) {
			
			HistoryController historyController =  SceneHandler.getInstance().getLoader("HistoryPage").getController() ;
			historyController.init(DataMatch.getMatches((String) message.getMessage()));
			historyController.setReady(true);
		}else if(message.getProtocol().equals(Protocol.PASSWORDCHANGEDACCOUNTSTATE) || message.getProtocol().equals(Protocol.OLDPASSOWORDNOTCORRECT)) {
			
			AccountController accountController =  SceneHandler.getInstance().getLoader("AccountPage").getController() ;
			
			if(message.getProtocol().equals(Protocol.PASSWORDCHANGEDACCOUNTSTATE))
				accountController.showText(message.getProtocol(), 14, Dialog.INFORMATION_WINDOW, 3);
			else
				accountController.showText(message.getProtocol(), 14, Dialog.ERROR_WINDOW, 3);
			
		}else if(message.getProtocol().equals(Protocol.USERNAMEDOESNTEXIST)) {
			
			Step1PSW step1psw = SceneHandler.getInstance().getLoader("Step1PSW").getController();
			step1psw.showError(Protocol.USERNAMEDOESNTEXIST, 18);
			
		}else if(message.getProtocol().equals(Protocol.EMAILSENT)) {
			
			Client.getInstance().setCurrentState(Client.STEP2PSW);
			SceneHandler.getInstance().loadScene("Step2PSW", false, true);
			Step2PSW step2psw = SceneHandler.getInstance().getLoader("Step2PSW").getController() ;
			step2psw.setText_area_step2psw((String) message.getMessage());
			step2psw.setReady(true);
		}else if(message.getProtocol().equals(Protocol.CODENOTVALID)) {
			
			
			Step2PSW step2psw = SceneHandler.getInstance().getLoader("Step2PSW").getController() ;
			step2psw.showError(Protocol.CODENOTVALID, 17);
			
		}else if(message.getProtocol().equals(Protocol.PASSWORDCHANGED)) {
			
			Client.getInstance().setCurrentState(Client.STEP_LOGIN);
			SceneHandler.getInstance().loadScene("LoginPage", false, true);
			LoginController loginController = SceneHandler.getInstance().getLoader("LoginPage").getController() ;
			loginController.showText(message.getProtocol(),15,Dialog.INFORMATION_WINDOW,4);
			loginController.setPressed(false);
			
		}else if(message.getProtocol().equals(Protocol.GENERALERROR)) {
			
			Dialog.getInstance().showInformationDialog(Dialog.ERROR_WINDOW,message.getProtocol());
			System.exit(0);
		}else if(message.getProtocol().equals(Protocol.CONNECTION_LOST)) {
			
			Dialog.getInstance().showInformationDialog(Dialog.ERROR_WINDOW,message.getProtocol());
			System.exit(0);
		}else if(message.getProtocol().equals(Protocol.PREPARINGMATCH)) {
			MatchClient match = new MatchClient(Client.getInstance(),false,0);
			Client.getInstance().setCurrentMatch(match);
			match.setOnSucceeded(new MatchSucceedController());
		}
		
		
		Client.getInstance().restart();
	}

}
