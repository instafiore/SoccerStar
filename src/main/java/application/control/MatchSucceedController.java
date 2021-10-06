package application.control;

import application.SceneHandler;
import application.Updater;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.Dialog;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class MatchSucceedController implements EventHandler<WorkerStateEvent>{

	public void handle(WorkerStateEvent event) {
		
		String res = (String) event.getSource().getValue() ;
		if(res.equals(Protocol.YOUWON) || res.equals(Protocol.YOULOST) || res.equals(Protocol.NOERRORBUTLEFTMATCH) || res.equals(Protocol.NOERRORMATCH)){

			System.out.println("[MATCHSUCCEEDCONTROLLER] "+Protocol.MATCHSUCCEED);
			Client.getInstance().matchEnded();
			Updater.getInstance().stopUpdater();
			Updater.getInstance().setFirstTime(true);
			if(!res.equals(Protocol.NOERRORMATCH))
				Dialog.getInstance().showInformationDialog(Dialog.INFORMATION_WINDOW,res);
			Client.getInstance().setCurrentState(Client.MAINPAGE);
			SceneHandler.getInstance().loadScene("MainPage",true , true);
		}else {
			System.out.println("[MATCHSUCCEEDCONTROLLER] "+Protocol.MATCHFAILED);
			System.out.println("[MATCHSUCCEEDCONTROLLER] "+Protocol.GENERALERROR);
			Dialog.getInstance().showInformationDialog(Dialog.ERROR_WINDOW,res);
			System.exit(0);
		}
		
		
		
	}
	
}
