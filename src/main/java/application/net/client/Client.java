package application.net.client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import application.SceneHandler;
import application.Settings;
import application.net.common.Protocol;
import javafx.animation.AnimationTimer;

public class Client extends AnimationTimer{

	private BufferedReader in = null;
	private PrintWriter out = null;
	private Socket socket = null;
	
	private static Client instance = null;
	private String username = null;
	
	public static final int STEP_REGISTRATION = 0 ;
	public static final int STEP_LOGIN = 1 ;
	public static final int IN_GAME = 2 ;
	public static final int IN_APP = 3 ;
	
	private long currentTime = 0 ;
	//TODO DO USERNAME THING 
	
	private int currentState = 1;
	
	private Client() {}
	
	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}
	
	public BufferedReader getIn() {
		return in;
	}
	
	public PrintWriter getOut() {
		return out;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public static Client getInstance() {
		if(instance == null)
			instance = new Client();
		return instance ;
	}
	
	@Override
	public void handle(long now) {
		
		if(now - currentTime < Settings.REFRESHCLIENT * 1000000)
			return;
	
		currentTime = now ;
		
		try {
			read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void read() throws IOException {
		
		if(in == null || !in.ready())
			return;
		
		this.stop();
		
		
		String message = in.readLine();
		
		System.out.println(message);
		
		if(message.equals(Protocol.GENERALERROR))
		{
			// RELOADING APP
			System.err.println(Protocol.GENERALERROR);
			in = null ;
		}
		
		switch (currentState) {
	
		case STEP_REGISTRATION:
			readRegistation(message);
			break;
			
		case STEP_LOGIN:
			readLogin(message);
			break;
		
		default:
			//ERROR
			break;
		}
		
		this.start();
	}
	
	public void readRegistation(String message) throws IOException {
		
		
		if(message.equals(Protocol.REGISTRATIONCOMPLETED))
		{
			SceneHandler.getInstance().loadScene("LoginPage", false);
			setCurrentState(STEP_LOGIN);
			username = in.readLine();
		}else {
			//TODO
			
		}
		
	}
	
	public void readLogin(String message) throws IOException {
		
		if(message.equals(Protocol.LOGINCOMPLETED)) {
			
			username = in.readLine();
			setCurrentState(IN_GAME);
			startMatch();
			
		}else {
			//TODO
		}
		
	}
	
	
	public void connectToServer() {
		
		try {
			socket = new Socket(Settings.ADDRESS_SERVER,Settings.PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()),true);
		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		this.start();
	}
	
	public void startMatch() {
		
		this.stop();
		sendMessage(Protocol.NEWGAMEREQUEST);
		MatchClient match = new MatchClient(this);
		
	}

	public void sendMessage(String message) {
		
		if(out == null || message == null)
			return ;
		
		out.println(message);
	}


	public void notifyClient() {
		this.start();
	}

	
}
