package application.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import application.Settings;

public class Server implements Runnable{

	private ServerSocket server ;
	
	public void startServer() throws IOException {
	
		server = new ServerSocket(Settings.PORT);
		
		System.out.println("Server has started...");
		
		try {
			Database.getInstance().connectToDatabase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Database not connected");
			e.printStackTrace();
		}
		
		Thread t = new Thread(this);
		
		t.start();

	}

	

	public void run() {
		
		while(!Thread.interrupted()){
			
			System.out.println("Waiting for connections...");
			try {
				
				Socket socket = server.accept();
				System.out.println("Client connected");
				ClientHandler clientHandler = new ClientHandler(socket);
				Thread t = new Thread(clientHandler);
				t.start();
			
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		
	}
	
}
