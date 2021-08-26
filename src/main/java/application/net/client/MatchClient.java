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
import javafx.concurrent.Task;

public class MatchClient extends Task<Void>{

	
	private Client client = null ;
	private MatchHandler matchHandler = null ;
	private BufferedReader in = null ;
	private Lineup lineup1 = new Lineup(Lineup.LINEUP1);
	private String usernameGuest = null ;
	private boolean match_activated = false ;
	
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

	
	public void initalSettings() throws IOException {
		
		String message = null ;
		
		message = in.readLine();
		
		if(!message.equals(Protocol.ITSTHETURNOF)) {
			return ;
		}

		message = in.readLine();
		
		System.out.println("[CLIENT] Setting message: "+Protocol.ITSTHETURNOF+" -> "+message);
		
		if(message.equals(Protocol.ITSYOURTURN))
			matchHandler.setTurn(true);
		else 
			matchHandler.setTurn(false);

		
		client.sendMessage(Protocol.TYPEOFLINEUP);
		client.sendMessage(""+lineup1.getCurrentLineup());
		
		
		message = in.readLine();
		
		if(!message.equals(Protocol.USERNAMEGUEST))
		{
			return;
		}
		
		usernameGuest = in.readLine();
						
		System.out.println("[CLIENT] Setting message: "+Protocol.USERNAMEGUEST+" -> "+usernameGuest);
		
		message = in.readLine();

		
		if(!message.equals(Protocol.TYPEOFLINEUP))
		{
			return;
		}
		
		message = in.readLine();
		
		System.out.println("[CLIENT] Setting message: "+Protocol.TYPEOFLINEUP+" -> "+message);
		
		int typeOfGuestLineUp = Integer.parseInt(message);
		
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
		
		message = in.readLine();
		
		if(!message.equals(Protocol.GAMESTARTED)) {
			return;
		}
		
		System.out.println("[CLIENT] Setting message: "+Protocol.GAMESTARTED);
		
		showView();
		match_activated  = true ;
	}
	
	public void read() throws IOException {
		
		String message = null ;
		
		if(in.ready()) {
			
			message = in.readLine();
			
			System.out.print("[CLIENT] Match message: "+message);
			
			if(message.equals(Protocol.MOVEBALL)) {
				
				message = in.readLine();
				
				System.out.println(usernameGuest+" -> "+message);
				
				String[] stringa = message.split(";");
				
				double xPos = Protocol.parseCoordinates(stringa[0])[0] ;
				double yPos = Protocol.parseCoordinates(stringa[0])[1] ;
				
				double xVel = Protocol.parseCoordinates(stringa[1])[0] * -1  ;
				double yVel = Protocol.parseCoordinates(stringa[1])[1] ;
				
				xPos+= Settings.DIMENSIONSTANDARDBALL;
				yPos+= Settings.DIMENSIONSTANDARDBALL;
				
				xPos = Settings.FIELDWIDTHFRAME - xPos ;
				
				Ball b = matchHandler.tookBall(xPos, yPos);
				
				if(b == null || b.getPlayer() == 1 || matchHandler.getTurn()) {
					return;
				}
				
				b.setVelocity(new VelocityNoSync(xVel, yVel));
				
				matchHandler.setTurn(!matchHandler.getTurn());
			}
			else if(message.equals(Protocol.LEFTGAME)) {
				
				setMatch_activated(false);
				
			}
			else {
				System.out.println();
			}
		}
		
	}

	public void showView() {
		Updater.getInstance().startUpdater();
	}


	@Override
	protected Void call() throws Exception {
		
		try {
			initalSettings();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while(match_activated) {
			
			try {
				read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	
	public boolean isMatch_activated() {
		return match_activated;
	}
	
	public void setMatch_activated(boolean match_activated) {
		this.match_activated = match_activated;
	}
}
