package application.net.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
	private Socket player1 ;
	private Socket player2 ;
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
	
	public MatchServer(Socket player1, Socket player2 , Field field) {
		super();
		this.field = field ;
		this.player1 = player1;
		this.player2 = player2;
		
		
		try {
			in1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
			in2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
			out1 = new PrintWriter(new BufferedOutputStream(player1.getOutputStream()),true);
			out2 = new PrintWriter(new BufferedOutputStream(player2.getOutputStream()),true);
			
			Thread t = new Thread(this);
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void run() {
		
		
		try {
		
			matchHandler.setTurn( new Random().nextBoolean() );
			
			sendMessageAll(Protocol.ITSTHETURNOF);
			
			if(matchHandler.getTurn())
				sendMessageAll("1");
			else
				sendMessageAll("2");
			
			
			String mess = in1.readLine();
			if(!mess.equals(Protocol.MYUSERNAMEIS))
			{
				// Errore
				return;
			}
			
			username1 = in1.readLine();
		
			sendMessage(Protocol.USERNAMEGUEST, 1);
			sendMessage(username1, 1);
			
			mess = in2.readLine();
			if(!mess.equals(Protocol.MYUSERNAMEIS))
			{
				// Errore
				return;
			}
			
			username2 = in2.readLine();
			sendMessage(Protocol.USERNAMEGUEST, 2);
			sendMessage(username2, 2);
			
			
			mess = in1.readLine();
			typeOfLineup[0] = Integer.parseInt(mess);
			sendMessage(Protocol.TYPEOFLINEUP, 1);
			sendMessage(""+typeOfLineup[0], 1);
			
			
			mess = in2.readLine();
			typeOfLineup[1] = Integer.parseInt(mess);
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
			
			while(!Thread.interrupted()) {
					
				int i ; 
				
				Pair<String, Integer> p = getAction();
				
				if(p == null)
					continue;
				
				if(p.getKey().equals(Protocol.LEFTGAME)) {
					
					// The game is over
					
				}else if(p.getKey().equals(Protocol.MOVEBALL)) {
					
					
					i = p.getValue();
					
					if(i == 1) {
						mess = in1.readLine();
					}else {
						mess = in2.readLine();
					}
					
					String[] stringa = mess.split(";");
					
					double xPos = Protocol.parseCoordinates(stringa[0])[0];
					double yPos = Protocol.parseCoordinates(stringa[0])[1];
					
					double xVel = Protocol.parseCoordinates(stringa[1])[0];
					double yVel = Protocol.parseCoordinates(stringa[1])[1];
					
					
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
						sendMessage(mess, i);
					}
				
					
				}else if(p.getKey().equals(Protocol.MYUSERNAMEIS)) {
					
					// Error
					return;
				
				}else if(p.getKey().equals(Protocol.TYPEOFLINEUP)) {
					
					// Error
					return;
					
				}
			}
		
		} catch (IOException e) {
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
			out2.println(message);
		else if(out1 != null)
			out1.println(message);

	}
	
	private void sendMessageAll(String message) {
		
		if(out1 != null)
			out1.println(message);
		if(out2 != null)
			out2.println(message);
	
	}
	
	
//	else if(p.getKey().equals(Protocol.POSITIONSOFBALLS)) {
//		
//		if(ballAcquired == 2 )
//		{
//			// Error
//			return;
//		}
//		
//		++ballAcquired;
//		
//		i = p.getValue();
//		
//		
//		if(i == 1) 
//		{
//			if(!firstTypeOfLineUp1)
//			{
//				// Error
//				return;
//			}
//			mess = in1.readLine();
//		}
//		else {
//			
//			if(!firstTypeOfLineUp2)
//			{
//				// Error
//				return;
//			}
//			mess = in2.readLine();
//		}	
//		
//		
//		for(VectorFioreNoSync pos : Protocol.parsePositions(mess)) 
//			matchHandler.add(new Ball(pos, new VelocityNoSync(0.0), Settings.DIMENSIONSTANDARDBALL, i));
//		
//		
//		sendMessage(Protocol.POSITIONSOFBALLS, i);
//		sendMessage(mess, i);
//		
//	}
	
	
	
	
	
	
	
	

}
