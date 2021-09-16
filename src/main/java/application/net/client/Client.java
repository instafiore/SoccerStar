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
import application.view.Field;
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
	public static final int MAINPAGE = 3 ;
	public static final int ACCOUNT = 4 ;
	public static final int STEP1PSW = 5 ;
	public static final int STEP2PSW = 6 ;
	public static final int HISTORY = 7 ;
	public static final int SHOP = 8;
	
	public static final int FIELD1 = 1 ;
	public static final int FIELD2 = 2 ;
	public static final int FIELD3 = 3 ;

	
	
	private int currentState = STEP_LOGIN;
	private MatchClient currentMatch = null ;
	private int currentField = FIELD1 ;
		
	private Client() {}
	
	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}
	
	public void clickButtonLeft() {
		currentField--;
		if(currentField < FIELD1)
			currentField = FIELD3 ;
	}
	
	public void clickButtonRight() {
		currentField++;
		if(currentField > FIELD3)
			currentField = FIELD1 ;
	}
	public int getCurrentField() {
		return currentField;
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
			return new Message(Protocol.CONNECTION_LOST);
		
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
		case MAINPAGE:
			return readMainPage(message);
		case ACCOUNT:
			return readAccount(message);
		case STEP1PSW:
			return readStep1PSW(message);
		case STEP2PSW:
			return readStep2PSW(message);
		case HISTORY:
			return readHistory(message);
		case SHOP:
			return readShop(message);
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
			setCurrentState(MAINPAGE);
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
			setCurrentState(MAINPAGE);
			
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
	
	public Message readMainPage(String protocol) throws IOException {
		Message message = null ;
		String mess = null ;
		if(protocol.equals(Protocol.INITIALINFORMATION)) {
			mess = in.readLine();
			if(mess == null )
			{
				closeStreams();
				message = new Message(Protocol.GENERALERROR);
				return message ;
			}
			message = new Message(protocol,mess);
		}else {
			message = new Message();
			message.setProtocol(Protocol.GENERALERROR);
		}
		
		return message ;
	}
	
	public Message readAccount(String protocol) throws IOException {
		Message message = null ;
		String mess = null ;
		
		if(protocol.equals(Protocol.INFORMATIONACCOUNT)) {
			mess = in.readLine();
			if(mess == null )
			{
				closeStreams();
				message = new Message(Protocol.GENERALERROR);
				return message ;
			}
			message = new Message(protocol,mess);
		}else if(protocol.equals(Protocol.PASSWORDCHANGED) || protocol.equals(Protocol.OLDPASSOWORDNOTCORRECT)) {
			
			message = new Message(protocol);
			
		}else {
			message = new Message();
			message.setProtocol(Protocol.GENERALERROR);
		}
		
		return message ;
	}
	
	public Message readHistory(String protocol) throws IOException {
		Message message = null ;
		String mess = null ;
		
		if(protocol.equals(Protocol.INFORMATIONHISTORY)) {
			
			mess = in.readLine();
			if(mess == null )
			{
				closeStreams();
				message = new Message(Protocol.GENERALERROR);
				return message ;
			}
			message = new Message(protocol,mess);
			
		}else {
			message = new Message();
			message.setProtocol(Protocol.GENERALERROR);
		}
		return message ;
	}
	
	public Message readShop(String protocol) throws IOException {
		
		Message message = null ;
		String mess = null ;
		
		if(protocol.equals(Protocol.INFORMATIONSHOP)) {
			
			mess = in.readLine();
			if(mess == null )
			{
				closeStreams();
				message = new Message(Protocol.GENERALERROR);
				return message ;
			}
			message = new Message(protocol,mess);
			
		}else if(protocol.equals(Protocol.SKINBOUGHT) || protocol.equals(Protocol.SKINNOTBOUGHT)) {
			
			message = new Message(protocol);
			
		}else {
			message = new Message();
			message.setProtocol(Protocol.GENERALERROR);
		}
		return message ;
	}
	
	public Message readStep1PSW(String protocol) throws IOException {
		Message message = null ;
		String mess = null ;
		
		if(protocol.equals(Protocol.USERNAMEDOESNTEXIST) ) {
			
			message = new Message(protocol);
			
		}else if (protocol.equals(Protocol.EMAILSENT) ){
			
			mess = in.readLine();
			if(mess == null )
			{
				closeStreams();
				message = new Message(Protocol.GENERALERROR);
				return message ;
			}
			message = new Message(protocol,mess);
			
		}else {
			message = new Message();
			message.setProtocol(Protocol.GENERALERROR);
		}
		
		return message ;
	}
	
	public Message readStep2PSW(String protocol) throws IOException {
		Message message = null ;
		String mess = null ;
		
		if(protocol.equals(Protocol.PASSWORDCHANGED) || protocol.equals(Protocol.CODENOTVALID) ) {
			
			message = new Message(protocol);
			
		}else if (protocol.equals(Protocol.EMAILSENT) ){
			
			mess = in.readLine();
			if(mess == null )
			{
				closeStreams();
				message = new Message(Protocol.GENERALERROR);
				return message ;
			}
			message = new Message(protocol,mess);
			
		}else {
			message = new Message();
			message.setProtocol(Protocol.GENERALERROR);
		}
		
		return message ;
	}
	
	public void logout() {
		sendMessage(Protocol.LOGOUT);
		SceneHandler.getInstance().loadScene("LoginPage", false , true);
		currentState =  STEP_LOGIN ;
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
	
	public void cancelRequest() {
		setCurrentState(MAINPAGE);
		sendMessage(Protocol.REQUESTCANCELED);
	}
	
	public void startMatchField1() {
		
		
		if(currentState == IN_GAME)
			return;
		
		MatchController.getInstance().getMatchView().setField(MatchView.FIELD1);
		setCurrentState(IN_GAME);
		sendMessage(Protocol.NEWGAMEREQUESTFIELD1);
		
	}
	
	public void startMatchField2() {
			
			if(currentState == IN_GAME)
				return;
			MatchController.getInstance().getMatchView().setField(MatchView.FIELD2);
			setCurrentState(IN_GAME);
			sendMessage(Protocol.NEWGAMEREQUESTFIELD2);
		
		}
	
	public void startMatchField3() {
		
		if(currentState == IN_GAME)
			return;
		MatchController.getInstance().getMatchView().setField(MatchView.FIELD3);
		setCurrentState(IN_GAME);
		sendMessage(Protocol.NEWGAMEREQUESTFIELD3);
		
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
		currentState = MAINPAGE;
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
