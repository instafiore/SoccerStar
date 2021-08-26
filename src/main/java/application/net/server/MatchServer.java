package application.net.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigestSpi;
import java.util.ArrayList;
import java.util.Random;
import application.Settings;
import application.model.game.entity.Ball;
import application.model.game.entity.Field;
import application.model.game.entity.Lineup;
import application.model.game.handler.MatchHandler;
import application.model.game.physics.VectorFioreNoSync;
import application.model.game.physics.VelocityNoSync;
import application.net.common.Protocol;
import javafx.util.Pair;



public class MatchServer implements Runnable {

	
	private String username1 ;
	private String username2 ;
	private ClientHandler player1 ;
	private ClientHandler player2 ;
	private BufferedReader in1 ;
	private BufferedReader in2 ;
	private PrintWriter out1 ;
	private PrintWriter out2 ;
	private int ballAcquired = 0 ;
	private MatchHandler matchHandler;
	private Field field ;
	private Lineup lineup1 ;
	private Lineup lineup2 ;
	
	private Integer[] typeOfLineup = new Integer[2] ;
	private boolean matchActive = false ;
	
	public MatchServer(ClientHandler player1, ClientHandler player2 , Field field) {
		super();
		this.field = field ;
		this.player1 = player1;
		this.player2 = player2;
		
		
		username1 = player1.getUsername();
		username2 = player2.getUsername();
		
		matchHandler = new MatchHandler();
		
		try {
			in1 = new BufferedReader(new InputStreamReader(player1.getClient().getInputStream()));
			in2 = new BufferedReader(new InputStreamReader(player2.getClient().getInputStream()));
			out1 = new PrintWriter(new BufferedOutputStream(player1.getClient().getOutputStream()),true);
			out2 = new PrintWriter(new BufferedOutputStream(player2.getClient().getOutputStream()),true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		matchActive = true ;
	}

	public void run() {
		
		
		try {
			
			sendMessageAll(Protocol.PREPARINGMATCH);
			
			if(!matchActive)
			{
				return;
			}
			
			String message = null ;
			matchHandler.setTurn( new Random().nextBoolean() );
			
			sendMessageAll(Protocol.ITSTHETURNOF);
			
			if(!matchActive)
			{
				return;
			}
			
			if(matchHandler.getTurn())
			{
				sendMessage(Protocol.ITSYOURTURN, 2);
				sendMessage(Protocol.ITSNOTYOURTURN, 1);
			}
			else
			{
				sendMessage(Protocol.ITSYOURTURN, 1);
				sendMessage(Protocol.ITSNOTYOURTURN, 2);
			}

			if(!matchActive)
			{
				return;
			}
			
			sendMessage(Protocol.USERNAMEGUEST, 1);
			sendMessage(username1, 1);
			
			if(!matchActive)
			{
				return;
			}
			
			sendMessage(Protocol.USERNAMEGUEST, 2);
			sendMessage(username2, 2);
			
			if(!matchActive)
			{
				return;
			}
			
			message = in1.readLine();
			
			if(message == null || !message.equals(Protocol.TYPEOFLINEUP)) {
				System.out.println("ERRORE");
				player1.notifyClient();
				player2.notifyClient();
				sendMessageAll(Protocol.LEFTGAME);
				return;
			}
		
			message = in1.readLine();
			
			typeOfLineup[0] = Integer.parseInt(message);
			sendMessage(Protocol.TYPEOFLINEUP, 1);
			sendMessage(""+typeOfLineup[0], 1);
			
			message = in2.readLine();
			
			if(!message.equals(Protocol.TYPEOFLINEUP)) {
				System.out.println("ERRORE");
				player1.notifyClient();
				player2.notifyClient();
				sendMessageAll(Protocol.LEFTGAME);
				return;
			}
	
			message = in2.readLine();
			typeOfLineup[1] = Integer.parseInt(message);
			sendMessage(Protocol.TYPEOFLINEUP, 2);
			sendMessage(""+typeOfLineup[1], 2);
			
			
			
			ArrayList<Ball> balls1 = new ArrayList<Ball>();
			for(int j = 0 ; j < 5 ; ++j)
				balls1.add(new Ball(new VectorFioreNoSync(0.0), new VelocityNoSync(0.0), Settings.DIMENSIONSTANDARDBALL, 1));
			
			
			lineup1 = new Lineup(balls1, typeOfLineup[0]);
			

			
			ArrayList<Ball> balls2 = new ArrayList<Ball>();
			for(int j = 0 ; j < 5 ; ++j)
				balls2.add(new Ball(new VectorFioreNoSync(0.0), new VelocityNoSync(0.0), Settings.DIMENSIONSTANDARDBALL, 2));
			
			
			lineup2 = new Lineup(balls2, typeOfLineup[1]);

			lineup2.mirrorLineup();
			
			for(Ball b : balls1)
				matchHandler.add(b);
			
			for(Ball b : balls2)
				matchHandler.add(b);
			
			double x11 = Settings.FIELDWIDTHFRAME * 0.50 - Settings.DIMENSIONOFBALLTOPLAY ;
			double y11 = Settings.FIELDHEIGHTFRAME * 0.50 - Settings.DIMENSIONOFBALLTOPLAY ;
			VectorFioreNoSync position11 = new VectorFioreNoSync(x11,y11);
			
			Ball ball = new Ball(position11,new VelocityNoSync(0.0),Settings.DIMENSIONOFBALLTOPLAY , Ball.NOPLAYER);
			ball.setColor(Ball.WHITE);
			matchHandler.add(ball);
			
			sendMessageAll(Protocol.GAMESTARTED);
			
			
			System.out.println("[SERVER] "+Protocol.GAMESTARTED+" -> Player1: "+username1+" , Player2: "+username2);
			
			while(!Thread.interrupted()) {
					
				int i ; 
				
				Pair<String, Integer> p = getAction();
				
				if(p == null)
					continue;
				
				if(p.getKey().equals(Protocol.LEFTGAME)) {
					
					i = p.getValue();
					
					if(i == 1) 
					{
						System.out.println("[Player 1] -> "+ Protocol.LEFTGAME);
						sendMessage(Protocol.LEFTGAME, i);
					}
					else 
					{
						System.out.println("[Player 2] -> "+ Protocol.LEFTGAME);
						sendMessage(Protocol.LEFTGAME, i);
						
					}
					
					// The game is over	
					player1.notifyClient();
					player2.notifyClient();
					return ;
					
				}else if(p.getKey().equals(Protocol.MOVEBALL)) {
					
					i = p.getValue();
					
					if(i == 1) {
						message = in1.readLine();
						System.out.print("[Player 1] -> ");
						
					}else {
						message = in2.readLine();
						System.out.print("[Player 2] -> ");
					}
					
					System.out.println(Protocol.MOVEBALL+ " -> " +message);
					
					String[] stringa = message.split(";");
					
					double xPos = Protocol.parseCoordinates(stringa[0])[0];
					double yPos = Protocol.parseCoordinates(stringa[0])[1];
					
					double xVel = Protocol.parseCoordinates(stringa[1])[0];
					double yVel = Protocol.parseCoordinates(stringa[1])[1];
					
					
					xPos+= Settings.DIMENSIONSTANDARDBALL;
					yPos+= Settings.DIMENSIONSTANDARDBALL;
			
					if(i == 2)
						xPos = Settings.FIELDWIDTHFRAME - xPos ;
					
					Ball b = matchHandler.tookBall(xPos, yPos);
					
					if(b == null) {
						// Error
						return;
					}
					
					if(b.getPlayer() == i && ( i == 1 && matchHandler.getTurn() || i == 2 && !matchHandler.getTurn() ) )
					{
						matchHandler.setTurn(!matchHandler.getTurn());
						
						if(i == 2)
							xVel *= -1 ;
						b.setVelocity(new VelocityNoSync(xVel, yVel));
						while(!matchHandler.allStopped())
							matchHandler.moveBalls(field);
						
						sendMessage(Protocol.MOVEBALL, i);
						sendMessage(message, i);
						
					}
					
				}else if(p.getKey().equals(Protocol.MYUSERNAMEIS)) {
					
					player1.notifyClient();
					player2.notifyClient();
					return;
				
				}else if(p.getKey().equals(Protocol.TYPEOFLINEUP)) {
					
					player1.notifyClient();
					player2.notifyClient();
					return;
					
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Pair<String, Integer> getAction() throws IOException{
		
		String mess ;
		Integer i ;
		
		if(in1.ready()) {
			
			mess = in1.readLine();
			i = 1 ;
			return new Pair<String, Integer>(mess, i);
		}else if(in2.ready()) {
			mess = in2.readLine();
			i = 2 ;
			return new Pair<String, Integer>(mess, i);
		}
		
		return null ;
		
	}
	
	
	private void sendMessage(String message, int sender)
	{
		if(sender == 1 && out2 != null) 
		{
			if(username1 != null && username2 != null)
				System.out.println("[SERVER] Message from :"+username1+" to : "+username2+" , Message: "+message);
			else
				System.out.println("[SERVER] Message from : player1 to : player2 , Message: "+message);
			out2.println(message);
		}
		else if(out1 != null)
		{
			if(username1 != null && username2 != null)
				System.out.println("[SERVER] Message from :"+username2+" to : "+username1+" , Message: "+message);
			else
				System.out.println("[SERVER] Message from : player2 to : player1 , Message: "+message);
			out1.println(message);
		}

	}
	
	private void sendMessageAll(String message) {
		
		if(out1 == null && out2 == null )
			matchActive = false ;
		
		if(out1 != null && out2 != null )
			System.out.println("[SERVER] Message from server for "+username1+" and "+username2+" , message: "+message);
		else
			System.out.println("[SERVER] Message from server for player1 and player2 , message: "+message);
	
			out1.println(message);
			out2.println(message);
		
	}

}
