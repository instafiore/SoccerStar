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
import application.control.MatchController;
import application.model.game.entity.Ball;
import application.model.game.entity.Lineup;
import application.model.game.entity.ParseMatchInformation;
import application.model.game.handler.MatchHandler;
import application.model.game.physics.VectorFioreNoSync;
import application.model.game.physics.VelocityNoSync;
import application.net.common.Protocol;
import application.view.Field;
import javafx.concurrent.Task;

public class MatchClient extends Task<Boolean>{

	
	private Client client = null ;
	private BufferedReader in = null ;
	private Lineup lineup1 = new Lineup(Lineup.LINEUP1);
	private String usernameGuest = null ;
	private boolean match_activated = false ;
	
	private static final int ERROR = 0 ;
	private static final int NOERROR = 1 ;
	private static final int NOERRORBUTLEFT = 2 ;
	private ParseMatchInformation parseMatchInformation ;
	private int field ;
	
	public MatchClient(Client client ) {
		super();
		this.client = client;
		in = client.getIn();
		parseMatchInformation = new ParseMatchInformation();
		
		MatchController.getInstance().setParseMatchInformation(parseMatchInformation);
		
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	
	public int initalSettings() throws IOException {
		
		String message = null ;
		
		client.sendMessage(Protocol.TYPEOFLINEUP);
		client.sendMessage(""+lineup1.getCurrentLineup());
		
		
		message = read() ;
		
		if(message == null)
			return ERROR;
		
		
		if(message.equals(Protocol.CONNECTION_LOST))
		{
			printConnectionLost();
			return NOERRORBUTLEFT ;
		}
		
		if(message.equals(Protocol.RELOADING_APP)) {
			printConnectionLost();
			return ERROR;
		}
		
		
		if(!message.equals(Protocol.USERNAMEGUEST))
		{
			printConnectionLost();
			return ERROR;
		}
		
		
		usernameGuest = read() ;
		if(usernameGuest == null)
			return ERROR;
		
		
		if(usernameGuest.equals(Protocol.CONNECTION_LOST)) 
		{
			printConnectionLost();
			return NOERRORBUTLEFT ;
		}
		
		if(usernameGuest.equals(Protocol.RELOADING_APP)) {
			printConnectionLost();
			return ERROR;
		}
		
		message = read();
		
		if(message.equals(Protocol.CONNECTION_LOST)) {
			printConnectionLost();
			return NOERRORBUTLEFT ;
		}
		if(message.equals(Protocol.RELOADING_APP)) {
			printConnectionLost();
			return ERROR;
		}
		
		if(!message.equals(Protocol.INFORMATIONMATCHMESSAGE))
		{
			printConnectionLost();
			return ERROR;
		}
		
		message = read() ;
		
		if(message.equals(Protocol.CONNECTION_LOST)) {
			printConnectionLost();
			return NOERRORBUTLEFT ;
		}
		if(message.equals(Protocol.RELOADING_APP)) {
			printConnectionLost();
			return ERROR;
		}
		
		parseMatchInformation.addNewInformation(message);
				
		message = read() ;
		
		if(message == null)
			return ERROR;
		
		if(message.equals(Protocol.CONNECTION_LOST)) {
			printConnectionLost();
			return NOERRORBUTLEFT ;
		}
		if(message.equals(Protocol.RELOADING_APP)) {
			printConnectionLost();
			return ERROR;
		}
		
		if(!message.equals(Protocol.GAMESTARTED))
			return ERROR;

		
		showView();
		setMatch_activated(true);
		
		return NOERROR;
}
	
	public String read() {
		
		String message = null ;
		
		try {
			
			if(in == null) {
				System.out.println("[MATCHCLIENT] "+Protocol.SERVERDISCONNETED);
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
	
	public boolean readingMatchStarted() throws IOException {
		
		String message = null ;
		
		if(in == null) {
			setMatch_activated(false);
			return false;
		}
		
	
		if(in.ready()) {
			message = read() ;
			
			if(message == null)
			{
				setMatch_activated(false);
				return false;
			}else if(message.equals(Protocol.INFORMATIONMATCHMESSAGE)) {
				message = read() ;
				parseMatchInformation.addNewInformation(message);
			}
			else if(message.equals(Protocol.CONNECTION_LOST)){
				
				setMatch_activated(false);
				return true;
			}else if(message.equals(Protocol.RELOADING_APP) || message.equals(Protocol.GENERALERROR)){
				setMatch_activated(false);
				return false;
			}
		}
		return true ;
		
	}

	public void showView() {
		Updater.getInstance().startUpdater();
	}


	@Override
	protected Boolean call() throws Exception {
		
		boolean res = false ;
		
		try {
			
			switch (initalSettings()) {
			case NOERROR:
				break;
			case NOERRORBUTLEFT:
					return true;		
			case ERROR:
					return false;
			default:
					return false;
			}
			
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
		
		System.out.println("[MATCHCLIENT] "+ Protocol.CONNECTION_LOST + " with: SERVER " );
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
