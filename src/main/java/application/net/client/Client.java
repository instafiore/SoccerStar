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

public class Client implements Runnable{

	private BufferedReader in = null;
	private PrintWriter out = null;
	private Socket socket = null;
	
	private static Client instance = null;
	private String username = null;
	
	public static final int STEP_REGISTRATION = 0 ;
	public static final int STEP_LOGIN = 1 ;
	public static final int IN_GAME = 2 ;
	public static final int IN_APP = 3 ;
	
	
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
	
	public void read() throws IOException {
		
		if(in == null)
			return;
		
		String message = in.readLine();
		
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
			
			break;
		
		case IN_GAME: 
		
		default:
			//ERROR
			break;
		}
		
	}
	
	public void readRegistation(String message) throws IOException {
		
		
		if(message.equals(Protocol.REGISTRATIONCOMPLETED))
		{
			SceneHandler.getInstance().loadScene("LoginPage", false);
			
			username = in.readLine();
		}else {
			//TODO
			
		}
		
	}
	
	public void readLogin(String message) throws IOException {
		
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
		Thread t = new Thread(this);
		t.setDaemon(false);
		t.start();
	}
	
	public void startMatch() {
		
		sendMessage(Protocol.NEWGAMEREQUEST);
		MatchClient match = new MatchClient(this);
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void sendMessage(String message) {
		
		if(out == null || message == null)
			return ;
		
		out.println(message);
	}
	
	public void run() {
		
		while(!Thread.interrupted()) {
			
			try {
				read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	

	public void notifyClient() {
		synchronized (this) {
			this.notify();
		}
	}
}
