package application.net.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import application.model.game.entity.LoginClient;
import application.model.game.entity.RegistrationClient;
import application.net.common.Protocol;

public class ClientHandler implements Runnable{

	private Socket client = null ;
	private BufferedReader in = null ;
	private PrintWriter out = null ;
	private String username = null ;
	
	
	
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
	
	public Socket getClient() {
		return client;
	}
	
	public void run() {
		
		String message = null ;
		
		message = read();
		
		while(username == null) {
			
			if(message.equals(Protocol.REGISTRATIONREQUEST)) {
				
				message  = read();
				
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
				System.out.println("SENT FROM HERE 1");
				sendMessage(Protocol.GENERALERROR);
				username = null ;
				return;
			}
		}
		
		

		while(!Thread.interrupted()) {
			
			System.out.println("CLIENT HANDLER IS RUNNING FOR "+username);
		
			message = read();
			
			if(message == Protocol.CONNECTION_LOST)
				return;
			
			if(message.equals(Protocol.NEWGAMEREQUEST)) {
				
				RequestMatchHandler.getInstace().addPlayer(this);
				try {
					
					synchronized (this) {
						this.wait();
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else if(message.equals(Protocol.REGISTRATIONREQUEST)) {
				
				System.out.println("SENT FROM HERE 2");
				sendMessage(Protocol.GENERALERROR);
				return;
			
			}else if(message.equals(Protocol.LOGINREQUEST)) {
				
				System.out.println("SENT FROM HERE 1");
				sendMessage(Protocol.GENERALERROR);
				return;
			
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
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
	

	public void sendMessage(String message) {
		
		if(out == null || message == null)
			return ;
		
		System.out.print("SENT : "+message);
		
		if(username != null)
			System.out.println(" to: "+username);
		else 
			System.out.println();
		
		out.println(message);
	}
	
	
	public String read() {
		
		String message = null ;
		try {
			
			message = in.readLine();
			if(username != null)
				System.out.print("Message from : "+username + " ");
			
			if(message == null)
			{
				System.out.println(Protocol.CONNECTION_LOST);
				if(username != null)
					System.out.println( Protocol.CONNECTION_LOST + " with: "+ username );
				else 
					System.out.println( Protocol.CONNECTION_LOST) ;
				return Protocol.CONNECTION_LOST;
			}
		
			System.out.println("RECEIVED: " + message);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return message;
	}
}
