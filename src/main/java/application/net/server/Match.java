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



public class Match implements Runnable {

	
	private String username1 ;
	private String username2 ;
	private Socket player1 ;
	private Socket player2 ;
	private BufferedReader in1 ;
	private BufferedReader in2 ;
	private PrintWriter out1 ;
	private PrintWriter out2 ;
	private boolean turn ;
	private int ballAcquired = 0 ;
	private MatchHandler matchHandler;
	private Field field ;
	private Lineup lineup ;
	
	// Tell if before sending lineup client1 sent type of lineup
	private Boolean firstTypeOfLineUp1 = false ;
	// Tell if before sending lineup client2 sent type of lineup
	private Boolean firstTypeOfLineUp2 = false ;
	
	private Integer[] typeOfLineup = new Integer[2] ;
	
	public Match(Socket player1, Socket player2 , Field field) {
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
		
			turn = new Random().nextBoolean();
			
			sendMessageAll(Protocol.ITSTHETURNOF);
			
			if(turn)
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
		
			sendMessage(Protocol.MYUSERNAMEIS, 1);
			sendMessage(username1, 1);
			
			mess = in2.readLine();
			if(!mess.equals(Protocol.MYUSERNAMEIS))
			{
				// Errore
				return;
			}
			
			username2 = in2.readLine();
			sendMessage(Protocol.MYUSERNAMEIS, 2);
			sendMessage(username2, 2);
			
			
			while(!Thread.interrupted()) {
					
				int i ; 
				
				Pair<String, Integer> p = getAction();
				
				if(p == null)
					continue;
				
				if(p.getKey().equals(Protocol.LEFTGAME)) {
					
					// The game is over
					
				}else if(p.getKey().equals(Protocol.MOVEBALL)) {
					
					if(!firstTypeOfLineUp1 || !firstTypeOfLineUp2)
					{
						// Error
						return;
					}
					
					
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
					
					if(b.getPlayer() == i && ( i == 1 && turn || i == 2 && !turn) )
					{
						turn = !turn ;
						
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
					
					
					i = p.getValue();
					
					if(i == 1) 
					{
						
						if(firstTypeOfLineUp1)
						{
							// Error
							return;
						}
						
						firstTypeOfLineUp1 = true;
						mess = in1.readLine();
						typeOfLineup[0] = Integer.parseInt(mess);
						sendMessage(Protocol.TYPEOFLINEUP, i);
						sendMessage(""+typeOfLineup[0], i);
						
					}
					else {
						
						if(firstTypeOfLineUp2)
						{
							// Error
							return;
						}
						
						firstTypeOfLineUp2 = true;
						mess = in2.readLine();
						typeOfLineup[1] = Integer.parseInt(mess);
						sendMessage(Protocol.TYPEOFLINEUP, i);
						sendMessage(""+typeOfLineup[1], i);
					}
					
					ArrayList<Ball> balls = new ArrayList<Ball>();
					for(int j = 0 ; j < 5 ; ++j)
						balls.add(new Ball(new VectorFioreNoSync(0.0), new VelocityNoSync(0.0), Settings.DIMENSIONSTANDARDBALL, i));
					
					
					lineup = new Lineup(balls, typeOfLineup[i - 1 ]);
					
					if(i == 2)
						lineup.mirrorLineup();
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
