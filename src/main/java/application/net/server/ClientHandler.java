package application.net.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import application.net.common.Protocol;

public class ClientHandler implements Runnable{

	private Socket client = null ;
	private BufferedReader in = null ;
	private PrintWriter out = null ;
	private boolean onGame = false;
	
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
		

		while(!Thread.interrupted()) {
			
			String message = null ;
			
			try {
				if(!isOnGame())
					message = in.readLine();

			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			if(message == null)
				continue;
			
			if(message.equals(Protocol.NEWGAMEREQUEST)) {
				
				RequestMatchHandler.getInstace().addPlayer(this);
				onGame = true; 
			}
			
		}
		
	}
	
	public void setOnGame(boolean onGame) {
		this.onGame = onGame;
	}
	
	public boolean isOnGame() {
		return onGame;
	}
}
