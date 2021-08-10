package application.net.client;

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

public class MatchClient implements Runnable{

	
	private Client client = null ;
	private MatchHandler matchHandler = null ;
	private BufferedReader in = null ;
	private Lineup lineup1 = new Lineup(Lineup.LINEUP1);
	private String usernameGuest = null ;
	
	public MatchClient(Client client) {
		super();
		this.client = client;
		this.matchHandler = new MatchHandler();
		
		
		in = client.getIn();
		
		MatchController.getInstance().addMatchHandler(matchHandler);
	
		Thread t = new Thread(this);
		t.start();
	}

	
	public void initalSettings() throws IOException {
		
		String message = null ;
		
		System.out.println("MIAO");
		
		message = in.readLine();
		
		
		
		if(!message.equals(Protocol.ITSTHETURNOF)) {
			//Errore
			return ;
		}
	
		
		
		message = in.readLine();
		System.out.println(message);
		
		if(message.equals(Protocol.ITSYOURTURN))
			matchHandler.setTurn(true);
		else 
			matchHandler.setTurn(false);
			
		client.sendMessage(Protocol.MYUSERNAMEIS);
		client.sendMessage(client.getUsername());
		
		client.sendMessage(Protocol.TYPEOFLINEUP);
		client.sendMessage(""+lineup1.getCurrentLineup());
		
		
		
		message = in.readLine();
		
		if(!message.equals(Protocol.USERNAMEGUEST))
		{
			// error
			return;
		}
		
		usernameGuest = in.readLine();
		
		
		
		message = in.readLine();
		
		System.out.println(message);
		
		
		if(!message.equals(Protocol.TYPEOFLINEUP))
		{
			// error
			return;
		}
		
		int typeOfGuestLineUp = Integer.parseInt(in.readLine());
		
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
			matchHandler.add(b);
		
		for(Ball b : balls2)
			matchHandler.add(b);
		
		
		message = in.readLine();
		
		
		
		if(!message.equals(Protocol.GAMESTARTED)) {
			// Error
			return;
		}
		
		showView();
	}
	
	public void read() throws IOException {
		
		String message = null ;
		
		if(in.ready()) {
			
			message = in.readLine();
			
			System.out.println(message);
			
			if(message.equals(Protocol.MOVEBALL)) {
				
				message = in.readLine();
				
				String[] stringa = message.split(";");
				
				double xPos = Protocol.parseCoordinates(stringa[0])[0] ;
				double yPos = Protocol.parseCoordinates(stringa[0])[1] ;
				
				double xVel = Protocol.parseCoordinates(stringa[1])[0] * -1  ;
				double yVel = Protocol.parseCoordinates(stringa[1])[1] ;
				
				Ball b = matchHandler.tookBall(xPos, yPos);
				
				if(b.getPlayer() == 1 || matchHandler.getTurn()) {
					// Error
					return;
				}
				
				b.setVelocity(new VelocityNoSync(xVel, yVel));
				
				matchHandler.setTurn(!matchHandler.getTurn());
			}
		}
		
	}

	public void showView() {
		Updater.getInstance().startUpdater();
	}
	
	
	
	public void run() {
		
		try {
			initalSettings();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while(!Thread.interrupted()) {
			
			try {
				read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
}
