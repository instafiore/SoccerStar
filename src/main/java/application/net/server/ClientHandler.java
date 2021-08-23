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
		
		if(message.equals(Protocol.REGISTRATIONREQUEST)) {
			
			
				message  = read();
				
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
				
		
			
		}else if(message.equals(Protocol.LOGINREQUEST)) {
			
			message = read();
			
			LoginClient client = new LoginClient();
			client.parseLoginClient(message);
			
			username = client.getPassword();
			
			if(Database.getInstance().checkLogin(client))
			{
				sendMessage(Protocol.LOGINCOMPLETED);
				System.out.println(Protocol.LOGINCOMPLETED);
				sendMessage(username);
			}
			else
				sendMessage(Protocol.LOGINFAILED);
			
		}else {
			
			// ERROR
			sendMessage(Protocol.GENERALERROR);
			return;
		}

		while(!Thread.interrupted()) {
			
			System.out.println("CLIENT HANDLER");
		
			
			try {
				
				message = in.readLine();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(message == null)
				return;
			
			System.out.println(message);
			
			if(message.equals(Protocol.NEWGAMEREQUEST)) {
				
				System.out.println(Protocol.NEWGAMEREQUEST);
				
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
		
		out.println(message);
	}
	
	
	public String read() {
		
		String message = null ;
		try {
			
			message = in.readLine();
			System.out.println(message);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return message;
	}
}
