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
import application.model.game.handler.MatchHandler;
import application.model.game.physics.VectorFioreNoSync;
import application.model.game.physics.VelocityNoSync;
import application.net.common.Protocol;
import javafx.concurrent.Task;

public class MatchClient extends Task<Boolean>{

	
	private Client client = null ;
	private MatchHandler matchHandler = null ;
	private BufferedReader in = null ;
	private Lineup lineup1 = new Lineup(Lineup.LINEUP1);
	private String usernameGuest = null ;
	private boolean match_activated = false ;
	
	private static final int ERROR = 0 ;
	private static final int NOERROR = 1 ;
	private static final int NOERRORBUTLEFT = 2 ;
	
	public MatchClient(Client client) {
		super();
		this.client = client;
		this.matchHandler = new MatchHandler();
		
		
		in = client.getIn();
		
		MatchController.getInstance().addMatchHandler(matchHandler);
	
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	
	public int initalSettings() throws IOException {
		
		String message = null ;
		
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
		
		if(!message.equals(Protocol.ITSTHETURNOF)) 
		{
			printConnectionLost();
			return ERROR;
		}
		

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
		
		System.out.println("[MATCHCLIENT] Setting message: "+Protocol.ITSTHETURNOF+" -> "+message);
		
		if(message.equals(Protocol.ITSYOURTURN))
			matchHandler.setTurn(true);
		else 
			matchHandler.setTurn(false);

		
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
						
		System.out.println("[MATCHCLIENT] Setting message: "+Protocol.USERNAMEGUEST+" -> "+usernameGuest);
		
		message = read() ;
		if(message == null)
			return ERROR;
		
		
		if(message.equals(Protocol.CONNECTION_LOST)) {
			
			printConnectionLost();
			return NOERRORBUTLEFT ;
		}
		
		if(!message.equals(Protocol.TYPEOFLINEUP))
		{
			printConnectionLost();
			return ERROR;
		}
		
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
		
		System.out.println("[MATCHCLIENT] Setting message: "+Protocol.TYPEOFLINEUP+" -> "+message);
		
		int typeOfGuestLineUp ;
		try {
			typeOfGuestLineUp = Integer.parseInt(message);
		} catch (Exception e) {
			System.out.println("[MATCHCLIENT] Type of typeOfGuestLineUp is not a number ");
			
			client.sendMessage(Protocol.LEFTGAME);
			printConnectionLost();
			return ERROR;
		}
		
		ArrayList<Ball> balls1 = new ArrayList<Ball>();
		
		for(int j = 0 ; j < 5 ; ++j)
			balls1.add(new Ball(new VectorFioreNoSync(0.0), new VelocityNoSync(0.0), Settings.DIMENSIONSTANDARDBALL, 1));
		
		lineup1.addBalls(balls1);
		lineup1.setPositions();
		
		ArrayList<Ball> balls2 = new ArrayList<Ball>();
		for(int j = 0 ; j < 5 ; ++j)
			balls2.add(new Ball(new VectorFioreNoSync(0.0), new VelocityNoSync(0.0), Settings.DIMENSIONSTANDARDBALL, 2));
		
		
		Lineup lineup2 = new Lineup(balls2, typeOfGuestLineUp);

		lineup2.mirrorLineup();
		
		for(Ball b : balls1)
		{
			b.setColor(Ball.BLUE);
			matchHandler.add(b);
		}
		
		for(Ball b : balls2)
		{
			b.setColor(Ball.RED);
			matchHandler.add(b);
		}
			
		
		double x11 = Settings.FIELDWIDTHFRAME * 0.50 - Settings.DIMENSIONOFBALLTOPLAY ;
		double y11 = Settings.FIELDHEIGHTFRAME * 0.50 - Settings.DIMENSIONOFBALLTOPLAY ;
		VectorFioreNoSync position11 = new VectorFioreNoSync(x11,y11);
		
		Ball ball = new Ball(position11,new VelocityNoSync(0.0),Settings.DIMENSIONOFBALLTOPLAY , Ball.NOPLAYER);
		ball.setColor(Ball.WHITE);
		matchHandler.add(ball);
		
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
			
			if( message.equals(Protocol.GAMESTARTED) )
				System.out.println("[MATCHCLIENT] Setting message: "+message);
			else if(message.equals(Protocol.ITSTHETURNOF))
				System.out.println("[MATCHCLIENT] Setting message: "+Protocol.ITSTHETURNOF+" -> "+message);
			else if(message.equals(Protocol.USERNAMEGUEST))
				System.out.println("[MATCHCLIENT] Setting message: "+Protocol.USERNAMEGUEST+" -> "+usernameGuest);
			else if(!message.equals(Protocol.MOVEBALL))
				System.out.println("[MATCHCLIENT] Match message: "+message);
			
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
			}
			
			
			if(message.equals(Protocol.MOVEBALL)) {
				
				String otherPartOfMessage = read() ;
				
				if(otherPartOfMessage == null)
				{
					setMatch_activated(false);
					return false;
				}
				
				message ="[MATCHCLIENT] "+ message +": " + usernameGuest + " -> " + otherPartOfMessage ;
				
				
				System.out.println(message);
				
				String[] stringa = otherPartOfMessage.split(";");
				
				double xPos  = 0 ;
				double yPos = 0 ;
				double xVel = 0 ;
				double yVel = 0 ;
				
				try {
					
					xPos = Protocol.parseCoordinates(stringa[0])[0] ;
					yPos = Protocol.parseCoordinates(stringa[0])[1] ;
					xVel = Protocol.parseCoordinates(stringa[1])[0] * -1  ;
					yVel = Protocol.parseCoordinates(stringa[1])[1] ;
					
				} catch (Exception e) {
					System.out.println("[MATCHCLIENT] NOT A NUMBER");
					client.sendMessage(Protocol.CONNECTION_LOST);
					setMatch_activated(false);
					return false;
				}
				
				xPos+= Settings.DIMENSIONSTANDARDBALL;
				yPos+= Settings.DIMENSIONSTANDARDBALL;
				
				xPos = Settings.FIELDWIDTHFRAME - xPos ;
				
				Ball b = matchHandler.tookBall(xPos, yPos);
				
				if(b == null || b.getPlayer() == 1 || matchHandler.getTurn()) {
					if(b == null )
					{
						System.out.println("B NULL");
						setMatch_activated(false);
						return true;
					}
					if(b.getPlayer() == 1 )
						System.out.println("PLAYER = 1");
					if(matchHandler.getTurn() )
						System.out.println("MY TURN NOT HIS");
					setMatch_activated(false);
					return true;
				}
				
				b.setVelocity(new VelocityNoSync(xVel, yVel));
				
				matchHandler.setTurn(!matchHandler.getTurn());
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
