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
import application.model.game.entity.DataMatch;
import application.model.game.entity.Field;
import application.model.game.entity.Lineup;
import application.model.game.entity.ParseMatchInformation;
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
	private boolean gotInformationMessage = false ;
	
	private static final String DISCONNECTEDBOTH = "Both are disconneted" ;
	private static final String DISCONNECTEDPLAYER1 = "Player1 is disconnected" ;
	private static final String DISCONNECTEDPLAYER2 = "Player2 is disconnected" ;
	private static final String NOONEISDISCONNETED = "No one is disconnected" ;
	
	private static final int PLAYER1 = Ball.BLUE ;
	private static final int PLAYER2 = Ball.RED ;
	
	private Integer[] typeOfLineup = new Integer[2] ;
	private boolean matchActive = false ;
	private String informationMessagePlayer1 = null ;
	private String informationMessagePlayer2 = null ;
	private DataMatch dataMatch = null ;
	
	public MatchServer(ClientHandler player1, ClientHandler player2 , Field field) {
		super();
		this.field = field ;
		this.player1 = player1;
		this.player2 = player2;
		
		
		username1 = player1.getUsername();
		username2 = player2.getUsername();
		
		dataMatch = new DataMatch();
		matchHandler = new MatchHandler(dataMatch);

		in1 = player1.getIn();
		in2 = player2.getIn();
		out1 = player1.getOut();
		out2 = player2.getOut();
			
		matchActive = true ;
	}

	
	public void run() {
		
		
		try {

			sendMessageAll(Protocol.PREPARINGMATCH);
			
			String message = null ;
			
			matchHandler.setTurn( new Random().nextBoolean() );
	
			
			sendMessage(Protocol.USERNAMEGUEST, PLAYER1);
			sendMessage(username1, PLAYER1);
			
			
			sendMessage(Protocol.USERNAMEGUEST, PLAYER2);
			sendMessage(username2, PLAYER2);
			
			
			message = read1(); 
			if(message == null)
			{
				sendMessage(Protocol.CONNECTION_LOST, PLAYER1);
				notifyClients(DISCONNECTEDPLAYER1);
				return ;
			
			}
			
			if(!message.equals(Protocol.TYPEOFLINEUP)) {
				
				sendMessage(Protocol.CONNECTION_LOST, PLAYER1);
				sendMessage(Protocol.RELOADING_APP, PLAYER2);
				notifyClients(DISCONNECTEDPLAYER1);
				return;
			}
		
			message = read1();
			if(message == null)
			{
				sendMessage(Protocol.CONNECTION_LOST, PLAYER1);
				sendMessage(Protocol.RELOADING_APP, PLAYER2);
				notifyClients(DISCONNECTEDPLAYER1);
				return ;
			}
			
			try {
				typeOfLineup[0] = Integer.parseInt(message);
			} catch (Exception e) {
				
				System.out.println("[MATCHSERVER] Type of lineup1 is not a number ");
			
				sendMessage(Protocol.CONNECTION_LOST, PLAYER1);
				sendMessage(Protocol.RELOADING_APP, PLAYER2);
				notifyClients(DISCONNECTEDPLAYER1);
				return ;
			}
			

			
			message = read2();
			
			if(message == null)
			{
				sendMessage(Protocol.CONNECTION_LOST, PLAYER2);
				sendMessage(Protocol.RELOADING_APP, PLAYER1);
				notifyClients(DISCONNECTEDPLAYER2);
				return ;
			
			}
			
			if(!message.equals(Protocol.TYPEOFLINEUP)) {
				sendMessage(Protocol.CONNECTION_LOST, PLAYER2);
				sendMessage(Protocol.RELOADING_APP, PLAYER1);
				notifyClients(DISCONNECTEDPLAYER2);
				return;
			}
	
			message = read2();
			
			if(message == null)
			{
				sendMessage(Protocol.CONNECTION_LOST, PLAYER2);
				sendMessage(Protocol.RELOADING_APP, PLAYER1);
				notifyClients(DISCONNECTEDPLAYER2);
				return ;
			
			}
			
			try {
				typeOfLineup[1] = Integer.parseInt(message);
			} catch (Exception e) {
				
				System.out.println("[MATCHSERVER] Type of lineup2 is not a number ");
				
				sendMessage(Protocol.CONNECTION_LOST, PLAYER2);
				sendMessage(Protocol.RELOADING_APP, PLAYER1);
				notifyClients(DISCONNECTEDPLAYER2);
				return ;

			}
			
			
			ArrayList<Ball> balls1 = new ArrayList<Ball>();
			for(int j = 0 ; j < 5 ; ++j)
				balls1.add(new Ball(new VectorFioreNoSync(0.0), new VelocityNoSync(0.0), Settings.DIMENSIONSTANDARDBALL, Ball.BLUE));
			
			lineup1 = new Lineup(balls1, typeOfLineup[0]);
			
			ArrayList<Ball> balls2 = new ArrayList<Ball>();
			for(int j = 0 ; j < 5 ; ++j)
				balls2.add(new Ball(new VectorFioreNoSync(0.0), new VelocityNoSync(0.0), Settings.DIMENSIONSTANDARDBALL, Ball.RED));
			
			
			lineup2 = new Lineup(balls2, typeOfLineup[1]);

			lineup2.mirrorLineup();
			
			for(Ball b : balls1)
				matchHandler.add(b);
			
			for(Ball b : balls2)
				matchHandler.add(b);
			
			double x11 = Settings.FIELDWIDTHFRAME * 0.50 - Settings.DIMENSIONOFBALLTOPLAY ;
			double y11 = Settings.FIELDHEIGHTFRAME * 0.50 - Settings.DIMENSIONOFBALLTOPLAY ;
			VectorFioreNoSync position11 = new VectorFioreNoSync(x11,y11);
			
			Ball ball = new Ball(position11,new VelocityNoSync(0.0),Settings.DIMENSIONOFBALLTOPLAY , Ball.WHITE);
			ball.setColor(Ball.WHITE);
			matchHandler.add(ball);
			
			
			System.out.println("[MATCHSERVER] "+Protocol.GAMESTARTED+" -> Player1: "+username1+" , Player2: "+username2);
			
			sendMessageAll(Protocol.INFORMATIONMATCHMESSAGE);
			
			sendMessage(ParseMatchInformation.getString(matchHandler.getBalls() , matchHandler.getTurn() , PLAYER1) + Protocol.STRINGINFORMATIONDELIMITER, PLAYER2 );
			sendMessage(ParseMatchInformation.getString(matchHandler.getBalls() , !matchHandler.getTurn(), PLAYER2) + Protocol.STRINGINFORMATIONDELIMITER, PLAYER1 );
			
			sendMessageAll(Protocol.GAMESTARTED);
			
			while(whoIsDisconnected().equals(NOONEISDISCONNETED)) {
					
				int i ; 
				
				Pair<String, Integer> p = getAction();
				
				if(p == null && informationMessagePlayer1 != null && informationMessagePlayer2 != null) {
					
					sendMessageAll(Protocol.INFORMATIONMATCHMESSAGE);
					
					sendMessage(informationMessagePlayer1, PLAYER2 );
					sendMessage(informationMessagePlayer2, PLAYER1 );
					
					informationMessagePlayer1 = null ;
					informationMessagePlayer2 = null ;
					
				}else if(p == null) 
					continue ;
				else if(p.getKey() == null) {
					
					i = p.getValue();
					
					if(i == PLAYER1) 
					{
						System.out.println("[MATCHSERVER] Player 1 -> "+ Protocol.CONNECTION_LOST);
						sendMessage(Protocol.CONNECTION_LOST, i);
						notifyClients(DISCONNECTEDPLAYER1);
					}
					else if( i == PLAYER2)
					{
						System.out.println("[MATCHSERVER] Player 2 -> "+ Protocol.CONNECTION_LOST);
						sendMessage(Protocol.CONNECTION_LOST, i);
						notifyClients(DISCONNECTEDPLAYER2);
					}
					
					return ;
					
				}
				else if(p.getKey().equals(Protocol.LEFTGAME)) {
					
					i = p.getValue();
					
					if(i == PLAYER1) 
					{
						System.out.println("[MATCHSERVER] Player 1 -> "+ Protocol.LEFTGAME);
						sendMessage(Protocol.CONNECTION_LOST, i);
						notifyClients(NOONEISDISCONNETED);
					}
					else 
					{
						System.out.println("[MATCHSERVER] Player 2 -> "+ Protocol.LEFTGAME);
						sendMessage(Protocol.CONNECTION_LOST, i);
						notifyClients(NOONEISDISCONNETED);
					}
					
					// The game is over	

					return ;
					
				}else if(p.getKey().equals(Protocol.MOVEBALL)) {
					
					i = p.getValue();
					
					if(i == PLAYER1) {
						message = read1();
						if(message == null)
						{
							sendMessage(Protocol.CONNECTION_LOST, PLAYER1);
							sendMessage(Protocol.RELOADING_APP, PLAYER2);
							notifyClients(DISCONNECTEDPLAYER1);
							return ;
						
						}
						System.out.println("[MATCHSERVER] "+Protocol.MOVEBALL+ " player: "+username1+" -> " +message);
						
					}else {
						message = read2();
						if(message == null)
						{
							sendMessage(Protocol.CONNECTION_LOST, PLAYER2);
							sendMessage(Protocol.RELOADING_APP, PLAYER1);
							notifyClients(DISCONNECTEDPLAYER2);
							return ;
						
						}
						System.out.println("[MATCHSERVER] "+Protocol.MOVEBALL+ " player: "+username2+" -> " +message);
					}
					
					
					
					String[] stringa = message.split(";");
					
					double xPos = Protocol.parseCoordinates(stringa[0])[0];
					double yPos = Protocol.parseCoordinates(stringa[0])[1];
					
					double xVel = Protocol.parseCoordinates(stringa[1])[0];
					double yVel = Protocol.parseCoordinates(stringa[1])[1];
					
					
					xPos += Settings.DIMENSIONSTANDARDBALL;
					yPos += Settings.DIMENSIONSTANDARDBALL;
			
					if(i == PLAYER2)
						xPos = Settings.FIELDWIDTHFRAME - xPos ;
					
					Ball b = matchHandler.tookBall(xPos, yPos);
					
					if(b == null) {
						
						if(i == PLAYER1) 
						{
							System.out.println("[MATCHSERVER] Player 1 -> "+ Protocol.CONNECTION_LOST);
							sendMessage(Protocol.CONNECTION_LOST, PLAYER1);
							sendMessage(Protocol.RELOADING_APP, PLAYER2);
							notifyClients(DISCONNECTEDPLAYER1);
						}
						else 
						{
							System.out.println("[MATCHSERVER] Player 2 -> "+ Protocol.CONNECTION_LOST);
							sendMessage(Protocol.CONNECTION_LOST, PLAYER2);
							sendMessage(Protocol.RELOADING_APP, PLAYER1);
							notifyClients(DISCONNECTEDPLAYER2);
						}
						return ;
					}
					
					if(b.getColor() == i && ( i == PLAYER1 && matchHandler.getTurn() || i == PLAYER2 && !matchHandler.getTurn() ) )
					{
						matchHandler.setTurn(!matchHandler.getTurn());
						
						if(i == PLAYER2)
							xVel *= -1 ;
						
						b.setVelocity(new VelocityNoSync(xVel, yVel));
	
						informationMessagePlayer1 = "";
						informationMessagePlayer2 = "";
					
						do {
							matchHandler.moveBalls(field);
							informationMessagePlayer1 += ParseMatchInformation.getString(matchHandler.getBalls(), matchHandler.getTurn(), PLAYER1);
							informationMessagePlayer1 += Protocol.STRINGINFORMATIONDELIMITER ;
							informationMessagePlayer2 += ParseMatchInformation.getString(matchHandler.getBalls(), !matchHandler.getTurn(), PLAYER2);
							informationMessagePlayer2 += Protocol.STRINGINFORMATIONDELIMITER ;
						}while(!matchHandler.allStopped());
						
					}
					
				}else if(p.getKey().equals(Protocol.MYUSERNAMEIS)) {
					
					
					i = p.getValue();
					
					if(i == PLAYER1) {
						
						sendMessage(Protocol.CONNECTION_LOST, PLAYER1);
						sendMessage(Protocol.RELOADING_APP, PLAYER2);
						notifyClients(DISCONNECTEDPLAYER1);
						
					}else {
						sendMessage(Protocol.CONNECTION_LOST, PLAYER2);
						sendMessage(Protocol.RELOADING_APP, PLAYER1);
						notifyClients(DISCONNECTEDPLAYER2);
					}
					
					return ; 
				}else if(p.getKey().equals(Protocol.TYPEOFLINEUP)) {
					
					i = p.getValue();
					
					if(i == PLAYER1) {
						
						sendMessage(Protocol.CONNECTION_LOST, PLAYER1);
						sendMessage(Protocol.RELOADING_APP, PLAYER2);
						notifyClients(DISCONNECTEDPLAYER1);
						return ;
						
					}else {
						sendMessage(Protocol.CONNECTION_LOST, PLAYER2);
						sendMessage(Protocol.RELOADING_APP, PLAYER1);
						notifyClients(DISCONNECTEDPLAYER2);
						return ;
					}
					
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
			i = PLAYER1 ;
			if(mess == null || mess.equals(Protocol.CONNECTION_LOST))
				return new Pair<String, Integer>(null, i);
			return new Pair<String, Integer>(mess, i);
		
		}else if(in2.ready()) {
			mess = in2.readLine();
			i = PLAYER2 ;
			if(mess == null || mess.equals(Protocol.CONNECTION_LOST))
				return new Pair<String, Integer>(null, i);
			return new Pair<String, Integer>(mess, i);
		}
		
		return null ;
		
	}
	
	
	private void sendMessage(String message, int sender)
	{
		String disconnected = whoIsDisconnected() ;
		
		if(sender == PLAYER1 && out2 != null && !disconnected.equals(DISCONNECTEDPLAYER2)) 
		{
			if(!message.equals(Protocol.INFORMATIONMATCHMESSAGE) && !gotInformationMessage) {
				gotInformationMessage = false;
				if(username1 != null && username2 != null)
					System.out.println("[MATCHSERVER] Message from :"+username1+" to : "+username2+" , Message: "+message);
				else
					System.out.println("[MATCHSERVER] Message from : player1 to : player2 , Message: "+message);
			}else
				gotInformationMessage = true ;
			
			out2.println(message);
		}
		else if(sender == PLAYER2 && out1 != null && !disconnected.equals(DISCONNECTEDPLAYER1))
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
		
		if(player1 != null && !player1.getClient().isClosed())
			try {
				player1.getClient().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		player1 = null ;
		
		if(player2 != null && !player2.getClient().isClosed())
			try {
				player2.getClient().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		player2 = null ;
		
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
		
		if(player1 != null && !player1.getClient().isClosed())
			try {
				player1.getClient().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		player1 = null ;
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
		
		if(player2 != null && !player2.getClient().isClosed())
			try {
				player2.getClient().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		player2 = null ;
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
