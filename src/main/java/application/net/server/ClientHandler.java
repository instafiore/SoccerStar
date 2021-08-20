package application.net.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
		
		if(message.equals(Protocol.REGISTRATIONREQUEST)) {
			
			try {
				
				message = in.readLine();
				System.out.println(message);
				
				RegistrationClient client = new RegistrationClient();
				client.parseRegistrationClient(message);
				
				username = client.getUsername();
				
				if(Database.getInstance().insertUser(client))
				{
					sendMessage(Protocol.REGISTRATIONCOMPLETED);
					sendMessage(username);
				}
				else
					sendMessage(Protocol.REGISTRATIONFAILED);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(message.equals(Protocol.LOGINREQUEST)) {
			
			
			
		}else {
			
			// ERROR
			sendMessage(Protocol.GENERALERROR);
			return;
		}

		while(!Thread.interrupted()) {
			
			System.out.println("CLIENT HANDLER");
		
			
			try {
				
				message = in.readLine();
				System.out.println(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(message == null)
				continue;
			
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
				
				sendMessage(Protocol.GENERALERROR);
				return;
			
			}else if(message.equals(Protocol.LOGINREQUEST)) {
				
				sendMessage(Protocol.GENERALERROR);
				return;
			
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
		
		out.println(message);
	}
	
}
