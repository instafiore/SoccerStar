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
	
	public void run() {
		
		String message = null ;
		
		while(!Thread.interrupted()) {
			
			try {
				message = in.readLine();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			if(message.equals(Protocol.NEWGAMEREQUEST)) {
				
				RequestMatchHandler.getInstace().addPlayer(client);
				
			}
			
		}
		
	}
}
