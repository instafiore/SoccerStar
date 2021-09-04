package application.net.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import application.Settings;
import application.model.game.entity.LoginClient;
import application.model.game.entity.RegistrationClient;
import application.net.common.Protocol;

public class ClientHandler implements Runnable{

	private Socket client = null ;
	private BufferedReader in = null ;
	private PrintWriter out = null ;
	private String username = null ;
	private boolean throwMessagesMatch = false ;
	
	
	public String getUsername() {
		return username;
	}
	
	public ClientHandler(Socket client) {
		super();
		this.client = client;
		
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(new BufferedOutputStream(client.getOutputStream()),true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public BufferedReader getIn() {
		return in;
	}
	
	public PrintWriter getOut() {
		return out;
	}
	
	public Socket getClient() {
		return client;
	}
	
	public void run() {
		
		String message = null ;
		
		while(username == null) {
			
			message = read();

			if(message == null ) 
				return;
			
			if(message.equals(Protocol.REGISTRATIONREQUEST)) {
				
				message = read();
				
				if(message == null ) {
					printConnectionLost();
					return;
				}
				
				RegistrationClient client = new RegistrationClient();
				client.parseRegistrationClient(message);
				
				if(Database.getInstance().insertUser(client))
				{
					sendMessage(Protocol.REGISTRATIONCOMPLETED);
					username = client.getUsername();
					sendMessage(username);

				}
				else
				{
					sendMessage(Protocol.REGISTRATIONFAILED);
					username = null ;
					
				}
				
		
			
			}else if(message.equals(Protocol.LOGINREQUEST)) {
				
				message = read();
				if(message == null )
					return;
				
				LoginClient client = new LoginClient();
				client.parseLoginClient(message);
				
				
				if(Database.getInstance().checkLogin(client))
				{
					sendMessage(Protocol.LOGINCOMPLETED);
					username = client.getUsername();
					sendMessage(username);
					
				}
				else
				{
					sendMessage(Protocol.LOGINFAILED);
					username = null ;
				}
				
			}else {
				
				// ERROR
				sendMessage(Protocol.RELOADING_APP);
				username = null ;
				printConnectionLost();
				return;
			}
		}
		
		

		while(!Thread.interrupted()) {
			
			System.out.println("[CLIENTHANDLER] CLIENT HANDLER IS RUNNING FOR "+username);
		
			message = read();
			
			if(message == null )
				return;
		
			if(message.equals(Protocol.NEWGAMEREQUESTFIELD1)) {
				
				RequestMatchHandler.getInstace().addPlayerField1(this);
				try {
					
					synchronized (this) {
						this.wait();
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else if(message.equals(Protocol.NEWGAMEREQUESTFIELD2)) {
				
				RequestMatchHandler.getInstace().addPlayerField2(this);
				try {
					
					synchronized (this) {
						this.wait();
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else if(message.equals(Protocol.NEWGAMEREQUESTFIELD3)) {
				
				RequestMatchHandler.getInstace().addPlayerField3(this);
				try {
					
					synchronized (this) {
						this.wait();
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else if(message.equals(Protocol.REGISTRATIONREQUEST)) {
				
				sendMessage(Protocol.RELOADING_APP);
				printConnectionLost();
				return;
			
			}else if(message.equals(Protocol.LOGINREQUEST)) {
				
				sendMessage(Protocol.RELOADING_APP);
				printConnectionLost();
				return;
			
			}
			
			try {
				Thread.sleep(Settings.REFRESHSERVER);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void notifyEndMatch() {
		synchronized (this) {
			throwMessagesMatch = true ;
			this.notify();
		}
	}
	

	public void sendMessage(String message) {
		
		if(out == null || message == null)
			return;
		
		
		System.out.print("[CLIENTHANDLER] SENT : "+message);
		
		if(username != null)
			System.out.println(" to: "+username);
		else 
			System.out.println(" to unknown receiver");
		
		out.println(message);
	}
	
	private void printConnectionLost() {
		if(username != null)
			System.out.println("[CLIENTHANDLER] "+ Protocol.CONNECTION_LOST + " with: "+ username );
		else 
			System.out.println("[CLIENTHANDLER] "+ Protocol.CONNECTION_LOST) ;
		closeStreams();
	}
	
		

	public String read() {
	
		String message = null ;
		try {
			
			do {
				
				if(in == null || out == null || client.isClosed())
				{
					printConnectionLost();
					return null ;
				}
				
				message = in.readLine();
				
				if(message == null)
				{
					printConnectionLost();
					return null;
				}
				
				
				if(Protocol.protocolMatch().contains(message) && !throwMessagesMatch) {
					//TODO
					// ERROR 
					System.out.println("[CLIENTHANDLER] "+Protocol.RELOADING_APP);
					printConnectionLost();
					return null;
				}
			
			}while(Protocol.protocolMatch().contains(message) && throwMessagesMatch);
			
			throwMessagesMatch = false ;
			
			if(username != null)
				System.out.println("[CLIENTHANDLER] Message from : "+username + " -> receive: " + message);
			else 	
				System.out.println("[CLIENTHANDLER] Unknown sender -> receive: " + message);
		
		} catch (IOException e) {
			printConnectionLost();
			return null ;
		}
		return message ;
	}
	
	private void closeStreams() {
		
		try {
			if(in != null) 
				in.close();
			in = null ;
			if(out != null)
				out.close(); 
			out = null ;
			if(client != null && !client.isClosed())
				client.close();
			client = null ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
