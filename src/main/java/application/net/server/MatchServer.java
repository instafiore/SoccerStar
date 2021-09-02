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

import com.sun.prism.paint.Stop;

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
	
	private static final String DISCONNECTEDBOTH = "Both are disconneted" ;
	private static final String DISCONNECTEDPLAYER1 = "Player1 is disconnected" ;
	private static final String DISCONNECTEDPLAYER2 = "Player2 is disconnected" ;
	private static final String NOONEISDISCONNETED = "No one is disconnected" ;
	
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
			
			String message = null ;
			
			matchHandler.setTurn( new Random().nextBoolean() );
	
			
			sendMessageAll(Protocol.ITSTHETURNOF);
	
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
			
			
			sendMessage(Protocol.USERNAMEGUEST, 1);
			sendMessage(username1, 1);
			
			
			sendMessage(Protocol.USERNAMEGUEST, 2);
			sendMessage(username2, 2);
			
			
			message = read1(); 
			if(message == null)
			{
				sendMessage(Protocol.CONNECTION_LOST, 1);
				notifyClients(DISCONNECTEDPLAYER1);
				return ;
			
			}
			
			if(!message.equals(Protocol.TYPEOFLINEUP)) {
				
				sendMessage(Protocol.CONNECTION_LOST, 1);
				sendMessage(Protocol.RELOADING_APP, 2);
				notifyClients(DISCONNECTEDPLAYER1);
				return;
			}
		
			message = read1();
			if(message == null)
			{
				sendMessage(Protocol.CONNECTION_LOST, 1);
				sendMessage(Protocol.RELOADING_APP, 2);
				notifyClients(DISCONNECTEDPLAYER1);
				return ;
			}
			
			try {
				typeOfLineup[0] = Integer.parseInt(message);
			} catch (Exception e) {
				
				System.out.println("[MATCHSERVER] Type of lineup1 is not a number ");
			
				sendMessage(Protocol.CONNECTION_LOST, 1);
				sendMessage(Protocol.RELOADING_APP, 2);
				notifyClients(DISCONNECTEDPLAYER1);
				return ;
			}
			
			sendMessage(Protocol.TYPEOFLINEUP, 1);
			sendMessage(""+typeOfLineup[0], 1);
			
			message = read2();
			
			if(message == null)
			{
				sendMessage(Protocol.CONNECTION_LOST, 2);
				sendMessage(Protocol.RELOADING_APP, 1);
				notifyClients(DISCONNECTEDPLAYER2);
				return ;
			
			}
			
			if(!message.equals(Protocol.TYPEOFLINEUP)) {
				sendMessage(Protocol.CONNECTION_LOST, 2);
				sendMessage(Protocol.RELOADING_APP, 1);
				notifyClients(DISCONNECTEDPLAYER2);
				return;
			}
	
			message = read2();
			
			if(message == null)
			{
				sendMessage(Protocol.CONNECTION_LOST, 2);
				sendMessage(Protocol.RELOADING_APP, 1);
				notifyClients(DISCONNECTEDPLAYER2);
				return ;
			
			}
			
			try {
				typeOfLineup[1] = Integer.parseInt(message);
			} catch (Exception e) {
				
				System.out.println("[MATCHSERVER] Type of lineup2 is not a number ");
				
				sendMessage(Protocol.CONNECTION_LOST, 2);
				sendMessage(Protocol.RELOADING_APP, 1);
				notifyClients(DISCONNECTEDPLAYER2);
				return ;

			}
			
			
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
			
			
			System.out.println("[MATCHSERVER] "+Protocol.GAMESTARTED+" -> Player1: "+username1+" , Player2: "+username2);
			
			while(whoIsDisconnected().equals(NOONEISDISCONNETED)) {
					
				int i ; 
				
				Pair<String, Integer> p = getAction();
				
				if(p == null)
					continue;
				
				if(p.getKey() == null) {
					
					i = p.getValue();
					
					if(i == 1) 
					{
						System.out.println("[Player 1] -> "+ Protocol.CONNECTION_LOST);
						sendMessage(Protocol.CONNECTION_LOST, i);
						notifyClients(NOONEISDISCONNETED);
					}
					else 
					{
						System.out.println("[Player 2] -> "+ Protocol.CONNECTION_LOST);
						sendMessage(Protocol.CONNECTION_LOST, i);
						notifyClients(NOONEISDISCONNETED);
					}
					
				}
				if(p.getKey().equals(Protocol.LEFTGAME)) {
					
					i = p.getValue();
					
					if(i == 1) 
					{
						System.out.println("[Player 1] -> "+ Protocol.CONNECTION_LOST);
						sendMessage(Protocol.CONNECTION_LOST, i);
						notifyClients(NOONEISDISCONNETED);
					}
					else 
					{
						System.out.println("[Player 2] -> "+ Protocol.CONNECTION_LOST);
						sendMessage(Protocol.CONNECTION_LOST, i);
						notifyClients(NOONEISDISCONNETED);
					}
					
					// The game is over	

					return ;
					
				}else if(p.getKey().equals(Protocol.MOVEBALL)) {
					
					i = p.getValue();
					
					if(i == 1) {
						message = read1();
						if(message == null)
						{
							sendMessage(Protocol.CONNECTION_LOST, 1);
							sendMessage(Protocol.RELOADING_APP, 2);
							notifyClients(DISCONNECTEDPLAYER1);
							return ;
						
						}
						System.out.print("[Player 1] -> ");
						
					}else {
						message = read2();
						if(message == null)
						{
							sendMessage(Protocol.CONNECTION_LOST, 2);
							sendMessage(Protocol.RELOADING_APP, 1);
							notifyClients(DISCONNECTEDPLAYER2);
							return ;
						
						}
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
					
					
					i = p.getValue();
					
					if(i == 1) {
						
						sendMessage(Protocol.CONNECTION_LOST, 1);
						sendMessage(Protocol.RELOADING_APP, 2);
						notifyClients(DISCONNECTEDPLAYER1);
						return ;
						
					}else {
						sendMessage(Protocol.CONNECTION_LOST, 2);
						sendMessage(Protocol.RELOADING_APP, 1);
						notifyClients(DISCONNECTEDPLAYER2);
						return ;
					}
					
				
				}else if(p.getKey().equals(Protocol.TYPEOFLINEUP)) {
					
					i = p.getValue();
					
					if(i == 1) {
						
						sendMessage(Protocol.CONNECTION_LOST, 1);
						sendMessage(Protocol.RELOADING_APP, 2);
						notifyClients(DISCONNECTEDPLAYER1);
						return ;
						
					}else {
						sendMessage(Protocol.CONNECTION_LOST, 2);
						sendMessage(Protocol.RELOADING_APP, 1);
						notifyClients(DISCONNECTEDPLAYER2);
						return ;
					}
					
				}
				
				try {
					Thread.sleep(Settings.REFRESHSERVER);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			sendMessageAll(Protocol.GAMEOVER);
			System.out.println("[MATCHSERVER] "+Protocol.GAMEOVER);
			notifyClients(NOONEISDISCONNETED);
					
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
			if(mess == null)
				return new Pair<String, Integer>(null, i);
			return new Pair<String, Integer>(mess, i);
		
		}else if(in2.ready()) {
			mess = in2.readLine();
			i = 2 ;
			if(mess == null)
				return new Pair<String, Integer>(null, i);
			return new Pair<String, Integer>(mess, i);
		}
		
		return null ;
		
	}
	
	
	private void sendMessage(String message, int sender)
	{
		String disconnected = whoIsDisconnected() ;
		
		if(sender == 1 && out2 != null && !disconnected.equals(DISCONNECTEDPLAYER2)) 
		{
			if(username1 != null && username2 != null)
				System.out.println("[MATCHSERVER] Message from :"+username1+" to : "+username2+" , Message: "+message);
			else
				System.out.println("[MATCHSERVER] Message from : player1 to : player2 , Message: "+message);
			out2.println(message);
		}
		else if(out1 != null && !disconnected.equals(DISCONNECTEDPLAYER1))
		{
			if(username1 != null && username2 != null && !disconnected.equals(DISCONNECTEDPLAYER1))
				System.out.println("[MATCHSERVER] Message from :"+username2+" to : "+username1+" , Message: "+message);
			else
				System.out.println("[MATCHSERVER] Message from : player2 to : player1 , Message: "+message);
			out1.println(message);
		}

	}
	
	private void sendMessageAll(String message) {
		
		String disconnected = whoIsDisconnected() ;
		
		if(disconnected.equals(NOONEISDISCONNETED) )
		{
			if(username1 != null && username2 != null )
				System.out.println("[MATCHSERVER] Message from server for "+username1+" and "+username2+" , message: "+message);
			else
				System.out.println("[MATCHSERVER] Message from server for player1 and player2 , message: "+message);
		}else if(disconnected.equals(DISCONNECTEDPLAYER1)) {
			if(username2 != null )
				System.out.println("[MATCHSERVER] Message from server just for "+username2+" , message: "+message);
			else
				System.out.println("[MATCHSERVER] Message from server just for player2 , message: "+message);
		}else if(disconnected.equals(DISCONNECTEDPLAYER2)) {
			if(username1 != null )
				System.out.println("[MATCHSERVER] Message from server just for "+username1+" , message: "+message);
			else
				System.out.println("[MATCHSERVER] Message from server just for player1 , message: "+message);
		}else {
			return ;
		}
	
		if(out1 != null && !disconnected.equals(DISCONNECTEDPLAYER1))
			out1.println(message);
		
		if(out2 != null && !disconnected.equals(DISCONNECTEDPLAYER2))
			out2.println(message);

			
	}
	
	private String whoIsDisconnected() {
		
		if(in1 == null && in2 == null)
		{
			System.out.println("[MATCHSERVER] "+DISCONNECTEDBOTH);
			return DISCONNECTEDBOTH ;
		}
		
		if(in1 == null)
		{
			System.out.println("[MATCHSERVER] "+DISCONNECTEDPLAYER1);
			return DISCONNECTEDPLAYER1 ;
		}
		
		if(in2 == null)
		{
			System.out.println("[MATCHSERVER] "+DISCONNECTEDPLAYER2);
			return DISCONNECTEDPLAYER2 ;
		}
		
		return NOONEISDISCONNETED ;
		
	}
	
	public String read1() {
		
		String message = null ;
		
		String disconnected = whoIsDisconnected() ;
		
		if(!disconnected.equals(NOONEISDISCONNETED) )
		{
			if(!whoIsDisconnected().equals(DISCONNECTEDBOTH))
			{
				if(whoIsDisconnected().equals(DISCONNECTEDPLAYER1))
					out2.println(Protocol.LEFTGAME);
				else 
					out1.println(Protocol.LEFTGAME);
			}
			
			return null;
		}
		
		try {
			message = in1.readLine();
			
			if(message == null) {
				if(username1 != null)
					System.out.println("[MATCHSERVER] "+Protocol.CONNECTION_LOST+" with "+username1);
				else
					System.out.println("[MATCHSERVER] "+Protocol.CONNECTION_LOST+" with player1");
				in1 = null ;
				out1 = null ;
				out2.println(Protocol.LEFTGAME);
				return null;
			}else
			{
				if(username1 != null)
					System.out.println("[MATCHSERVER] Message: "+message+" from "+username1);
				else
					System.out.println("[MATCHSERVER] Message: "+message+" from player1");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return message ;
	}
	
	public String read2() {

		String message = null ;

		String disconnected = whoIsDisconnected() ;
		
		if(!disconnected.equals(NOONEISDISCONNETED) )
		{
			if(!whoIsDisconnected().equals(DISCONNECTEDBOTH))
			{
				if(whoIsDisconnected().equals(DISCONNECTEDPLAYER1))
					out2.println(Protocol.LEFTGAME);
				else 
					out1.println(Protocol.LEFTGAME);
			}
			return null;
		}
		
		try {
			message = in2.readLine();
			
			if(message == null) {
				if(username2 != null)
					System.out.println("[MATCHSERVER] "+Protocol.CONNECTION_LOST+" with "+username2);
				else
					System.out.println("[MATCHSERVER] "+Protocol.CONNECTION_LOST+" with player2");
				in2 = null ;
				out2 = null ;
				out1.println(Protocol.LEFTGAME);
				
				return null;
			}else
			{
				if(username2 != null)
					System.out.println("[MATCHSERVER] Message: "+message+" from "+username2);
				else
					System.out.println("[MATCHSERVER] Message: "+message+" from player2");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return message ;
	}

	private void closeStreams() {
		
		try {
			if(in1 != null) 
				in1.close();
			in1 = null ;
			if(out1 != null)
				out1.close(); 
			out1 = null ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			if(in2 != null) 
				in2.close();
			in2 = null ;
			if(out2 != null)
				out2.close(); 
			out2 = null ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	private void closeStream1() {
		try {
			if(in1 != null) 
				in1.close();
			in1 = null ;
			if(out1 != null)
				out1.close(); 
			out1 = null ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void closeStream2() {
		try {
			if(in2 != null) 
				in2.close();
			in2 = null ;
			if(out2 != null)
				out2.close(); 
			out2 = null ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void notifyClients(String whoIsDisconnected) {
		player1.notifyEndMatch();
		player2.notifyEndMatch();
		
		if(whoIsDisconnected.equals(DISCONNECTEDBOTH)) {
			closeStreams();
		}else if(whoIsDisconnected.equals(DISCONNECTEDPLAYER1)){
			closeStream1();
		}else if(whoIsDisconnected.equals(DISCONNECTEDPLAYER2)){
			closeStream2();
		}
	}

}
