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
import application.control.MatchController;
import application.control.MatchSucceedController;
import application.model.game.entity.Message;
import application.net.common.Protocol;
import application.view.MatchView;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class Client extends Service<Message>{

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
	private MatchClient currentMatch = null ;
	
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
	
	public MatchClient getCurrentMatch() {
		return currentMatch;
	}
	
	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentMatch(MatchClient currentMatch) {
		this.currentMatch = currentMatch;
	}
	
	public static Client getInstance() {
		if(instance == null)
			instance = new Client();
		return instance ;
	}
	
	
	@Override
	protected Task<Message> createTask() {
		return new Task<Message>() {
			@Override
			protected Message call() throws Exception {
				return read();
			}
		};
	}

	
	public Message read() throws IOException {
		
		if(in == null)
			return new Message(Protocol.INPUT_STREAM_NULL);
		
		String message = in.readLine();
		
		
		if(message == null )
		{
			printConnectionLost();
			return new Message(Protocol.CONNECTION_LOST);
		}
		
		System.out.println("[CLIENT] RECEIVED CLIENT : "+message);
		
		if(message.equals(Protocol.GENERALERROR))
		{
			// RELOADING APP
			in = null ;
			System.err.println("[CLIENT] "+Protocol.GENERALERROR);
			return new Message("[CLIENT] "+Protocol.GENERALERROR);
			
		}
		
		switch (currentState) {
	
		case STEP_REGISTRATION:
			return readRegistation(message);
		case STEP_LOGIN:
			return readLogin(message);
		case IN_GAME:
			return readIN_GAME(message);
		case IN_APP:
			return readIN_APP(message);
		default:
			return new Message(Protocol.GENERALERROR);
		}
		
	}
	
	public Message readIN_GAME(String protocol) {
		
		if(protocol.equals(Protocol.PREPARINGMATCH)) 
			return new Message(Protocol.PREPARINGMATCH);
		
		return new Message(Protocol.GENERALERROR);
	}
	
	public Message readRegistation(String protocol) throws IOException {
		
		Message message = null ;
		
		if(protocol.equals(Protocol.REGISTRATIONCOMPLETED))
		{
			setCurrentState(STEP_LOGIN);
			username = in.readLine();
			if(username == null)
			{
				closeStreams();
				message = new Message(Protocol.GENERALERROR);
				return message ;
			}
			message = new Message(protocol, username);
		}else if(protocol.equals(Protocol.REGISTRATIONFAILED) || protocol.equals(Protocol.ALREADYEXISTS)){
			message = new Message();
			message.setProtocol(protocol);
		}
		else {
			
			message = new Message();
			message.setProtocol(Protocol.GENERALERROR);
		}
		return message;
	}
	
	public Message readLogin(String protocol) throws IOException {
		
		Message message = null ;
		
		if(protocol.equals(Protocol.LOGINCOMPLETED)) {
			
			username = in.readLine();
			if(username == null)
			{
				closeStreams();
				message = new Message(Protocol.GENERALERROR);
				return message ;
			}
			message = new Message(protocol, username);
			
		}else if(protocol.equals(Protocol.LOGINFAILED) || protocol.equals(Protocol.ALREADYONLINE)){
			message = new Message();
			message.setProtocol(protocol);
		}
		else {
			message = new Message();
			message.setProtocol(Protocol.GENERALERROR);
		}
		
		return message;
	}
	
	public void logout() {
		sendMessage(Protocol.LOGOUT);
		SceneHandler.getInstance().loadScene("LoginPage", false);
		currentState =  STEP_LOGIN ;
	}
	
	public Message readIN_APP(String protocol) throws IOException {
		Message message = null ;
		//TODO
		String mess = in.readLine();
		
		message = new Message(protocol);
		return message ;
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
	
	public void startMatchField1() {
		
		if(currentState == IN_GAME)
			return;
		
		MatchController.getInstance().getMatchView().setField(MatchView.FIELD1);
		setCurrentState(IN_GAME);
		sendMessage(Protocol.NEWGAMEREQUESTFIELD1);
		MatchClient match = new MatchClient(this);
		setCurrentMatch(match);
		match.setOnSucceeded(new MatchSucceedController());
		
	}
	
	public void startMatchField2() {
			
			if(currentState == IN_GAME)
				return;
			MatchController.getInstance().getMatchView().setField(MatchView.FIELD2);
			setCurrentState(IN_GAME);
			sendMessage(Protocol.NEWGAMEREQUESTFIELD2);
			MatchClient match = new MatchClient(this);
			setCurrentMatch(match);
			match.setOnSucceeded(new MatchSucceedController());
			
		}
	
	public void startMatchField3() {
		
		if(currentState == IN_GAME)
			return;
		MatchController.getInstance().getMatchView().setField(MatchView.FIELD3);
		setCurrentState(IN_GAME);
		sendMessage(Protocol.NEWGAMEREQUESTFIELD3);
		MatchClient match = new MatchClient(this);
		setCurrentMatch(match);
		match.setOnSucceeded(new MatchSucceedController());
		
	}

	public void sendMessage(String message) {
		
		if(out == null || message == null)
			return ;
		
		out.println(message);
	}

	@Override
	public void restart() {
		if(currentState != IN_GAME)
		{
			System.out.println("[CLIENT] DONE RESTART");
			super.restart();
		}else {
			System.out.println("[CLIENT] DIDN'T RESTART");
		}
	}

	public void matchEnded() {
		currentState = IN_APP;
		restart();
	}
	
	private void printConnectionLost() {
		
		System.out.println("[CLIENT] "+ Protocol.CONNECTION_LOST + " with: SERVER " );
		closeStreams();
	}
	
	private void closeStreams() {
		
		try {
			if(in != null) 
				in.close();
			in = null ;
			if(out != null)
				out.close(); 
			out = null ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
