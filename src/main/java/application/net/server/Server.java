package application.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import application.Settings;

public class Server implements Runnable{

	private ServerSocket server ;
	private RequestMatchHandler requestMatchHandler;
	
	public void startServer() throws IOException {
		
		requestMatchHandler = new RequestMatchHandler();
		server = new ServerSocket(Settings.PORT);
		
		System.out.println("Server has started...");
		
		Thread t = new Thread(this);
		
		t.start();

	}

	

	public void run() {
		
		while(!Thread.interrupted()){
			
			System.out.println("Waiting for connections...");
			try {
				
				Socket socket = server.accept();
				System.out.println("Client connected");
				requestMatchHandler.addPlayer(socket);
			
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		
	}
	
}
