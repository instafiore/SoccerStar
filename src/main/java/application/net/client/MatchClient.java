package application.net.client;

import java.awt.PageAttributes.PrintQualityType;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import application.SceneHandler;
import application.Settings;
import application.Updater;
import application.control.FriendControllerOffline;
import application.control.MainPageController;
import application.control.MatchController;
import application.model.game.entity.Ball;
import application.model.game.entity.GeneratorLineup;
import application.model.game.entity.ParseMatchInformation;
import application.model.game.handler.MatchHandler;
import application.model.game.handler.SoundHandler;
import application.model.game.physics.VectorFioreNoSync;
import application.model.game.physics.VelocityNoSync;
import application.net.common.Protocol;
import application.view.Dialog;
import application.view.Field;
import javafx.concurrent.Task;

public class MatchClient extends Task<String>{

	
	private Client client = null ;
	private BufferedReader in = null ;
	private int lineup = GeneratorLineup.LINEUP2;
	private String usernameGuest = null ;
	private boolean match_activated = false ;
	private String colorHome = "" ;
	private String colorGuest = "" ;
	public boolean friendlyBattle = false ;
	
	private ParseMatchInformation parseMatchInformation ;
	private int field ;
	
	public MatchClient(Client client , boolean friendlyBattle,int field) {
		super();
		this.client = client;
		this.friendlyBattle = friendlyBattle ;
		in = client.getIn();
		parseMatchInformation = new ParseMatchInformation();
		this.field = field ;
		MatchController.getInstance().setParseMatchInformation(parseMatchInformation);
		
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	public boolean isFriendlyBattle() {
		return friendlyBattle;
	}
	
	public String initalSettings() throws IOException {
		
		String message = null ;
		
		if(isFriendlyBattle())
			client.sendMessage(Protocol.FRIENDLYMATCHSTARTED);
		else
			client.sendMessage(Protocol.MATCHSTARTED);
		
		
		message = read() ;
		
		if(message == null)
			return Protocol.ERRORMATCH;
		
		
		if(message.equals(Protocol.CONNECTION_LOST))
		{
			printConnectionLost();
			return Protocol.NOERRORBUTLEFTMATCH ;
		}
		
		if(message.equals(Protocol.GENERALERROR)) {
			printConnectionLost();
			return Protocol.ERRORMATCH;
		}
		
		
		if(!message.equals(Protocol.USERNAMEGUEST))
		{
			printConnectionLost();
			return Protocol.ERRORMATCH;
		}
		
		
		usernameGuest = read() ;
		if(usernameGuest == null)
			return Protocol.ERRORMATCH;
		
		
		if(usernameGuest.equals(Protocol.CONNECTION_LOST)) 
		{
			printConnectionLost();
			return Protocol.NOERRORBUTLEFTMATCH ;
		}
		
		if(usernameGuest.equals(Protocol.GENERALERROR)) {
			printConnectionLost();
			return Protocol.ERRORMATCH;
		}
		
		MatchController.getInstance().setUsernameGuest(usernameGuest);
		
		message = read();
		
		if(message == null)
			return Protocol.ERRORMATCH;
		
		if(message.equals(Protocol.CONNECTION_LOST)) {
			printConnectionLost();
			return Protocol.NOERRORBUTLEFTMATCH ;
		}
		if(message.equals(Protocol.GENERALERROR)) {
			printConnectionLost();
			return Protocol.ERRORMATCH;
		}
		
		if(!message.equals(Protocol.YOURCOLOR))
		{
			printConnectionLost();
			return Protocol.ERRORMATCH;
		}
		
		colorHome = read() ;
		
		if(colorHome == null)
			return Protocol.ERRORMATCH;
		

		if(colorHome.equals(Protocol.CONNECTION_LOST)) {
			printConnectionLost();
			return Protocol.NOERRORBUTLEFTMATCH ;
		}
		if(colorHome.equals(Protocol.GENERALERROR)) {
			printConnectionLost();
			return Protocol.ERRORMATCH;
		}
		
		message = read();
		
		if(message == null)
			return Protocol.ERRORMATCH;
		
		if(message.equals(Protocol.CONNECTION_LOST)) {
			printConnectionLost();
			return Protocol.NOERRORBUTLEFTMATCH ;
		}
		if(message.equals(Protocol.GENERALERROR)) {
			printConnectionLost();
			return Protocol.ERRORMATCH;
		}
		
		if(!message.equals(Protocol.COLORGUEST))
		{
			printConnectionLost();
			return Protocol.ERRORMATCH;
		}
		
		colorGuest = read() ;
		
		if(colorGuest == null)
			return Protocol.ERRORMATCH;
		
		if(colorGuest.equals(Protocol.CONNECTION_LOST)) {
			printConnectionLost();
			return Protocol.NOERRORBUTLEFTMATCH ;
		}
		if(colorGuest.equals(Protocol.GENERALERROR)) {
			printConnectionLost();
			return Protocol.ERRORMATCH;
		}
		
		MatchController.getInstance().setColorHome(colorHome);
		MatchController.getInstance().setColorGuest(colorGuest);

		message = read();
		
		if(message.equals(Protocol.CONNECTION_LOST)) {
			printConnectionLost();
			return Protocol.NOERRORBUTLEFTMATCH ;
		}
		if(message.equals(Protocol.GENERALERROR)) {
			printConnectionLost();
			return Protocol.ERRORMATCH;
		}
		
		if(!message.equals(Protocol.INFORMATIONMATCHMESSAGE))
		{
			printConnectionLost();
			return Protocol.ERRORMATCH;
		}
		
		message = read() ;
		
		if(message.equals(Protocol.CONNECTION_LOST)) {
			printConnectionLost();
			return Protocol.NOERRORBUTLEFTMATCH ;
		}
		if(message.equals(Protocol.GENERALERROR)) {
			printConnectionLost();
			return Protocol.ERRORMATCH;
		}
		
		parseMatchInformation.addNewInformation(message);
				
		message = read() ;
		
		if(message == null)
			return Protocol.ERRORMATCH;
		
		if(message.equals(Protocol.CONNECTION_LOST)) {
			printConnectionLost();
			return Protocol.NOERRORBUTLEFTMATCH ;
		}
		if(message.equals(Protocol.GENERALERROR)) {
			printConnectionLost();
			return Protocol.ERRORMATCH;
		}
		
		if(!message.equals(Protocol.MATCHSTARTED))
			return Protocol.ERRORMATCH;

		showView(friendlyBattle,field);
		setMatch_activated(true);
		parseMatchInformation.setReady(true);
		
		return Protocol.NOERRORMATCH;
}
	
	public String read() {
		
		String message = null ;
		
		try {
			
			if(in == null) {
				return null ;
			}
			
			message = in.readLine();
			
			if(message == null)
			{
				printConnectionLost();
				return null ;
			}
			
			System.out.println("[MATCHCLIENT] Message: "+message);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			printConnectionLost();
			return null ;
		
		}
		return message;
	}
	
	public String readingMatchStarted() throws IOException {
		
		String message = null ;

		if(in == null) {
			setMatch_activated(false);
			return Protocol.ERRORMATCH;
		}
		
		if(in.ready()) {
			message = read() ;
			if(message == null)
			{
				setMatch_activated(false);
				return Protocol.ERRORMATCH;
				
			}else if(message.equals(Protocol.INFORMATIONMATCHMESSAGE)) {
				SoundHandler.getInstance().startHit();
		    	
				message = read() ;
				parseMatchInformation.addNewInformation(message);
				
			}else if(message.equals(Protocol.HOVERBALL)) {
				
				message = read() ;
				
				double x = Double.parseDouble(message.split(Protocol.BALLDELIMITER)[0]);
				double y = Double.parseDouble(message.split(Protocol.BALLDELIMITER)[1]);
				
				parseMatchInformation.setHover(new VectorFioreNoSync(x, y));
				
			}else if(message.equals(Protocol.HOVERNOBALL)) {
				
				parseMatchInformation.setHoverFalseAll();
				
			}else if(message.equals(Protocol.YOUWON)){
				//TODO
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setMatch_activated(false);
				return Protocol.YOUWON;
			}else if(message.equals(Protocol.YOULOST)){
				//TODO
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setMatch_activated(false);
				return Protocol.YOULOST;
			}else if(message.equals(Protocol.LEFTGAME) || message.equals(Protocol.CONNECTION_LOST)){
				MatchController.getInstance().setTextToShow(message, 13 , Dialog.ERROR_WINDOW, 4);
				setMatch_activated(false);
				return Protocol.NOERRORBUTLEFTMATCH;
			}
			else if(message.equals(Protocol.YOUSCORED)){
				SoundHandler.getInstance().startGoalHome();
				MatchController.getInstance().setTextToShow(message, 13 , Dialog.INFORMATION_WINDOW, 7);
				parseMatchInformation.setHomeScored(true);
				
			}else if(message.equals(Protocol.OPPONENTSCORED)){
				SoundHandler.getInstance().startGolGuest();
				MatchController.getInstance().setTextToShow(message, 13 , Dialog.ATTENTION_WINDOW, 7);
				parseMatchInformation.setGuestScored(true);
				
			}else if(message.equals(Protocol.NOATKICKOFF)){
				MatchController.getInstance().setTextToShow(message, 10 , Dialog.ATTENTION_WINDOW, 7);
				
			}else if(message.equals(Protocol.GENERALERROR)){
				setMatch_activated(false);
				return Protocol.ERRORMATCH;
			}
		}
		return Protocol.NOERRORMATCH ;
		
	}

	public void showView(boolean friendly_battle,int field) {
		Updater.getInstance().startUpdater(friendly_battle,field);
	}


	@Override
	protected String call() throws Exception {
		
		String res = Protocol.NOERRORMATCH ;
		
		try {
			
			res = initalSettings() ;
			if(res.equals(Protocol.NOERRORBUTLEFTMATCH) || res.equals(Protocol.ERRORMATCH) ) 
				return res ;
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while(match_activated) {
			
			try {
				res = readingMatchStarted();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}
		return res;
	}
	
	
	public boolean isMatch_activated() {
		return match_activated;
	}
	
	public void setMatch_activated(boolean match_activated) {
		this.match_activated = match_activated;
	}
	
	
	private void printConnectionLost() {
		closeStream();
	}
	
	private void closeStream() {
		
		try {
			if(in != null) 
				in.close();
			in = null ;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
